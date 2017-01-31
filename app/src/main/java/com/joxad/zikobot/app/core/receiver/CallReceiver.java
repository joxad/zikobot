package com.joxad.zikobot.app.core.receiver;

import android.content.Context;

import java.util.Date;

/**
 * Created by Jocelyn on 12/11/2016.
 */

public class CallReceiver extends PhonecallReceiver {

    @Override
    protected void onIncomingCallReceived(Context ctx, String number, Date start) {
       // PlayerMusicManager.getInstance().pause();
    }

    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start) {

    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {

    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
     //   PlayerMusicManager.getInstance().pause();

    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
      //  PlayerMusicManager.getInstance().resume();
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {

    }
}
