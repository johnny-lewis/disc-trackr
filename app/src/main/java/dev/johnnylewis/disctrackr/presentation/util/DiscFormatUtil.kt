package dev.johnnylewis.disctrackr.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.johnnylewis.disctrackr.R
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
