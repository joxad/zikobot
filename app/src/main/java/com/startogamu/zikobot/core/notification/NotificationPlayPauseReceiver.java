package com.startogamu.zikobot.core.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.joxad.zikobot.data.db.CurrentPlaylistManager;
import com.joxad.zikobot.data.db.model.ZikoTrack;

/**
 * Created by Jocelyn on 08/01/2017.
 */

public class NotificationPlayPauseReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ZikoTrack current = CurrentPlaylistManager.INSTANCE.getCurrentTrack();
        if (CurrentPlaylistManager.INSTANCE.getPlaying())
            CurrentPlaylistManager.INSTANCE.pause(current);
        else
            CurrentPlaylistManager.INSTANCE.resume(current);
        //CurrentPlaylistManager.INSTANCE.forceRefresh();
    }
}