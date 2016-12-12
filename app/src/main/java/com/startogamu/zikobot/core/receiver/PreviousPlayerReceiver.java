package com.startogamu.zikobot.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.orhanobut.logger.Logger;


/**
 * Created by josh on 14/06/16.
 */
public class PreviousPlayerReceiver extends BroadcastReceiver {


    public static final String TAG = PreviousPlayerReceiver.class.getSimpleName();

    public PreviousPlayerReceiver() {
        super();

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.d(TAG);
    }

}
