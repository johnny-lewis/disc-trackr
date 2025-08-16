package dev.johnnylewis.disctrackr.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.johnnylewis.disctrackr.presentation.model.Country
import dev.johnnylewis.disctrackr.presentation.model.DiscFormResult
import dev.johnnylewis.disctrackr.presentation.util.CountryUtil
import dev.johnnylewis.disctrackr.presentation.util.hasRegions
import dev.johnnylewis.disctrackr.presentation.util.isAllRegions
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class)
@HiltViewModel
class DiscFormViewModel @Inject constructor() : ViewModel() {
  private val _state = MutableStateFlow<State>(State())
  val state = _state.asStateFlow()

  private val countryFilter = MutableStateFlow("")

  init {
    viewModelScope.launch {
      countryFilter
        .debounce(250.milliseconds)
        .collect {
          _state.value = _state.value.copy(
            countries = CountryUtil.countryList.filter { country ->
              country.name.contains(it, ignoreCase = true)
            },
            countryFilterText = it,
          )
        }
    }
  }

  fun onEvent(event: Event) {
    when (event) {
      Event.ClearState -> onClearState()
      is Event.NameChanged -> onNameChanged(event.name)
      is Event.FormatChanged -> onFormatChanged(event.format)
      is Event.RegionSelected -> onRegionSelected(event.region, event.selected)
      is Event.DistributorChanged -> onDistributorChanged(event.distributor)
      is Event.YearChanged -> onYearChanged(event.year)
      is Event.BlurayIdChanged -> onBlurayIdChanged(event.id)
      is Event.CountrySelected -> onCountrySelected(event.country)
      Event.CountryCleared -> onCountryCleared()
      Event.CountryFilterCleared -> onCountryFilterCleared()
      is Event.CountryFilterChanged -> onCountryFilterChanged(event.filter)
    }
  }

  private fun onClearState() {
    _state.value = State()
  }

  private fun onCountryFilterChanged(filter: String) {
    countryFilter.value = filter
    _state.value = _state.value.copy(countryFilterText = filter)
  }

  private fun onCountryFilterCleared() {
    countryFilter.value = ""
  }

  private fun onCountrySelected(country: Country) {
    _state.value = _state.value.copy(selectedCountry = country)
  }

  private fun onCountryCleared() {
    _state.value = _state.value.copy(selectedCountry = null)
  }

  private fun onNameChanged(name: String) {
    _state.value = _state.value.copy(name = name)
  }

  private fun onFormatChanged(format: DiscFormResult.DiscFormFormat) {
    if (format != _state.value.format) {
      _state.value = _state.value.copy(
        format = format,
        regions = emptySet(),
      )
    }
  }

  private fun onRegionSelected(region: DiscFormResult.DiscFormRegion, selected: Boolean) {
    _state.value = _state.value.copy(
      regions = if (selected) {
        if (region.isAllRegions()) {
          setOf(region)
        } else {
          _state.value.regions + region
        }
      } else {
        _state.value.regions - region
      },
    )
  }

  private fun onDistributorChanged(distributor: String) {
    _state.value = _state.value.copy(distributor = distributor)
  }

  private fun onYearChanged(year: String) {
    _state.value = _state.value.copy(year = year)
  }

  private fun onBlurayIdChanged(id: String) {
    _state.value = _state.value.copy(blurayId = id)
  }

  data class State(
    val countries: List<Country> = CountryUtil.countryList,
    val countryFilterText: String = "",
    val selectedCountry: Country? = null,
    val name: String = "",
    val format: DiscFormResult.DiscFormFormat = DiscFormResult.DiscFormFormat.BLU_RAY,
    val regions: Set<DiscFormResult.DiscFormRegion> = emptySet(),
    val distributor: String = "",
    val year: String = "",
    val blurayId: String = "",
  ) {
    val isFormValid: Boolean =
      name.isNotBlank() && (regions.isNotEmpty() || !format.hasRegions())

    fun mapToResult(): DiscFormResult =
      DiscFormResult(
        title = name,
        regions = regions.toList(),
        format = format,
        country = selectedCountry,
        distributor = distributor,
        year = year,
        blurayId = blurayId,
      )
  }

  sealed interface Event {
    data object ClearState : Event
    data class NameChanged(val name: String) : Event
    data class FormatChanged(val format: DiscFormResult.DiscFormFormat) : Event
    data class RegionSelected(
      val region: DiscFormResult.DiscFormRegion,
      val selected: Boolean,
    ) : Event
    data class DistributorChanged(val distributor: String) : Event
    data class YearChanged(val year: String) : Event
    data class BlurayIdChanged(val id: String) : Event
    data class CountrySelected(val country: Country) : Event
    data object CountryCleared : Event
    data object CountryFilterCleared : Event
    data class CountryFilterChanged(val filter: String) : Event
  }
}
