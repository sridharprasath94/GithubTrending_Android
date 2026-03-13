package com.flash.githubtrending.core.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val token: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()

        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Accept", "application/vnd.github+json")

        if (token.isNotBlank()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        val request = requestBuilder.build()

        return chain.proceed(request)
    }
}