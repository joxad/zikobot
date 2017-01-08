package com.startogamu.zikobot.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.startogamu.zikobot.core.event.player.EventPauseMediaButton;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Jocelyn on 08/01/2017.
 */

public class PausePlayerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        EventBus.getDefault().post(new EventPauseMediaButton());

    }
}
