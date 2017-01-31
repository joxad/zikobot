package com.joxad.zikobot.app.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.joxad.zikobot.app.player.event.EventStopPlayer;

import org.greenrobot.eventbus.EventBus;

public class NotificationDismissedReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
      int notificationId = intent.getExtras().getInt("notificationId");
      EventBus.getDefault().post(new EventStopPlayer());
      /* Your code to handle the event here */
  }
}