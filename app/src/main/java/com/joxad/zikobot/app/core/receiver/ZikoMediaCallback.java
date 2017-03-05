package com.joxad.zikobot.app.core.receiver;

import android.content.Intent;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.KeyEvent;

import com.joxad.zikobot.app.player.event.EventNextTrack;
import com.joxad.zikobot.app.player.event.EventPauseMediaButton;
import com.joxad.zikobot.app.player.event.EventPlayMediaButton;
import com.joxad.zikobot.app.player.event.EventPreviousTrack;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Jocelyn on 08/01/2017.
 */

public class ZikoMediaCallback extends MediaSessionCompat.Callback {
    @Override
    public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
        final String intentAction = mediaButtonEvent.getAction();
        if (Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
            final KeyEvent event = mediaButtonEvent.getParcelableExtra(
                    Intent.EXTRA_KEY_EVENT);
            if (event == null) {
                return super.onMediaButtonEvent(mediaButtonEvent);
            }
            final int keycode = event.getKeyCode();
            final int action = event.getAction();
            if (event.getRepeatCount() == 0 && action == KeyEvent.ACTION_DOWN) {
                switch (keycode) {
                    // Do what you want in here
                    case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                        Log.d("MPS", "KEYCODE_MEDIA_PLAY_PAUSE");
                        break;
                    case KeyEvent.KEYCODE_MEDIA_PAUSE:
                        Log.d("MPS", "KEYCODE_MEDIA_PAUSE");
                        EventBus.getDefault().post(new EventPauseMediaButton());
                        break;
                    case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                        EventBus.getDefault().post(new EventPreviousTrack());
                        break;
                    case KeyEvent.KEYCODE_MEDIA_NEXT:
                        EventBus.getDefault().post(new EventNextTrack());
                        break;
                    case KeyEvent.KEYCODE_MEDIA_PLAY:
                        Log.d("MPS", "KEYCODE_MEDIA_PLAY");
                        EventBus.getDefault().post(new EventPlayMediaButton());
                        break;
                }
            }
        }
        return super.onMediaButtonEvent(mediaButtonEvent);
    }
}
