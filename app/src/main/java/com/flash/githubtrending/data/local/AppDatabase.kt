package com.flash.githubtrending.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.flash.githubtrending.data.local.dao.RepoDao
import com.flash.githubtrending.data.local.entity.RepoEntity

@Database(
    entities = [RepoEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
}