package dev.johnnylewis.disctrackr.presentation.util

import dev.johnnylewis.disctrackr.presentation.model.DiscFormat
import dev.johnnylewis.disctrackr.presentation.model.DiscItem
import dev.johnnylewis.disctrackr.presentation.model.DiscRegion

fun DiscFormat.hasRegions(): Boolean =
  DiscRegion.entries.any { it.format == this }

fun List<DiscItem>.isLastIndex(id: Int): Boolean =
  indexOfLast { it.id == id } == indices.last
