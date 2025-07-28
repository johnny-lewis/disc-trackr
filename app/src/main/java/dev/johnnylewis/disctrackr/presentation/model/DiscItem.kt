package dev.johnnylewis.disctrackr.presentation.model

data class DiscItem(
  val id: Int,
  val title: String,
  val imageUrl: String?,
  val format: DiscFormat,
  val regions: List<DiscRegion>,
  val country: Country?,
  val distributor: String,
  val year: Int?,
  val blurayId: String?,
)
