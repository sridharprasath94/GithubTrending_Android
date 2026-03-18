package com.flash.githubtrending.domain.repository

import androidx.paging.PagingData
import com.flash.githubtrending.core.RepoResult
import com.flash.githubtrending.domain.model.Repo
import kotlinx.coroutines.flow.Flow

interface RepoRepository {
    suspend fun getRepoById(id: Long) : RepoResult<Repo>

    fun observePagedTrendingRepos() : Flow<PagingData<Repo>>

    fun observeSearchRepos(query: String): Flow<PagingData<Repo>>

    fun observeFavoriteRepos(): Flow<List<Repo>>

    suspend fun toggleFavorite(repo: Repo) : RepoResult<Unit>
}