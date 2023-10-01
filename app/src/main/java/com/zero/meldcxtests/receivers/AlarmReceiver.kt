package com.zero.meldcxtests.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.room.Room
import com.zero.meldcxtests.AppDatabase
import com.zero.meldcxtests.toScheduleLog
import com.zero.meldcxtests.utils.KEY_SCHEDULE_ID
import com.zero.meldcxtests.utils.goAsync
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, data: Intent?) {
        Log.i(AlarmReceiver::class.java.simpleName, "onReceive: ALARM ${data?.extras}")
        context?.let {
            data?.let dataNonNull@{
                val scheduleId = data.getIntExtra(KEY_SCHEDULE_ID, -1)
                if (scheduleId == -1) return@dataNonNull
                val database = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "database").build()
                val now = Calendar.getInstance().timeInMillis
                goAsync(Dispatchers.IO) {
                    database.scheduleItemDao().getScheduleItem(scheduleId)?.let { scheduleItem ->
                        database.scheduleItemDao().delete(scheduleItem)
                        database.scheduleItemDao().upsert(scheduleItem.toScheduleLog(now))
                        withContext(Dispatchers.Main.immediate) {
                            Log.i(AlarmReceiver::class.java.simpleName, "LAUNCHING INTENT")
                            context.startActivity(context.packageManager.getLaunchIntentForPackage(scheduleItem.schedulePackageName))
                        }
                    }
                }
            }
        }
    }
}