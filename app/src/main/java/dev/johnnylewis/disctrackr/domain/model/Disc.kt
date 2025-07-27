package dev.johnnylewis.disctrackr.domain.model

import java.util.Locale

data class Disc(
  val id: Int?,
  val title: String,
  val imageUrl: String?,
  val format: DiscFormat,
  val countryCode: Locale.IsoCountryCode?,
  val distributor: String?,
  val blurayId: String?,
)
