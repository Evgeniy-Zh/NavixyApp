package com.blueprint.squaregps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.blueprint.squaregps.feature.auth.ui.LoginScreen
import com.blueprint.squaregps.feature.tracker.ui.details.TrackerDetailsScreen
import com.blueprint.squaregps.feature.tracker.ui.list.TrackerListScreen
import com.blueprint.squaregps.navigation.LoginRoute
import com.blueprint.squaregps.navigation.NavAction
import com.blueprint.squaregps.navigation.NavigationEventHandler
import com.blueprint.squaregps.navigation.TrackerDetailsRoute
import com.blueprint.squaregps.navigation.TrackerListRoute
import com.blueprint.squaregps.ui.theme.SquareGPSTheme
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    private val navHandler: NavigationEventHandler by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SquareGPSTheme {

                val viewModel: MainViewModel = koinViewModel()
                val navController = rememberNavController()
                val lifecycleOwner = LocalLifecycleOwner.current
                LaunchedEffect(Unit) {
                    navHandler.handle(lifecycleOwner) { action ->

                        when (action) {
                            is NavAction.NavigateTo -> {
                                navController.navigate(action.route) {
                                    action.popupTo?.let { popUpTo(it) { inclusive = true } }
                                }
                            }

                            is NavAction.NavigateBack -> {
                                navController.popBackStack()
                            }
                        }

                    }
                }

                NavHost(navController = navController, startDestination = TrackerListRoute) {
                    composable<LoginRoute> {
                        LoginScreen()
                    }
                    composable<TrackerListRoute> {
                        TrackerListScreen()
                    }

                    composable<TrackerDetailsRoute>() { entry ->
                        val trackerId = entry.toRoute<TrackerDetailsRoute>().trackerId
                        TrackerDetailsScreen(trackerId)
                    }

                }

            }
        }
    }
}
