package com.flash.githubtrending.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.flash.githubtrending.core.Result
import com.flash.githubtrending.data.error.NetworkErrorMapper
import com.flash.githubtrending.data.error.NetworkErrorMapper.toDomain
import com.flash.githubtrending.data.local.AppDatabase
import com.flash.githubtrending.data.local.dao.RepoDao
import com.flash.githubtrending.data.local.mapper.toDomain
import com.flash.githubtrending.data.local.mapper.toEntity
import com.flash.githubtrending.data.remote.api.GithubApi
import com.flash.githubtrending.data.remote.dto.SearchReposResponseDto
import com.flash.githubtrending.data.remote.dto.toDomainList
import com.flash.githubtrending.data.remote.paging.RepoRemoteMediator
import com.flash.githubtrending.data.utils.*
import com.flash.githubtrending.domain.model.Repo
import com.flash.githubtrending.domain.repository.RepoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException


@Singleton
class RepoRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    private val api: GithubApi,
    private val repoDao: RepoDao,
    private val ioDispatcher: CoroutineDispatcher
) : RepoRepository {
    override fun observeFavoriteRepos(): Flow<List<Repo>> {
        return repoDao.observeFavoriteRepos()
            .map { entities -> entities.map { it.toDomain() } }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun observePagedTrendingRepos(): Flow<PagingData<Repo>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                prefetchDistance = 2,
                enablePlaceholders = false,
                maxSize = PagingConfig.MAX_SIZE_UNBOUNDED
            ),
            remoteMediator = RepoRemoteMediator(
                api = api,
                repoDao = repoDao,
                database = database
            ),
            pagingSourceFactory = { repoDao.pagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override suspend fun searchRepos(query: String): Result<List<Repo>> =
        withContext(ioDispatcher) {
            try {
                val response: SearchReposResponseDto =
                    api.searchRepos(query = query, page = 1, perPage = 30)
                val favoriteIds = repoDao.getFavoriteIdsSet()

                val repos = response.items
                    .toDomainList()
                    .applyFavorites(favoriteIds)

                Result.Success(repos)

            } catch (t: Throwable) {
                if (t is CancellationException) throw t
                Result.Error(NetworkErrorMapper.fromThrowable(t).toDomain())
            }
        }

    override suspend fun toggleFavorite(repo: Repo): Result<Unit> =
        withContext(ioDispatcher) {
            try {
                val existing = repoDao.getRepoById(repo.id)
                if (existing == null) {
                    repoDao.insert(repo.toEntity())
                }

                repoDao.toggleFavorite(repo.id)

                Result.Success(Unit)

            } catch (t: Throwable) {
                if (t is CancellationException) throw t
                Result.Error(NetworkErrorMapper.fromThrowable(t).toDomain())
            }
        }
}


