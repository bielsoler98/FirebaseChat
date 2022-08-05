package com.senyor_o.firebasechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.senyor_o.firebasechat.navigation.Destinations
import com.senyor_o.firebasechat.presentation.home.HomeScreen
import com.senyor_o.firebasechat.presentation.home.HomeViewModel
import com.senyor_o.firebasechat.presentation.login.LoginScreen
import com.senyor_o.firebasechat.presentation.login.LoginViewModel
import com.senyor_o.firebasechat.presentation.registration.RegisterViewModel
import com.senyor_o.firebasechat.presentation.registration.RegistrationScreen
import com.senyor_o.firebasechat.presentation.splash.SplashScreen
import com.senyor_o.firebasechat.presentation.splash.SplashViewModel
import com.senyor_o.firebasechat.ui.theme.FirebaseChatTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterial3Api
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseChatTheme {
                val navController = rememberAnimatedNavController()

                BoxWithConstraints {
                    AnimatedNavHost(
                        navController = navController,
                        startDestination = Destinations.Splash.route
                    ){
                        addSplash(navController)

                        addLogin(navController)

                        addRegister(navController)

                        addHome(navController)
                    }
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@ExperimentalAnimationApi
fun NavGraphBuilder.addSplash(
    navController: NavHostController
){
    composable(
        route = Destinations.Splash.route,
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
    ){
        val viewModel: SplashViewModel = hiltViewModel()
        val email = viewModel.state.value.email

        if (viewModel.state.value.sessionRetrieved) {
            if(viewModel.state.value.successSession){
                LaunchedEffect(key1 = Unit){
                    navController.navigate(
                        Destinations.Home.route + "/$email"
                    ){
                        popUpTo(Destinations.Splash.route){
                            inclusive = true
                        }
                    }
                }
            } else {
                LaunchedEffect(key1 = Unit){
                    navController.navigate(
                        Destinations.Login.route
                    ){
                        popUpTo(Destinations.Splash.route){
                            inclusive = true
                        }
                    }
                }
            }
        } else {
            SplashScreen(onEnter = viewModel::validateSession)
        }
    }
}

@ExperimentalMaterial3Api
@ExperimentalAnimationApi
fun NavGraphBuilder.addLogin(
    navController: NavHostController
){
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
    ){
        val viewModel: LoginViewModel = hiltViewModel()
        val email = viewModel.state.value.email

        if(viewModel.state.value.successLogin){
            LaunchedEffect(key1 = Unit){
                navController.navigate(
                    Destinations.Home.route + "/$email"
                ){
                    popUpTo(Destinations.Login.route){
                        inclusive = true
                    }
                }
            }
        } else {
            LoginScreen(
                state = viewModel.state.value,
                onLogin = viewModel::login,
                onNavigateToRegister = {
                    navController.navigate(Destinations.Register.route)
                },
                onDismissDialog = viewModel::hideErrorDialog,
            )
        }
    }
}

@ExperimentalMaterial3Api
@ExperimentalAnimationApi
fun NavGraphBuilder.addRegister(
    navController: NavHostController
){
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
    ){
        val viewModel: RegisterViewModel = hiltViewModel()
        val email = viewModel.state.value.email
        if(viewModel.state.value.successRegister){
            LaunchedEffect(key1 = Unit){
                navController.navigate(
                    Destinations.Home.route + "/$email"
                ){
                    popUpTo(Destinations.Login.route){
                        inclusive = true
                    }
                }
            }
        } else {
            RegistrationScreen(
                state = viewModel.state.value,
                onRegister = viewModel::register,
                onBack = {
                    navController.popBackStack()
                },
                onDismissDialog = viewModel::hideErrorDialog
            )
        }
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.addHome(
    navController: NavHostController
) {
    composable(
        route = Destinations.Home.route  + "/{email}",
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
    ){ backStackEntry->
        val viewModel: HomeViewModel = hiltViewModel()
        val email = backStackEntry.arguments?.getString("email") ?: ""
        viewModel.state.value = viewModel.state.value.copy(email = email)
        if(viewModel.state.value.successLogOut){
            LaunchedEffect(key1 = Unit){
                navController.navigate(
                    Destinations.Login.route
                ){
                    popUpTo(Destinations.Home.route){
                        inclusive = true
                    }
                }
            }
        } else {
            HomeScreen(
                state = viewModel.state.value,
                onLogOut = viewModel::logOut,
                onEnter = viewModel::saveCredentials
            )
        }
    }
}














