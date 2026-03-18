package com.flash.githubtrending.presentation.common.trending

sealed class TrendingReposUiState {
    object Idle : TrendingReposUiState()
    object Loading : TrendingReposUiState()
}