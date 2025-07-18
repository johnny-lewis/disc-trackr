package dev.johnnylewis.disctrackr.presentation

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.johnnylewis.disctrackr.presentation.screen.DiscScreen
import dev.johnnylewis.disctrackr.presentation.viewmodel.DiscScreenViewModel
import kotlinx.serialization.Serializable

class NavigationGraph(
  activity: ComponentActivity,
) {
  private val discScreenViewModel: DiscScreenViewModel by activity.viewModels()

  private lateinit var navController: NavHostController

  @Composable
  fun Build(startDestination: Route) {
    navController = rememberNavController()
    NavHost(
      navController = navController,
      startDestination = startDestination,
    ) {
      composable<Route.Disc> {
        DiscScreen(
          viewModel = discScreenViewModel,
        )
      }
    }
  }

  sealed interface Route {
    @Serializable
    data object Disc : Route
  }
}
