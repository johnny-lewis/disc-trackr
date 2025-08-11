package dev.johnnylewis.disctrackr.data.builder

import dev.johnnylewis.disctrackr.data.database.entity.DiscEntity

fun buildDiscEntity(
  id: Int? = 1,
  title: String = "DISC_TITLE",
  imageUrl: String? = "IMAGE_URL",
  format: String = "br",
  region: String? = "a,b",
  countryCode: String? = "au",
  distributor: String = "DISTRIBUTOR",
  year: Int? = 2025,
  blurayId: String = "BLURAY_ID",
  titleSort: String = "TITLE_SORT",
): DiscEntity =
  DiscEntity(
    id = id,
    title = title,
    imageUrl = imageUrl,
    format = format,
    region = region,
    countryCode = countryCode,
    distributor = distributor,
    year = year,
    blurayId = blurayId,
    titleSort = titleSort,
  )
