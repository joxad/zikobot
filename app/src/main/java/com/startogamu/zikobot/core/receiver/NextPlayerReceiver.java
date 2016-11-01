package com.startogamu.zikobot.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.orhanobut.logger.Logger;
import com.startogamu.zikobot.core.module.music.PlayerMusicManager;


/**
 * Created by josh on 14/06/16.
 */
public class NextPlayerReceiver extends BroadcastReceiver {


    public static final String TAG = NextPlayerReceiver.class.getSimpleName();
    private PlayerMusicManager playerMusicManager;
    public NextPlayerReceiver() {
        super();
        playerMusicManager = PlayerMusicManager.getInstance();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        playerMusicManager.next();
        Logger.d(TAG);
    }

}
