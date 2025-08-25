package dev.johnnylewis.disctrackr.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.johnnylewis.disctrackr.data.database.AppDatabase
import dev.johnnylewis.disctrackr.data.repository.DatabaseRepository
import dev.johnnylewis.disctrackr.data.repository.ExternalAppRepository
import dev.johnnylewis.disctrackr.domain.repository.DatabaseRepositoryContract
import dev.johnnylewis.disctrackr.domain.repository.ExternalAppRepositoryContract
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
  @Singleton
  @Provides
  fun provideAppDatabase(
    @ApplicationContext context: Context,
  ): AppDatabase =
    AppDatabase.build(context)

  @Singleton
  @Provides
  fun provideDatabaseRepository(
    appDatabase: AppDatabase,
  ): DatabaseRepositoryContract =
    DatabaseRepository(
      appDatabase = appDatabase,
      discDao = appDatabase.discDao(),
    )

  @Singleton
  @Provides
  fun provideExternalAppRepository(
    @ApplicationContext context: Context,
  ): ExternalAppRepositoryContract =
    ExternalAppRepository(
      context = context,
    )
}
