package dev.johnnylewis.disctrackr.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.johnnylewis.disctrackr.domain.contract.AddOrUpdateDiscContract
import dev.johnnylewis.disctrackr.domain.contract.AddOrUpdateDiscContract.Companion.invoke
import dev.johnnylewis.disctrackr.domain.contract.OpenWebLinkContract
import dev.johnnylewis.disctrackr.domain.contract.OpenWebLinkContract.Companion.invoke
import dev.johnnylewis.disctrackr.domain.model.Disc
import dev.johnnylewis.disctrackr.domain.usecase.GetDiscDetailsUseCase
import dev.johnnylewis.disctrackr.presentation.NavigationGraph
import dev.johnnylewis.disctrackr.presentation.mapper.mapToDisc
import dev.johnnylewis.disctrackr.presentation.model.DiscFormResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscDetailScreenViewModel @Inject constructor(
  private val navigationFlow: MutableSharedFlow<NavigationGraph.Route>,
  private val getDiscDetails: GetDiscDetailsUseCase,
  private val openWebLink: OpenWebLinkContract,
  private val addOrUpdateDisc: AddOrUpdateDiscContract,
) : ViewModel() {
  private val _state = MutableStateFlow<State>(State.Initial)
  val state = _state.asStateFlow()

  fun setDiscId(id: Int) {
    getDiscDetails(id)
      .onSuccess { discFlow ->
        discFlow
          .onEach { disc ->
            disc?.let {
              _state.value = when (val stateValue = _state.value) {
                State.Initial -> State.Loaded(disc = disc)
                is State.Loaded -> stateValue.copy(disc = disc)
              }
            } ?: navigateBack()
          }
          .launchIn(viewModelScope)
      }
      .onFailure {
        navigateBack()
      }
  }

  fun onEvent(event: Event) {
    when (event) {
      Event.BackPressed -> navigateBack()
      is Event.OpenInBrowser -> onOpenInBrowser(event.bluRayId)
      is Event.BottomSheetVisibilityChanged -> onBottomSheetVisibilityChanged(event.visibility)

      Event.DiscFormStateCleared -> onDiscFormStateCleared()
      is Event.DiscFormSubmitted -> onDiscFormSubmitted(event.result)

      is Event.DiscImageSelected -> onDiscImageSelected(event.url)
      Event.DiscImageSelectStateCleared -> onDiscImageSelectStateCleared()
    }
  }

  private fun navigateBack() {
    viewModelScope.launch {
      navigationFlow.emit(NavigationGraph.Route.Pop)
    }
  }

  private fun onOpenInBrowser(bluRayId: String) {
    openWebLink(url = BLURAY_URL.replace("{{id}}", bluRayId))
  }

  private fun onDiscFormStateCleared() {
    (_state.value as? State.Loaded)?.let {
      _state.value = it.copy(
        shouldClearDiscFormState = false,
      )
    }
  }

  private fun onBottomSheetVisibilityChanged(visibility: BottomSheetVisible) {
    (_state.value as? State.Loaded)?.let {
      _state.value = it.copy(
        bottomSheetVisibility = visibility,
        shouldClearDiscFormState = if (visibility != BottomSheetVisible.DiscForm) {
          true
        } else {
          it.shouldClearDiscFormState
        },
        shouldClearImageSelectState = if (visibility != BottomSheetVisible.ImageSelect) {
          true
        } else {
          it.shouldClearImageSelectState
        },
      )
    }
  }

  private fun onDiscFormSubmitted(result: DiscFormResult) {
    (_state.value as? State.Loaded)?.let {
      onBottomSheetVisibilityChanged(visibility = BottomSheetVisible.None)
      viewModelScope.launch {
        addOrUpdateDisc(result.mapToDisc(id = it.disc.id))
      }
    }
  }

  private fun onDiscImageSelected(url: String) {
    (_state.value as? State.Loaded)?.let {
      onBottomSheetVisibilityChanged(visibility = BottomSheetVisible.None)
      viewModelScope.launch {
        addOrUpdateDisc(it.disc.copy(imageUrl = url))
      }
    }
  }

  private fun onDiscImageSelectStateCleared() {
    (_state.value as? State.Loaded)?.let {
      _state.value = it.copy(
        shouldClearImageSelectState = false,
      )
    }
  }

  sealed interface State {
    data object Initial : State
    data class Loaded(
      val disc: Disc,
      val bottomSheetVisibility: BottomSheetVisible = BottomSheetVisible.None,
      val shouldClearDiscFormState: Boolean = false,
      val shouldClearImageSelectState: Boolean = false,
    ) : State
  }

  sealed interface Event {
    data object BackPressed : Event
    data class OpenInBrowser(val bluRayId: String) : Event
    data class BottomSheetVisibilityChanged(val visibility: BottomSheetVisible) : Event

    data object DiscFormStateCleared : Event
    data class DiscFormSubmitted(val result: DiscFormResult) : Event

    data class DiscImageSelected(val url: String) : Event
    data object DiscImageSelectStateCleared : Event
  }

  enum class BottomSheetVisible {
    None, DiscForm, ImageSelect
  }

  private companion object {
    const val BLURAY_URL = "https://www.blu-ray.com/movies/x-Blu-ray/{{id}}/"
  }
}
