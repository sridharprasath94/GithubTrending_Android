package com.flash.githubtrending.presentation.common.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flash.githubtrending.domain.usecase.ObserveFavoriteReposUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoriteReposViewModel @Inject constructor(
    observeFavoriteReposUseCase: ObserveFavoriteReposUseCase
) : ViewModel() {
    val state: StateFlow<FavoriteReposUiState> =
        observeFavoriteReposUseCase()
            .map { repos ->
                if (repos.isEmpty()) {
                    FavoriteReposUiState.Empty
                } else {
                    FavoriteReposUiState.Success(
                        repos.sortedByDescending { it.isFavorite }
                    )
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = FavoriteReposUiState.Loading
            )
}