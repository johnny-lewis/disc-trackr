package dev.johnnylewis.disctrackr.presentation.builder

import dev.johnnylewis.disctrackr.presentation.model.Country

fun buildCountry(
  name: String = "Australia",
  code: String = "au",
): Country =
  Country(
    name = name,
    code = code,
  )
