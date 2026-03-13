package com.flash.githubtrending.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.flash.githubtrending.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE repoId = :id")
    suspend fun delete(id: Long)

    @Query("SELECT repoId FROM favorites")
    suspend fun getFavoriteIds(): List<Long>

    @Query("SELECT repoId FROM favorites")
    fun observeFavoriteIds(): Flow<List<Long>>
}