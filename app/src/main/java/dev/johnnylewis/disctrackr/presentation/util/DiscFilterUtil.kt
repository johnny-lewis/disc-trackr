package dev.johnnylewis.disctrackr.presentation.util

import dev.johnnylewis.disctrackr.domain.model.Disc
import dev.johnnylewis.disctrackr.domain.model.DiscFormat
import dev.johnnylewis.disctrackr.presentation.model.Country
import dev.johnnylewis.disctrackr.presentation.model.DiscFilterState
import dev.johnnylewis.disctrackr.presentation.viewmodel.DiscScreenViewModel

fun DiscFilterState.update(discs: List<Disc>): DiscFilterState {
  val formats = discs.map { it.format }.distinct().sortedBy { it::class.simpleName }
  val countries = discs
    .mapNotNull { disc ->
      disc.countryCode?.let { CountryUtil.getCountryFromCode(it) }
    }.distinct().sortedBy { it.name }
  val distributors = discs
    .mapNotNull { it.distributor }.filterNot { it.isBlank() }.distinct().sorted()

  return copy(
    selection = selection.copy(
      format = if (selection.format in formats) selection.format else null,
      country = if (selection.country in countries) selection.country else null,
      distributor = if (selection.distributor in distributors) selection.distributor else null,
    ),
    options = DiscFilterState.Options(
      formats = formats,
      countries = countries,
      distributors = distributors,
    ),
  )
}

fun DiscScreenViewModel.State.withFilter(format: DiscFormat?): DiscScreenViewModel.State =
  copy(
    filterState = filterState.copy(
      selection = filterState.selection.copy(
        format = format,
      ),
    ),
  )

fun DiscScreenViewModel.State.withFilter(country: Country?): DiscScreenViewModel.State =
  copy(
    filterState = filterState.copy(
      selection = filterState.selection.copy(
        country = country,
      ),
    ),
  )

fun DiscScreenViewModel.State.withFilter(distributor: String?): DiscScreenViewModel.State =
  copy(
    filterState = filterState.copy(
      selection = filterState.selection.copy(
        distributor = distributor,
      ),
    ),
  )
