package ru.alexangan.developer.idiomswidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;

public class ConfigActivity extends Activity {

    final static String LOG_TAG = "iw";
    ViewPager viewPager;
    PagerAdapter adapter;

    public final static String WIDGET_PREF = "widget_prefs";

    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    Intent resultValue;
    SharedPreferences sharedPrefs;
    int curId = 0;
    public static int maxId = 0;
    public static String pageDisplayMode, langDisplayMode = "Everything";
    static String HiddenPagesIdsSet = ",";


    protected void onCreate(Bundle savedInstanceState)
    {
        //setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        pageDisplayMode = this.getString(R.string.pageDisplayMode_NoHidden);
        langDisplayMode = this.getString(R.string.languages_display_mode_everything);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);

        setResult(RESULT_CANCELED, resultValue); // initial set negative answer

        setContentView(R.layout.config);

        sharedPrefs = getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);

        curId = sharedPrefs.getInt("curId" + widgetID, 0);

        String[] Files;

        AssetManager assetsManager = getAssets();

        try {
            Files = assetsManager.list("htm");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "assetsManager.list failed");
            return;
        }

        loadPrefs();

        maxId = Files.length - 1;

        Log.d(LOG_TAG, "maxId: " + maxId);

        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new ViewPagerAdapter(ConfigActivity.this);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(curId);
    }

    public void onClick(View v)
    {

        if(v.getId() == R.id.btn_close_config)
        {
            setResult(RESULT_OK, resultValue);

            savePrefs();

            finish();
        }

        if(v.getId() == R.id.btn_settings)
        {
            Intent intent = new Intent(this, PrefActivity.class);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        langDisplayMode = sPrefs.getString(PrefActivity.APP_PREFERENCES_LANG_DISPLAY_MODE, getString(R.string.languages_display_mode_everything));

        Log.d(LOG_TAG, "langDisplayMode= " + langDisplayMode);

        pageDisplayMode = sPrefs.getString(PrefActivity.APP_PREFERENCES_PAGE_DISPLAY_MODE, getString(R.string.pageDisplayMode_NoHidden));

        Log.d(LOG_TAG, "pageDisplayMode= " + pageDisplayMode);

        /*
        Boolean resetEvent = sharedPrefs.getBoolean(PrefActivity.APP_PREFERENCES_RESET_SETTINGS, false);

        if(resetEvent == true)
        {
            for (int i = 0; i <= maxId; i++)
            {
                ViewPagerAdapter.arrPageDisplayMode.add(0);
            }
        }
        */
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            savePrefs();

            finish();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void loadPrefs()
    {
        sharedPrefs = getPreferences(MODE_PRIVATE);

        HiddenPagesIdsSet = sharedPrefs.getString("HiddenPagesIdsSet", ",");
        String [] hiddenPagesIds = HiddenPagesIdsSet.split(",");

        for(String curA : hiddenPagesIds)
        {
            try
            {
                int curN = Integer.parseInt(curA);
                ViewPagerAdapter.arrPageDisplayMode.add(curN);
            }
            catch(NumberFormatException nfe)
            {
            }
        }

        Log.d(LOG_TAG, "loadPrefs");
    }

    private void savePrefs()
    {
        sharedPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPrefs.edit();

        ed.putString("HiddenPagesIdsSet", HiddenPagesIdsSet);

        Log.d(LOG_TAG, "savePrefs");

        ed.commit();
    }
}

