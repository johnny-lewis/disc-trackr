package dev.johnnylewis.disctrackr.data.repository

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import dev.johnnylewis.disctrackr.data.database.dao.DiscDao
import dev.johnnylewis.disctrackr.data.database.entity.DiscEntity
import dev.johnnylewis.disctrackr.data.mapper.mapToDisc
import dev.johnnylewis.disctrackr.domain.model.Disc
import dev.johnnylewis.disctrackr.domain.repository.DatabaseRepositoryContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DatabaseRepository(
  private val discDao: DiscDao,
) : DatabaseRepositoryContract {
  override fun getAllDiscs(): Result<Flow<List<Disc>>, Throwable> = runCatching {
    discDao.getAll()
      .map(List<DiscEntity>::mapToDisc)
  }
}
