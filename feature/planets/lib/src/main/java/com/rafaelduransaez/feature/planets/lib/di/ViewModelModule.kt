package com.rafaelduransaez.feature.planets.lib.di

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.rafaelduransaez.core.common.formatter.NumberFormatter
import com.rafaelduransaez.feature.planets.presentation.detail.mapper.PlanetDetailUiMapper
import com.rafaelduransaez.feature.planets.presentation.detail.mapper.PlanetDetailUiMapperImpl
import com.rafaelduransaez.feature.planets.presentation.list.mapper.PlanetListUiMapper
import com.rafaelduransaez.feature.planets.presentation.list.mapper.PlanetListUiMapperImpl
import com.rafaelduransaez.feature.planets.presentation.navigation.PlanetDetail
import com.rafaelduransaez.feature.planets.presentation.utils.PlanetUid
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    @PlanetUid
    fun providePlanetDetailUid(savedStateHandle: SavedStateHandle): String =
        savedStateHandle.toRoute<PlanetDetail>().planetUid

    @Provides
    @ViewModelScoped
    fun providePlanetDetailMapper(numberFormatter: NumberFormatter): PlanetDetailUiMapper {
        return PlanetDetailUiMapperImpl(numberFormatter)
    }

    @Provides
    @ViewModelScoped
    fun providePlanetListMapper(): PlanetListUiMapper {
        return PlanetListUiMapperImpl()
    }

}