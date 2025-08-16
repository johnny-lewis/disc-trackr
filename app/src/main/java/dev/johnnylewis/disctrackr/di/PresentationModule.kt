package dev.johnnylewis.disctrackr.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.johnnylewis.disctrackr.presentation.NavigationGraph
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PresentationModule {
  @Singleton
  @Provides
  fun provideNavigationFlow(): MutableSharedFlow<NavigationGraph.Route> =
    MutableSharedFlow()
}
