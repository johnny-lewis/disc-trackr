package dev.johnnylewis.disctrackr.domain.repository

import com.github.michaelbull.result.Result
import dev.johnnylewis.disctrackr.domain.model.Disc
import kotlinx.coroutines.flow.Flow

interface DatabaseRepositoryContract {
  fun getAllDiscs(): Result<Flow<List<Disc>>, Throwable>
  suspend fun addDisc(disc: Disc): Result<Unit, Throwable>
}
