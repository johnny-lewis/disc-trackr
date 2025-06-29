package dev.johnnylewis.disctrackr.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.johnnylewis.disctrackr.presentation.viewmodel.DiscScreenViewModel

@Composable
fun DiscScreen(
  viewModel: DiscScreenViewModel,
) {
  DiscScreenContent()
}

@Composable
private fun DiscScreenContent() {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background),
    contentAlignment = Alignment.Center,
  ) {
    Text(
      text = "Disc Screen",
      style = MaterialTheme.typography.displayMedium,
      color = MaterialTheme.colorScheme.onBackground,
    )
  }
}
