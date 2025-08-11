package dev.johnnylewis.disctrackr.presentation.model

data class DiscFormResult(
  val title: String,
  val format: DiscFormFormat,
  val regions: List<DiscFormRegion>,
  val country: Country?,
  val distributor: String,
  val year: String,
  val blurayId: String,
) {
  enum class DiscFormFormat {
    DVD, BLU_RAY, UHD
  }

  enum class DiscFormRegion(val format: DiscFormFormat) {
    ZERO(DiscFormFormat.DVD),
    ONE(DiscFormFormat.DVD),
    TWO(DiscFormFormat.DVD),
    THREE(DiscFormFormat.DVD),
    FOUR(DiscFormFormat.DVD),
    FIVE(DiscFormFormat.DVD),
    SIX(DiscFormFormat.DVD),
    A(DiscFormFormat.BLU_RAY),
    B(DiscFormFormat.BLU_RAY),
    C(DiscFormFormat.BLU_RAY),
    ALL(DiscFormFormat.BLU_RAY),
  }
}
