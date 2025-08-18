package dev.johnnylewis.disctrackr.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.johnnylewis.disctrackr.R
import dev.johnnylewis.disctrackr.domain.model.Disc
import dev.johnnylewis.disctrackr.domain.model.DiscFormat
import dev.johnnylewis.disctrackr.presentation.model.DiscFormResult
import kotlin.reflect.KClass

@Composable
fun DiscFormResult.DiscFormFormat.getName(): String =
  when (this) {
    DiscFormResult.DiscFormFormat.DVD -> stringResource(R.string.disc_format_dvd)
    DiscFormResult.DiscFormFormat.BLU_RAY -> stringResource(R.string.disc_format_bluray)
    DiscFormResult.DiscFormFormat.UHD -> stringResource(R.string.disc_format_uhd)
  }

@Composable
fun KClass<out DiscFormat>.getName(): String =
  when (simpleName) {
    DiscFormat.DVD::class.simpleName -> stringResource(R.string.disc_format_dvd)
    DiscFormat.BluRay::class.simpleName -> stringResource(R.string.disc_format_bluray)
    DiscFormat.UHD::class.simpleName -> stringResource(R.string.disc_format_uhd)
    else -> throw IllegalArgumentException("Unknown disc format: $simpleName")
  }

@Composable
fun Disc.getFormatWithRegions(): String =
  when (format) {
    is DiscFormat.DVD ->
      format.getRegionString().takeIf { it.isNotEmpty() }?.let { regions ->
        stringResource(
          id = R.string.disc_format_region,
          formatArgs = arrayOf(stringResource(R.string.disc_format_dvd), regions),
        )
      } ?: stringResource(R.string.disc_format_dvd)
    is DiscFormat.BluRay ->
      format.getRegionString().takeIf { it.isNotEmpty() }?.let { regions ->
        stringResource(
          id = R.string.disc_format_region,
          formatArgs = arrayOf(stringResource(R.string.disc_format_bluray), regions),
        )
      } ?: stringResource(R.string.disc_format_bluray)
    DiscFormat.UHD -> stringResource(R.string.disc_format_uhd)
  }

private fun DiscFormat.DVD.getRegionString(): String =
  regions.joinToString(", ") {
    when (it) {
      DiscFormat.DVD.Region.ALL -> "0"
      DiscFormat.DVD.Region.ONE -> "1"
      DiscFormat.DVD.Region.TWO -> "2"
      DiscFormat.DVD.Region.THREE -> "3"
      DiscFormat.DVD.Region.FOUR -> "4"
      DiscFormat.DVD.Region.FIVE -> "5"
      DiscFormat.DVD.Region.SIX -> "6"
    }
  }

@Composable
private fun DiscFormat.BluRay.getRegionString(): String =
  if (regions.firstOrNull() == DiscFormat.BluRay.Region.ALL) {
    stringResource(R.string.disc_format_region_all)
  } else {
    regions.joinToString(", ") { it.name.uppercase() }
  }
