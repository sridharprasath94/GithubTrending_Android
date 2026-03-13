package com.flash.githubtrending.domain.usecase

import com.flash.githubtrending.domain.repository.RepoRepository
import javax.inject.Inject

class ObserveTrendingReposUseCase @Inject constructor(
    private val repository: RepoRepository
) {
    operator fun invoke() = repository.observeTrendingRepos()
}