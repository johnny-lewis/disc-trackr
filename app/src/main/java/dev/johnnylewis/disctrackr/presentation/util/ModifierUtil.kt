package dev.johnnylewis.disctrackr.presentation.util

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun Modifier.screenSize(): Modifier =
  this.fillMaxSize().padding(horizontal = 16.dp)

// For ktlint. Remove once there are more functions in this file
object ModifierUtil
