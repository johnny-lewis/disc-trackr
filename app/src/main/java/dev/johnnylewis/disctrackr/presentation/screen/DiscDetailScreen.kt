package dev.johnnylewis.disctrackr.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.johnnylewis.disctrackr.R
import dev.johnnylewis.disctrackr.domain.model.Disc
import dev.johnnylewis.disctrackr.presentation.viewmodel.DiscDetailScreenViewModel

@Composable
fun DiscDetailScreen(
  viewModel: DiscDetailScreenViewModel,
) {
  val state by viewModel.state.collectAsState()
  DiscDetailScreenContent(
    state = state,
    onEvent = viewModel::onEvent,
  )
}

@Composable
private fun DiscDetailScreenContent(
  state: DiscDetailScreenViewModel.State,
  onEvent: (DiscDetailScreenViewModel.Event) -> Unit,
) {
  when (state) {
    DiscDetailScreenViewModel.State.Initial ->
      InitialContent()
    is DiscDetailScreenViewModel.State.Loaded ->
      LoadedContent(
        disc = state.disc,
        onEvent = onEvent,
      )
  }
}

@Composable
private fun InitialContent() {
  Box(
    modifier = Modifier
      .fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    CircularProgressIndicator(
      modifier = Modifier
        .size(32.dp),
      color = MaterialTheme.colorScheme.secondary,
      trackColor = MaterialTheme.colorScheme.surfaceVariant,
    )
  }
}

@Composable
private fun LoadedContent(
  disc: Disc,
  onEvent: (DiscDetailScreenViewModel.Event) -> Unit,
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
  ) {
    TopBar(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp),
      onBackPressed = { onEvent(DiscDetailScreenViewModel.Event.BackPressed) },
    )
  }
}

@Composable
private fun TopBar(
  modifier: Modifier = Modifier,
  onBackPressed: () -> Unit,
) {
  Row(modifier = modifier) {
    IconButton(
      onClick = onBackPressed,
    ) {
      Icon(
        painter = painterResource(R.drawable.arrow_back),
        contentDescription = null,
      )
    }
  }
}
