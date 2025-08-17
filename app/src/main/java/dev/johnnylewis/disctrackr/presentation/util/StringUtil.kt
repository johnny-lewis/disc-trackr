package dev.johnnylewis.disctrackr.presentation.util

fun String.isOnlyDigits(): Boolean =
  "^\\d+$".toRegex().matches(this)
