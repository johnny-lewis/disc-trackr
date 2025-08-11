package dev.johnnylewis.disctrackr.presentation.component

import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.snapFlingBehavior
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import dev.johnnylewis.disctrackr.R
import dev.johnnylewis.disctrackr.domain.model.Disc
import dev.johnnylewis.disctrackr.presentation.util.CountryUtil
import dev.johnnylewis.disctrackr.presentation.util.getImageUrl
import dev.johnnylewis.disctrackr.presentation.util.noRippleClickable
import kotlinx.coroutines.launch

@Composable
fun DiscListItem(
  disc: Disc,
  onDeletedClicked: () -> Unit,
) {
  Box {
    BehindButtonRow(
      modifier = Modifier
        .matchParentSize(),
    )
    val scope = rememberCoroutineScope()

    val listState = rememberLazyListState()
    val snappingLayout = remember(listState) {
      SnapLayoutInfoProvider(listState, SnapPosition.End)
    }
    val snappyAnimationSpec: DecayAnimationSpec<Float> = rememberSplineBasedDecay()
    val flingBehavior = remember(snappingLayout, snappyAnimationSpec) {
      snapFlingBehavior(
        snapLayoutInfoProvider = snappingLayout,
        decayAnimationSpec = snappyAnimationSpec,
        snapAnimationSpec = spring(stiffness = Spring.StiffnessMedium),
      )
    }

    var itemHeight by remember { mutableIntStateOf(0) }
    LazyRow(
      modifier = Modifier
        .fillMaxWidth(),
      state = listState,
      flingBehavior = flingBehavior,
    ) {
      item {
        DiscRow(
          modifier = Modifier
            .fillParentMaxWidth()
            .onSizeChanged { itemHeight = it.height },
          disc = disc,
        )
      }
      item {
        Box(
          modifier = Modifier
            .fillParentMaxWidth(0.3f)
            .height(with(LocalDensity.current) { itemHeight.toDp() })
            .noRippleClickable {
              scope.launch {
                listState.animateScrollToItem(0)
                onDeletedClicked()
              }
            },
        )
      }
    }
  }
}

@Composable
private fun BehindButtonRow(
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier
      .fillMaxWidth(),
  ) {
    Box(
      modifier = Modifier
        .weight(1f)
        .fillMaxHeight()
        .background(MaterialTheme.colorScheme.background),
    )
    Box(
      modifier = Modifier
        .weight(1f)
        .fillMaxHeight()
        .background(MaterialTheme.colorScheme.errorContainer),
    ) {
      Row(
        modifier = Modifier
          .align(Alignment.CenterEnd),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
      ) {
        Text(
          text = "Delete",
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onErrorContainer,
          fontWeight = FontWeight.SemiBold,
        )
        Icon(
          modifier = Modifier
            .padding(end = 8.dp)
            .size(24.dp),
          tint = MaterialTheme.colorScheme.onErrorContainer,
          painter = painterResource(R.drawable.delete),
          contentDescription = null,
        )
      }
    }
  }
}

@Composable
private fun DiscRow(
  modifier: Modifier = Modifier,
  disc: Disc,
) {
  Row(
    modifier = modifier
      .background(MaterialTheme.colorScheme.background),
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    DiscImage(
      imageUrl = disc.getImageUrl(),
    )
    Column(
      verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
      Text(
        text = disc.title,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.SemiBold,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
      )
      if (disc.distributor?.isNotBlank() ?: false) {
        Text(
          text = disc.distributor,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onBackground,
          fontStyle = FontStyle.Italic,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      }
      Text(
        text = disc.getBottomText(),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground,
        fontStyle = FontStyle.Italic,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
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

private fun Disc.getBottomText(): String =
  listOfNotNull(
    countryCode?.let { CountryUtil.getCountryFromCode(it).name },
    year?.toString(),
  ).joinToString(" / ")
