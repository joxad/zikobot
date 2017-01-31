package com.joxad.zikobot.app.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * Created by josh on 12/07/16.
 */
public class AppWidgetHelper {
    /***
     * Update a widget with the layout and the provider given
     *
     * @param context
     * @param appWidgetClass
     */
    private static void updateAllWidgets(final Context context, final Class<? extends AppWidgetProvider> appWidgetClass) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        final int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(context, appWidgetClass));
        Intent intent = new Intent(context.getApplicationContext(), appWidgetClass);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

        context.sendBroadcast(intent);

    }


    public static void update(Context context) {
        AppWidgetHelper.updateAllWidgets(context, NextAlarmWidgetProvider.class);
        AppWidgetHelper.updateAllWidgets(context, NextAlarmClockWidgetProvider.class);
    }
}
