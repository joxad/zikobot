package com.joxad.zikobot.app.core.receiver;

import android.content.Context;

import com.joxad.zikobot.app.player.event.EventPauseMediaButton;
import com.joxad.zikobot.app.player.event.EventPlayMediaButton;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

/**
 * Created by Jocelyn on 12/11/2016.
 */

public class CallReceiver extends PhonecallReceiver {

    @Override
    protected void onIncomingCallReceived(Context ctx, String number, Date start) {
        EventBus.getDefault().post(new EventPauseMediaButton());
    }

    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start) {

    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        EventBus.getDefault().post(new EventPlayMediaButton());

    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        EventBus.getDefault().post(new EventPauseMediaButton());

    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        EventBus.getDefault().post(new EventPlayMediaButton());
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {

    }
}
