package dev.johnnylewis.disctrackr.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import dev.johnnylewis.disctrackr.presentation.theme.AppTheme
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  @Inject
  lateinit var navigationFlow: MutableSharedFlow<NavigationGraph.Route>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      AppTheme {
        NavigationGraph(
          activity = this,
          navigationFlow = navigationFlow.asSharedFlow(),
        )
          .Build(startDestination = NavigationGraph.Route.Disc)
      }
    }
  }
}
