package com.startogamu.musicalarm.core.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.drawable.Icon;
import android.os.Build;

import com.startogamu.musicalarm.R;

/**
 * Created by josh on 10/04/16.
 */
public class MusicNotification {

    private PendingIntent intentPlay;

    private PendingIntent intentPause;

    public void show(Context context) {

        Notification notification = new Notification.Builder(context)
                .addAction(R.drawable.ic_pause, "Pause", intentPause).build();


    }
}
