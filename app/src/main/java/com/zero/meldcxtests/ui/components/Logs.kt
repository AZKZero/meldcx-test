package com.zero.meldcxtests.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zero.meldcxtests.ScheduleLog
import java.util.Calendar

@Composable
fun LogItem(modifier: Modifier, scheduleLog: ScheduleLog) {
    val then = Calendar.getInstance().apply { timeInMillis = scheduleLog.scheduleFireTimeMillis }
    Column(modifier = modifier.padding(10.dp)) {
        Row {
            Text(modifier = Modifier.weight(1f), text = scheduleLog.scheduleName)
            Text(text = "${then.get(Calendar.HOUR_OF_DAY)}:${then.get(Calendar.MINUTE)}")
        }
        AppLine(name = scheduleLog.appName, iconDrawable = scheduleLog.appIcon)
    }
}

@Composable
fun LogList(logList: List<ScheduleLog>) {

    LazyColumn(Modifier.padding(16.dp)) {
        items(
            items = logList,
            key = {
                it.id
            },
        ) {
            LogItem(modifier = Modifier.fillMaxWidth(), scheduleLog = it)
        }
    }
}