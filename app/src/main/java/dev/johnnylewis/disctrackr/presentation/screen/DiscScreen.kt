package dev.johnnylewis.disctrackr.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.johnnylewis.disctrackr.R
import dev.johnnylewis.disctrackr.presentation.component.DiscForm
import dev.johnnylewis.disctrackr.presentation.component.DiscListItem
import dev.johnnylewis.disctrackr.presentation.model.DiscFormResult
import dev.johnnylewis.disctrackr.presentation.util.LightDarkPreview
import dev.johnnylewis.disctrackr.presentation.util.PreviewHelper
import dev.johnnylewis.disctrackr.presentation.util.screenSize
import dev.johnnylewis.disctrackr.presentation.viewmodel.DiscScreenViewModel

@Composable
fun DiscScreen(viewModel: DiscScreenViewModel) {
  val state by viewModel.state.collectAsState()
  DiscScreenContent(
    state = state,
    onEvent = viewModel::onEvent,
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiscScreenContent(
  state: DiscScreenViewModel.State,
  onEvent: (DiscScreenViewModel.Event) -> Unit,
) {
  when (state.subState) {
    DiscScreenViewModel.State.SubState.Initial ->
      InitialState()
    DiscScreenViewModel.State.SubState.Empty ->
      EmptyState(
        onEvent = onEvent,
      )
    DiscScreenViewModel.State.SubState.Loaded ->
      LoadedState(
        state = state,
        onEvent = onEvent,
      )
    DiscScreenViewModel.State.SubState.Error ->
      ErrorState()
  }

  BottomSheet(
    isSheetExpanded = state.isDiscFormExpanded,
    shouldClearState = state.shouldClearDiscFormState,
    onStateCleared = { onEvent(DiscScreenViewModel.Event.DiscFormStateCleared) },
    onDismiss = {
      onEvent(DiscScreenViewModel.Event.DiscFormExpandedChanged(isExpanded = false))
    },
    onSubmit = { result ->
      onEvent(DiscScreenViewModel.Event.DiscFormSubmitted(result))
    },
  )
}

@Composable
private fun LoadedState(
  state: DiscScreenViewModel.State,
  onEvent: (DiscScreenViewModel.Event) -> Unit,
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
  ) {
    item {
      Row(
        modifier = Modifier
          .padding(vertical = 8.dp)
          .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          modifier = Modifier
            .weight(1f),
          text = stringResource(R.string.disc_screen_title).lowercase(),
          style = MaterialTheme.typography.headlineMedium,
          color = MaterialTheme.colorScheme.onBackground,
          fontWeight = FontWeight.SemiBold,
        )
        IconButton(
          onClick = {
            onEvent(DiscScreenViewModel.Event.DiscFormExpandedChanged(isExpanded = true))
          },
        ) {
          Icon(
            painter = painterResource(R.drawable.add),
            contentDescription = null,
          )
        }
      }
    }
    items(state.discs.size) { index ->
      Column {
        DiscListItem(discItem = state.discs[index])
        if (index != state.discs.size - 1) {
          Box(
            modifier = Modifier
              .padding(8.dp)
              .fillMaxWidth()
              .height((0.5).dp)
              .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)),
          )
        }
      }
    }
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
  onEvent: (DiscScreenViewModel.Event) -> Unit,
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
        onClick = {
          onEvent(DiscScreenViewModel.Event.DiscFormExpandedChanged(isExpanded = true))
        },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheet(
  isSheetExpanded: Boolean,
  shouldClearState: Boolean,
  onStateCleared: () -> Unit,
  onDismiss: () -> Unit,
  onSubmit: (DiscFormResult) -> Unit,
) {
  if (!isSheetExpanded) {
    return
  }
  ModalBottomSheet(
    modifier = Modifier
      .systemBarsPadding(),
    onDismissRequest = onDismiss,
    sheetState = rememberModalBottomSheetState(
      skipPartiallyExpanded = true,
    ),
  ) {
    DiscForm(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.95f),
      shouldClearState = shouldClearState,
      onStateCleared = onStateCleared,
      onSubmit = onSubmit,
    )
  }
}

@LightDarkPreview
@Composable
private fun EmptyStatePreview() = PreviewHelper.Screen {
  EmptyState(
    onEvent = {},
  )
}

@LightDarkPreview
@Composable
private fun InitialStatePreview() = PreviewHelper.Screen {
  InitialState()
}

@LightDarkPreview
@Composable
private fun ErrorStatePreview() = PreviewHelper.Screen {
  ErrorState()
}
