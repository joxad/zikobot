package com.joxad.zikobot.app.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.joxad.zikobot.app.core.utils.EXTRA;
import com.joxad.zikobot.app.player.alarm.AlarmService;
import com.orhanobut.logger.Logger;

/**
 * Created by josh on 28/03/16.
 */
public class AlarmReceiver extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        long alarmId = intent.getLongExtra(EXTRA.ALARM_ID, -1);
        Logger.d("AlarmReceiver" + alarmId);
        context.startService(AlarmService.newInstance(context, alarmId));
    }


}

