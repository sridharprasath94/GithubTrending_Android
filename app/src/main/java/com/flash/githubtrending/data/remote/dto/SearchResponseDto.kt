package com.flash.githubtrending.data.remote.dto

import com.flash.githubtrending.domain.model.Repo
import com.google.gson.annotations.SerializedName

data class SearchReposResponseDto(
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("items")
    val items: List<RepoDto>
)

data class RepoDto(
    val id: Long?,
    val name: String?,
    @SerializedName("full_name") val fullName: String?,
    val description: String?,
    @SerializedName("stargazers_count") val stars: Int?,
    val forks: Int?,
    val language: String?,
    @SerializedName("html_url") val htmlUrl: String?,
    val owner: OwnerDto?
)

data class OwnerDto(
    @SerializedName("login") val login: String?,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("html_url") val htmlUrl: String?
)

/**
 * DTO
 */
fun RepoDto.toDomain(isFavorite: Boolean = false): Repo {
    return Repo(
        id = id ?: -1L,
        name = name.orEmpty(),
        fullName = fullName.orEmpty(),
        description = description,
        stars = stars ?: 0,
        forks = forks ?: 0,
        language = language,
        ownerName = owner?.login.orEmpty(),
        ownerAvatarUrl = owner?.avatarUrl,
        ownerHtmlUrl = owner?.htmlUrl.orEmpty(),
        repoHtmlUrl = htmlUrl.orEmpty(),
        isFavorite = isFavorite
    )
}

fun List<RepoDto>.toDomainList(): List<Repo> = map { it.toDomain(isFavorite = false) }