package com.flash.githubtrending.data.repository

import com.flash.githubtrending.core.Result
import com.flash.githubtrending.data.error.NetworkErrorMapper
import com.flash.githubtrending.data.error.NetworkErrorMapper.toDomain
import com.flash.githubtrending.data.local.dao.FavoriteDao
import com.flash.githubtrending.data.local.dao.RepoDao
import com.flash.githubtrending.data.local.entity.FavoriteEntity
import com.flash.githubtrending.data.local.entity.RepoEntity
import com.flash.githubtrending.data.local.mapper.toDomain
import com.flash.githubtrending.data.local.mapper.toEntity
import com.flash.githubtrending.data.remote.api.GithubApi
import com.flash.githubtrending.data.remote.dto.SearchReposResponseDto
import com.flash.githubtrending.data.remote.dto.toDomainList
import com.flash.githubtrending.domain.model.Repo
import com.flash.githubtrending.domain.repository.RepoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException


@Singleton
class RepoRepositoryImpl @Inject constructor(
    private val api: GithubApi,
    private val repoDao: RepoDao,
    private val favoriteDao: FavoriteDao,
    private val ioDispatcher: CoroutineDispatcher
) : RepoRepository {
    private val refreshMutex = Mutex()
    override fun observeTrendingRepos(): Flow<List<Repo>> {
        return combine(
            repoDao.observeRepos(),
            favoriteDao.observeFavoriteIds()
        ) { repos, favoriteIds ->

            val favoriteSet = favoriteIds.toSet()

            repos.map { entity ->
                entity.toDomain(favoriteSet.contains(entity.id))
            }
        }
    }

    override suspend fun refreshTrendingRepos(): Result<Unit> {
        return withContext(ioDispatcher) {
            refreshMutex.withLock {
                try {
                    val response: SearchReposResponseDto =
                        api.getTrendingRepos(page = 1, perPage = 30)

                    val entities: List<RepoEntity> = response.items
                        .toDomainList()
                        .map { it.toEntity() }

                    repoDao.insertRepos(entities)

                    Result.Success(Unit)

                } catch (t: Throwable) {
                    if (t is CancellationException) throw t
                    Result.Error(NetworkErrorMapper.fromThrowable(t).toDomain())
                }
            }
        }
    }

    override suspend fun searchRepos(query: String): Result<List<Repo>> =
        withContext(ioDispatcher) {
            try {
                val response = api.searchRepos(query = query, page = 1, perPage = 30)
                val favoriteIds = favoriteDao.getFavoriteIds().toSet()

                val repos = response.items
                    .toDomainList()
                    .map { repo ->
                        repo.copy(isFavorite = favoriteIds.contains(repo.id))
                    }

                Result.Success(repos)

            } catch (t: Throwable) {
                if (t is CancellationException) throw t
                Result.Error(NetworkErrorMapper.fromThrowable(t).toDomain())
            }
        }

    override suspend fun toggleFavorite(repo: Repo): Result<Unit> =
        withContext(ioDispatcher) {
            try {
                if (repo.isFavorite) {
                    favoriteDao.delete(repo.id)
                } else {
                    val existing = repoDao.getRepoById(repo.id)
                    if (existing == null) {
                        repoDao.insertRepos(listOf(repo.toEntity()))
                    }
                    favoriteDao.insert(
                        FavoriteEntity(
                            repoId = repo.id,
                            name = repo.name,
                            fullName = repo.fullName
                        )
                    )
                }
                Result.Success(Unit)

            } catch (t: Throwable) {
                if (t is CancellationException) throw t
                Result.Error(NetworkErrorMapper.fromThrowable(t).toDomain())
            }
        }
}