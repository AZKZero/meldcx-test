package com.zero.meldcxtests.utils

import android.content.BroadcastReceiver
import android.content.BroadcastReceiver.PendingResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

const val ACTION_RECEIVE_ALARM_BROADCAST = "ACTION_RECEIVE_ALARM_BROADCAST"
const val KEY_SCHEDULE_ID = "KEY_SCHEDULE_ID"

fun BroadcastReceiver.goAsync(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> Unit
) {
    val pendingResult = goAsync()
    @OptIn(DelicateCoroutinesApi::class) // Must run globally; there's no teardown callback.
    GlobalScope.launch(context) {
        try {
            block()
        } finally {
            pendingResult.finish()
        }
    }
}

/*
fun <T> BroadcastReceiver.goAsyncResult(
    context: CoroutineContext = EmptyCoroutineContext,
    secondaryContext: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.(PendingResult) -> Unit,
    resultBlock: suspend CoroutineScope.(PendingResult) -> Unit
) {
    val pendingResult = goAsync()
    @OptIn(DelicateCoroutinesApi::class) // Must run globally; there's no teardown callback.
    GlobalScope.launch(context) {
        try {
            block(pendingResult)
        } finally {
            withContext(secondaryContext) {
                pendingResult.finish()
            }
        }
    }
}*/
