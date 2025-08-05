package dev.johnnylewis.disctrackr.domain.usecase

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map
import dev.johnnylewis.disctrackr.domain.model.Disc
import dev.johnnylewis.disctrackr.domain.model.DiscFilter
import dev.johnnylewis.disctrackr.domain.repository.DatabaseRepositoryContract
import dev.johnnylewis.disctrackr.domain.util.filterWith
import kotlinx.coroutines.flow.Flow

class GetDiscsUseCase(
  private val databaseRepository: DatabaseRepositoryContract,
) {
  operator fun invoke(filterFlow: Flow<DiscFilter>): Result<Flow<List<Disc>>, Throwable> =
    databaseRepository.getAllDiscs()
      .map { discsFlow ->
        discsFlow.filterWith(filterFlow) { disc, filter ->
          (filter.format == null || disc.format::class == filter.format) &&
            (filter.countryCode == null || disc.countryCode == filter.countryCode) &&
            (filter.distributor == null || disc.distributor == filter.distributor)
        }
      }
}
