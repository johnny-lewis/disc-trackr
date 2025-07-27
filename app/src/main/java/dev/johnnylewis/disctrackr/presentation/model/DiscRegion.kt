package dev.johnnylewis.disctrackr.presentation.model

enum class DiscRegion(val format: DiscFormat) {
  Zero(DiscFormat.DVD),
  One(DiscFormat.DVD),
  Two(DiscFormat.DVD),
  Three(DiscFormat.DVD),
  Four(DiscFormat.DVD),
  Five(DiscFormat.DVD),
  Six(DiscFormat.DVD),
  A(DiscFormat.BLU_RAY),
  B(DiscFormat.BLU_RAY),
  C(DiscFormat.BLU_RAY),
  ALL(DiscFormat.BLU_RAY),
}
