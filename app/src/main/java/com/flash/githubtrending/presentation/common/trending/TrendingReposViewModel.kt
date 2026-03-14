package com.flash.githubtrending.presentation.common.trending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flash.githubtrending.core.Result
import com.flash.githubtrending.domain.error.DomainError
import com.flash.githubtrending.domain.model.Repo
import com.flash.githubtrending.domain.usecase.ObserveTrendingReposUseCase
import com.flash.githubtrending.domain.usecase.RefreshTrendingReposUseCase
import com.flash.githubtrending.domain.usecase.SearchReposUseCase
import com.flash.githubtrending.domain.usecase.ToggleFavouritesUseCase
import com.flash.githubtrending.presentation.error.UIError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class TrendingReposViewModel @Inject constructor(
    observeTrendingReposUseCase: ObserveTrendingReposUseCase,
    private val refreshTrendingRepos: RefreshTrendingReposUseCase,
    private val searchRepos: SearchReposUseCase,
    private val toggleFavouritesUseCase: ToggleFavouritesUseCase
) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<Repo>?>(null)
    private val _isLoading = MutableStateFlow(false)
    private val _events = MutableSharedFlow<UIError>()
    val events = _events.asSharedFlow()

    private val searchQuery = MutableStateFlow("")

    init {
        viewModelScope.launch {
            searchQuery
                .debounce(400)
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.isBlank()) {
                        clearSearch()
                    } else {
                        performSearch(query)
                    }
                }
        }
        refresh()
    }

    private val trendingReposFlow: StateFlow<List<Repo>> =
        observeTrendingReposUseCase()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val uiState: StateFlow<TrendingReposUiState> =
        combine(
            trendingReposFlow,
            _searchResults,
            _isLoading
        ) { trending, searchResults, isLoading ->
            val baseList = searchResults ?: trending

            TrendingReposUiState(
                isLoading = isLoading,
                repos = baseList.sortedBy {
                    if (it.isFavorite) 0 else 1
                }
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TrendingReposUiState()
        )

    fun refresh() {
        viewModelScope.launch {
            _searchResults.value = null
            _isLoading.value = true

            when (val result = refreshTrendingRepos()) {
                is Result.Success -> {
                    _isLoading.value = false
                }

                is Result.Error -> {
                    _isLoading.value = false
                    _events.emit(UIError.from(result.error))
                }
            }
        }
    }

    private suspend fun performSearch(query: String) {
        if (query.isBlank()) {
            _searchResults.value = null
            return
        }

        _isLoading.value = true

        when (val result = searchRepos(query)) {
            is Result.Success -> {
                _searchResults.value = result.data
                _isLoading.value = false
            }

            is Result.Error -> {
                _isLoading.value = false
                _events.emit(UIError.from(result.error))
            }
        }
    }

    fun toggleFavorite(repo: Repo) {
        viewModelScope.launch {
            when (val result = toggleFavouritesUseCase(repo)) {
                is Result.Success -> {
                    _searchResults.value = _searchResults.value?.map {
                        if (it.id == repo.id)
                            it.copy(isFavorite = !it.isFavorite)
                        else it
                    }
                }

                is Result.Error -> {
                    _events.emit(UIError.from(result.error))
                }
            }
        }
    }

    fun clearSearch() {
        _searchResults.value = null
    }

    fun onSearchQueryChanged(query: String) {
        searchQuery.value = query
    }
}
