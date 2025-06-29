package dev.johnnylewis.disctrackr.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import dev.johnnylewis.disctrackr.presentation.theme.AppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      AppTheme {
        NavigationGraph(activity = this)
          .Build(startDestination = NavigationGraph.Route.Disc)
      }
    }
  }
}
