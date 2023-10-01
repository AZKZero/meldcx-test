package com.zero.meldcxtests.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.DrawablePainter
import com.zero.meldcxtests.R
import com.zero.meldcxtests.models.ApplicationData


@Composable
fun Applist(applist: List<ApplicationData>, onClick: (ApplicationData) -> Unit) {

    LazyColumn(Modifier.padding(16.dp)) {
        items(
            items = applist,
            key = {
                it.packageName
            },
        ) {
            AppItem(app = it, onClick = { onClick(it) })
        }
    }
}

@Composable
fun AppItem(app: ApplicationData, onClick: (ApplicationData) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().clickable {
        onClick(app)
    }, verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = app.icon?.let {
                DrawablePainter(it)
            } ?: painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = app.name, modifier = Modifier
                .width(48.dp)
                .height(48.dp)
        )
        Text(text = app.name)
    }
}

/*private suspend fun loadAppList() {
    val packageManager: PackageManager = packageManager
    val appData = storageLoader.loadAppList()
    activityAppSelectionBinding.exclusion.isChecked = appData?.exclude ?: true
    lifecycleScope.launch {
        activityAppSelectionBinding.load.isRefreshing = true
        withContext(Dispatchers.IO) {
            appList = packageManager.getPackagesHoldingPermissions(
                arrayOf(Manifest.permission.INTERNET),
                0
            ).filter {
                packageManager.getLaunchIntentForPackage(it.packageName) != null && it.packageName != BuildConfig.APPLICATION_ID
            }.map {
                ApplicationData(
                    it.applicationInfo.loadIcon(packageManager),
                    it.applicationInfo.loadLabel(packageManager).toString(),
                    it.packageName,
                    appData?.apps?.contains<Any?>(it.packageName) ?: false
                )
            }
                .*//*sortedBy { it.name }*//*sortedWith(compareByDescending<ApplicationData> { it.checked }.thenBy { it.name })*//*)*//*
            withContext(Dispatchers.Main) {
                activityAppSelectionBinding.list.apply {
                    addItemDecoration(
                        DividerItemDecoration(
                            this@AppSelectionActivity,
                            LinearLayoutManager.VERTICAL
                        )
                    )
                    layoutManager =
                        LinearLayoutManager(this@AppSelectionActivity)
                    adapter =
                        AppAdapter(appList, this@AppSelectionActivity)
                }
                activityAppSelectionBinding.save.setOnClickListener {
                    saveAppList((activityAppSelectionBinding.list.adapter as AppAdapter).appData)
                }
                activityAppSelectionBinding.load.isRefreshing = false
            }
        }
    }
}*/

/*
private fun saveAppList(appData: List<ApplicationData>) {
    val storedAppData = StoredAppData(
        activityAppSelectionBinding.exclusion.isChecked,
        appData.filter { it.checked }.map { it.packageName })
//        Logger.i(TAG, "saveAppList: " + storageLoader.gson.toJson(storedAppData))
    lifecycleScope.launch {
        withContext(Dispatchers.IO) {
            storageLoader.saveAppList(storedAppData)
            starfishVPN.apply {
                if (tunnel.state == Tunnel.State.UP) tunnel.wgConfig?.let {
                    connectVPN(
                        null,
                        it
                    )
                }
            }

            withContext(Dispatchers.Main.immediate) {
                finish()
            }
        }
    }
}*/
