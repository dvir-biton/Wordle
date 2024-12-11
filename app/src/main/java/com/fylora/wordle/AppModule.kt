package com.fylora.wordle

import android.content.Context
import com.fylora.wordle.data.DataStoreStatisticsService
import com.fylora.wordle.data.StatisticsService
import com.fylora.wordle.data.TextWordDataSource
import com.fylora.wordle.data.WordDataSource
import com.fylora.wordle.ui.MapCharacterToStateUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideWordDataSource(
        @ApplicationContext
        context: Context
    ): WordDataSource {
        return TextWordDataSource(context)
    }

    @Provides
    @Singleton
    fun provideStatisticsService(
        @ApplicationContext
        context: Context
    ): StatisticsService {
       return DataStoreStatisticsService(context)
    }

    @Provides
    @Singleton
    fun provideUseCase(): MapCharacterToStateUseCase {
        return MapCharacterToStateUseCase()
    }
}