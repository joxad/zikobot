package com.startogamu.zikobot.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.orhanobut.logger.Logger;
import com.startogamu.zikobot.core.event.player.EventResumePlayer;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by josh on 14/06/16.
 */
public class ResumePlayerReceiver extends BroadcastReceiver {


    public static final String TAG = ResumePlayerReceiver.class.getSimpleName();

    public ResumePlayerReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        EventBus.getDefault().post(new EventResumePlayer());
        Logger.d(TAG);
    }

}
