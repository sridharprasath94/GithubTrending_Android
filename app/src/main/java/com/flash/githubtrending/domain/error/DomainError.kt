package com.flash.githubtrending.domain.error

sealed class DomainError {

    object NetworkUnavailable : DomainError()

    object RateLimitExceeded : DomainError()

    object InvalidRequest : DomainError()

    object Unknown : DomainError()
}