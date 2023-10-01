package com.zero.meldcxtests.ui.core

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zero.meldcxtests.AppDatabase
import com.zero.meldcxtests.models.ApplicationData
import com.zero.meldcxtests.ui.screens.AppListScreen
import com.zero.meldcxtests.ui.screens.HomeScreen
import com.zero.meldcxtests.ui.screens.LogListScreen
import com.zero.meldcxtests.utils.askForAlarmPermission
import com.zero.meldcxtests.utils.shouldAskForAlarmPermission


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UICore(database: AppDatabase, navController: NavHostController = rememberNavController()) {
    ScaffoldDefaults.contentWindowInsets  // A surface container using the 'background' color from the theme
    var title by remember {
        mutableStateOf("Home")
    }
    val context = LocalContext.current
    var rememberForceRecomposition by remember {
        mutableStateOf(true)
    }
    LifecycleResumeEffect(key1 = Lifecycle.Event.ON_RESUME, effects = {
        Log.i("HomeScreen", "HomeScreen: REFRESHING")
        database.invalidationTracker.refreshVersionsAsync()
        rememberForceRecomposition = rememberForceRecomposition.not()
        onPauseOrDispose {

        }
    })
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = title) },
                navigationIcon = {
                    if (navController.previousBackStackEntry != null) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "back"
                            )
                        }
                    }
                }, actions = {
                    if (title == "Home") Button(onClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("selected_app", null)
                        navController.navigate(Routes.LOGS.name)
                    }) {
                        Text(text = "Logs")
                    }
                })
        }
    ) {
        NavHost(navController = navController, startDestination = Routes.HOME.name, modifier = Modifier.padding(paddingValues = it)) {
            composable(route = Routes.HOME.name) { navBackStackEntry ->
                title = "Home"
                val `package`: ApplicationData? = navBackStackEntry.savedStateHandle["selected_app"]
                HomeScreen(database = database, syncKey = rememberForceRecomposition, selectedPackage = `package`) {
                    navBackStackEntry.savedStateHandle["selected_app"] = null
                    if (context.shouldAskForAlarmPermission()) {
                        context.askForAlarmPermission()
                        (context as? Activity)?.finish()
                    } else navController.navigate(Routes.APP_SELECT.name)
                }
            }
            composable(route = Routes.APP_SELECT.name) {
                title = "Select App"
                AppListScreen(navHostController = navController, onAppSelect = {
                    navController.popBackStack()
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("selected_app", it)
                })
            }
            composable(route = Routes.LOGS.name) {
                title = "Logs"
                LogListScreen(database = database)
            }
        }
    }

}