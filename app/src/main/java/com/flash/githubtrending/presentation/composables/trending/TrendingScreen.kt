package com.flash.githubtrending.presentation.composables.trending

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flash.githubtrending.presentation.common.trending.TrendingReposUiState
import com.flash.githubtrending.presentation.common.trending.TrendingReposViewModel
import com.flash.githubtrending.presentation.composables.RepoRow

@Composable
fun TrendingScreen(
    onRepoClick: (String) -> Unit,
    viewModel: TrendingReposViewModel = hiltViewModel()
) {
    val uiState : TrendingReposUiState by viewModel.state.collectAsStateWithLifecycle()


    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(searchQuery) {
        if (searchQuery.isBlank()) {
            viewModel.clearSearch()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    )  {
        Column {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
//                        if (searchQuery.isNotBlank()) {
//                            viewModel.search(searchQuery)
//                        }
                    }
                )
            )

//            if (uiState.isLoading && uiState.repos.isEmpty()) {
//                CircularProgressIndicator()
//            } else {
//                LazyColumn {
//                    items(uiState.repos) { repo ->
//                        RepoRow(
//                            repo = repo,
//                            onRepoClick = {
//                                onRepoClick(repo.name)
//                            },
//                            onToggleFavorite = {
//                                viewModel.toggleFavorite(repo)
//                            }
//                        )
//                    }
//                }
//            }
        }
    }
}