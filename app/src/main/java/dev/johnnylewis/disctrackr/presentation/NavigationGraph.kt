package dev.johnnylewis.disctrackr.presentation

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.coroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.johnnylewis.disctrackr.presentation.screen.DiscDetailScreen
import dev.johnnylewis.disctrackr.presentation.screen.DiscListScreen
import dev.johnnylewis.disctrackr.presentation.viewmodel.DiscDetailScreenViewModel
import dev.johnnylewis.disctrackr.presentation.viewmodel.DiscListScreenViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class NavigationGraph(
  activity: ComponentActivity,
  navigationFlow: SharedFlow<Route>,
) {
  private val discListScreenViewModel: DiscListScreenViewModel by activity.viewModels()
  private val discDetailScreenViewModel: DiscDetailScreenViewModel by activity.viewModels()

  private lateinit var navController: NavHostController

  init {
    activity.lifecycle.coroutineScope.launch {
      navigationFlow.collect { route ->
        println("NavigationGraph: $route")
        if (::navController.isInitialized) {
          when (route) {
            Route.Pop -> navController.popBackStack()
            else -> navController.navigate(route)
          }
        }
      }
    }
  }

  @Composable
  fun Build(startDestination: Route) {
    navController = rememberNavController()
    Scaffold { paddingValues ->
      NavHost(
        modifier = Modifier
          .padding(paddingValues),
        navController = navController,
        startDestination = startDestination,
      ) {
        composable<Route.Disc> {
          DiscListScreen(
            viewModel = discListScreenViewModel,
          )
        }
        composable<Route.DiscDetail> {
          it.toRoute<Route.DiscDetail>().let { route ->
            discDetailScreenViewModel.setDiscId(route.discId)
            DiscDetailScreen(
              viewModel = discDetailScreenViewModel,
            )
          }
        }
      }
    }
  }

  sealed interface Route {
    @Serializable
    data object Pop : Route

    @Serializable
    data object Disc : Route

    @Serializable
    data class DiscDetail(
      val discId: Int,
    ) : Route
  }
}
