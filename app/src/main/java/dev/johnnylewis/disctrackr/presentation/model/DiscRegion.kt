package dev.johnnylewis.disctrackr.presentation.model

enum class DiscRegion(val format: DiscFormat) {
  ZERO(DiscFormat.DVD),
  ONE(DiscFormat.DVD),
  TWO(DiscFormat.DVD),
  THREE(DiscFormat.DVD),
  FOUR(DiscFormat.DVD),
  FIVE(DiscFormat.DVD),
  SIX(DiscFormat.DVD),
  A(DiscFormat.BLU_RAY),
  B(DiscFormat.BLU_RAY),
  C(DiscFormat.BLU_RAY),
  ALL(DiscFormat.BLU_RAY),
}
