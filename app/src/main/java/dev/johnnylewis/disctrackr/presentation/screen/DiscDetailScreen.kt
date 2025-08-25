package dev.johnnylewis.disctrackr.presentation.screen

import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import androidx.palette.graphics.Palette.from
import coil3.asDrawable
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import dev.johnnylewis.disctrackr.R
import dev.johnnylewis.disctrackr.domain.model.Disc
import dev.johnnylewis.disctrackr.presentation.component.DiscForm
import dev.johnnylewis.disctrackr.presentation.model.DiscFormResult
import dev.johnnylewis.disctrackr.presentation.util.CountryUtil
import dev.johnnylewis.disctrackr.presentation.util.asComposeColor
import dev.johnnylewis.disctrackr.presentation.util.darken
import dev.johnnylewis.disctrackr.presentation.util.getFormatWithRegions
import dev.johnnylewis.disctrackr.presentation.util.getImageUrl
import dev.johnnylewis.disctrackr.presentation.util.toPalette
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
    is DiscDetailScreenViewModel.State.Loaded -> {
      LoadedContent(
        disc = state.disc,
        onEvent = onEvent,
      )
      BottomSheet(
        isSheetExpanded = state.isDiscFormExpanded,
        disc = state.disc,
        shouldClearState = state.shouldClearDiscFormState,
        onStateCleared = {
          onEvent(DiscDetailScreenViewModel.Event.DiscFormStateCleared)
        },
        onDismiss = {
          onEvent(DiscDetailScreenViewModel.Event.DiscFormExpandedChanged(isExpanded = false))
        },
        onSubmit = { result ->
          onEvent(DiscDetailScreenViewModel.Event.DiscFormSubmitted(result))
        },
      )
    }
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
  val defaultPaletteColor = MaterialTheme.colorScheme.background.toArgb()
  var palette by remember {
    mutableStateOf(
      from(listOf(Palette.Swatch(defaultPaletteColor, 1))),
    )
  }

  var isMoreMenuVisible by remember { mutableStateOf(false) }

  Box(
    modifier = Modifier
      .fillMaxSize(),
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.3f)
        .align(Alignment.TopCenter)
        .background(
          brush = Brush.verticalGradient(
            colors = listOf(
              palette.getDominantColor(Color.Black.toArgb())
                .darken(darkLevel = if (isSystemInDarkTheme()) 0.65f else 1.65f)
                .asComposeColor(),
              Color.Transparent,
            ),
          ),
        ),
    )
    CircleIconButton(
      modifier = Modifier
        .align(Alignment.TopStart)
        .statusBarsPadding()
        .padding(top = 8.dp, start = 8.dp),
      icon = painterResource(R.drawable.arrow_back),
      onClick = { onEvent(DiscDetailScreenViewModel.Event.BackPressed) },
    )
    Box(
      modifier = Modifier
        .align(Alignment.TopEnd)
        .statusBarsPadding()
        .padding(top = 8.dp, end = 8.dp),
    ) {
      CircleIconButton(
        icon = painterResource(R.drawable.more_vert),
        onClick = { isMoreMenuVisible = true },
      )
      MoreMenu(
        isVisible = isMoreMenuVisible,
        offset = DpOffset(x = 0.dp, y = 0.dp),
        disc = disc,
        onEvent = onEvent,
        onDismiss = { isMoreMenuVisible = false },
      )
    }

    Column(
      modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .padding(horizontal = 16.dp),
    ) {
      DiscImage(
        modifier = Modifier
          .align(Alignment.CenterHorizontally)
          .fillMaxHeight(0.4f)
          .padding(top = 32.dp)
          .clip(RoundedCornerShape(6.dp)),
        url = disc.getImageUrl(),
        onDrawn = { palette = it.toBitmap().toPalette() },
      )
      InfoSection(
        modifier = Modifier
          .padding(top = 20.dp),
        disc = disc,
      )
    }
  }
}

@Composable
private fun MoreMenu(
  isVisible: Boolean,
  offset: DpOffset,
  disc: Disc,
  onEvent: (DiscDetailScreenViewModel.Event) -> Unit,
  onDismiss: () -> Unit,
) {
  DropdownMenu(
    offset = offset,
    expanded = isVisible,
    onDismissRequest = onDismiss,
  ) {
    DropdownMenuItem(
      text = { Text(stringResource(R.string.disc_detail_edit)) },
      leadingIcon = {
        Icon(
          painter = painterResource(R.drawable.edit),
          contentDescription = null,
        )
      },
      onClick = {
        onEvent(DiscDetailScreenViewModel.Event.DiscFormExpandedChanged(isExpanded = true))
        onDismiss()
      },
    )
    if (!disc.blurayId.isNullOrBlank()) {
      DropdownMenuItem(
        text = { Text(stringResource(R.string.disc_detail_open_in_browser)) },
        leadingIcon = {
          Icon(
            painter = painterResource(R.drawable.open_in_new),
            contentDescription = null,
          )
        },
        onClick = {
          onEvent(DiscDetailScreenViewModel.Event.OpenInBrowser(disc.blurayId))
          onDismiss()
        },
      )
    }
  }
}

@Composable
private fun DiscImage(
  modifier: Modifier = Modifier,
  url: String?,
  onDrawn: (Drawable) -> Unit,
) {
  val resources = LocalResources.current
  SubcomposeAsyncImage(
    modifier = modifier,
    model = url,
    contentDescription = null,
    contentScale = ContentScale.FillHeight,
  ) {
    val painterState = painter.state.collectAsState()
    when (val painter = painterState.value) {
      is AsyncImagePainter.State.Success -> {
        SubcomposeAsyncImageContent()
        onDrawn(painter.result.image.asDrawable(resources))
      }
      is AsyncImagePainter.State.Loading -> {
        Box(
          modifier = modifier
            .aspectRatio(ratio = 0.7f)
            .background(MaterialTheme.colorScheme.surfaceVariant),
          contentAlignment = Alignment.Center,
        ) {
          CircularProgressIndicator(
            modifier = Modifier
              .size(20.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 2.dp,
          )
        }
      }
      else -> {
        Box(
          modifier = modifier
            .aspectRatio(ratio = 0.7f)
            .background(MaterialTheme.colorScheme.surfaceVariant),
          contentAlignment = Alignment.Center,
        ) {
          Icon(
            modifier = Modifier
              .size(36.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            painter = painterResource(R.drawable.broken_image),
            contentDescription = null,
          )
        }
      }
    }
  }
}

@Composable
private fun InfoSection(
  modifier: Modifier = Modifier,
  disc: Disc,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    InfoField(
      title = stringResource(R.string.disc_detail_title),
      value = disc.title,
    )
    if (!disc.distributor.isNullOrBlank()) {
      InfoField(
        title = stringResource(R.string.disc_detail_distributor),
        value = disc.year?.let {
          stringResource(R.string.disc_detail_distributor_with_year, disc.distributor, it)
        } ?: disc.distributor,
      )
    }
    CountryUtil.getCountryFromCode(disc.countryCode ?: "")?.let { country ->
      InfoField(
        title = stringResource(R.string.disc_detail_country),
        value = "${country.name} ${CountryUtil.getFlag(country.code)}",
      )
    }
    InfoField(
      title = "Format",
      value = disc.getFormatWithRegions(),
    )
  }
}

@Composable
fun InfoField(
  modifier: Modifier = Modifier,
  title: String,
  value: String,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(2.dp),
  ) {
    Text(
      text = title,
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onBackground,
    )
    Text(
      text = value,
      style = MaterialTheme.typography.bodyLarge,
      color = MaterialTheme.colorScheme.onBackground,
    )
  }
}

@Composable
private fun CircleIconButton(
  modifier: Modifier = Modifier,
  icon: Painter,
  onClick: () -> Unit,
) {
  IconButton(
    modifier = modifier
      .clip(CircleShape)
      .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)),
    onClick = onClick,
  ) {
    Icon(
      tint = MaterialTheme.colorScheme.onBackground,
      painter = icon,
      contentDescription = null,
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheet(
  isSheetExpanded: Boolean,
  disc: Disc,
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
      editDisc = disc,
      shouldClearState = shouldClearState,
      onStateCleared = onStateCleared,
      onSubmit = onSubmit,
    )
  }
}
