package dev.johnnylewis.disctrackr.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import dev.johnnylewis.disctrackr.R
import dev.johnnylewis.disctrackr.presentation.model.DiscItem

@Composable
fun DiscListItem(
  discItem: DiscItem,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    DiscImage(
      imageUrl = discItem.imageUrl,
    )
    Column(
      verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
      Text(
        text = discItem.title,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.SemiBold,
      )
      if (discItem.distributor.isNotBlank()) {
        Text(
          text = discItem.distributor,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onBackground,
          fontStyle = FontStyle.Italic,
        )
      }
      Text(
        text = discItem.getBottomText(),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground,
        fontStyle = FontStyle.Italic,
      )
    }
  }
}

@Composable
private fun DiscImage(
  modifier: Modifier = Modifier,
  imageUrl: String?,
) {
  SubcomposeAsyncImage(
    modifier = modifier
      .size(width = 60.dp, height = 80.dp)
      .clip(RoundedCornerShape(6.dp)),
    model = imageUrl,
    contentDescription = null,
    contentScale = ContentScale.FillWidth,
  ) {
    val state by painter.state.collectAsState()
    when (state) {
      is AsyncImagePainter.State.Success ->
        SubcomposeAsyncImageContent()
      is AsyncImagePainter.State.Loading ->
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
          contentAlignment = Alignment.Center,
        ) {
          CircularProgressIndicator(
            modifier = Modifier
              .size(20.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 2.dp,
          )
        }
      else ->
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
          contentAlignment = Alignment.Center,
        ) {
          Icon(
            modifier = Modifier
              .size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            painter = painterResource(R.drawable.broken_image),
            contentDescription = null,
          )
        }
    }
  }
}

private fun DiscItem.getBottomText(): String =
  listOfNotNull(country?.name, year?.toString())
    .joinToString(" / ")
