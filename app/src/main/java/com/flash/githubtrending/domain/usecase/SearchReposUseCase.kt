package com.flash.githubtrending.domain.usecase

import com.flash.githubtrending.core.Result
import com.flash.githubtrending.domain.model.Repo
import com.flash.githubtrending.domain.repository.RepoRepository
import javax.inject.Inject

class SearchReposUseCase @Inject constructor(
    private val repository: RepoRepository
) {
    suspend operator fun invoke(query: String) : Result<List<Repo>> =
        repository.searchRepos(query)
}