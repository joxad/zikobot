package com.startogamu.zikobot.player.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.startogamu.zikobot.Constants;

public class AlarmReceiver extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        int alarmId = intent.getIntExtra(Constants.Extra.INSTANCE.getALARM_ID(), -1);
        context.startService(AlarmService.newInstance(context, alarmId));
    }


}