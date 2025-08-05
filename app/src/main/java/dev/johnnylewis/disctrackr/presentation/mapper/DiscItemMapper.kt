package dev.johnnylewis.disctrackr.presentation.mapper

import dev.johnnylewis.disctrackr.domain.model.Disc
import dev.johnnylewis.disctrackr.presentation.model.DiscItem
import dev.johnnylewis.disctrackr.presentation.util.CountryUtil

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
