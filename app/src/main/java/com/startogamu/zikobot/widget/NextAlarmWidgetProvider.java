package com.startogamu.zikobot.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.utils.ZikoUtils;
import com.startogamu.zikobot.module.zikobot.manager.AlarmManager;
import com.startogamu.zikobot.module.zikobot.model.Alarm;

import java.util.Collections;

/**
 * Created by josh on 11/07/16.
 */
public class NextAlarmWidgetProvider extends AppWidgetProvider {


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getExtras().containsKey(AppWidgetManager.EXTRA_APPWIDGET_IDS)) {
            onUpdate(context, AppWidgetManager.getInstance(context), intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS));
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int i : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.view_widget_next_alarm);

            AlarmManager.loadAlarms().subscribe(alarms -> {
                if (alarms.isEmpty()) {
                    remoteViews.setTextViewText(R.id.tv_next, context.getString(R.string.no_alarm));
                    remoteViews.setTextViewText(R.id.tv_message, context.getString(R.string.add_alarm));
                } else {
                    Collections.sort(alarms, (a1, a2) -> (int) (a1.getTimeInMillis() - a2.getTimeInMillis()));
                    Alarm alarm = alarms.get(0);
                    remoteViews.setTextViewText(R.id.tv_next, context.getString(R.string.next_alarm));
                    remoteViews.setTextViewText(R.id.tv_message, String.format("%02d: %02d %s", alarm.getHour(), alarm.getMinute(), ZikoUtils.amPm(alarm.getHour())));
                }
                appWidgetManager.updateAppWidget(i, remoteViews);

            }, throwable -> {

            });

        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }
}
