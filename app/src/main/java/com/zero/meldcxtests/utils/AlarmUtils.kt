package com.zero.meldcxtests.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import android.util.Log
import androidx.annotation.RequiresApi
import com.zero.meldcxtests.receivers.AlarmReceiver

@SuppressLint("ScheduleExactAlarm")
fun Context.setAlarm(timestamp: Long, scheduleId: Int, packageName: String) {
    Log.i("setAlarm", "setAlarm: $timestamp ${System.currentTimeMillis() - timestamp}")
    val launchIntent = this.packageManager.getLaunchIntentForPackage(packageName)
    val alarmManager = this.getSystemService(AlarmManager::class.java)
    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        timestamp,
        /*PendingIntent.getActivity(
            this,
            scheduleId,
            launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )*/
        PendingIntent.getBroadcast(
            this,
            scheduleId,
            Intent(this, AlarmReceiver::class.java).setAction(ACTION_RECEIVE_ALARM_BROADCAST).putExtra(
                KEY_SCHEDULE_ID, scheduleId
            ),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    )
}

fun Context.cancelAlarm(scheduleId: Int) {
    val alarmManager = this.getSystemService(AlarmManager::class.java)
    alarmManager.cancel(
        PendingIntent.getBroadcast(
            this,
            scheduleId,
            Intent(this, AlarmReceiver::class.java).setAction(ACTION_RECEIVE_ALARM_BROADCAST).putExtra(
                KEY_SCHEDULE_ID, scheduleId
            ),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    )
}

fun Context.shouldAskForAlarmPermission() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && this.getSystemService(AlarmManager::class.java).canScheduleExactAlarms().also {
    Log.i("SHOULD_ASK_FOR_ALARM_PERMISSION", "shouldAskForAlarmPermission: $it")
}.not()

@RequiresApi(Build.VERSION_CODES.S)
fun Context.askForAlarmPermission() {
    startActivity(Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM).setData(Uri.parse("package:$packageName")))
}