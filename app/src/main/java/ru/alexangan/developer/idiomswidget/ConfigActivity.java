package ru.alexangan.developer.idiomswidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ConfigActivity extends Activity {

    final static String LOG_TAG = "myLogs";

    public final static String WIDGET_PREF = "widget_pref";
    public static String WIDGET_PHRASE = "Type your phrase_";
    //public final static String WIDGET_COUNT = "widget_count_";

    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    Intent resultValue;
    SharedPreferences sharedPrefs;
    EditText etEditPhrase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);

        setResult(RESULT_CANCELED, resultValue); // initial set negative answer

        setContentView(R.layout.config);

        sharedPrefs = getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
        etEditPhrase = (EditText) findViewById(R.id.etEditPhrase);

        //Log.d(LOG_TAG, "curReceivedAnswer= " + ConfigActivity.WIDGET_TIME_FORMAT + widgetID);

        etEditPhrase.setText(sharedPrefs.getString(ConfigActivity.WIDGET_PHRASE + widgetID, "Start"));

        //int cnt = sp.getInt(ConfigActivity.WIDGET_COUNT + widgetID, -1);
        //if (cnt == -1) sp.edit().putInt(WIDGET_COUNT + widgetID, 0);
    }

    public void onClick(View v){

        sharedPrefs.edit().putString(WIDGET_PHRASE + widgetID, etEditPhrase.getText().toString()).commit();

        MyWidget.updateWidget(this, AppWidgetManager.getInstance(this), widgetID);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}
