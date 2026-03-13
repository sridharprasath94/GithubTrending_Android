package com.flash.githubtrending.domain.model

data class Repo(
    val id: Long,
    val name: String,
    val fullName: String,
    val description: String?,
    val stars: Int,
    val forks: Int,
    val language: String?,
    val ownerName: String,
    val ownerAvatarUrl: String?,
    val isFavorite: Boolean
)