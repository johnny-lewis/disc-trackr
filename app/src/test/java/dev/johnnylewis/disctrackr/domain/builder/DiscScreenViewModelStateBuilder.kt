package dev.johnnylewis.disctrackr.domain.builder

import dev.johnnylewis.disctrackr.domain.model.Disc
import dev.johnnylewis.disctrackr.presentation.model.DiscFilterState
import dev.johnnylewis.disctrackr.presentation.viewmodel.DiscListScreenViewModel

fun buildDiscScreenViewModelState(
  subState: DiscListScreenViewModel.State.SubState = DiscListScreenViewModel.State.SubState.Initial,
  discs: List<Disc> = listOf(buildDisc()),
  isDiscFormExpanded: Boolean = false,
  shouldClearDiscFormState: Boolean = false,
  filterState: DiscFilterState = buildDiscFilterState(),
): DiscListScreenViewModel.State =
  DiscListScreenViewModel.State(
    subState = subState,
    discs = discs,
    isDiscFormExpanded = isDiscFormExpanded,
    shouldClearDiscFormState = shouldClearDiscFormState,
    filterState = filterState,
  )
