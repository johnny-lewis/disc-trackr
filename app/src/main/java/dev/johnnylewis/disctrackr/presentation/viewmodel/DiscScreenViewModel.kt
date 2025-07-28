package dev.johnnylewis.disctrackr.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.johnnylewis.disctrackr.domain.model.Disc
import dev.johnnylewis.disctrackr.domain.repository.DatabaseRepositoryContract
import dev.johnnylewis.disctrackr.presentation.mapper.mapToDisc
import dev.johnnylewis.disctrackr.presentation.mapper.mapToPresentation
import dev.johnnylewis.disctrackr.presentation.model.DiscFormResult
import dev.johnnylewis.disctrackr.presentation.model.DiscItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscScreenViewModel @Inject constructor(
  private val discRepository: DatabaseRepositoryContract,
) : ViewModel() {
  private val _state = MutableStateFlow<State>(State())
  val state = _state.asStateFlow()

  init {
    discRepository.getAllDiscs()
      .onSuccess { discsFlow ->
        viewModelScope.launch {
          discsFlow.collect { discs ->
            _state.value = _state.value.copy(
              subState = if (discs.isEmpty()) {
                State.SubState.Empty
              } else {
                State.SubState.Loaded
              },
              discs = discs.map(Disc::mapToPresentation),
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

  data class State(
    val subState: SubState = SubState.Initial,
    val discs: List<DiscItem> = emptyList(),
    val isDiscFormExpanded: Boolean = false,
    val shouldClearDiscFormState: Boolean = false,
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
  }
}
