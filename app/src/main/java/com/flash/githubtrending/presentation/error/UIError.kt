package com.flash.githubtrending.presentation.error

import com.flash.githubtrending.domain.error.DomainError

sealed class UIError(val message: String) {

    object NoInternet : UIError("No internet connection. Please check your network.")

    object RateLimited : UIError("GitHub API rate limit exceeded. Please try again later.")

    object BadRequest : UIError("Something went wrong with the request.")

    object Unknown : UIError("An unexpected error occurred. Please try again.")

    companion object {

        fun from(domainError: DomainError): UIError {
            return when (domainError) {
                DomainError.NetworkUnavailable -> NoInternet
                DomainError.RateLimitExceeded -> RateLimited
                DomainError.InvalidRequest -> BadRequest
                DomainError.Unknown -> Unknown
            }
        }
    }
}