package dev.johnnylewis.disctrackr.presentation

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.zIndex
import androidx.core.view.WindowCompat
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
    Scaffold(
      topBar = { StatusBar() },
    ) { paddingValues ->
      val layoutDirection = LocalLayoutDirection.current
      NavHost(
        modifier = Modifier
          .padding(
            bottom = paddingValues.calculateBottomPadding(),
            start = paddingValues.calculateStartPadding(layoutDirection),
            end = paddingValues.calculateEndPadding(layoutDirection),
          ),
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

  @Composable
  private fun StatusBar() {
    LocalView.current.let { view ->
      if (!view.isInEditMode) {
        val window = (view.context as Activity).window
        val insetsController = WindowCompat.getInsetsController(window, view)
        insetsController.isAppearanceLightStatusBars = false
      }
    }
    val height = LocalDensity.current.let { density ->
      WindowInsets.statusBars.getTop(density).let {
        with(density) { it.toDp() }
      }
    }
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(height)
        .background(Color.Black.copy(alpha = 0.25f))
        .zIndex(1f),
    )
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
