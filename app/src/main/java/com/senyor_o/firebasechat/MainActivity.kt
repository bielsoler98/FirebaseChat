package com.senyor_o.firebasechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.firebase.FirebaseApp
import com.senyor_o.firebasechat.presentation.home.HomeScreen
import com.senyor_o.firebasechat.presentation.login.LoginScreen
import com.senyor_o.firebasechat.presentation.login.LoginViewModel
import com.senyor_o.firebasechat.presentation.navigation.Destinations
import com.senyor_o.firebasechat.presentation.registration.RegistrationScreen
import com.senyor_o.firebasechat.ui.theme.FirebaseChatTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            FirebaseChatTheme {
                navController = rememberAnimatedNavController()
                BoxWithConstraints {
                    AnimatedNavHost(
                        navController = navController,
                        startDestination = Destinations.Login.route
                    ) {
                        addLogin(navController)

                        addRegister(navController)

                        addHome(navController)
                    }
                    checkAuthStatus()
                    getAuthState()
                }
            }

        }
    }

    private fun checkAuthStatus() {
        if (viewModel.isUserAuthenticated) {
            navController.navigate(Destinations.Home.route) {
                navController.popBackStack()
            }
        }
    }

    private fun getAuthState() = viewModel.getAuthState()
}

@ExperimentalAnimationApi
fun NavGraphBuilder.addLogin(
    navController: NavHostController,
) {
    composable(
        route = Destinations.Login.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { 1000 },
                animationSpec = tween(500)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -1000 },
                animationSpec = tween(500)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -1000 },
                animationSpec = tween(500)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { 1000 },
                animationSpec = tween(500)
            )
        }
    ) {
        LoginScreen(
            navigateToRegister = {
                navController.navigate(Destinations.Register.route)
            },
            navigateToHomeScreen = {
                navController.navigate(Destinations.Home.route) {
                    navController.popBackStack()
                }
            }
        )
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.addRegister(
    navController: NavHostController
) {
    composable(
        route = Destinations.Register.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { 1000 },
                animationSpec = tween(500)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -1000 },
                animationSpec = tween(500)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -1000 },
                animationSpec = tween(500)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { 1000 },
                animationSpec = tween(500)
            )
        }
    ) {
        RegistrationScreen(
            onBack = {
                navController.popBackStack()
            },
            navigateToHomeScreen = {
                navController.navigate(Destinations.Home.route) {
                    navController.popBackStack()
                }
            }
        )
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.addHome(
    navController: NavHostController
) {
    composable(
        route = Destinations.Home.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { 1000 },
                animationSpec = tween(500)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -1000 },
                animationSpec = tween(500)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -1000 },
                animationSpec = tween(500)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { 1000 },
                animationSpec = tween(500)
            )
        }
    ) {
        HomeScreen(
            navigateToLoginScreen = {
                navController.navigate(Destinations.Login.route) {
                    navController.popBackStack()
                }
            }
        )
    }
}












