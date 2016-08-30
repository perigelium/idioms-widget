package ru.alexangan.developer.idiomswidget;

/**
 * Created by Administrator on 16.07.16.
 *
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Random;

public class MyWidget extends AppWidgetProvider {

    final static String LOG_TAG = "iw";
    final String UPDATE_ALL_WIDGETS = "update_all_widgets";
    static int curId = 0;
    static String packageName;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Intent intent = new Intent(context, MyWidget.class);
        intent.setAction(UPDATE_ALL_WIDGETS);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(),
                60000, pIntent);

        packageName = context.getPackageName();

    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Log.d(LOG_TAG, "On update");

        for (int i : appWidgetIds) {
            updateWidget(context, appWidgetManager, i);
        }
    }

    public void onDeleted(Context context, int[] appWidgetIds)
    {
        super.onDeleted(context, appWidgetIds);

        Editor editor = context.getSharedPreferences(ConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE).edit();

        //for (int widgetID : appWidgetIds)
        {
            //editor.remove(ConfigActivity.WIDGET_PHRASE + widgetID);
        }

        editor.apply();
    }

    static void updateWidget(Context context, AppWidgetManager appWidgetManager, int widgetID) {

        SharedPreferences sp = context.getSharedPreferences(ConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE);

        RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget);

        displayItem(context, widgetView);

        sp.edit().putInt("curId" + widgetID, curId).apply();

        Log.d(LOG_TAG, "curId sent from widget: " + curId);

        Intent configIntent = new Intent(context, ConfigActivity.class);
        configIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
        PendingIntent pIntent = PendingIntent.getActivity(context, widgetID, configIntent, 0);

        widgetView.setOnClickPendingIntent(R.id.backgroundImage, pIntent);

        appWidgetManager.updateAppWidget(widgetID, widgetView);

        Random rnd = new Random();
        curId = rnd.nextInt(ConfigActivity.maxId + 1);

        Log.d(LOG_TAG, "widget updated");
    }

    private static void displayItem(Context context, RemoteViews widgetView)
    {
        ItemContent itemContentInstance = new ItemContent(context, curId);

        String imageFileName = itemContentInstance.getImageFileName();

        //Log.d(LOG_TAG, "imageFileName= " + imageFileName);

        int imageId = context.getResources().getIdentifier(imageFileName, "drawable", packageName);

        //Log.d(LOG_TAG, "imageId= " + imageId);

        widgetView.setImageViewResource(R.id.backgroundImage, imageId);

        String rusIdiom = itemContentInstance.getRusIdiom();
        //Log.d(LOG_TAG, "rusIdiom= " + rusIdiom);
        String engIdiom = itemContentInstance.getEngIdiom();
        //Log.d(LOG_TAG, "engIdiom= " + engIdiom);
        String translation = itemContentInstance.getTranslation();
        //Log.d(LOG_TAG, "translation= " + translation);

        String engStr = engIdiom.length() == 1 ? translation : engIdiom;
        String widgetText = rusIdiom + "\n" + engStr;

        widgetView.setTextViewText(R.id.tvPhrase, widgetText);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Intent intent = new Intent(context, MyWidget.class);
        intent.setAction(UPDATE_ALL_WIDGETS);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equalsIgnoreCase(UPDATE_ALL_WIDGETS)) {

            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), getClass().getName());
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);

            for (int appWidgetID : ids) {
                updateWidget(context, appWidgetManager, appWidgetID);
            }
        }
    }
}
