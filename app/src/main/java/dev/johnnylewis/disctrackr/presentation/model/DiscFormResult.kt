package dev.johnnylewis.disctrackr.presentation.model

data class DiscFormResult(
  val title: String,
  val format: DiscFormat,
  val regions: List<DiscRegion>,
  val country: Country?,
  val distributor: String,
  val blurayId: String,
)
