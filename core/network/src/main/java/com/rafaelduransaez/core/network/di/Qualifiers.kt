package com.rafaelduransaez.core.network.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SwapiApiUrl

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SwapiRetrofitClient