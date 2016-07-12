package com.startogamu.zikobot.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.utils.ZikoUtils;
import com.startogamu.zikobot.module.zikobot.manager.AlarmManager;
import com.startogamu.zikobot.module.zikobot.model.Alarm;
import com.startogamu.zikobot.view.Henson;

import java.util.Collections;
import java.util.Date;

/**
 * Created by josh on 12/07/16.
 */
public abstract class ZikobotNextAlarmProvider extends AppWidgetProvider {


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getExtras() == null) return;
        if (intent.getExtras().containsKey(AppWidgetManager.EXTRA_APPWIDGET_IDS)) {
            onUpdate(context, AppWidgetManager.getInstance(context), intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS));
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int i : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), layout());

            AlarmManager.loadAlarms().subscribe(alarms -> {
                if (alarms.isEmpty()) {
                    remoteViews.setTextViewText(R.id.tv_next, context.getString(R.string.no_alarm));
                    remoteViews.setTextViewText(R.id.tv_message, context.getString(R.string.add_alarm));
                    Intent intent = Henson.with(context).gotoActivityAlarm().alarm(null).build();
                    remoteViews.setOnClickPendingIntent(R.id.ll_alarm, PendingIntent.getActivity(context, 0,
                            intent, 0));

                } else {
                    Collections.sort(alarms, (a1, a2) -> new Date(a1.getTimeInMillis()).compareTo(new Date(a2.getTimeInMillis())));
                    boolean done = false;
                    for (Alarm alarm : alarms) {
                        if (alarm.getActive() == 1) {
                            remoteViews.setTextViewText(R.id.tv_next, context.getString(R.string.next_alarm));
                            remoteViews.setTextViewText(R.id.tv_message, String.format("%02d: %02d %s", alarm.getHour(), alarm.getMinute(), ZikoUtils.amPm(alarm.getHour())));
                            done = true;
                            Intent intent = Henson.with(context).gotoActivityAlarm().alarm(alarm).build();
                            remoteViews.setOnClickPendingIntent(R.id.ll_alarm, PendingIntent.getActivity(context, 0,
                                    intent, 0));

                            break;
                        }
                    }
                    if (!done) {
                        remoteViews.setTextViewText(R.id.tv_next, context.getString(R.string.no_active_alarm));
                        remoteViews.setTextViewText(R.id.tv_message, context.getString(R.string.activate_alarm));
                        Intent intent = Henson.with(context).gotoActivityMain().fromWidget("ALARM").build();
                        remoteViews.setOnClickPendingIntent(R.id.ll_alarm, PendingIntent.getActivity(context, 0,
                                intent, 0));
                    }

                }

                appWidgetManager.updateAppWidget(i, remoteViews);

            }, throwable -> {

            });

        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }

    protected abstract int layout();
}
