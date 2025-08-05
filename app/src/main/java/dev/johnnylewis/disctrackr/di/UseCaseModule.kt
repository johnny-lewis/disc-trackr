package dev.johnnylewis.disctrackr.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.johnnylewis.disctrackr.domain.repository.DatabaseRepositoryContract
import dev.johnnylewis.disctrackr.domain.usecase.GetDiscsUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
  @Singleton
  @Provides
  fun provideGetDiscsUseCase(
    databaseRepository: DatabaseRepositoryContract,
  ): GetDiscsUseCase =
    GetDiscsUseCase(
      databaseRepository = databaseRepository,
    )
}
