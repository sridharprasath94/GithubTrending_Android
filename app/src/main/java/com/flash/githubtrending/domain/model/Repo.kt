package com.flash.githubtrending.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
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
    val ownerHtmlUrl: String,
    val repoHtmlUrl: String,
    val isFavorite: Boolean
) : Parcelable