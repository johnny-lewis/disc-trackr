package dev.johnnylewis.disctrackr.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.johnnylewis.disctrackr.R
import dev.johnnylewis.disctrackr.domain.model.Disc
import dev.johnnylewis.disctrackr.presentation.component.DiscForm
import dev.johnnylewis.disctrackr.presentation.component.DiscListFilters
import dev.johnnylewis.disctrackr.presentation.component.DiscListItem
import dev.johnnylewis.disctrackr.presentation.model.DiscFormResult
import dev.johnnylewis.disctrackr.presentation.util.LightDarkPreview
import dev.johnnylewis.disctrackr.presentation.util.PreviewHelper
import dev.johnnylewis.disctrackr.presentation.util.screenSize
import dev.johnnylewis.disctrackr.presentation.viewmodel.DiscListScreenViewModel
import kotlinx.coroutines.launch

@Composable
fun DiscListScreen(viewModel: DiscListScreenViewModel) {
  val state by viewModel.state.collectAsState()
  DiscListScreenContent(
    state = state,
    onEvent = viewModel::onEvent,
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiscListScreenContent(
  state: DiscListScreenViewModel.State,
  onEvent: (DiscListScreenViewModel.Event) -> Unit,
) {
  when (state.subState) {
    DiscListScreenViewModel.State.SubState.Initial ->
      InitialState()
    DiscListScreenViewModel.State.SubState.Empty ->
      EmptyState(
        onEvent = onEvent,
      )
    DiscListScreenViewModel.State.SubState.Loaded ->
      LoadedState(
        state = state,
        onEvent = onEvent,
      )
    DiscListScreenViewModel.State.SubState.Error ->
      ErrorState()
  }

  BottomSheet(
    isSheetExpanded = state.isDiscFormExpanded,
    shouldClearState = state.shouldClearDiscFormState,
    onStateCleared = { onEvent(DiscListScreenViewModel.Event.DiscFormStateCleared) },
    onDismiss = {
      onEvent(DiscListScreenViewModel.Event.DiscFormExpandedChanged(isExpanded = false))
    },
    onSubmit = { result ->
      onEvent(DiscListScreenViewModel.Event.DiscFormSubmitted(result))
    },
  )
}

@Composable
private fun LoadedState(
  state: DiscListScreenViewModel.State,
  onEvent: (DiscListScreenViewModel.Event) -> Unit,
) {
  val coroutineScope = rememberCoroutineScope()
  val listState = rememberLazyListState()
  val isScrollButtonVisible by remember {
    derivedStateOf { listState.firstVisibleItemIndex > 0 && listState.canScrollForward }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .statusBarsPadding(),
  ) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      state = listState,
    ) {
      stickyHeader {
        Row(
          modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
            modifier = Modifier
              .weight(1f),
            text = stringResource(R.string.disc_screen_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.SemiBold,
          )
          IconButton(
            onClick = {
              onEvent(DiscListScreenViewModel.Event.DiscFormExpandedChanged(isExpanded = true))
            },
          ) {
            Icon(
              painter = painterResource(R.drawable.add),
              contentDescription = null,
            )
          }
        }
      }
      item {
        DiscListFilters(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
          state = state.filterState,
          onEvent = onEvent,
        )
      }
      items(
        items = state.discs,
        key = { disc -> disc.id ?: Unit },
      ) { disc ->
        Column(
          modifier = Modifier
            .animateItem()
            .clickable {
              onEvent(DiscListScreenViewModel.Event.DiscPressed(disc))
            },
        ) {
          DiscListItem(
            disc = disc,
            onDeletedClicked = {
              onEvent(DiscListScreenViewModel.Event.DiscDeleted(disc.id))
            },
          )
          if (!state.discs.isLastIndex(disc.id)) {
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
    ScrollTopButton(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(bottom = 12.dp),
      isVisible = isScrollButtonVisible,
      onClick = {
        coroutineScope.launch {
          listState.animateScrollToItem(0)
        }
      },
    )
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
  onEvent: (DiscListScreenViewModel.Event) -> Unit,
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
          onEvent(DiscListScreenViewModel.Event.DiscFormExpandedChanged(isExpanded = true))
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

@Composable
private fun ScrollTopButton(
  modifier: Modifier = Modifier,
  isVisible: Boolean,
  onClick: () -> Unit,
) {
  AnimatedVisibility(
    modifier = modifier,
    visible = isVisible,
    enter = slideInVertically(initialOffsetY = { it / 2 }),
    exit = slideOutVertically(targetOffsetY = { it / 2 }),
  ) {
    IconButton(
      modifier = Modifier
        .size(46.dp),
      colors = IconButtonDefaults.iconButtonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
      ),
      onClick = onClick,
    ) {
      Icon(
        modifier = Modifier
          .size(22.dp),
        painter = painterResource(R.drawable.scroll_top),
        contentDescription = null,
      )
    }
  }
}

private fun List<Disc>.isLastIndex(id: Int?): Boolean =
  indexOfLast { it.id == id } == indices.last

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
