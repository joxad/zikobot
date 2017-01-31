package com.joxad.zikobot.app.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

import com.orhanobut.logger.Logger;

/**
 * Created by Jocelyn on 08/01/2017.
 */

public class MediaButtonReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
            final KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);

            if (event != null && event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                        Logger.d("Media play pause");
                        break;
                    case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                        Logger.d("PREVIOUS");
                        break;
                    case KeyEvent.KEYCODE_MEDIA_PAUSE:
                        Logger.d("PAUSE");
                        break;
                    case KeyEvent.KEYCODE_MEDIA_NEXT:
                        Logger.d("NEXT");
                        break;
                    case KeyEvent.KEYCODE_MEDIA_STOP:
                        Logger.d("STOP");
                        break;
                }
            }
        }

    }
}
