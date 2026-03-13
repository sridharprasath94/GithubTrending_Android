package com.flash.githubtrending.domain.usecase

import com.flash.githubtrending.core.Result
import com.flash.githubtrending.domain.repository.RepoRepository
import javax.inject.Inject

class RefreshTrendingReposUseCase @Inject constructor(
    private val repository: RepoRepository
) {
    suspend operator fun invoke() : Result<Unit> =
        repository.refreshTrendingRepos()

}