package dev.johnnylewis.disctrackr.presentation.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun Modifier.screenSize(): Modifier =
  this.fillMaxSize().padding(horizontal = 16.dp)

@Composable
fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier =
  this.clickable(
    indication = null,
    interactionSource = remember { MutableInteractionSource() },
    onClick = onClick,
  )
