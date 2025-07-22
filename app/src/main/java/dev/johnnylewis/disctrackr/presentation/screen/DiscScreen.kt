package dev.johnnylewis.disctrackr.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.johnnylewis.disctrackr.R
import dev.johnnylewis.disctrackr.presentation.util.LightDarkPreview
import dev.johnnylewis.disctrackr.presentation.util.PreviewHelper
import dev.johnnylewis.disctrackr.presentation.util.screenSize
import dev.johnnylewis.disctrackr.presentation.viewmodel.DiscScreenViewModel

@Composable
fun DiscScreen(viewModel: DiscScreenViewModel) {
  val state by viewModel.state.collectAsState()
  DiscScreenContent(
    state = state,
    onAddDiscPressed = viewModel::onAddDiscPressed,
  )
}

@Composable
private fun DiscScreenContent(
  state: DiscScreenViewModel.State,
  onAddDiscPressed: () -> Unit,
) {
  when (state) {
    DiscScreenViewModel.State.Initial ->
      InitialState()
    DiscScreenViewModel.State.Empty ->
      EmptyState(
        onAddDiscPressed = onAddDiscPressed,
      )
    is DiscScreenViewModel.State.Loaded ->
      InitialState()
    DiscScreenViewModel.State.Error ->
      ErrorState()
  }
}

@Composable
private fun InitialState() {
  Box(
    modifier = Modifier
      .screenSize(),
    contentAlignment = Alignment.Center,
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      CircularProgressIndicator(
        modifier = Modifier
          .size(32.dp),
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
      )
      Text(
        text = stringResource(R.string.disc_screen_initial_state_message),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground,
      )
    }
  }
}

@Composable
private fun EmptyState(
  onAddDiscPressed: () -> Unit,
) {
  Box(
    modifier = Modifier
      .screenSize(),
    contentAlignment = Alignment.Center,
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(
        text = stringResource(R.string.disc_screen_empty_state_message_title),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.SemiBold,
      )
      Text(
        text = stringResource(R.string.disc_screen_empty_state_message_body),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center,
      )
      Button(
        modifier = Modifier
          .padding(top = 8.dp),
        shape = RoundedCornerShape(6.dp),
        onClick = onAddDiscPressed,
      ) {
        Text(
          text = stringResource(R.string.disc_screen_empty_state_button),
          style = MaterialTheme.typography.labelLarge,
          color = MaterialTheme.colorScheme.onSecondary,
          fontWeight = FontWeight.SemiBold,
        )
      }
    }
  }
}

@Composable
private fun ErrorState() {
  Box(
    modifier = Modifier
      .screenSize(),
    contentAlignment = Alignment.Center,
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Text(
        text = "¯\\_(ツ)_/¯",
        style = MaterialTheme.typography.displayMedium,
        color = MaterialTheme.colorScheme.onBackground,
      )
      Text(
        text = stringResource(R.string.disc_screen_error_state_message),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center,
      )
    }
  }
}

@LightDarkPreview
@Composable
private fun InitialStatePreview() = PreviewHelper.Screen {
  InitialState()
}

@LightDarkPreview
@Composable
private fun EmptyStatePreview() = PreviewHelper.Screen {
  EmptyState(
    onAddDiscPressed = {},
  )
}

@LightDarkPreview
@Composable
private fun ErrorStatePreview() = PreviewHelper.Screen {
  ErrorState()
}
