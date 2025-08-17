package dev.johnnylewis.disctrackr.presentation.util

import com.google.common.truth.Truth.assertThat
import dev.johnnylewis.disctrackr.domain.builder.buildBluRayDiscFormat
import dev.johnnylewis.disctrackr.domain.builder.buildDVDDiscFormat
import dev.johnnylewis.disctrackr.domain.builder.buildDisc
import dev.johnnylewis.disctrackr.domain.builder.buildDiscFilterState
import dev.johnnylewis.disctrackr.domain.builder.buildDiscFilterStateOptions
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
import kotlin.reflect.KClass

@RunWith(JUnitParamsRunner::class)
class DiscFilterUtilTest {
  @Test
  fun `Given empty filter state and no discs passed into update, when updating the filter options, then filter state is still empty`() {
    val filterState = buildDiscFilterState(
      selection = buildDiscFilterStateSelection(
        format = null,
        country = null,
        distributor = null,
      ),
      options = buildDiscFilterStateOptions(
        formats = emptyList(),
        countries = emptyList(),
        distributors = emptyList(),
      ),
    )

    with(filterState.update(emptyList())) {
      assertThat(selection.format).isNull()
      assertThat(selection.country).isNull()
      assertThat(selection.distributor).isNull()
      assertThat(options.formats).isEmpty()
      assertThat(options.countries).isEmpty()
      assertThat(options.distributors).isEmpty()
    }
  }

  @Test
  fun `Given empty filter state and discs passed into update, when updating the filter options, then filter state is updated with options that are sorted`() {
    val filterState = buildDiscFilterState(
      selection = buildDiscFilterStateSelection(
        format = null,
        country = null,
        distributor = null,
      ),
    )
    val discs = listOf(
      buildDisc(distributor = "DISTRIBUTOR 3", format = buildDVDDiscFormat(), countryCode = "NZ"),
      buildDisc(distributor = "DISTRIBUTOR 1", format = buildUHDDiscFormat(), countryCode = "AU"),
      buildDisc(distributor = "DISTRIBUTOR 2", format = buildBluRayDiscFormat(), countryCode = "HK"),
    )

    with(filterState.update(discs)) {
      assertThat(options.formats.map { it.simpleName }).isEqualTo(listOf("BluRay", "DVD", "UHD"))
      assertThat(options.countries.map(Country::code)).isEqualTo(listOf("AU", "HK", "NZ"))
      assertThat(options.distributors).isEqualTo(listOf("DISTRIBUTOR 1", "DISTRIBUTOR 2", "DISTRIBUTOR 3"))
    }
  }

  @Test
  fun `Given filter state with selections, when updating the filter options with discs that don't include the selections, then filter state is updated and selections are reset`() {
    val filterState = buildDiscFilterState(
      selection = buildDiscFilterStateSelection(
        format = DiscFormat.DVD::class,
        country = buildCountry(name = "New Zealand", code = "NZ"),
        distributor = "DISTRIBUTOR 1",
      ),
    )
    val discs = listOf(
      buildDisc(distributor = "DISTRIBUTOR 2", format = buildBluRayDiscFormat(), countryCode = "AU"),
      buildDisc(distributor = "DISTRIBUTOR 3", format = buildUHDDiscFormat(), countryCode = "HK"),
    )

    with(filterState.update(discs)) {
      assertThat(options.formats.map { it.simpleName }).isEqualTo(listOf("BluRay", "UHD"))
      assertThat(options.countries.map(Country::code)).isEqualTo(listOf("AU", "HK"))
      assertThat(options.distributors).isEqualTo(listOf("DISTRIBUTOR 2", "DISTRIBUTOR 3"))
      assertThat(selection.format).isNull()
      assertThat(selection.country).isNull()
      assertThat(selection.distributor).isNull()
    }
  }

  @Test
  fun `Given filter state with selections, when updating the filter options with discs that include the selections, then filter state is updated and selections are not reset`() {
    val filterState = buildDiscFilterState(
      selection = buildDiscFilterStateSelection(
        format = DiscFormat.DVD::class,
        country = buildCountry(name = "New Zealand", code = "NZ"),
        distributor = "DISTRIBUTOR 1",
      ),
    )
    val discs = listOf(
      buildDisc(distributor = "DISTRIBUTOR 1", format = buildDVDDiscFormat(), countryCode = "NZ"),
      buildDisc(distributor = "DISTRIBUTOR 2", format = buildBluRayDiscFormat(), countryCode = "AU"),
      buildDisc(distributor = "DISTRIBUTOR 3", format = buildUHDDiscFormat(), countryCode = "HK"),
    )

    with(filterState.update(discs)) {
      assertThat(options.formats.map { it.simpleName }).isEqualTo(listOf("BluRay", "DVD", "UHD"))
      assertThat(options.countries.map(Country::code)).isEqualTo(listOf("AU", "HK", "NZ"))
      assertThat(options.distributors).isEqualTo(listOf("DISTRIBUTOR 1", "DISTRIBUTOR 2", "DISTRIBUTOR 3"))
      assertThat(selection.format?.simpleName).isEqualTo("DVD")
      assertThat(selection.country?.code).isEqualTo("NZ")
      assertThat(selection.distributor).isEqualTo("DISTRIBUTOR 1")
    }
  }

  @Test
  @Parameters(method = "withFilterFormatParameters")
  @TestCaseName("Given {0} passed into filter, when updating the format filter, then it should be updated")
  fun testWithFilterFormat(
    formatName: String,
    format: KClass<DiscFormat>?,
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
      arrayOf("DVD", DiscFormat.DVD::class),
      arrayOf("Blu-Ray", DiscFormat.BluRay::class),
      arrayOf("4K UHD", DiscFormat.UHD::class),
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
