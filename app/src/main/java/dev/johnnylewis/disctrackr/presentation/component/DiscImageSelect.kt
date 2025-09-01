package dev.johnnylewis.disctrackr.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import dev.johnnylewis.disctrackr.R
import dev.johnnylewis.disctrackr.presentation.util.LightDarkPreview
import dev.johnnylewis.disctrackr.presentation.util.PreviewHelper
import dev.johnnylewis.disctrackr.presentation.viewmodel.DiscImageSelectViewModel

@Composable
fun DiscImageSelect(
  modifier: Modifier = Modifier,
  viewModel: DiscImageSelectViewModel = viewModel(),
  shouldClearState: Boolean,
  onStateCleared: () -> Unit,
  onSubmit: (String) -> Unit,
) {
  LaunchedEffect(shouldClearState) {
    if (shouldClearState) {
      viewModel.onEvent(DiscImageSelectViewModel.Event.ClearState)
      onStateCleared()
    }
  }

  val state = viewModel.state.collectAsState()
  DiscImageSelectContent(
    modifier = modifier,
    state = state.value,
    onEvent = viewModel::onEvent,
    onSubmit = onSubmit,
  )
}

@Composable
private fun DiscImageSelectContent(
  modifier: Modifier = Modifier,
  state: DiscImageSelectViewModel.State,
  onEvent: (DiscImageSelectViewModel.Event) -> Unit,
  onSubmit: (String) -> Unit,
) {
  Column(
    modifier = modifier
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      TextField(
        modifier = Modifier
          .weight(1f),
        value = state.imageUrlTextValue,
        onValueChange = {
          onEvent(DiscImageSelectViewModel.Event.ImageUrlTextValueChanged(it))
        },
        label = { Text(text = stringResource(R.string.disc_image_select_text_label)) },
        singleLine = true,
        enabled = state.imageState.isTextFieldEnabled(),
      )
      ImageCheckButton(
        imageState = state.imageState,
        onEvent = onEvent,
      )
    }
    SubcomposeAsyncImage(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .clip(RoundedCornerShape(6.dp)),
      model = state.checkedImageUrl,
      contentDescription = null,
      contentScale = ContentScale.FillHeight,
    ) {
      val painterState = painter.state.collectAsState()
      when (painterState.value) {
        is AsyncImagePainter.State.Success -> {
          SubcomposeAsyncImageContent()
          onEvent(DiscImageSelectViewModel.Event.ImageUrlValid)
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
              .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center,
          ) {
            if (LocalInspectionMode.current && state.checkedImageUrl.isNotBlank()) {
              Icon(
                modifier = Modifier
                  .size(56.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                painter = painterResource(R.drawable.image),
                contentDescription = null,
              )
            } else {
              Text(
                modifier = Modifier
                  .padding(horizontal = 16.dp),
                text = stringResource(
                  id = if (state.checkedImageUrl.isBlank()) {
                    R.string.disc_image_select_initial_message
                  } else {
                    R.string.disc_image_select_error_message
                  },
                ),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
          }
        }
      }
    }
    Button(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp),
      shape = RoundedCornerShape(6.dp),
      enabled = state.imageState == DiscImageSelectViewModel.ImageState.LOADED,
      onClick = { onSubmit(state.checkedImageUrl) },
    ) {
      Text(
        text = stringResource(R.string.disc_image_select_save),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSecondary,
        fontWeight = FontWeight.SemiBold,
      )
    }
  }
}

@Composable
private fun ImageCheckButton(
  imageState: DiscImageSelectViewModel.ImageState,
  onEvent: (DiscImageSelectViewModel.Event) -> Unit,
) {
  imageState.getImageCheckButtonColors().let { (buttonColor, contentColor) ->
    IconButton(
      modifier = Modifier
        .size(56.dp)
        .clip(RoundedCornerShape(6.dp))
        .background(buttonColor),
      onClick = { onEvent(DiscImageSelectViewModel.Event.CheckOrClearImageUrl) },
      enabled = imageState.isImageCheckButtonEnabled(),
    ) {
      if (imageState == DiscImageSelectViewModel.ImageState.LOADING) {
        CircularProgressIndicator(
          modifier = Modifier
            .size(20.dp),
          color = contentColor,
          strokeWidth = 2.dp,
        )
      } else {
        Icon(
          tint = contentColor,
          painter = imageState.getImageCheckButtonIcon(),
          contentDescription = null,
        )
      }
    }
  }
}

private fun DiscImageSelectViewModel.ImageState.isTextFieldEnabled(): Boolean =
  this == DiscImageSelectViewModel.ImageState.INVALID ||
    this == DiscImageSelectViewModel.ImageState.VALID

@Composable
private fun DiscImageSelectViewModel.ImageState.getImageCheckButtonColors(): Pair<Color, Color> =
  when (this) {
    DiscImageSelectViewModel.ImageState.INVALID,
    DiscImageSelectViewModel.ImageState.LOADING,
    -> ButtonDefaults.buttonColors().disabledContainerColor to
      ButtonDefaults.buttonColors().disabledContentColor
    DiscImageSelectViewModel.ImageState.VALID,
    DiscImageSelectViewModel.ImageState.LOADED,
    -> MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
  }

@Composable
private fun DiscImageSelectViewModel.ImageState.getImageCheckButtonIcon(): Painter =
  if (this == DiscImageSelectViewModel.ImageState.LOADED) {
    painterResource(R.drawable.close)
  } else {
    painterResource(R.drawable.cloud_download)
  }

private fun DiscImageSelectViewModel.ImageState.isImageCheckButtonEnabled(): Boolean =
  this == DiscImageSelectViewModel.ImageState.VALID ||
    this == DiscImageSelectViewModel.ImageState.LOADED

@Composable
@LightDarkPreview
private fun DiscImageSelectPreview_Invalid() = PreviewHelper.Component {
  DiscImageSelectContent(
    state = DiscImageSelectViewModel.State(
      imageState = DiscImageSelectViewModel.ImageState.INVALID,
    ),
    onSubmit = {},
    onEvent = {},
  )
}

@Composable
@LightDarkPreview
private fun DiscImageSelectPreview_Valid() = PreviewHelper.Component {
  DiscImageSelectContent(
    state = DiscImageSelectViewModel.State(
      imageState = DiscImageSelectViewModel.ImageState.VALID,
      imageUrlTextValue = "https://example.com/image.jpg",
      checkedImageUrl = "https://example.com/image.jpg",
    ),
    onSubmit = {},
    onEvent = {},
  )
}
