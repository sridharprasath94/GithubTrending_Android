package com.flash.githubtrending.data.local.mapper


import com.flash.githubtrending.data.local.entity.RepoEntity
import com.flash.githubtrending.domain.model.Repo

fun RepoEntity.toDomain(): Repo {
    return Repo(
        id = id,
        name = name,
        fullName = fullName,
        description = description,
        stars = stars,
        forks = forks,
        language = language,
        ownerName = ownerName,
        ownerAvatarUrl = ownerAvatarUrl,
        ownerHtmlUrl = ownerHtmlUrl,
        repoHtmlUrl = repoHtmlUrl,
        isFavorite = isFavorite
    )
}

fun Repo.toEntity(): RepoEntity {
    return RepoEntity(
        id = id,
        name = name,
        fullName = fullName,
        description = description,
        stars = stars,
        forks = forks,
        language = language,
        ownerName = ownerName,
        ownerAvatarUrl = ownerAvatarUrl,
        ownerHtmlUrl = ownerHtmlUrl,
        repoHtmlUrl = repoHtmlUrl,
        isFavorite = isFavorite
    )
}