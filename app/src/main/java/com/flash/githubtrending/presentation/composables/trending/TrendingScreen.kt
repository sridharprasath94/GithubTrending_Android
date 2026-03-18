package com.flash.githubtrending.presentation.composables.trending

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.flash.githubtrending.presentation.common.trending.TrendingReposUiState
import com.flash.githubtrending.presentation.common.trending.TrendingReposViewModel
import com.flash.githubtrending.presentation.composables.RepoRow

@Composable
fun TrendingScreen(
    onRepoClick: (String) -> Unit,
    viewModel: TrendingReposViewModel = hiltViewModel()
) {
    val lazyPagingItems = viewModel.repoFlow.collectAsLazyPagingItems()
    val uiState: TrendingReposUiState by viewModel.state.collectAsStateWithLifecycle()


    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(searchQuery) {
        if (searchQuery.isBlank()) {
            viewModel.clearSearch()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.onSearchQueryChanged(it)
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                )
            )

            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                LazyColumn {
                    items(lazyPagingItems.itemCount) { index ->
                        val repo = lazyPagingItems[index]
                        repo?.let { it ->
                            RepoRow(
                                repo = it,
                                onRepoClick = {
                                    onRepoClick(it.name)
                                },
                                onToggleFavorite = {
                                    viewModel.toggleFavorite(it)
                                    lazyPagingItems.refresh()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}