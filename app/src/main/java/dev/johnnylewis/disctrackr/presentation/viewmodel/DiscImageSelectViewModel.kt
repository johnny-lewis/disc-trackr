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
      Event.SubmitImageUrl -> onSubmitImageUrl()
      Event.ImageUrlValid -> onImageValid()
    }
  }

  private fun onClearState() {
    _state.value = State()
  }

  private fun imageUrlTextValueChanged(text: String) {
    _state.value = _state.value.copy(
      imageUrlTextValue = text,
      isImageUrlTextValueValid = text.isValidUrl(),
    )
  }

  private fun onSubmitImageUrl() {
    _state.value = _state.value.copy(
      submittedImageUrl = _state.value.imageUrlTextValue,
      isImageValid = false,
    )
  }

  private fun onImageValid() {
    _state.value = _state.value.copy(isImageValid = true)
  }

  data class State(
    val imageUrlTextValue: String = "",
    val isImageUrlTextValueValid: Boolean = false,
    val submittedImageUrl: String = "",
    val isImageValid: Boolean = false,
  )

  sealed interface Event {
    data object ClearState : Event
    data class ImageUrlTextValueChanged(val text: String) : Event
    data object SubmitImageUrl : Event
    data object ImageUrlValid : Event
  }
}
