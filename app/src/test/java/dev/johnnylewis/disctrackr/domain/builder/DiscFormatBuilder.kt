package dev.johnnylewis.disctrackr.domain.builder

import dev.johnnylewis.disctrackr.domain.model.DiscFormat

fun buildDVDDiscFormat(
  regions: List<DiscFormat.DVD.Region> = listOf(DiscFormat.DVD.Region.FOUR),
): DiscFormat.DVD =
  DiscFormat.DVD(
    regions = regions,
  )

fun buildBluRayDiscFormat(
  regions: List<DiscFormat.BluRay.Region> = listOf(DiscFormat.BluRay.Region.B),
): DiscFormat.BluRay =
  DiscFormat.BluRay(
    regions = regions,
  )

fun buildUHDDiscFormat(): DiscFormat.UHD =
  DiscFormat.UHD
