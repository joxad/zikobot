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

    public PreviousPlayerReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        PlayerMusicManager.previous();
        Logger.d(TAG);
    }

}
