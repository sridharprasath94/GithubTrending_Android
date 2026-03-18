package com.flash.githubtrending.domain.usecase

import com.flash.githubtrending.domain.repository.RepoRepository
import javax.inject.Inject

class GetRepoUseCase @Inject constructor(
    private val repository: RepoRepository
) {
    suspend operator fun invoke(id: Long) = repository.getRepoById(id = id)
}