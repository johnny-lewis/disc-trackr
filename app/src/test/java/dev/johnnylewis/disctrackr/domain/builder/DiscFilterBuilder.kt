package dev.johnnylewis.disctrackr.domain.builder

import dev.johnnylewis.disctrackr.domain.model.DiscFilter
import dev.johnnylewis.disctrackr.domain.model.DiscFormat
import kotlin.reflect.KClass

fun buildDiscFilter(
  format: KClass<out DiscFormat>? = DiscFormat.BluRay::class,
  countryCode: String? = "au",
  distributor: String? = "DISTRIBUTOR",
): DiscFilter =
  DiscFilter(
    format = format,
    countryCode = countryCode,
    distributor = distributor,
  )

fun buildEmptyDiscFilter(): DiscFilter =
  DiscFilter(
    format = null,
    countryCode = null,
    distributor = null,
  )
