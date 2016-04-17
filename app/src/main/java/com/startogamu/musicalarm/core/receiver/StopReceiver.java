package com.startogamu.musicalarm.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.startogamu.musicalarm.core.notification.MusicNotification;

/**
 * Created by josh on 17/04/16.
 */
public class StopReceiver extends BroadcastReceiver {


    public static final String TAG = StopReceiver.class.getSimpleName();
    private Listener listener;

    public StopReceiver(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        MusicNotification.cancel();

    }

    public interface Listener {
        void onCall();
    }
}
