package dev.johnnylewis.disctrackr.presentation.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.johnnylewis.disctrackr.presentation.builder.buildCountry
import dev.johnnylewis.disctrackr.presentation.model.DiscFormResult
import dev.johnnylewis.disctrackr.util.TestConst.TOTAL_COUNTRIES
import dev.johnnylewis.disctrackr.util.finishFlow
import dev.johnnylewis.disctrackr.util.testAndSkipFirst
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DiscFormViewModelTest {
  private lateinit var viewModel: DiscFormViewModel

  private val testScope = TestScope()

  @Before
  fun before() {
    viewModel = DiscFormViewModel()
  }

  @Test
  fun `Given name changed event triggered, when listening to disc form state flow, then it updates name`() =
    testScope.runTest {
      viewModel.onEvent(DiscFormViewModel.Event.NameChanged("Disc Name"))
      viewModel.state.test {
        assertThat(awaitItem().name).isEqualTo("Disc Name")

        finishFlow()
      }
    }

  @Test
  fun `Given distributor changed event triggered, when listening to disc form state flow, then it updates distributor`() =
    testScope.runTest {
      viewModel.onEvent(DiscFormViewModel.Event.DistributorChanged("Disc Distributor"))
      viewModel.state.test {
        assertThat(awaitItem().distributor).isEqualTo("Disc Distributor")

        finishFlow()
      }
    }

  @Test
  fun `Given year changed event triggered, when listening to disc form state flow, then it updates year`() =
    testScope.runTest {
      viewModel.onEvent(DiscFormViewModel.Event.YearChanged("2025"))
      viewModel.state.test {
        assertThat(awaitItem().year).isEqualTo("2025")

        finishFlow()
      }
    }

  @Test
  fun `Given bluray id changed event triggered, when listening to disc form state flow, then it updates bluray id`() =
    testScope.runTest {
      viewModel.onEvent(DiscFormViewModel.Event.BluRayIdChanged("12345"))
      viewModel.state.test {
        assertThat(awaitItem().blurayId).isEqualTo("12345")

        finishFlow()
      }
    }

  @Test
  fun `Given country selected event triggered, when listening to disc form state flow, then it updates selected country`() =
    testScope.runTest {
      val country = buildCountry()
      viewModel.onEvent(DiscFormViewModel.Event.CountrySelected(country))
      viewModel.state.test {
        assertThat(awaitItem().selectedCountry).isEqualTo(country)

        finishFlow()
      }
    }

  @Test
  fun `Given country cleared event triggered, when listening to disc form state flow, then it updates selected country to null`() =
    testScope.runTest {
      val country = buildCountry()
      viewModel.state.testAndSkipFirst {
        viewModel.onEvent(DiscFormViewModel.Event.CountrySelected(country))
        assertThat(awaitItem().selectedCountry).isEqualTo(country)
        viewModel.onEvent(DiscFormViewModel.Event.CountryCleared)
        assertThat(awaitItem().selectedCountry).isNull()

        finishFlow()
      }
    }

  @Test
  fun `Given country filter changed event triggered, when listening to disc form state flow, then it updates country filter text and filters countries`() =
    testScope.runTest {
      viewModel.state.test {
        with(awaitItem()) {
          assertThat(countryFilterText.isEmpty()).isTrue()
          assertThat(countries.size).isEqualTo(TOTAL_COUNTRIES)
        }
        viewModel.onEvent(DiscFormViewModel.Event.CountryFilterChanged("New"))
        skipItems(1) // Event is debounced
        with(awaitItem()) {
          assertThat(countryFilterText).isEqualTo("New")
          assertThat(countries).isEqualTo(
          listOf(
            buildCountry(name = "New Caledonia", code = "NC"),
            buildCountry(name = "New Zealand", code = "NZ"),
            buildCountry(name = "Papua New Guinea", code = "PG"),
          ))
        }

        finishFlow()
      }
    }

  @Test
  fun `Given country filter cleared event triggered, when listening to disc form state flow, then it updates country filter text and unfilters countries`() =
    testScope.runTest {
      viewModel.onEvent(DiscFormViewModel.Event.CountryFilterChanged("New"))
      viewModel.state.testAndSkipFirst {
        with(awaitItem()) {
          assertThat(countryFilterText).isEqualTo("New")
          assertThat(countries.size).isEqualTo(3)
        }
        viewModel.onEvent(DiscFormViewModel.Event.CountryFilterCleared)
        // No need to skip as initial filter is before flow is listened to, so no debounce
        with(awaitItem()) {
          assertThat(countryFilterText).isEmpty()
          assertThat(countries.size).isEqualTo(TOTAL_COUNTRIES)
        }

        finishFlow()
      }
    }

  @Test
  fun `Given no format selected and format changed event triggered, when listening to disc form state flow, then it updates format`() =
    testScope.runTest {
      viewModel.onEvent(DiscFormViewModel.Event.FormatChanged(DiscFormResult.DiscFormFormat.BLU_RAY))
      viewModel.state.test {
        with(awaitItem()) {
          assertThat(format).isEqualTo(DiscFormResult.DiscFormFormat.BLU_RAY)
          assertThat(regions).isEmpty()
        }

        finishFlow()
      }
    }

  @Test
  fun `Given different format selected and format changed event triggered, when listening to disc form state flow, then it updates format and clears regions`() =
    testScope.runTest {
      viewModel.onEvent(DiscFormViewModel.Event.FormatChanged(DiscFormResult.DiscFormFormat.DVD))
      viewModel.onEvent(DiscFormViewModel.Event.RegionSelected(DiscFormResult.DiscFormRegion.FOUR, true))
      viewModel.state.test {
        with(awaitItem()) {
          assertThat(format).isEqualTo(DiscFormResult.DiscFormFormat.DVD)
          assertThat(regions).isEqualTo(setOf(DiscFormResult.DiscFormRegion.FOUR))
        }
        viewModel.onEvent(DiscFormViewModel.Event.FormatChanged(DiscFormResult.DiscFormFormat.BLU_RAY))
        with(awaitItem()) {
          assertThat(format).isEqualTo(DiscFormResult.DiscFormFormat.BLU_RAY)
          assertThat(regions).isEmpty()
        }

        finishFlow()
      }
    }

  @Test
  fun `Given region selected event triggered with a region added, when listening to disc form state flow, then it adds the region`() =
    testScope.runTest {
      viewModel.onEvent(DiscFormViewModel.Event.FormatChanged(DiscFormResult.DiscFormFormat.BLU_RAY))
      viewModel.onEvent(DiscFormViewModel.Event.RegionSelected(DiscFormResult.DiscFormRegion.B, true))
      viewModel.state.test {
        with(awaitItem()) {
          assertThat(format).isEqualTo(DiscFormResult.DiscFormFormat.BLU_RAY)
          assertThat(regions).isEqualTo(setOf(DiscFormResult.DiscFormRegion.B))
        }

        finishFlow()
      }
    }

  @Test
  fun `Given region selected event triggered with a region removed, when listening to disc form state flow, then it removes the region`() =
    testScope.runTest {
      viewModel.onEvent(DiscFormViewModel.Event.FormatChanged(DiscFormResult.DiscFormFormat.BLU_RAY))
      viewModel.onEvent(DiscFormViewModel.Event.RegionSelected(DiscFormResult.DiscFormRegion.A, true))
      viewModel.onEvent(DiscFormViewModel.Event.RegionSelected(DiscFormResult.DiscFormRegion.B, true))
      viewModel.state.test {
        with(awaitItem()) {
          assertThat(format).isEqualTo(DiscFormResult.DiscFormFormat.BLU_RAY)
          assertThat(regions).isEqualTo(setOf(DiscFormResult.DiscFormRegion.A, DiscFormResult.DiscFormRegion.B))
        }
        viewModel.onEvent(DiscFormViewModel.Event.RegionSelected(DiscFormResult.DiscFormRegion.A, false))
        with(awaitItem()) {
          assertThat(format).isEqualTo(DiscFormResult.DiscFormFormat.BLU_RAY)
          assertThat(regions).isEqualTo(setOf(DiscFormResult.DiscFormRegion.B))
        }

        finishFlow()
      }
    }

  @Test
  fun `Given regions are selected and region selected event triggered with all region, when listening to disc form state flow, then it updates regions to all regions and removes individual regions`() =
    testScope.runTest {
      viewModel.onEvent(DiscFormViewModel.Event.FormatChanged(DiscFormResult.DiscFormFormat.BLU_RAY))
      viewModel.onEvent(DiscFormViewModel.Event.RegionSelected(DiscFormResult.DiscFormRegion.A, true))
      viewModel.onEvent(DiscFormViewModel.Event.RegionSelected(DiscFormResult.DiscFormRegion.B, true))
      viewModel.state.test {
        with(awaitItem()) {
          assertThat(format).isEqualTo(DiscFormResult.DiscFormFormat.BLU_RAY)
          assertThat(regions).isEqualTo(setOf(DiscFormResult.DiscFormRegion.A, DiscFormResult.DiscFormRegion.B))
        }
        viewModel.onEvent(DiscFormViewModel.Event.RegionSelected(DiscFormResult.DiscFormRegion.ALL, true))
        with(awaitItem()) {
          assertThat(format).isEqualTo(DiscFormResult.DiscFormFormat.BLU_RAY)
          assertThat(regions).isEqualTo(setOf(DiscFormResult.DiscFormRegion.ALL))
        }

        finishFlow()
      }
    }

  @Test
  fun `Given clear state event triggered, when listening to disc form state flow, then it resets the state`() =
    testScope.runTest {
      viewModel.onEvent(DiscFormViewModel.Event.NameChanged("NAME"))
      viewModel.onEvent(DiscFormViewModel.Event.FormatChanged(DiscFormResult.DiscFormFormat.DVD))
      viewModel.onEvent(DiscFormViewModel.Event.RegionSelected(DiscFormResult.DiscFormRegion.FOUR, true))
      viewModel.onEvent(DiscFormViewModel.Event.DistributorChanged("DISTRIBUTOR"))
      viewModel.onEvent(DiscFormViewModel.Event.YearChanged("2025"))
      viewModel.onEvent(DiscFormViewModel.Event.BluRayIdChanged("12345"))
      viewModel.onEvent(DiscFormViewModel.Event.CountrySelected(buildCountry()))
      viewModel.onEvent(DiscFormViewModel.Event.CountryFilterChanged("New"))
      viewModel.state.test {
        with(awaitItem()) {
          assertThat(name).isEqualTo("NAME")
          assertThat(format).isEqualTo(DiscFormResult.DiscFormFormat.DVD)
          assertThat(regions).isEqualTo(setOf(DiscFormResult.DiscFormRegion.FOUR))
          assertThat(distributor).isEqualTo("DISTRIBUTOR")
          assertThat(year).isEqualTo("2025")
          assertThat(blurayId).isEqualTo("12345")
          assertThat(selectedCountry).isNotNull()
          assertThat(countryFilterText).isEqualTo("New")
        }
        viewModel.onEvent(DiscFormViewModel.Event.ClearState)
        with(awaitItem()) {
          assertThat(name).isEmpty()
          assertThat(format).isEqualTo(DiscFormResult.DiscFormFormat.BLU_RAY)
          assertThat(regions).isEmpty()
          assertThat(distributor).isEmpty()
          assertThat(year).isEmpty()
          assertThat(blurayId).isEmpty()
          assertThat(selectedCountry).isNull()
          assertThat(countryFilterText).isEmpty()
        }
      }
    }
}
