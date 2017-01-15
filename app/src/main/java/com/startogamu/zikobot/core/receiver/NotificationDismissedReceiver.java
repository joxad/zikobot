package com.startogamu.zikobot.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.startogamu.zikobot.core.event.player.EventStopPlayer;

import org.greenrobot.eventbus.EventBus;

public class NotificationDismissedReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
      int notificationId = intent.getExtras().getInt("notificationId");
      EventBus.getDefault().post(new EventStopPlayer());
      /* Your code to handle the event here */
  }
}