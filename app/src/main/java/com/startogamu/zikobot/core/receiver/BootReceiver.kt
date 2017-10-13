package com.startogamu.zikobot.core.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.startogamu.zikobot.player.alarm.AlarmManager

/**
 * Created by Jocelyn on 11/10/2017.
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, p1: Intent?) {
        var alarms = AlarmManager.INSTANCE.findAll()
        for (alarm in alarms) {
            AlarmManager.INSTANCE.prepareAlarm(context, alarm)
        }

        // Reschedule alarms if needed
    }

}