package dev.johnnylewis.disctrackr.domain.usecase

import com.github.michaelbull.result.Result
import dev.johnnylewis.disctrackr.domain.model.Disc
import dev.johnnylewis.disctrackr.domain.repository.DatabaseRepositoryContract

class GetDiscDetailsUseCase(
  private val databaseRepository: DatabaseRepositoryContract,
) {
  suspend operator fun invoke(id: Int): Result<Disc?, Throwable> =
    databaseRepository.getDisc(id)
}
