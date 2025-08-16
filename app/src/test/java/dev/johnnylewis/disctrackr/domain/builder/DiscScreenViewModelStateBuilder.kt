package dev.johnnylewis.disctrackr.domain.builder

import dev.johnnylewis.disctrackr.domain.model.Disc
import dev.johnnylewis.disctrackr.presentation.model.DiscFilterState
import dev.johnnylewis.disctrackr.presentation.viewmodel.DiscScreenViewModel

fun buildDiscScreenViewModelState(
  subState: DiscScreenViewModel.State.SubState = DiscScreenViewModel.State.SubState.Initial,
  discs: List<Disc> = listOf(buildDisc()),
  isDiscFormExpanded: Boolean = false,
  shouldClearDiscFormState: Boolean = false,
  filterState: DiscFilterState = buildDiscFilterState(),
): DiscScreenViewModel.State =
  DiscScreenViewModel.State(
    subState = subState,
    discs = discs,
    isDiscFormExpanded = isDiscFormExpanded,
    shouldClearDiscFormState = shouldClearDiscFormState,
    filterState = filterState,
  )
