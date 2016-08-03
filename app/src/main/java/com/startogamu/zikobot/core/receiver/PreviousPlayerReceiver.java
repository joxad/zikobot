package com.startogamu.zikobot.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.orhanobut.logger.Logger;
import com.startogamu.zikobot.module.component.Injector;
import com.startogamu.zikobot.module.music.manager.PlayerMusicManager;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by josh on 14/06/16.
 */
public class PreviousPlayerReceiver extends BroadcastReceiver {


    public static final String TAG = PreviousPlayerReceiver.class.getSimpleName();
    private PlayerMusicManager playerMusicManager;

    public PreviousPlayerReceiver() {
        super();
        playerMusicManager = Injector.INSTANCE.playerComponent().manager();

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        playerMusicManager.previous();
        Logger.d(TAG);
    }

}
