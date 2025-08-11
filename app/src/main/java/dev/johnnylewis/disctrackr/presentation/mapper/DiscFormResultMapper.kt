package dev.johnnylewis.disctrackr.presentation.mapper

import dev.johnnylewis.disctrackr.domain.model.Disc
import dev.johnnylewis.disctrackr.domain.model.DiscFormat
import dev.johnnylewis.disctrackr.presentation.model.DiscFormResult

fun DiscFormResult.mapToDisc(): Disc =
  Disc(
    id = null,
    title = title.trim(),
    imageUrl = null,
    format = format.mapToDiscFormat(regions = regions),
    countryCode = country?.code,
    distributor = distributor.ifBlank { null }?.trim(),
    year = year.toIntOrNull(),
    blurayId = blurayId.ifBlank { null }?.trim(),
  )

private fun DiscFormResult.DiscFormFormat.mapToDiscFormat(
  regions: List<DiscFormResult.DiscFormRegion>,
): DiscFormat =
  when (this) {
    DiscFormResult.DiscFormFormat.DVD ->
      DiscFormat.DVD(regions = regions.mapToDvdRegion())
    DiscFormResult.DiscFormFormat.BLU_RAY ->
      DiscFormat.BluRay(regions = regions.mapToBluRayRegion())
    DiscFormResult.DiscFormFormat.UHD ->
      DiscFormat.UHD
  }

// If the list size is 6, then all regions are selected as choosing 0 with other regions is not
// possible from the UI
private fun List<DiscFormResult.DiscFormRegion>.mapToDvdRegion(): List<DiscFormat.DVD.Region> =
  if (size == DiscFormat.DVD.Region.entries.size - 1) {
    listOf(DiscFormat.DVD.Region.ALL)
  } else {
    mapNotNull(DiscFormResult.DiscFormRegion::mapToDvdRegion)
  }

// Same here. ALL is not possible from the UI if there are other regions selected. So the assumption
// that if the size is 3 then A, B, and C are selected is used.
private fun List<DiscFormResult.DiscFormRegion>.mapToBluRayRegion():
  List<DiscFormat.BluRay.Region> =
  if (size == DiscFormat.BluRay.Region.entries.size - 1) {
    listOf(DiscFormat.BluRay.Region.ALL)
  } else {
    mapNotNull(DiscFormResult.DiscFormRegion::mapToBluRayRegion)
  }

private fun DiscFormResult.DiscFormRegion.mapToDvdRegion(): DiscFormat.DVD.Region? =
  when (this) {
    DiscFormResult.DiscFormRegion.ZERO -> DiscFormat.DVD.Region.ALL
    DiscFormResult.DiscFormRegion.ONE -> DiscFormat.DVD.Region.ONE
    DiscFormResult.DiscFormRegion.TWO -> DiscFormat.DVD.Region.TWO
    DiscFormResult.DiscFormRegion.THREE -> DiscFormat.DVD.Region.THREE
    DiscFormResult.DiscFormRegion.FOUR -> DiscFormat.DVD.Region.FOUR
    DiscFormResult.DiscFormRegion.FIVE -> DiscFormat.DVD.Region.FIVE
    DiscFormResult.DiscFormRegion.SIX -> DiscFormat.DVD.Region.SIX
    else -> null
  }

private fun DiscFormResult.DiscFormRegion.mapToBluRayRegion(): DiscFormat.BluRay.Region? =
  when (this) {
    DiscFormResult.DiscFormRegion.A -> DiscFormat.BluRay.Region.A
    DiscFormResult.DiscFormRegion.B -> DiscFormat.BluRay.Region.B
    DiscFormResult.DiscFormRegion.C -> DiscFormat.BluRay.Region.C
    DiscFormResult.DiscFormRegion.ALL -> DiscFormat.BluRay.Region.ALL
    else -> null
  }
