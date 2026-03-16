package com.flash.githubtrending.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import com.flash.githubtrending.data.local.dao.RepoDao
import com.flash.githubtrending.data.local.entity.RepoEntity
import com.flash.githubtrending.data.local.mapper.toEntity
import com.flash.githubtrending.data.remote.api.GithubApi
import com.flash.githubtrending.data.remote.dto.toDomainList


@OptIn(ExperimentalPagingApi::class)
class RepoRemoteMediator(
    private val api: GithubApi,
    private val repoDao: RepoDao,
    private val database: RoomDatabase
) : RemoteMediator<Int, RepoEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    private var currentPage: Int = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RepoEntity>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                currentPage = 0
                0
            }

            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val next = currentPage + 1
                currentPage = next
                next
            }
        }
        Log.d("RepoPaging", "LoadType=$loadType | page=$page | mediatorPage=$currentPage")
        return try {
            Log.d("RepoAdapter", "Page: ${page}, Page count ${state.pages.size}")
            val response = api.getTrendingRepos(page = page, perPage = state.config.pageSize)
            Log.d(
                "RepoPaging",
                "Page: $page | PerPage=${response.items.size}"
            )
            // Preserve existing favorites before refresh
            val favoriteIds = repoDao.getFavoriteIdsSet()
            val favoriteRepos = repoDao.observeFavoriteReposOnce()

            database.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    repoDao.clearRepos()
                }

                val entities = response.items
                    .toDomainList()
                    .applyFavorites(favoriteIds)
                    .map { it.toEntity() }

                repoDao.insertRepos(entities)

                // Only restore favorites during REFRESH to avoid invalidating Paging repeatedly
                if (loadType == LoadType.REFRESH) {
                    favoriteRepos
                        .filter { fav -> entities.none { it.id == fav.id } }
                        .let { repoDao.insertRepos(it) }
                }
            }
            val endReached = response.items.isEmpty()
            Log.d(
                "RepoPaging",
                "EndReached=$endReached | nextPage=${if (endReached) "none" else page + 1}"
            )
            MediatorResult.Success(
                endOfPaginationReached = endReached
            )

        } catch (t: Throwable) {
            MediatorResult.Error(t)
        }
    }
}
