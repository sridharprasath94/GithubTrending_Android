package com.flash.githubtrending.data.utils

import com.flash.githubtrending.data.local.dao.RepoDao
import com.flash.githubtrending.data.local.entity.RepoEntity
import com.flash.githubtrending.domain.model.Repo
import kotlinx.coroutines.flow.first


suspend fun RepoDao.getFavoriteIdsSet(): Set<Long> {
    return getFavoriteIds().toSet()
}

fun List<Repo>.applyFavorites(favoriteIds: Set<Long>): List<Repo> {
    return map { repo ->
        repo.copy(isFavorite = repo.id in favoriteIds)
    }
}

suspend fun RepoDao.observeFavoriteReposOnce(): List<RepoEntity> {
    return observeFavoriteRepos().first()
}