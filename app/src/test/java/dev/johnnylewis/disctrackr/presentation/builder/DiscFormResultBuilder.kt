package dev.johnnylewis.disctrackr.presentation.builder

import dev.johnnylewis.disctrackr.presentation.model.Country
import dev.johnnylewis.disctrackr.presentation.model.DiscFormResult

fun buildDiscFormResult(
  title: String = "TITLE",
  format: DiscFormResult.DiscFormFormat = DiscFormResult.DiscFormFormat.BLU_RAY,
  regions: List<DiscFormResult.DiscFormRegion> = listOf(DiscFormResult.DiscFormRegion.B),
  country: Country? = buildCountry(),
  distributor: String = "DISTRIBUTOR",
  year: String = "2025",
  blurayId: String = "BLURAY_ID",
): DiscFormResult =
  DiscFormResult(
    title = title,
    format = format,
    regions = regions,
    country = country,
    distributor = distributor,
    year = year,
    blurayId = blurayId,
  )
