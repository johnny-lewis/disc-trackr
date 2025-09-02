package dev.johnnylewis.disctrackr.presentation.util

import android.graphics.Bitmap
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.palette.graphics.Palette
import androidx.compose.ui.graphics.Color as ComposeColor

fun Bitmap.toPalette(): Palette =
  copy(Bitmap.Config.RGBA_F16, true).let { bitmap ->
    Palette.from(bitmap).generate()
  }

fun @receiver:ColorInt Int.darken(darkLevel: Float): Int =
  if (darkLevel < 0f || darkLevel > 1f) {
    this
  } else {
    Color.HSVToColor(
      FloatArray(3).apply {
        Color.colorToHSV(this@darken, this)
        this[2] *= darkLevel
      },
    )
  }

fun @receiver:ColorInt Int.asComposeColor(): ComposeColor =
  ComposeColor(this)
