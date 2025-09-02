package dev.johnnylewis.disctrackr.domain.contract

import com.github.michaelbull.result.Result
import dev.johnnylewis.disctrackr.domain.model.Disc

fun interface AddOrUpdateDiscContract {
  suspend fun addOrUpdate(disc: Disc): Result<Unit, Throwable>

  companion object {
    suspend operator fun AddOrUpdateDiscContract.invoke(disc: Disc) = addOrUpdate(disc)
  }
}
