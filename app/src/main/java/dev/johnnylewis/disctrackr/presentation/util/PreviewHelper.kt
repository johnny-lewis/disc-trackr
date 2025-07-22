package dev.johnnylewis.disctrackr.presentation.util

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.johnnylewis.disctrackr.presentation.theme.AppTheme

object PreviewHelper {
  @Composable
  fun Screen(
    content: @Composable () -> Unit,
  ) {
    AppTheme {
      Scaffold { padding ->
        Box(
          modifier = Modifier
            .padding(padding)
        ) {
          content()
        }
      }
    }
  }
}

@Preview(
  name = "Light Mode",
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
  name = "Dark Mode",
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class LightDarkPreview
