package dev.johnnylewis.disctrackr.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.johnnylewis.disctrackr.R
import dev.johnnylewis.disctrackr.presentation.model.DiscFormat

@Composable
fun DiscFormat.getName(): String =
  when (this) {
    DiscFormat.DVD -> stringResource(R.string.disc_format_dvd)
    DiscFormat.BLU_RAY -> stringResource(R.string.disc_format_bluray)
    DiscFormat.UHD -> stringResource(R.string.disc_format_uhd)
  }
