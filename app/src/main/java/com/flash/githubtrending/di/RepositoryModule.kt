package com.flash.githubtrending.di

import com.flash.githubtrending.data.repository.RepoRepositoryImpl
import com.flash.githubtrending.domain.repository.RepoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepoRepository(
        impl: RepoRepositoryImpl
    ): RepoRepository
}