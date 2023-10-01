package com.zero.meldcxtests.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.zero.meldcxtests.models.ApplicationData
import com.zero.meldcxtests.ui.components.Applist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun AppListScreen(navHostController: NavHostController, onAppSelect: (ApplicationData) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        var applist by remember {
            mutableStateOf(emptyList<ApplicationData>())
        }
        val packageManager = LocalContext.current.packageManager
        val packageName = LocalContext.current.packageName
        var showProgressBar by remember {
            mutableStateOf(true)
        }
        LaunchedEffect(Unit) {
            applist = withContext(Dispatchers.IO) {
                packageManager.getInstalledPackages(0).filter {
                    packageManager.getLaunchIntentForPackage(it.packageName) != null && it.packageName != packageName
                }.map {
                    ApplicationData(
                        it.applicationInfo.loadIcon(packageManager),
                        it.applicationInfo.loadLabel(packageManager).toString(),
                        it.packageName,
                    )
                }.sortedBy {
                    it.name
                }
            }
            showProgressBar = false
        }
        if (showProgressBar) CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .weight(1f)
        )
        Applist(applist = applist, onClick = {
            onAppSelect(it)
        })
    }
}