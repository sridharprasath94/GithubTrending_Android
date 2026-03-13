package com.flash.githubtrending.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repos")
data class RepoEntity(
    @PrimaryKey
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