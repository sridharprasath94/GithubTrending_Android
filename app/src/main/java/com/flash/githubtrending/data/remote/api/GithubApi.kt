package com.flash.githubtrending.data.remote.api

import com.flash.githubtrending.data.remote.dto.SearchReposResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {

    // Trending feed (stars filter)
    @GET("search/repositories")
    suspend fun getTrendingRepos(
        @Query("q") query: String = "stars:>10000",
        @Query("sort") sort: String = "stars",
        @Query("order") order: String = "desc",
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): SearchReposResponseDto

    // Search (user query)
    @GET("search/repositories")
    suspend fun searchRepos(
        @Query("q") query: String,
        @Query("sort") sort: String = "stars",
        @Query("order") order: String = "desc",
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): SearchReposResponseDto
}