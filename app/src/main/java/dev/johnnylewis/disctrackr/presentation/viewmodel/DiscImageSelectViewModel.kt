package dev.johnnylewis.disctrackr.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.johnnylewis.disctrackr.presentation.util.isValidUrl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DiscImageSelectViewModel @Inject constructor() : ViewModel() {
  private val _state = MutableStateFlow<State>(State())
  val state = _state.asStateFlow()

  fun onEvent(event: Event) {
    when (event) {
      Event.ClearState -> onClearState()
      is Event.ImageUrlTextValueChanged -> imageUrlTextValueChanged(event.text)
      Event.CheckOrClearImageUrl -> onCheckOrClearImageUrl()
      Event.ImageUrlValid -> onImageValid()
    }
  }

  private fun onClearState() {
    _state.value = State()
  }

  private fun imageUrlTextValueChanged(text: String) {
    _state.value = _state.value.copy(
      imageUrlTextValue = text,
      imageState = if (text.isValidUrl()) ImageState.VALID else ImageState.INVALID,
    )
  }

  private fun onCheckOrClearImageUrl() {
    if (_state.value.imageState == ImageState.VALID) {
      _state.value = _state.value.copy(
        checkedImageUrl = _state.value.imageUrlTextValue,
        imageState = ImageState.LOADING,
      )
    } else {
      onClearState()
    }
  }

  private fun onImageValid() {
    if (_state.value.imageState == ImageState.LOADING) {
      _state.value = _state.value.copy(imageState = ImageState.LOADED)
    }
  }

  data class State(
    val imageUrlTextValue: String = "",
    val checkedImageUrl: String = "",
    val imageState: ImageState = ImageState.INVALID,
  )

  sealed interface Event {
    data object ClearState : Event
    data class ImageUrlTextValueChanged(val text: String) : Event
    data object CheckOrClearImageUrl : Event
    data object ImageUrlValid : Event
  }

  enum class ImageState {
    INVALID, VALID, LOADING, LOADED
  }
}
