package dev.johnnylewis.disctrackr.presentation.mapper

import dev.johnnylewis.disctrackr.domain.model.Disc
import dev.johnnylewis.disctrackr.presentation.model.Country
import dev.johnnylewis.disctrackr.presentation.model.DiscFormResult
import dev.johnnylewis.disctrackr.presentation.model.DiscRegion
import java.util.Locale
import dev.johnnylewis.disctrackr.domain.model.DiscFormat as DomainDiscFormat
import dev.johnnylewis.disctrackr.presentation.model.DiscFormat as PresentationDiscFormat

fun DiscFormResult.mapToDisc(): Disc =
  Disc(
    id = null,
    title = title,
    imageUrl = null,
    format = format.mapToDomain(
      regions = regions,
    ),
    countryCode = country?.mapToLocalIsoCode(),
    distributor = distributor,
    blurayId = blurayId,
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
    DiscRegion.Zero -> DomainDiscFormat.DVD.Region.ALL
    DiscRegion.One -> DomainDiscFormat.DVD.Region.ONE
    DiscRegion.Two -> DomainDiscFormat.DVD.Region.TWO
    DiscRegion.Three -> DomainDiscFormat.DVD.Region.THREE
    DiscRegion.Four -> DomainDiscFormat.DVD.Region.FOUR
    DiscRegion.Five -> DomainDiscFormat.DVD.Region.FIVE
    DiscRegion.Six -> DomainDiscFormat.DVD.Region.SIX
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

private fun Country.mapToLocalIsoCode(): Locale.IsoCountryCode =
  Locale.IsoCountryCode.valueOf(this.code)
