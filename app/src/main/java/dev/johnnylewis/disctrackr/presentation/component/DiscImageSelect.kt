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
import androidx.compose.ui.layout.ContentScale
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
  modifier: Modifier,
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
      )
      IconButton(
        modifier = Modifier
          .size(56.dp)
          .clip(RoundedCornerShape(6.dp))
          .background(
            color = if (state.isImageUrlTextValueValid) {
              MaterialTheme.colorScheme.primary
            } else {
              ButtonDefaults.buttonColors().disabledContainerColor
            },
          ),
        onClick = { onEvent(DiscImageSelectViewModel.Event.SubmitImageUrl) },
        enabled = state.isImageUrlTextValueValid,
      ) {
        Icon(
          tint = if (state.isImageUrlTextValueValid) {
            MaterialTheme.colorScheme.onPrimary
          } else {
            ButtonDefaults.buttonColors().disabledContentColor
          },
          painter = painterResource(R.drawable.cloud_download),
          contentDescription = null,
        )
      }
    }
    SubcomposeAsyncImage(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .clip(RoundedCornerShape(6.dp)),
      model = state.submittedImageUrl,
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
            Text(
              modifier = Modifier
                .padding(horizontal = 16.dp),
              text = stringResource(
                id = if (state.submittedImageUrl.isBlank()) {
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
    Button(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp),
      shape = RoundedCornerShape(6.dp),
      enabled = state.isImageValid,
      onClick = { onSubmit(state.submittedImageUrl) },
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
@LightDarkPreview
private fun DiscImageSelectPreview() = PreviewHelper.Screen {
//  DiscImageSelectContent(
//    onSubmit = {},
//  )
}
