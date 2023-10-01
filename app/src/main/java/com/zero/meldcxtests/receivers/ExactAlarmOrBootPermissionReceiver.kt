package com.zero.meldcxtests.receivers

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.getSystemService
import androidx.room.Room
import com.zero.meldcxtests.AppDatabase
import com.zero.meldcxtests.utils.goAsync
import com.zero.meldcxtests.utils.setAlarm
import kotlinx.coroutines.Dispatchers
import java.util.Calendar

class ExactAlarmOrBootPermissionReceiver : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, p1: Intent?) {
        context?.let {
            val database = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "database").build()
            val alarmManager: AlarmManager = context.getSystemService() ?: return@let
            val shouldRescheduleAlarms = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                alarmManager.canScheduleExactAlarms()
            } else {
                false
            }
            if (shouldRescheduleAlarms) {
                val now = Calendar.getInstance().timeInMillis
                goAsync(Dispatchers.IO) {
                    database.scheduleItemDao().getAllSync(now).forEach {
                        context.setAlarm(it.scheduleTimeMillis, scheduleId = it.id, packageName = it.schedulePackageName)
                    }
                }
            }
        }
    }
}