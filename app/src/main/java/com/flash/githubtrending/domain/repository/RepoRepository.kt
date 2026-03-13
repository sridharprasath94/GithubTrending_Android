package com.flash.githubtrending.domain.repository

import com.flash.githubtrending.domain.model.Repo
import  com.flash.githubtrending.core.Result
import kotlinx.coroutines.flow.Flow

interface RepoRepository {

    fun observeTrendingRepos(): Flow<List<Repo>>

    suspend fun refreshTrendingRepos(): Result<Unit>

    suspend fun searchRepos(query: String): Result<List<Repo>>

    suspend fun toggleFavorite(repo: Repo) : Result<Unit>
}