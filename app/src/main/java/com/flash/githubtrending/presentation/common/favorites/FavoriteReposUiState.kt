package com.flash.githubtrending.presentation.common.favorites

import com.flash.githubtrending.domain.model.Repo

sealed class FavoriteReposUiState {
    object Empty : FavoriteReposUiState()
    object Loading : FavoriteReposUiState()
    data class Success(val repos: List<Repo>) : FavoriteReposUiState()
}
