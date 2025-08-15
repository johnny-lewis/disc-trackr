package dev.johnnylewis.disctrackr.presentation.util

import com.google.common.truth.Truth.assertThat
import dev.johnnylewis.disctrackr.domain.builder.buildBluRayDiscFormat
import dev.johnnylewis.disctrackr.domain.builder.buildDVDDiscFormat
import dev.johnnylewis.disctrackr.domain.builder.buildDiscFilterState
import dev.johnnylewis.disctrackr.domain.builder.buildDiscFilterStateSelection
import dev.johnnylewis.disctrackr.domain.builder.buildDiscScreenViewModelState
import dev.johnnylewis.disctrackr.domain.builder.buildUHDDiscFormat
import dev.johnnylewis.disctrackr.domain.model.DiscFormat
import dev.johnnylewis.disctrackr.presentation.builder.buildCountry
import dev.johnnylewis.disctrackr.presentation.model.Country
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import junitparams.naming.TestCaseName
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
class DiscFilterUtilTest {
  @Test
  @Parameters(method = "withFilterFormatParameters")
  @TestCaseName("Given {0} passed into filter, when updating the format filter, then it should be updated")
  fun testWithFilterFormat(
    formatName: String,
    format: DiscFormat?,
  ) {
    val state = buildDiscScreenViewModelState(
      filterState = buildDiscFilterState(
        selection = buildDiscFilterStateSelection(
          format = null,
        )
      )
    )

    with(state.withFilter(format = format)) {
      assertThat(filterState.selection.format).isEqualTo(format)
    }
  }

  @Test
  @Parameters(method = "withFilterCountryParameters")
  @TestCaseName("Given {0} passed into filter, when updating the country filter, then it should be updated")
  fun testWithFilterCountry(
    countryName: String,
    country: Country?,
  ) {
    val state = buildDiscScreenViewModelState(
      filterState = buildDiscFilterState(
        selection = buildDiscFilterStateSelection(
          country = null,
        )
      )
    )

    with(state.withFilter(country = country)) {
      assertThat(filterState.selection.country).isEqualTo(country)
    }
  }

  @Test
  @Parameters(method = "withFilterDistributorParameters")
  @TestCaseName("Given {0} passed into filter, when updating the distributor filter, then it should be updated")
  fun testWithFilterDistributor(
    distributorName: String,
    distributor: String?,
  ) {
    val state = buildDiscScreenViewModelState(
      filterState = buildDiscFilterState(
        selection = buildDiscFilterStateSelection(
          distributor = null,
        )
      )
    )

    with(state.withFilter(distributor = distributor)) {
      assertThat(filterState.selection.distributor).isEqualTo(distributor)
    }
  }

  @Suppress("unused")
  private fun withFilterFormatParameters(): Array<Any?> =
    arrayOf(
      arrayOf("DVD", buildDVDDiscFormat()),
      arrayOf("Blu-Ray", buildBluRayDiscFormat()),
      arrayOf("4K UHD", buildUHDDiscFormat()),
      arrayOf("null", null),
    )

  @Suppress("unused")
  private fun withFilterCountryParameters(): Array<Any?> =
    arrayOf(
      arrayOf("valid country", buildCountry()),
      arrayOf("null", null),
    )

  @Suppress("unused")
  private fun withFilterDistributorParameters(): Array<Any?> =
    arrayOf(
      arrayOf("valid distributor", "DISTRIBUTOR"),
      arrayOf("null", null),
    )
}
