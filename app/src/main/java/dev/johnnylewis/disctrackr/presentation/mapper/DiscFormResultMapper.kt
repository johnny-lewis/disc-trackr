package dev.johnnylewis.disctrackr.presentation.mapper

import dev.johnnylewis.disctrackr.domain.model.Disc
import dev.johnnylewis.disctrackr.presentation.model.DiscFormResult
import dev.johnnylewis.disctrackr.presentation.model.DiscRegion
import dev.johnnylewis.disctrackr.domain.model.DiscFormat as DomainDiscFormat
import dev.johnnylewis.disctrackr.presentation.model.DiscFormat as PresentationDiscFormat

fun DiscFormResult.mapToDisc(): Disc =
  Disc(
    id = null,
    title = title.trim(),
    imageUrl = null,
    format = format.mapToDomain(
      regions = regions,
    ),
    countryCode = country?.code,
    distributor = distributor.trim(),
    year = year.toIntOrNull(),
    blurayId = blurayId.trim(),
  )

private fun PresentationDiscFormat.mapToDomain(regions: List<DiscRegion>): DomainDiscFormat =
  when (this) {
    PresentationDiscFormat.DVD ->
      DomainDiscFormat.DVD(regions = regions.mapToDvdRegion())
    PresentationDiscFormat.BLU_RAY ->
      DomainDiscFormat.BluRay(regions = regions.mapToBluRayRegion())
    PresentationDiscFormat.UHD ->
      DomainDiscFormat.UHD
  }

// If the list size is 6, then all regions are selected as choosing 0 with other regions is not
// possible from the UI
private fun List<DiscRegion>.mapToDvdRegion(): List<DomainDiscFormat.DVD.Region> =
  if (size == DomainDiscFormat.DVD.Region.entries.size - 1) {
    listOf(DomainDiscFormat.DVD.Region.ALL)
  } else {
    mapNotNull(DiscRegion::mapToDvdRegion)
  }

// Same here. ALL is not possible from the UI if there are other regions selected. So the assumption
// that if the size is 3 then A, B, and C are selected is used.
private fun List<DiscRegion>.mapToBluRayRegion(): List<DomainDiscFormat.BluRay.Region> =
  if (size == DomainDiscFormat.BluRay.Region.entries.size - 1) {
    listOf(DomainDiscFormat.BluRay.Region.ALL)
  } else {
    mapNotNull(DiscRegion::mapToBluRayRegion)
  }

private fun DiscRegion.mapToDvdRegion(): DomainDiscFormat.DVD.Region? =
  when (this) {
    DiscRegion.ZERO -> DomainDiscFormat.DVD.Region.ALL
    DiscRegion.ONE -> DomainDiscFormat.DVD.Region.ONE
    DiscRegion.TWO -> DomainDiscFormat.DVD.Region.TWO
    DiscRegion.THREE -> DomainDiscFormat.DVD.Region.THREE
    DiscRegion.FOUR -> DomainDiscFormat.DVD.Region.FOUR
    DiscRegion.FIVE -> DomainDiscFormat.DVD.Region.FIVE
    DiscRegion.SIX -> DomainDiscFormat.DVD.Region.SIX
    else -> null
  }

private fun DiscRegion.mapToBluRayRegion(): DomainDiscFormat.BluRay.Region? =
  when (this) {
    DiscRegion.ALL -> DomainDiscFormat.BluRay.Region.ALL
    DiscRegion.A -> DomainDiscFormat.BluRay.Region.A
    DiscRegion.B -> DomainDiscFormat.BluRay.Region.B
    DiscRegion.C -> DomainDiscFormat.BluRay.Region.C
    else -> null
  }
