package com.flash.githubtrending.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.flash.githubtrending.data.local.dao.FavoriteDao
import com.flash.githubtrending.data.local.dao.RepoDao
import com.flash.githubtrending.data.local.entity.FavoriteEntity
import com.flash.githubtrending.data.local.entity.RepoEntity

@Database(
    entities = [RepoEntity::class, FavoriteEntity::class],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun repoDao(): RepoDao
    abstract fun favoriteDao(): FavoriteDao
}