package dev.johnnylewis.disctrackr.domain.repository

import com.github.michaelbull.result.Result
import dev.johnnylewis.disctrackr.domain.contract.AddOrUpdateDiscContract
import dev.johnnylewis.disctrackr.domain.model.Disc
import kotlinx.coroutines.flow.Flow

interface DatabaseRepositoryContract : AddOrUpdateDiscContract {
  fun getAllDiscs(): Result<Flow<List<Disc>>, Throwable>
  fun getDisc(id: Int): Result<Flow<Disc?>, Throwable>
  suspend fun upsertDisc(disc: Disc): Result<Unit, Throwable>
  suspend fun addDiscs(discs: List<Disc>): Result<Unit, Throwable>
  suspend fun deleteDisc(id: Int): Result<Unit, Throwable>

  override suspend fun addOrUpdate(disc: Disc): Result<Unit, Throwable> =
    upsertDisc(disc)
}
