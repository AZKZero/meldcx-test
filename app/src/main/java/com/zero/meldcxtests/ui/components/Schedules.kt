package com.zero.meldcxtests.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.zero.meldcxtests.ScheduleItem

@Composable
fun Schedule(scheduleItem: ScheduleItem, onEditClicked: (ScheduleItem) -> Unit, onDeleteClicked: (ScheduleItem) -> Unit) {
    Card(
        modifier = Modifier.padding(vertical = 5.dp), colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFCBFFEE))
    ) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row {
                Text(text = scheduleItem.scheduleName)
                Spacer(modifier = Modifier.weight(1f))
                Column {
                    Text(
                        text = "%02d:%02d".format(
                            scheduleItem.scheduleHour,
                            scheduleItem.scheduleMinute
                        ), fontSize = TextUnit(16f, TextUnitType.Sp)
                    )
//                    Text(text = scheduleItem.lastFireDateTimeString)
                }
            }
            Divider(thickness = 1.dp, color = Color.Gray)
            AppLine(name = scheduleItem.appName, iconDrawable = scheduleItem.appIcon)
            Row {
                Button(modifier = Modifier.weight(1f), onClick = { onEditClicked(scheduleItem) }) {
                    Text(text = "Edit")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color.Red), onClick = { onDeleteClicked(scheduleItem) }) {
                    Text(text = "Delete")
                }
            }
        }
    }
}

@Composable
fun Schedules(scheduleItems: List<ScheduleItem>, onAddNewClicked: () -> Unit, onEditClicked: (ScheduleItem) -> Unit, onDeleteClicked: (ScheduleItem) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp)) {
        items(scheduleItems) {
            Schedule(scheduleItem = it, onEditClicked = onEditClicked, onDeleteClicked = onDeleteClicked)
        }
        item(key = "NEW") {
            Button(modifier = Modifier.fillMaxWidth(), onClick = {
                onAddNewClicked()
            }) {
                Text(text = "Add Another Schedule")
            }
        }
    }
}