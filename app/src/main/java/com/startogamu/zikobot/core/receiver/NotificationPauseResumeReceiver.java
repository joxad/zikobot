package com.startogamu.zikobot.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.orhanobut.logger.Logger;
import com.startogamu.zikobot.module.component.Injector;
import com.startogamu.zikobot.module.music.manager.PlayerMusicManager;


/**
 * Created by josh on 14/06/16.
 */
public class NotificationPauseResumeReceiver extends BroadcastReceiver {

    public static final String TAG = NotificationPauseResumeReceiver.class.getSimpleName();
    private final PlayerMusicManager playerMusicManager;

    public NotificationPauseResumeReceiver() {
        super();
        playerMusicManager = Injector.INSTANCE.playerComponent().manager();

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        playerMusicManager.playOrResume();
        Logger.d(TAG);
    }

}
