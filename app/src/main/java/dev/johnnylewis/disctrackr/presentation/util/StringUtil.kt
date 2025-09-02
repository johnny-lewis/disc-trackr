package dev.johnnylewis.disctrackr.presentation.util

import android.util.Patterns

fun String.isOnlyDigits(): Boolean =
  "^\\d+$".toRegex().matches(this)

fun String.isValidUrl(): Boolean =
  Patterns.WEB_URL.matcher(this).matches()
