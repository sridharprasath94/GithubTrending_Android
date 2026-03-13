package com.flash.githubtrending.data.remote.dto

import com.flash.githubtrending.domain.model.Repo
import com.google.gson.annotations.SerializedName

data class SearchReposResponseDto(
    @SerializedName("items")
    val items: List<RepoDto>
)

data class RepoDto(
    val id: Long,
    val name: String,
    @SerializedName("full_name") val fullName: String,
    val description: String?,
    @SerializedName("stargazers_count") val stars: Int,
    val forks: Int,
    val language: String?,
    val owner: OwnerDto
)

data class OwnerDto(
    @SerializedName("login") val login: String,
    @SerializedName("avatar_url") val avatarUrl: String
)

/**
 * DTO -> Domain mapping (Swift-style extension equivalent)
 */
fun RepoDto.toDomain(isFavorite: Boolean = false): Repo {
    return Repo(
        id = id,
        name = name,
        fullName = fullName,
        description = description,
        stars = stars,
        forks = forks,
        language = language,
        ownerName = owner.login,
        ownerAvatarUrl = owner.avatarUrl,
        isFavorite = isFavorite
    )
}

fun List<RepoDto>.toDomainList(): List<Repo> = map { it.toDomain(isFavorite = false) }