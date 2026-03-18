package com.flash.githubtrending.core

import com.flash.githubtrending.domain.error.DomainError


sealed class RepoResult<out T> {

    data class Success<T>(val data: T) : RepoResult<T>()

    data class Error(val error: DomainError) : RepoResult<Nothing>()
}