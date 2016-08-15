package com.startogamu.zikobot.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.orhanobut.logger.Logger;
import com.startogamu.zikobot.core.analytics.AnalyticsManager;
import com.startogamu.zikobot.core.fragmentmanager.IntentManager;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.module.zikobot.manager.AlarmManager;

/**
 * Created by josh on 28/03/16.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long alarmId = intent.getLongExtra(EXTRA.ALARM_ID, -1);
        Logger.d("AlarmReceiver" + alarmId);
        AlarmManager.getAlarmById(alarmId).subscribe((alarm) -> {
            AlarmManager.prepareAlarm(context, alarm);
            if (AlarmManager.canStart(alarm)) {
                Logger.d("AlarmReceiver" + alarm.getName());
                AnalyticsManager.logStartAlarm(alarm);
                if (alarm.getRepeated() == 0) {
                    alarm.setActive(0);
                    alarm.save();
                }
                context.startActivity(IntentManager.goToWakeUp(alarm).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }, throwable -> {
            Logger.d(throwable.getMessage());
        });

    }
}

