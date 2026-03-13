package com.flash.githubtrending.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey
    val repoId: Long,
    var name : String,
    var fullName : String,
)