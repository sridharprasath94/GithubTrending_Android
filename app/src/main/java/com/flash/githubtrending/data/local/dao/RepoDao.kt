package com.flash.githubtrending.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.flash.githubtrending.data.local.entity.RepoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RepoDao {

    @Query("SELECT * FROM repos ORDER BY stars DESC")
    fun observeRepos(): Flow<List<RepoEntity>>

    @Query("SELECT * FROM repos WHERE id = :id LIMIT 1")
    suspend fun getRepoById(id: Long): RepoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepos(repos: List<RepoEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(repo: RepoEntity)

    @Query("DELETE FROM repos WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM repos")
    suspend fun clearRepos()

    @Query("SELECT id FROM repos WHERE isFavorite = 1")
    suspend fun getFavoriteIds(): List<Long>

    @Query("SELECT * FROM repos WHERE isFavorite = 1")
    fun observeFavorites(): Flow<List<RepoEntity>>

    @Query("UPDATE repos SET isFavorite = NOT isFavorite WHERE id = :id")
    suspend fun toggleFavorite(id: Long)
}