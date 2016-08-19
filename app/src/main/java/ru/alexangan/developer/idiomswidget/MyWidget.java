package ru.alexangan.developer.idiomswidget;

/**
 * Created by Administrator on 16.07.16.
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
import android.content.res.AssetManager;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyWidget extends AppWidgetProvider {

    final static String LOG_TAG = "iw";
    final String UPDATE_ALL_WIDGETS = "update_all_widgets";
    static String[] strArrPhrases;
    static ArrayList<String> arrayPhrases;
    static ArrayList<String> arrayHtmPathes;
    static String prevTypedPhrase = "null";
    static int curId = 1;
    static String packageName;
    static AssetManager assMan;

    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Log.d(LOG_TAG, "On update");
        // обновляем все экземпляры
        for (int i : appWidgetIds) {
            updateWidget(context, appWidgetManager, i);
        }
    }

    public void onDeleted(Context context, int[] appWidgetIds)
    {
        super.onDeleted(context, appWidgetIds);
        // Удаляем Preferences
        Editor editor = context.getSharedPreferences(
                ConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE).edit();
        for (int widgetID : appWidgetIds) {
            editor.remove(ConfigActivity.WIDGET_PHRASE + widgetID);
        }
        editor.commit();
    }

    static void updateWidget(Context context, AppWidgetManager appWidgetManager,
                             int widgetID) {

        Log.d(LOG_TAG, "update widget");

        SharedPreferences sp = context.getSharedPreferences(
                ConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE);

        String curPhrase = sp.getString(ConfigActivity.WIDGET_PHRASE + widgetID, null);

        RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget);
/*
        if (arrayPhrases == null) // first start
        {

            strArrPhrases = context.getResources().getStringArray(R.array.strArrPhrases);

            arrayPhrases = new ArrayList();

            for (String item : strArrPhrases) {
                arrayPhrases.add(item);
            }
        }
*/

/*
        if(curPhrase!= null && curPhrase != prevTypedPhrase && curPhrase.length() > 7)
        {
            prevTypedPhrase = curPhrase;
            arrayPhrases.add(curId, curPhrase);
        }
*/

        String strFileName = "";

        if( curId < 10)
        {
            strFileName = "_00" + curId;
        }
        else if( curId < 100)
        {
            strFileName = "_0" + curId;
        }
        else
        {
            strFileName = "_" + curId;
        }

        widgetView.setImageViewResource(R.id.backgroundImage, context.getResources().getIdentifier(strFileName,"drawable", packageName));

        String strRawHtm = readRawTextFile(context, arrayHtmPathes.get(curId - 1));

        String phrase = "";

        Pattern pattEngCf = Pattern.compile("((?<=<P><B>Cf\\..)[A-Z].+(?=</B>))"); // single english line

        Matcher matcher1 = pattEngCf.matcher(strRawHtm);

        if (matcher1.find())
        {
            phrase += matcher1.group(0);
        }

        if(phrase.length() == 0)
        {
            Pattern pattEngDirectTrans = Pattern.compile("((?<=<P><I>)[A-Z].+(?=</I>))"); // single english line

            Matcher matcher2 = pattEngDirectTrans.matcher(strRawHtm);

            if (matcher2.find()) {
                phrase += matcher2.group(0);
            }
        }

        widgetView.setTextViewText(R.id.tvPhrase, phrase);
        curId++;

        //widgetView.setTextViewText(R.id.tvPhrase, arrayPhrases.get(i++));

            if (curId == arrayHtmPathes.size())  // cycled show of phrases
            {
                curId = 1;
            }

        Intent configIntent = new Intent(context, ConfigActivity.class);
        configIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
        PendingIntent pIntent = PendingIntent.getActivity(context, widgetID,
                configIntent, 0);


        //sp.edit().putString(ConfigActivity.WIDGET_PHRASE + widgetID, arrayPhrases.get(curId-1)).commit();

        widgetView.setOnClickPendingIntent(R.id.backgroundImage, pIntent);

        appWidgetManager.updateAppWidget(widgetID, widgetView);

        Log.d(LOG_TAG, "widget updated");
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Intent intent = new Intent(context, MyWidget.class);
        intent.setAction(UPDATE_ALL_WIDGETS);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(),
                60000, pIntent);

        packageName = context.getPackageName();
        arrayHtmPathes = new ArrayList();
        String[] Files = null;
        assMan = context.getAssets();

        AssetManager assetsManager = context.getAssets();
        try {
            Files = assetsManager.list("htm");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String file : Files)
        {
            if (file.endsWith(".htm"))
            {
                arrayHtmPathes.add("htm/" + file);

                //Log.d(LOG_TAG, "htm_name: " + file);
            }
        }

        Log.d(LOG_TAG, "arrayHtmPathes size: " + arrayHtmPathes.size());
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

    public static String readRawTextFile(Context context, String filePath)
    {
        //InputStream inputStream = context.getResources().openRawResource(resId);

        InputStream inputStream = null;

        try {
            inputStream = assMan.open(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStreamReader inputReader = new InputStreamReader(inputStream);
        BufferedReader buffReader = new BufferedReader(inputReader);
        String line;
        StringBuilder builder = new StringBuilder();

        try {
            while (( line = buffReader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
        } catch (IOException e) {
            return null;
        }
        return builder.toString();
    }
}
