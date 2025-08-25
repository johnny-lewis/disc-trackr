package dev.johnnylewis.disctrackr.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.johnnylewis.disctrackr.domain.contract.AddOrUpdateDiscContract
import dev.johnnylewis.disctrackr.domain.contract.OpenWebLinkContract
import dev.johnnylewis.disctrackr.domain.repository.DatabaseRepositoryContract
import dev.johnnylewis.disctrackr.domain.repository.ExternalAppRepositoryContract
import dev.johnnylewis.disctrackr.domain.usecase.GetDiscDetailsUseCase
import dev.johnnylewis.disctrackr.domain.usecase.GetDiscsUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {
  @Singleton
  @Provides
  fun provideGetDiscsUseCase(
    databaseRepository: DatabaseRepositoryContract,
  ): GetDiscsUseCase =
    GetDiscsUseCase(
      databaseRepository = databaseRepository,
    )

  @Singleton
  @Provides
  fun provideGetDiscDetailsUseCase(
    databaseRepository: DatabaseRepositoryContract,
  ): GetDiscDetailsUseCase =
    GetDiscDetailsUseCase(
      databaseRepository = databaseRepository,
    )

  @Singleton
  @Provides
  fun provideAddOrUpdateDiscContract(
    databaseRepository: DatabaseRepositoryContract,
  ): AddOrUpdateDiscContract =
    databaseRepository

  @Singleton
  @Provides
  fun provideOpenWebLinkContract(
    externalAppRepository: ExternalAppRepositoryContract,
  ): OpenWebLinkContract =
    externalAppRepository
}
