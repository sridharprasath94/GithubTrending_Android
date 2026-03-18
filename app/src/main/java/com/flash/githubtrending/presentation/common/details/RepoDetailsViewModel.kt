package com.flash.githubtrending.presentation.common.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flash.githubtrending.core.RepoResult
import com.flash.githubtrending.domain.model.Repo
import com.flash.githubtrending.domain.usecase.GetRepoUseCase
import com.flash.githubtrending.presentation.error.UIError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RepoDetailsViewModel @Inject constructor(
    private val getRepoUseCase: GetRepoUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<RepoDetailsUiState>(RepoDetailsUiState.Initial)
    val state = _state.asStateFlow()
    private val _events = MutableSharedFlow<UIError>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val events = _events.asSharedFlow()

    fun loadRepo(id: Long) {
        viewModelScope.launch {
            _state.value = RepoDetailsUiState.Initial
            when (val result = getRepoUseCase.invoke(id)) {
                is RepoResult.Success<Repo> -> {
                    _state.value = RepoDetailsUiState.Success(result.data)
                }

                is RepoResult.Error -> _events.emit(UIError.from(result.error))
            }
        }
    }
}