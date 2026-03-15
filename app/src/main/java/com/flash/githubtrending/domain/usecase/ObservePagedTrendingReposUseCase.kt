package com.flash.githubtrending.domain.usecase

import androidx.paging.PagingData
import com.flash.githubtrending.domain.model.Repo
import com.flash.githubtrending.domain.repository.RepoRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObservePagedTrendingReposUseCase @Inject constructor(
    private val repository: RepoRepository
) {
    operator fun invoke(): Flow<PagingData<Repo>> {
        return repository.observePagedTrendingRepos()
    }
}