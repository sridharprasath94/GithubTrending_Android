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

    @Query("DELETE FROM repos")
    suspend fun clearRepos()
}