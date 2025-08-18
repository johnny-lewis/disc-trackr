package dev.johnnylewis.disctrackr.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.johnnylewis.disctrackr.domain.model.Disc
import dev.johnnylewis.disctrackr.domain.repository.OpenWebLinkContract
import dev.johnnylewis.disctrackr.domain.repository.OpenWebLinkContract.Companion.invoke
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
  private val openWebLink: OpenWebLinkContract,
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
      Event.Edit -> onEdit()
      is Event.OpenInBrowser -> onOpenInBrowser(event.bluRayId)
    }
  }

  private fun onBackPressed() {
    viewModelScope.launch {
      navigationFlow.emit(NavigationGraph.Route.Pop)
    }
  }

  private fun onOpenInBrowser(bluRayId: String) {
    openWebLink(url = BLURAY_URL.replace("{{id}}", bluRayId))
  }

  private fun onEdit() { }

  sealed interface State {
    data object Initial : State
    data class Loaded(
      val disc: Disc,
    ) : State
  }

  sealed interface Event {
    data object BackPressed : Event
    data object Edit : Event
    data class OpenInBrowser(val bluRayId: String) : Event
  }

  private companion object {
    const val BLURAY_URL = "https://www.blu-ray.com/movies/x-Blu-ray/{{id}}/"
  }
}
