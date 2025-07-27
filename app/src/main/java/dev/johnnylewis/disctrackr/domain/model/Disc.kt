package dev.johnnylewis.disctrackr.domain.model

data class Disc(
  val id: Int?,
  val title: String,
  val imageUrl: String?,
  val format: DiscFormat,
  val countryCode: String?,
  val distributor: String,
  val blurayId: String,
)
