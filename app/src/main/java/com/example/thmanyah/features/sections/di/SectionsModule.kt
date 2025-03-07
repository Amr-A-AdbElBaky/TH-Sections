package com.example.thmanyah.features.sections.di

import com.example.thmanyah.features.sections.data.repository.SectionsRepositoryImpl
import com.example.thmanyah.features.sections.data.source.remote.SectionsApi
import com.example.thmanyah.features.sections.domain.repository.SectionsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit


@Module
@InstallIn(ViewModelComponent::class)
abstract class SectionsRepositoryModule {
    @Binds
    abstract fun bindSectionsRepository(
        repository: SectionsRepositoryImpl
    ): SectionsRepository
}

@Module
@InstallIn(ViewModelComponent::class)
object SectionsApiModule {
    @Provides
    fun provideSectionApi(retrofit: Retrofit): SectionsApi {
        return retrofit.create(SectionsApi::class.java)
    }
}