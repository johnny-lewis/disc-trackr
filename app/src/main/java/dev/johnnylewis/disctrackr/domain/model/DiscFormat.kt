package dev.johnnylewis.disctrackr.domain.model

sealed class DiscFormat {
  data class DVD(
    val regions: List<Region>,
  ) : DiscFormat() {
    enum class Region {
      ALL, ONE, TWO, THREE, FOUR, FIVE, SIX,
    }
  }

  data class BluRay(
    val regions: List<Region>,
  ) : DiscFormat() {
    enum class Region {
      ALL, A, B, C,
    }
  }

  data object UHD : DiscFormat()
}
