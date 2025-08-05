package dev.johnnylewis.disctrackr.presentation.mapper

import dev.johnnylewis.disctrackr.presentation.model.DiscRegion
import kotlin.reflect.KClass
import dev.johnnylewis.disctrackr.domain.model.DiscFormat as DomainDiscFormat
import dev.johnnylewis.disctrackr.presentation.model.DiscFormat as PresentationDiscFormat

fun DomainDiscFormat.mapToPresentation(): Pair<PresentationDiscFormat, List<DiscRegion>> =
  when (this) {
    is DomainDiscFormat.DVD ->
      PresentationDiscFormat.DVD to regions.map(DomainDiscFormat.DVD.Region::mapToDiscRegion)
    is DomainDiscFormat.BluRay ->
      PresentationDiscFormat.BLU_RAY to regions.map(DomainDiscFormat.BluRay.Region::mapToDiscRegion)
    DomainDiscFormat.UHD -> PresentationDiscFormat.UHD to emptyList()
  }

fun PresentationDiscFormat.mapToDomainClass(): KClass<out DomainDiscFormat> =
  when (this) {
    PresentationDiscFormat.DVD -> DomainDiscFormat.DVD::class
    PresentationDiscFormat.BLU_RAY -> DomainDiscFormat.BluRay::class
    PresentationDiscFormat.UHD -> DomainDiscFormat.UHD::class
  }

private fun DomainDiscFormat.DVD.Region.mapToDiscRegion(): DiscRegion =
  when (this) {
    DomainDiscFormat.DVD.Region.ALL -> DiscRegion.ZERO
    DomainDiscFormat.DVD.Region.ONE -> DiscRegion.ONE
    DomainDiscFormat.DVD.Region.TWO -> DiscRegion.TWO
    DomainDiscFormat.DVD.Region.THREE -> DiscRegion.THREE
    DomainDiscFormat.DVD.Region.FOUR -> DiscRegion.FOUR
    DomainDiscFormat.DVD.Region.FIVE -> DiscRegion.FIVE
    DomainDiscFormat.DVD.Region.SIX -> DiscRegion.SIX
  }

private fun DomainDiscFormat.BluRay.Region.mapToDiscRegion(): DiscRegion =
  when (this) {
    DomainDiscFormat.BluRay.Region.ALL -> DiscRegion.ALL
    DomainDiscFormat.BluRay.Region.A -> DiscRegion.A
    DomainDiscFormat.BluRay.Region.B -> DiscRegion.B
    DomainDiscFormat.BluRay.Region.C -> DiscRegion.C
  }
