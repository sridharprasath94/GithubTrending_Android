package com.flash.githubtrending.presentation.common.favorites

import com.flash.githubtrending.domain.model.Repo

data class FavoriteReposUiState(
    val isLoading: Boolean = false,
    val repos: List<Repo> = emptyList(),
    val error: String? = null
)