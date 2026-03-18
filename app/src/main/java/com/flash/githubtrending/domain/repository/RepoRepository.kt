package com.flash.githubtrending.domain.repository

import androidx.paging.PagingData
import com.flash.githubtrending.core.Result
import com.flash.githubtrending.domain.model.Repo
import kotlinx.coroutines.flow.Flow

interface RepoRepository {
    fun observePagedTrendingRepos() : Flow<PagingData<Repo>>

    fun observeSearchRepos(query: String): Flow<PagingData<Repo>>

    fun observeFavoriteRepos(): Flow<List<Repo>>

    suspend fun toggleFavorite(repo: Repo) : Result<Unit>
}