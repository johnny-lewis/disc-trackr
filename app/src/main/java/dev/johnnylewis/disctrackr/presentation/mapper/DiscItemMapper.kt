package dev.johnnylewis.disctrackr.presentation.mapper

import dev.johnnylewis.disctrackr.domain.model.Disc
import dev.johnnylewis.disctrackr.presentation.model.DiscItem
import dev.johnnylewis.disctrackr.presentation.model.DiscRegion
import dev.johnnylewis.disctrackr.presentation.util.CountryUtil
import dev.johnnylewis.disctrackr.domain.model.DiscFormat as DomainDiscFormat
import dev.johnnylewis.disctrackr.presentation.model.DiscFormat as PresentationDiscFormat

private const val BLU_RAY_IMAGE_URL =
  "https://images.static-bluray.com/movies/covers/{{id}}_front.jpg"

fun List<Disc>.mapToPresentation(): List<DiscItem> =
  mapNotNull(Disc::mapToPresentation)

private fun Disc.mapToPresentation(): DiscItem? =
  id?.let {
    val (format, regions) = format.mapToPresentation()
    DiscItem(
      id = id,
      title = title,
      imageUrl = getImageUrl(),
      format = format,
      regions = regions,
      country = countryCode?.let(CountryUtil::getCountryFromCode),
      distributor = distributor,
      year = year,
      blurayId = blurayId,
    )
  }

private fun Disc.getImageUrl(): String? =
  imageUrl ?: run {
    if (blurayId.isNotBlank()) {
      BLU_RAY_IMAGE_URL.replace("{{id}}", blurayId)
    } else {
      null
    }
  }

private fun DomainDiscFormat.mapToPresentation(): Pair<PresentationDiscFormat, List<DiscRegion>> =
  when (this) {
    is DomainDiscFormat.DVD ->
      PresentationDiscFormat.DVD to regions.map(DomainDiscFormat.DVD.Region::mapToDiscRegion)
    is DomainDiscFormat.BluRay ->
      PresentationDiscFormat.BLU_RAY to regions.map(DomainDiscFormat.BluRay.Region::mapToDiscRegion)
    DomainDiscFormat.UHD -> PresentationDiscFormat.UHD to emptyList()
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
