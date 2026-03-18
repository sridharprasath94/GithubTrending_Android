package com.flash.githubtrending.data.remote.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.flash.githubtrending.data.local.dao.RepoDao
import com.flash.githubtrending.data.remote.api.GithubApi
import com.flash.githubtrending.data.remote.dto.SearchReposResponseDto
import com.flash.githubtrending.data.remote.dto.toDomainList
import com.flash.githubtrending.data.utils.*
import com.flash.githubtrending.domain.model.Repo

class RepoSearchPagingSource(
    private val api: GithubApi,
    private val query: String,
    private val repoDao: RepoDao,
    private val perPageItems: Int = 20,
) : PagingSource<Int, Repo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        val page = params.key ?: 0

        return try {
            val response: SearchReposResponseDto =
                api.searchRepos(query = query, page = page, perPage = perPageItems)

            val favoriteIds = repoDao.getFavoriteIdsSet()

            val repo = response.items
                .toDomainList()
                .applyFavorites(favoriteIds)

            Log.d("NewsPager", "Paging Page number ${page} and repo count ${repo.count()}")

            LoadResult.Page(
                data = repo,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (repo.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}