package dev.johnnylewis.disctrackr.domain.usecase

import com.github.michaelbull.result.Result
import dev.johnnylewis.disctrackr.domain.model.Disc
import dev.johnnylewis.disctrackr.domain.repository.DatabaseRepositoryContract
import kotlinx.coroutines.flow.Flow

class GetDiscDetailsUseCase(
  private val databaseRepository: DatabaseRepositoryContract,
) {
  // TODO: Will have movie details here as well
  operator fun invoke(id: Int): Result<Flow<Disc?>, Throwable> =
    databaseRepository.getDisc(id)
}
