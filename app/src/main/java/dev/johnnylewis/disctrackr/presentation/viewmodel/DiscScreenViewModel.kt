package dev.johnnylewis.disctrackr.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.johnnylewis.disctrackr.domain.model.Disc
import dev.johnnylewis.disctrackr.domain.model.DiscFilter
import dev.johnnylewis.disctrackr.domain.model.DiscFormat
import dev.johnnylewis.disctrackr.domain.repository.DatabaseRepositoryContract
import dev.johnnylewis.disctrackr.domain.usecase.GetDiscsUseCase
import dev.johnnylewis.disctrackr.presentation.mapper.mapToDisc
import dev.johnnylewis.disctrackr.presentation.model.Country
import dev.johnnylewis.disctrackr.presentation.model.DiscFilterState
import dev.johnnylewis.disctrackr.presentation.model.DiscFormResult
import dev.johnnylewis.disctrackr.presentation.util.update
import dev.johnnylewis.disctrackr.presentation.util.withFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.KClass

@HiltViewModel
class DiscScreenViewModel @Inject constructor(
  getDiscs: GetDiscsUseCase,
  private val discRepository: DatabaseRepositoryContract,
) : ViewModel() {
  private val _state = MutableStateFlow<State>(State())
  val state = _state.asStateFlow()

  private val _filterFlow = MutableStateFlow(DiscFilter())

  init {
    getDiscs(filterFlow = _filterFlow.asStateFlow())
      .onSuccess { discsFlow ->
        viewModelScope.launch {
          discsFlow.collect { discs ->
            if (discs.isEmpty() && _filterFlow.value.hasFilters()) {
              _filterFlow.value = DiscFilter()
            }
            _state.value = _state.value.copy(
              subState = if (discs.isEmpty()) {
                State.SubState.Empty
              } else {
                State.SubState.Loaded
              },
              discs = discs,
              filterState = _state.value.filterState.update(discs),
            )
          }
        }
      }.onFailure {
        _state.value = _state.value.copy(
          subState = State.SubState.Error,
        )
      }
  }

  fun onEvent(event: Event) {
    when (event) {
      is Event.DiscFormExpandedChanged -> onDiscFormExpandedChanged(event.isExpanded)
      Event.DiscFormStateCleared -> onDiscFormStateCleared()
      is Event.DiscFormSubmitted -> onDiscFormSubmitted(event.result)
      is Event.DiscDeleted -> onDiscDeleted(event.id)
      is Event.FormatFilterChanged -> onFormatFilterChanged(event.format)
      is Event.CountryFilterChanged -> onCountryFilterChanged(event.country)
      is Event.DistributorFilterChanged -> onDistributorFilterChanged(event.distributor)
    }
  }

  private fun onDiscFormExpandedChanged(isExpanded: Boolean) {
    _state.value = _state.value.copy(
      isDiscFormExpanded = isExpanded,
      shouldClearDiscFormState = if (!isExpanded) true else _state.value.shouldClearDiscFormState,
    )
  }

  private fun onDiscFormStateCleared() {
    _state.value = _state.value.copy(
      shouldClearDiscFormState = false,
    )
  }

  private fun onDiscFormSubmitted(result: DiscFormResult) {
    onDiscFormExpandedChanged(isExpanded = false)
    viewModelScope.launch {
      discRepository.addDisc(result.mapToDisc())
    }
  }

  private fun onDiscDeleted(id: Int?) {
    id?.let {
      viewModelScope.launch {
        discRepository.deleteDisc(id)
      }
    }
  }

  private fun onFormatFilterChanged(format: KClass<out DiscFormat>?) {
    _state.value = _state.value.withFilter(format)
    _filterFlow.value = _filterFlow.value.copy(format = format)
  }

  private fun onCountryFilterChanged(country: Country?) {
    _state.value = _state.value.withFilter(country)
    _filterFlow.value = _filterFlow.value.copy(countryCode = country?.code)
  }

  private fun onDistributorFilterChanged(distributor: String?) {
    _state.value = _state.value.withFilter(distributor)
    _filterFlow.value = _filterFlow.value.copy(distributor = distributor)
  }

  private fun DiscFilter.hasFilters(): Boolean =
    format != null || countryCode != null || distributor != null

  data class State(
    val subState: SubState = SubState.Initial,
    val discs: List<Disc> = emptyList(),
    val isDiscFormExpanded: Boolean = false,
    val shouldClearDiscFormState: Boolean = false,
    val filterState: DiscFilterState = DiscFilterState(),
  ) {
    enum class SubState {
      Initial,
      Empty,
      Loaded,
      Error,
    }
  }

  sealed interface Event {
    data class DiscFormExpandedChanged(val isExpanded: Boolean) : Event
    data object DiscFormStateCleared : Event
    data class DiscFormSubmitted(val result: DiscFormResult) : Event
    data class DiscDeleted(val id: Int?) : Event
    data class FormatFilterChanged(val format: KClass<out DiscFormat>?) : Event
    data class CountryFilterChanged(val country: Country?) : Event
    data class DistributorFilterChanged(val distributor: String?) : Event
  }
}
