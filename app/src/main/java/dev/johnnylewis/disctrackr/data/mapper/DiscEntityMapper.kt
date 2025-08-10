package dev.johnnylewis.disctrackr.data.mapper

import dev.johnnylewis.disctrackr.data.database.entity.DiscEntity
import dev.johnnylewis.disctrackr.domain.model.Disc
import dev.johnnylewis.disctrackr.domain.model.DiscFormat

fun List<DiscEntity>.mapToDisc(): List<Disc> =
  map(DiscEntity::mapToDisc)

fun DiscEntity.mapToDisc(): Disc =
  Disc(
    id = id,
    title = title,
    imageUrl = imageUrl,
    format = format.toDiscFormat(region),
    countryCode = countryCode,
    distributor = distributor,
    year = year,
    blurayId = blurayId,
  )

fun Disc.mapToDiscEntity(): DiscEntity =
  format.toEntity().let { (format, region) ->
    DiscEntity(
      id = id,
      title = title,
      imageUrl = imageUrl,
      format = format,
      region = region?.lowercase(),
      countryCode = countryCode,
      distributor = distributor,
      year = year,
      blurayId = blurayId,
      titleSort = createTitleSort(),
    )
  }

private fun String.toDiscFormat(region: String?): DiscFormat =
  when (this) {
    "dvd" -> DiscFormat.DVD(region.toDvdRegions())
    "br" -> DiscFormat.BluRay(region.toBluRayRegions())
    "uhd" -> DiscFormat.UHD
    else -> throw Exception()
  }

private fun String?.toDvdRegions(): List<DiscFormat.DVD.Region> =
  this?.split(",")?.mapNotNull { str ->
    DiscFormat.DVD.Region.entries.firstOrNull { it.name.lowercase() == str.lowercase() }
  }?.let { regions ->
    if (regions.contains(DiscFormat.DVD.Region.ALL)) {
      listOf(DiscFormat.DVD.Region.ALL)
    } else if (regions.size == 6) {
      listOf(DiscFormat.DVD.Region.ALL)
    } else {
      regions
    }
  } ?: emptyList()

private fun String?.toBluRayRegions(): List<DiscFormat.BluRay.Region> =
  this?.split(",")?.mapNotNull { str ->
    DiscFormat.BluRay.Region.entries.firstOrNull { it.name.lowercase() == str.lowercase() }
  }?.let { regions ->
    if (regions.contains(DiscFormat.BluRay.Region.ALL)) {
      listOf(DiscFormat.BluRay.Region.ALL)
    } else if (regions.size == 3) {
      listOf(DiscFormat.BluRay.Region.ALL)
    } else {
      regions
    }
  } ?: emptyList()

private fun DiscFormat.toEntity(): Pair<String, String?> =
  when (this) {
    is DiscFormat.DVD -> "dvd" to regions.joinToString(",")
    is DiscFormat.BluRay -> "br" to regions.joinToString(",")
    is DiscFormat.UHD -> "uhd" to null
  }

private val ignoredWords: List<String> = listOf("the", "a", "an", "of")
private fun Disc.createTitleSort(): String =
  title
    .lowercase()
    .split(" ")
    .let { parts ->
      if (parts.first() in ignoredWords) {
        parts.subList(1, parts.size - 1) + (parts.last() + ",") + parts.first()
      } else {
        parts
      }
    }
    .joinToString(" ")
