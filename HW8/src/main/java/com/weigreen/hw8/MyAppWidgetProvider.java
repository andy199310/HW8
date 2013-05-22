package com.weigreen.hw8;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Created by green on 2013/5/18.
 */
public class MyAppWidgetProvider extends AppWidgetProvider{
    //Log tag
    private static final String TAG = "MyAppWidgetProvider";



    @Override
    public void onEnabled(Context context) {
// TODO Auto-generated method stub
//super.onEnabled(context);
        Toast.makeText(context, "onEnabled()", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //super.onUpdate(context, appWidgetManager, appWidgetIds);

        Log.d(TAG, "on update");

        final int N = appWidgetIds.length;
        Log.d(TAG, "N=" + N);
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            Log.d(TAG, "Updating:"+appWidgetId);

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, MyAppWidgetConfigure.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget);
            views.setOnClickPendingIntent(R.id.button_submit, pendingIntent);
            views.setTextViewText(R.id.button_submit, MyAppWidgetConfigure.loadTitlePref(context, appWidgetId));
            views.setOnClickPendingIntent(R.id.image, pendingIntent);
            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }


    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(TAG, "on update");

        final int N = appWidgetIds.length;
        Log.d(TAG, "N=" + N);
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            Log.d("DEL", ""+i);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, String titlePrefix) {
        Log.d(TAG, "updateAppWidget appWidgetId=" + appWidgetId + " titlePrefix=" + titlePrefix);
        Log.d(TAG, "Really!?QAQ");
        // Getting the string this way allows the string to be localized.  The format
        // string is filled in using java.util.Formatter-style format strings.
        CharSequence text = MyAppWidgetConfigure.loadTitlePref(context, appWidgetId);

        // Construct the RemoteViews object.  It takes the package name (in our case, it's our
        // package, but it needs this because on the other side it's the widget host inflating
        // the layout from our package).
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget);
        views.setTextViewText(R.id.button_submit, text);

        //set the configure
        Intent intent = new Intent(context, MyAppWidgetConfigure.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_button, pendingIntent);
        views.setOnClickPendingIntent(R.id.image, pendingIntent);
        Log.d(TAG, "here:" + intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID));
        // Tell the widget manager
        appWidgetManager.updateAppWidget(appWidgetId, views);
        Log.d(TAG, "what-.-");
    }

    private static AlarmManager alarmManager;
    private static PendingIntent alarmPendingIntent;

    static void saveAlarmManager(AlarmManager alarmManagerT, PendingIntent alarmPendingIntentT){
        alarmManager = alarmManagerT;
        alarmPendingIntent = alarmPendingIntentT;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        Log.d(TAG, "OnReceive");

        if (intent.getAction() == "com.weigreen.hw8.APPWIDGET_ALARM_UPDATE"){
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context.getPackageName(), MyAppWidgetProvider.class.getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    @Override
    public void onDisabled(Context context) {
        alarmManager.cancel(alarmPendingIntent);
        Toast.makeText(context, "onDisabled()", Toast.LENGTH_LONG).show();

    }

}
