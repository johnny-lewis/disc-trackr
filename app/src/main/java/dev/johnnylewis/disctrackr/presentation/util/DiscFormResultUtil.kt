package dev.johnnylewis.disctrackr.presentation.util

import dev.johnnylewis.disctrackr.presentation.model.DiscFormResult

fun DiscFormResult.DiscFormFormat.hasRegions(): Boolean =
  DiscFormResult.DiscFormRegion.entries.any { it.format == this }

fun DiscFormResult.DiscFormFormat.getRegions(): List<DiscFormResult.DiscFormRegion> =
  DiscFormResult.DiscFormRegion.entries.filter { it.format == this }

fun DiscFormResult.DiscFormRegion.isAllRegions(): Boolean =
  this == DiscFormResult.DiscFormRegion.ALL || this == DiscFormResult.DiscFormRegion.ZERO
