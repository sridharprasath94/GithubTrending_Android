package com.flash.githubtrending.presentation.common.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flash.githubtrending.domain.model.Repo
import com.flash.githubtrending.domain.usecase.ObserveFavoriteReposUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoriteReposViewModel @Inject constructor(
    observeFavoriteReposUseCase: ObserveFavoriteReposUseCase
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)
    private val favoriteReposFlow: StateFlow<List<Repo>> =
        observeFavoriteReposUseCase()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val uiState: StateFlow<FavoriteReposUiState> =
        combine(
            favoriteReposFlow,
            _isLoading,
            _error
        ) { favorites, isLoading, error ->
            Log.d("SridharFavoriteReposViewModel", "Combining UI state: isLoading=$isLoading, error=$error, favoritesCount=${favorites.size}")
            FavoriteReposUiState(
                isLoading = isLoading,
                repos = favorites.sortedBy {
                    if (it.isFavorite) 0 else 1
                },
                error = error
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FavoriteReposUiState(isLoading = true, repos = emptyList(), error = null)
        )
}