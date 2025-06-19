package com.rafaelduransaez.core.common.di

import com.rafaelduransaez.core.common.formatter.DefaultNumberFormatter
import com.rafaelduransaez.core.common.formatter.NumberFormatter
import com.rafaelduransaez.core.common.time.Clock
import com.rafaelduransaez.core.common.time.SystemClock
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class StaleTimeout

@Module
@InstallIn(SingletonComponent::class)
object ConfigModule {

    private const val ONE_HOUR_IN_MS = /*60 **/ 60 * 1000

    @Provides
    @Singleton
    @StaleTimeout
    fun provideStaleTimeout(): Int = ONE_HOUR_IN_MS

    @Provides
    @Singleton
    fun provideClock(): Clock = SystemClock()

    @Provides
    @Singleton
    fun provideFormatter(): NumberFormatter = DefaultNumberFormatter()
}