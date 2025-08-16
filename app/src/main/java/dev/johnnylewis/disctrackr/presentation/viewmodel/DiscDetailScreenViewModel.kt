package dev.johnnylewis.disctrackr.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.johnnylewis.disctrackr.domain.model.Disc
import dev.johnnylewis.disctrackr.domain.usecase.GetDiscDetailsUseCase
import dev.johnnylewis.disctrackr.presentation.NavigationGraph
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscDetailScreenViewModel @Inject constructor(
  private val navigationFlow: MutableSharedFlow<NavigationGraph.Route>,
  private val getDiscDetails: GetDiscDetailsUseCase,
) : ViewModel() {
  private val _state = MutableStateFlow<State>(State.Initial)
  val state = _state.asStateFlow()

  fun setDiscId(id: Int) {
    viewModelScope.launch {
      getDiscDetails(id)
        .onSuccess { disc ->
          disc?.let {
            _state.emit(
              State.Loaded(
                disc = disc,
              ),
            )
          } ?: navigationFlow.emit(NavigationGraph.Route.Pop)
        }
        .onFailure {
          navigationFlow.emit(NavigationGraph.Route.Pop)
        }
    }
  }

  fun onEvent(event: Event) {
    when (event) {
      Event.BackPressed -> onBackPressed()
    }
  }

  private fun onBackPressed() {
    viewModelScope.launch {
      navigationFlow.emit(NavigationGraph.Route.Pop)
    }
  }

  sealed interface State {
    data object Initial : State
    data class Loaded(
      val disc: Disc,
    ) : State
  }

  sealed interface Event {
    data object BackPressed : Event
  }
}
