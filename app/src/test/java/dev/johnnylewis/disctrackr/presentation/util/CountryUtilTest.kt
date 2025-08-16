package dev.johnnylewis.disctrackr.presentation.util

import com.google.common.truth.Truth.assertThat
import dev.johnnylewis.disctrackr.presentation.model.Country
import dev.johnnylewis.disctrackr.util.TestConst.TOTAL_COUNTRIES
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import junitparams.naming.TestCaseName
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
class CountryUtilTest {
  @Test
  fun `Given list of countries from device locale, when getting country list, then it returns all countries other than those excluded, and sorted by name`() {
    assertThat(CountryUtil.countryList.size).isEqualTo(TOTAL_COUNTRIES)
    assertThat(CountryUtil.countryList).isInOrder { country1, country2 ->
      (country1 as Country).name.compareTo((country2 as Country).name)
    }
    assertThat(CountryUtil.countryList).doesNotContain(
      Country(
        name = "Antarctica",
        code = "AQ",
      ),
    )
    assertThat(CountryUtil.countryList).doesNotContain(
      Country(
        name = "Åland Islands",
        code = "AX",
      ),
    )
  }

  @Test
  @Parameters(method = "validFlagParameters")
  @TestCaseName("Given country code {0}, when getting flag, then it returns the flag emoji")
  fun testValidFlag(
    countryCode: String,
    expectedFlag: String,
  ) {
    assertThat(CountryUtil.getFlag(countryCode)).isEqualTo(expectedFlag)
  }

  @Test
  @Parameters(method = "invalidCountryCodeParameters")
  @TestCaseName("Given invalid country code '{0}', when getting flag, then it returns null")
  fun testInvalidFlag(countryCode: String) {
    assertThat(CountryUtil.getFlag(countryCode)).isNull()
  }

  @Test
  @Parameters(method = "validGetCountryParameters")
  @TestCaseName("Given country code '{0}', when getting country, then it returns the country")
  fun testValidGetCountryFromCode(
    countryCode: String,
    expectedCountry: Country,
  ) {
    assertThat(CountryUtil.getCountryFromCode(countryCode)).isEqualTo(expectedCountry)
  }

  @Test
  @Parameters(method = "invalidCountryCodeParameters")
  @TestCaseName("Given invalid country code '{0}', when getting country, then it returns null")
  fun testInvalidGetCountryFromCode(countryCode: String) {
    assertThat(CountryUtil.getCountryFromCode(countryCode)).isNull()
  }

  @Suppress("unused")
  private fun validFlagParameters(): Array<Any> =
    arrayOf(
      arrayOf("AU", "\uD83C\uDDE6\uD83C\uDDFA"),
      arrayOf("GB", "\uD83C\uDDEC\uD83C\uDDE7"),
      arrayOf("HK", "\uD83C\uDDED\uD83C\uDDF0"),
      arrayOf("NZ", "\uD83C\uDDF3\uD83C\uDDFF"),
      arrayOf("US", "\uD83C\uDDFA\uD83C\uDDF8"),
    )

  @Suppress("unused")
  private fun invalidCountryCodeParameters(): Array<Any?> =
    arrayOf(
      "INVALID",
      "",
      "au", // lowercase codes are invalid
    )

  @Suppress("unused")
  private fun validGetCountryParameters(): Array<Any> =
    arrayOf(
      arrayOf("AU", Country(name = "Australia", code = "AU")),
      arrayOf("GB", Country(name = "United Kingdom", code = "GB")),
      arrayOf("HK", Country(name = "Hong Kong SAR China", code = "HK")),
      arrayOf("NZ", Country(name = "New Zealand", code = "NZ")),
      arrayOf("US", Country(name = "United States", code = "US")),
    )
}
