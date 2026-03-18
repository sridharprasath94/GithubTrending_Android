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
import com.flash.githubtrending.data.remote.paging.RepoRemoteMediator
import com.flash.githubtrending.data.remote.paging.RepoSearchPagingSource
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

    @OptIn(ExperimentalPagingApi::class)
    override fun observeSearchRepos(query: String): Flow<PagingData<Repo>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                prefetchDistance = 2,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = {
                RepoSearchPagingSource(
                    api = api,
                    query = query,
                    repoDao = repoDao,
                )
            }
        ).flow
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


