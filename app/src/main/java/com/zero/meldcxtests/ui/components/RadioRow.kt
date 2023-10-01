package com.zero.meldcxtests.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> RadioRow(
    isChecked: Boolean = false, text: String, value: T, onCheckedChanged: (T) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(selected = isChecked, onClick = {
                onCheckedChanged(value)
            })
            .padding(horizontal = 16.dp)
    ) {
        RadioButton(selected = isChecked, onClick = {
            onCheckedChanged(value)
        })
        Text(
            text = text, modifier = Modifier.padding(start = 16.dp)
        )
    }
}

data class RadioItem<T>(val value: T, val label: String)

@Composable
fun <T> RadioGroup(
    values: List<RadioItem<T>>, onCheckedChanged: (T) -> Unit
) {
    if (values.isEmpty()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Empty")
        }
    } else {
        val (value, selectValue) = remember {
            mutableStateOf(values.first().value)
        }
        Column {
            values.forEach { item ->
                RadioRow(isChecked = value == item.value, text = item.label, value = item.value, onCheckedChanged = { value ->
                    selectValue(value)
                    onCheckedChanged(value)
                })
            }
        }
    }
}

/*
RadioGroup(values = AlarmType.values().map { RadioItem(it, it.name) }, onCheckedChanged = {

})*/
