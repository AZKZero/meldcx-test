package com.zero.meldcxtests.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.zero.meldcxtests.AppDatabase
import com.zero.meldcxtests.ScheduleItem
import com.zero.meldcxtests.models.ApplicationData
import com.zero.meldcxtests.ui.components.Schedules
import com.zero.meldcxtests.ui.components.dialogs.AddDialog
import com.zero.meldcxtests.ui.components.dialogs.EditDialog
import com.zero.meldcxtests.utils.cancelAlarm
import com.zero.meldcxtests.utils.setAlarm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(database: AppDatabase, syncKey:Boolean, selectedPackage: ApplicationData? = null, onNewSchedule: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        /*LifecycleResumeEffect(key1 = Lifecycle.Event.ON_RESUME, effects = {
            Log.i("HomeScreen", "HomeScreen: REFRESHING")
            database.invalidationTracker.refreshVersionsAsync()
            onPauseOrDispose {

            }
        })*/
        val dbScheduleItems by database.scheduleItemDao().getAll().observeAsState(initial = emptyList())
        val context = LocalContext.current
        val packageManager = context.packageManager
        val scope = rememberCoroutineScope()
        var showAddDialog by remember {
            mutableStateOf(true)
        }
        var showEditingDialog by remember {
            mutableStateOf(false)
        }
        var editingSchedule by remember {
            mutableStateOf<ScheduleItem?>(null)
        }
        selectedPackage?.let {
            with(packageManager.getPackageInfo(selectedPackage.packageName, 0).applicationInfo) {
                selectedPackage.icon = loadIcon(packageManager)
                selectedPackage.name = loadLabel(packageManager).toString()
            }
        }
        dbScheduleItems.onEach { schedule ->
            if (schedule.appIcon == null || schedule.appName == null) {
                with(packageManager.getPackageInfo(schedule.schedulePackageName, 0).applicationInfo) {
                    schedule.appIcon = loadIcon(packageManager)
                    schedule.appName = loadLabel(packageManager).toString()
                }
            }
        }
        key(syncKey){
            if (dbScheduleItems.isNotEmpty()) {
                Schedules(scheduleItems = dbScheduleItems,
                    onAddNewClicked = {
                        editingSchedule = null
                        onNewSchedule()
                    },
                    onEditClicked = { schedule ->
                        editingSchedule = schedule
                        showEditingDialog = true
                    }, onDeleteClicked = { schedule ->
                        scope.launch(Dispatchers.IO) {
                            database.scheduleItemDao().deleteSchedule(schedule = schedule)
                            context.cancelAlarm(scheduleId = schedule.id)
                        }
                    })

            } else {
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(text = "Add New Schedule", modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
                    Button(onClick = {
                        onNewSchedule()
                    }) {
                        Text(text = "Add Schedule")
                    }

                }
            }
        }
        if (selectedPackage != null && showAddDialog)
            AddDialog(app = selectedPackage, onConfirmed = {
                showAddDialog = false
                scope.launch(Dispatchers.IO) {
                    val id = database.scheduleItemDao().upsert(it)
                    context.setAlarm(
                        timestamp = it.scheduleTimeMillis, scheduleId = id.toInt(), packageName = it.schedulePackageName
                    )
                }
            }, onClosed = {
                showAddDialog = false
            })
        if (showEditingDialog && editingSchedule != null) {
            EditDialog(scheduleItem = editingSchedule!!, onConfirmed = {
                showEditingDialog = false
                scope.launch(Dispatchers.IO) {
                    database.scheduleItemDao().upsert(it)
                    context.setAlarm(
                        timestamp = it.scheduleTimeMillis, scheduleId = it.id, packageName = it.schedulePackageName
                    )
                }
            }, onClosed = {
                showEditingDialog = false
                editingSchedule = null
            })
        }
    }
}