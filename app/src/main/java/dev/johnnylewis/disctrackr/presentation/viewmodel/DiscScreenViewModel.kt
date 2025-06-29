package dev.johnnylewis.disctrackr.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.johnnylewis.disctrackr.domain.model.Disc
import dev.johnnylewis.disctrackr.domain.repository.DatabaseRepositoryContract
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DiscScreenViewModel @Inject constructor(
  private val discRepository: DatabaseRepositoryContract,
) : ViewModel() {
  private val _state = MutableStateFlow<State>(State.Initial)
  val state = _state.asStateFlow()

  init {
    discRepository.getAllDiscs()
      .onSuccess { discsFlow ->
        viewModelScope.launch {
          discsFlow.collect { discs ->
            if (discs.isEmpty()) {
              _state.value = State.Empty
            } else {
              _state.value = State.Loaded(discs)
            }
          }
        }
      }.onFailure {
        _state.value = State.Error
      }
  }

  fun onAddDiscPressed() {
    Timber.i("++++ ON DISC PRESSED")
  }

  sealed interface State {
    data object Initial : State

    data object Empty : State

    data class Loaded(
      val discs: List<Disc>,
    ) : State

    data object Error : State
  }
}
