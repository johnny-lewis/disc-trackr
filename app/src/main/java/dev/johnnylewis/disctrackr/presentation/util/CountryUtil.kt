package dev.johnnylewis.disctrackr.presentation.util

import dev.johnnylewis.disctrackr.presentation.model.Country
import java.util.Locale

object CountryUtil {
  private const val REGIONAL_BASE_CODE_POINT = 0x1F1E6
  private const val ASCII_UPPERCASE_A = 0x41

  private val excludedCountries = listOf(
    "AQ", // Antarctica
    "AX", // Åland Islands
  )

  val countryList: List<Country> =
    Locale.getISOCountries()
      .filterNot { it in excludedCountries }
      .map { code ->
        Country(
          name = Locale.Builder().setRegion(code).build().displayCountry,
          code = code,
        )
      }
      .sortedBy { it.name }

  fun getFlag(isoCode: String): String? =
    takeIf { Locale.getISOCountries().contains(isoCode) }?.let {
      isoCode.map { char ->
        String(Character.toChars(char.code - ASCII_UPPERCASE_A + REGIONAL_BASE_CODE_POINT))
      }.joinToString(separator = "")
    }

  fun getCountryFromCode(code: String): Country? =
    countryList.firstOrNull { it.code == code }
}
