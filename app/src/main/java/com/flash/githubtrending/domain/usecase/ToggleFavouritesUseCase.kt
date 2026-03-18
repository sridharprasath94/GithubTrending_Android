package com.flash.githubtrending.domain.usecase

import com.flash.githubtrending.core.RepoResult
import com.flash.githubtrending.domain.model.Repo
import com.flash.githubtrending.domain.repository.RepoRepository
import javax.inject.Inject

class ToggleFavouritesUseCase @Inject constructor(
    private val repository: RepoRepository
) {
    suspend operator fun invoke(repo: Repo): RepoResult<Unit> =
        repository.toggleFavorite(repo)

}