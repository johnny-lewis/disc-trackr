package dev.johnnylewis.disctrackr.presentation.util

import dev.johnnylewis.disctrackr.presentation.model.DiscFormat
import dev.johnnylewis.disctrackr.presentation.model.DiscRegion

fun DiscFormat.hasRegions(): Boolean =
  DiscRegion.entries.any { it.format == this }
