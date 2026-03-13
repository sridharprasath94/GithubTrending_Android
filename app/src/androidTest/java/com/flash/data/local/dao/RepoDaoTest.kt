package com.flash.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.flash.githubtrending.data.local.entity.RepoEntity
import com.flash.githubtrending.data.local.AppDatabase
import com.flash.githubtrending.data.local.dao.RepoDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class RepoDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: RepoDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        dao = db.repoDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    private fun createRepo(
        id: Long,
        stars: Int
    ) = RepoEntity(
        id = id,
        name = "Repo$id",
        fullName = "Full Repo$id",
        description = null,
        stars = stars,
        forks = 0,
        language = "Kotlin",
        ownerName = "Flash",
        ownerAvatarUrl = null
    )

    @Test
    fun insert_and_getById_returnsCorrectRepo() = runTest {
        val repo = createRepo(id = 1, stars = 100)

        dao.insertRepos(listOf(repo))

        val result = dao.getRepoById(1)

        assertEquals(repo, result)
    }

    @Test
    fun observeRepos_returnsSortedByStarsDescending() = runTest {
        val repo1 = createRepo(id = 1, stars = 10)
        val repo2 = createRepo(id = 2, stars = 50)
        val repo3 = createRepo(id = 3, stars = 20)

        dao.insertRepos(listOf(repo1, repo2, repo3))

        val result = dao.observeRepos().first()

        assertEquals(listOf(repo2, repo3, repo1), result)
    }

    @Test
    fun insert_withSameId_replacesExisting() = runTest {
        val repo1 = createRepo(id = 1, stars = 10)
        val updated = createRepo(id = 1, stars = 200)

        dao.insertRepos(listOf(repo1))
        dao.insertRepos(listOf(updated))

        val result = dao.getRepoById(1)

        assertEquals(200, result?.stars)
    }

    @Test
    fun clearRepos_removesAllData() = runTest {
        val repo = createRepo(id = 1, stars = 10)

        dao.insertRepos(listOf(repo))
        dao.clearRepos()

        val result = dao.observeRepos().first()

        assertTrue(result.isEmpty())
    }
}