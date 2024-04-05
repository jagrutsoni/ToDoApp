package com.sor.mytodo

import com.sor.mytodo.data.Repository
import com.sor.mytodo.data.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun providesRepository(): Repository {
            return RepositoryImpl()
    }
}