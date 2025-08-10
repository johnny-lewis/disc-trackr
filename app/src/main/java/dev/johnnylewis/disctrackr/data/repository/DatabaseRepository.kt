package dev.johnnylewis.disctrackr.data.repository

import androidx.room.withTransaction
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import dev.johnnylewis.disctrackr.data.database.AppDatabase
import dev.johnnylewis.disctrackr.data.database.dao.DiscDao
import dev.johnnylewis.disctrackr.data.database.entity.DiscEntity
import dev.johnnylewis.disctrackr.data.mapper.mapToDisc
import dev.johnnylewis.disctrackr.data.mapper.mapToDiscEntity
import dev.johnnylewis.disctrackr.domain.model.Disc
import dev.johnnylewis.disctrackr.domain.repository.DatabaseRepositoryContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DatabaseRepository(
  private val appDatabase: AppDatabase,
  private val discDao: DiscDao,
) : DatabaseRepositoryContract {
  override fun getAllDiscs(): Result<Flow<List<Disc>>, Throwable> = runCatching {
    discDao.getAll()
      .map(List<DiscEntity>::mapToDisc)
  }

  override suspend fun addDisc(disc: Disc): Result<Unit, Throwable> = runCatching {
    discDao.insert(disc.mapToDiscEntity())
  }

  override suspend fun addDiscs(discs: List<Disc>): Result<Unit, Throwable> = runCatching {
    appDatabase.withTransaction {
      discDao.insert(*(discs.map(Disc::mapToDiscEntity).toTypedArray()))
    }
  }

  override suspend fun deleteDisc(id: Int): Result<Unit, Throwable> = runCatching {
    discDao.deleteById(id)
  }
}
