@file:Suppress("ktlint:standard:filename")

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
    blurayId = blurayId.trim(),
  )

private fun PresentationDiscFormat.mapToDomain(regions: List<DiscRegion>): DomainDiscFormat =
  when (this) {
    PresentationDiscFormat.DVD ->
      DomainDiscFormat.DVD(
        regions = regions.mapNotNull(DiscRegion::mapToDvdRegion),
      )
    PresentationDiscFormat.BLU_RAY ->
      DomainDiscFormat.BluRay(
        regions = regions.mapNotNull(DiscRegion::mapToBluRayRegion),
      )
    PresentationDiscFormat.UHD ->
      DomainDiscFormat.UHD
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
