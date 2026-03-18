package com.flash.githubtrending.presentation.common.details

import com.flash.githubtrending.domain.model.Repo

sealed class RepoDetailsUiState {
    object Initial : RepoDetailsUiState()
    data class Success(val repo: Repo) : RepoDetailsUiState()
}