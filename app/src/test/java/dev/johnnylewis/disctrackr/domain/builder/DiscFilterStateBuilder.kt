package dev.johnnylewis.disctrackr.domain.builder

import dev.johnnylewis.disctrackr.domain.model.DiscFormat
import dev.johnnylewis.disctrackr.presentation.builder.buildCountry
import dev.johnnylewis.disctrackr.presentation.model.Country
import dev.johnnylewis.disctrackr.presentation.model.DiscFilterState
import kotlin.reflect.KClass

fun buildDiscFilterState(
  selection: DiscFilterState.Selection = buildDiscFilterStateSelection(),
  options: DiscFilterState.Options = buildDiscFilterStateOptions(),
): DiscFilterState =
  DiscFilterState(
    selection = selection,
    options = options,
  )

fun buildDiscFilterStateSelection(
  format: KClass<out DiscFormat>? = DiscFormat.BluRay::class,
  country: Country? = buildCountry(),
  distributor: String? = "DISTRIBUTOR",
): DiscFilterState.Selection =
  DiscFilterState.Selection(
    format = format,
    country = country,
    distributor = distributor,
  )

fun buildDiscFilterStateOptions(
  formats: List<KClass<out DiscFormat>> = listOf(DiscFormat.BluRay::class),
  countries: List<Country> = listOf(buildCountry()),
  distributors: List<String> = listOf("DISTRIBUTOR"),
): DiscFilterState.Options =
  DiscFilterState.Options(
    formats = formats,
    countries = countries,
    distributors = distributors,
  )
