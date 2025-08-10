package dev.johnnylewis.disctrackr.domain.builder

import dev.johnnylewis.disctrackr.domain.model.Disc
import dev.johnnylewis.disctrackr.domain.model.DiscFormat

fun buildDisc(
  id: Int? = 1,
  title: String = "DISC_TITLE",
  imageUrl: String? = "IMAGE_URL",
  format: DiscFormat = buildBluRayDiscFormat(),
  countryCode: String? = "au",
  distributor: String = "DISTRIBUTOR",
  year: Int? = 2025,
  blurayId: String = "BLURAY_ID",
): Disc =
  Disc(
    id = id,
    title = title,
    imageUrl = imageUrl,
    format = format,
    countryCode = countryCode,
    distributor = distributor,
    year = year,
    blurayId = blurayId,
  )
