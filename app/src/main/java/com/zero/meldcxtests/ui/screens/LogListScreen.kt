package com.zero.meldcxtests.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.zero.meldcxtests.AppDatabase
import com.zero.meldcxtests.ui.components.LogList

@Composable
fun LogListScreen(database: AppDatabase) {
    Column(modifier = Modifier.fillMaxSize()) {
        val dbScheduleLogs by database.scheduleItemDao().getLogs().observeAsState(initial = emptyList())

        val context = LocalContext.current
        val packageManager = context.packageManager
        dbScheduleLogs.onEach { schedule ->
            if (schedule.appIcon == null || schedule.appName == null) {
                with(packageManager.getPackageInfo(schedule.schedulePackageName, 0).applicationInfo) {
                    schedule.appIcon = loadIcon(packageManager)
                    schedule.appName = loadLabel(packageManager).toString()
                }
            }
        }
        LogList(logList = dbScheduleLogs)
    }
}