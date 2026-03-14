package com.flash.githubtrending.presentation.common.trending

import com.flash.githubtrending.domain.model.Repo
import com.flash.githubtrending.presentation.error.UIError

data class TrendingReposUiState(
    val isLoading: Boolean = false,
    val repos: List<Repo> = emptyList(),
    val error: UIError? = null
)