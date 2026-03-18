package com.flash.githubtrending.domain.usecase

import androidx.paging.PagingData
import com.flash.githubtrending.domain.model.Repo
import com.flash.githubtrending.domain.repository.RepoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchReposUseCase @Inject constructor(
    private val repository: RepoRepository
) {
    operator fun invoke(query: String) : Flow<PagingData<Repo>> =
        repository.observeSearchRepos(query)
}