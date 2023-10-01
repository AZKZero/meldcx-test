@file:OptIn(ExperimentalMaterial3Api::class)

package com.zero.meldcxtests.ui.components.dialogs

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.zero.meldcxtests.ScheduleItem
import com.zero.meldcxtests.models.ApplicationData
import com.zero.meldcxtests.ui.components.AppLine
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDialog(scheduleItem: ScheduleItem, onConfirmed: (ScheduleItem) -> Unit, onClosed: () -> Unit) {
    Dialog(onDismissRequest = { onClosed() }) {
        Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.padding(16.dp)) {
            var name by remember {
                mutableStateOf(scheduleItem.scheduleName)
            }
            val timePickerState = rememberTimePickerState(initialHour = scheduleItem.scheduleHour, initialMinute = scheduleItem.scheduleMinute, is24Hour = false)

            OutlinedTextField(value = "", onValueChange = {
                name = it
            })
            Divider(thickness = 1.dp, color = Color.Gray)
            AppLine(name = scheduleItem.appName, iconDrawable = scheduleItem.appIcon)
            Divider(thickness = 1.dp, color = Color.Gray)
            TimePicker(state = timePickerState, layoutType = TimePickerLayoutType.Vertical)
            Divider(thickness = 1.dp, color = Color.Gray)
            Button(modifier = Modifier.fillMaxWidth(), onClick = {
                val timestamp = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                    set(Calendar.MINUTE, timePickerState.minute)
                }.timeInMillis

                val editedScheduleItem = ScheduleItem(
                    id = 0,
                    scheduleName = name,
                    schedulePackageName = scheduleItem.schedulePackageName,
                    scheduleTimeMillis = timestamp,
                    scheduleHour = timePickerState.hour,
                    scheduleMinute = timePickerState.minute
                )
                Log.i("EditDialog", "AddDialog: $editedScheduleItem ${timePickerState.hour} ${timePickerState.minute}")
                onConfirmed(editedScheduleItem)
            }) {
                Text(text = "Confirm")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDialog(app: ApplicationData, onConfirmed: (ScheduleItem) -> Unit, onClosed: () -> Unit) {
    Dialog(onDismissRequest = { onClosed() }) {
        Card(shape = RoundedCornerShape(16.dp)) {
            Column(
                Modifier.padding(
                    16.dp
                )
            ) {
                var name by remember {
                    mutableStateOf("")
                }
                var time by remember {
                    mutableStateOf("")
                }
                var exact by remember {
                    mutableStateOf(false)
                }
                var repeating by remember {
                    mutableStateOf(false)
                }

                val timePickerState = rememberTimePickerState(initialHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY), initialMinute = Calendar.getInstance().get(Calendar.MINUTE), is24Hour = false)

                OutlinedTextField(value = name, onValueChange = {
                    name = it
                })
                Divider(thickness = 1.dp, color = Color.Gray)
                AppLine(name = app.name, iconDrawable = app.icon)
                Divider(thickness = 1.dp, color = Color.Gray)
                TimePicker(state = timePickerState, layoutType = TimePickerLayoutType.Vertical)
                Divider(thickness = 1.dp, color = Color.Gray)
                Button(modifier = Modifier.fillMaxWidth(), onClick = {
                    val timestamp = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                        set(Calendar.MINUTE, timePickerState.minute)
                    }.timeInMillis

                    val scheduleItem = ScheduleItem(
                        id = 0,
                        scheduleName = name,
                        schedulePackageName = app.packageName,
                        scheduleTimeMillis = timestamp,
                        scheduleHour = timePickerState.hour,
                        scheduleMinute = timePickerState.minute
                    )
                    Log.i("AddDialog", "AddDialog: $scheduleItem ${timePickerState.hour} ${timePickerState.minute}")
                    onConfirmed(scheduleItem)
                }) {
                    Text(text = "Confirm")
                }
            }

        }
    }
}