package dev.johnnylewis.disctrackr.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.johnnylewis.disctrackr.R
import dev.johnnylewis.disctrackr.domain.model.DiscFormat
import dev.johnnylewis.disctrackr.presentation.model.DiscFormResult

@Composable
fun DiscFormResult.DiscFormFormat.getName(): String =
  when (this) {
    DiscFormResult.DiscFormFormat.DVD -> stringResource(R.string.disc_format_dvd)
    DiscFormResult.DiscFormFormat.BLU_RAY -> stringResource(R.string.disc_format_bluray)
    DiscFormResult.DiscFormFormat.UHD -> stringResource(R.string.disc_format_uhd)
  }

@Composable
fun DiscFormat.getName(): String =
  when (this) {
    is DiscFormat.DVD -> stringResource(R.string.disc_format_dvd)
    is DiscFormat.BluRay -> stringResource(R.string.disc_format_bluray)
    DiscFormat.UHD -> stringResource(R.string.disc_format_uhd)
  }
