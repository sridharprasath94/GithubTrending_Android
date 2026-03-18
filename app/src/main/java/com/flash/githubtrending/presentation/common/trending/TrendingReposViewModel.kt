package com.flash.githubtrending.presentation.common.trending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.flash.githubtrending.core.Result
import com.flash.githubtrending.domain.model.Repo
import com.flash.githubtrending.domain.usecase.ObservePagedTrendingReposUseCase
import com.flash.githubtrending.domain.usecase.SearchReposUseCase
import com.flash.githubtrending.domain.usecase.ToggleFavouritesUseCase
import com.flash.githubtrending.presentation.error.UIError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class TrendingReposViewModel @Inject constructor(
    observePagedTrendingReposUseCase: ObservePagedTrendingReposUseCase,
    private val searchReposUseCase: SearchReposUseCase,
    private val toggleFavouritesUseCase: ToggleFavouritesUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(TrendingReposUiState())
    val state: StateFlow<TrendingReposUiState> = _state.asStateFlow()
    private val _searchResults = MutableStateFlow<PagingData<Repo>?>(null)
    private val _events = MutableSharedFlow<UIError>()
    val events = _events.asSharedFlow()

    private val _pagedRepos: Flow<PagingData<Repo>> =
        observePagedTrendingReposUseCase()
            .cachedIn(viewModelScope)

    private val _searchQuery = MutableStateFlow("")

    val repoFlow: Flow<PagingData<Repo>> =
        combine(
            _pagedRepos,
            _searchResults
        ) { paging, search ->
            search ?: paging
        }
    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            _searchQuery
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
    }


    private fun performSearch(query: String) {
        if (query.isBlank()) {
            clearSearch()
            return
        }

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            searchReposUseCase(query)
                .collectLatest { pagingData ->
                    _searchResults.value = pagingData
                    _state.update {
                        it.copy(isLoading = false)
                    }
                }
        }

    }

    fun toggleFavorite(repo: Repo) {
        viewModelScope.launch {
            when (val result = toggleFavouritesUseCase(repo)) {
                is Result.Success -> {
                }

                is Result.Error -> {
                    _events.emit(UIError.from(result.error))
                }
            }
        }
    }

    fun clearSearch() {
        _searchResults.value = null
        _state.update {
            it.copy(isLoading = false)
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}
