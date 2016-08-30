package ru.alexangan.developer.idiomswidget;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by Administrator on 24.08.16.
 *
 */
public class PrefActivity extends PreferenceActivity
{
    public final static String LOG_TAG = "iw";
    static SharedPreferences sharedPrefs;
    public static final String APP_PREFERENCES_RESET_SETTINGS = "PrefActivity_prefs";
    public static final String APP_PREFERENCES_LANG_DISPLAY_MODE = "languages_display_mode";
    public static final String APP_PREFERENCES_PAGE_DISPLAY_MODE = "pages_display_mode";
    public static String langDisplayMode, pageDisplayMode;
    //private LinearLayout rootView, buttonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
        setContentView(R.layout.pref_layout);

        //sharedPrefs = getSharedPreferences(ConfigActivity.WIDGET_PREF, MODE_PRIVATE);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.settings, false);
        //PreferenceManager.setDefaultValues(getApplicationContext(), APP_PREFERENCES_LANG_DISPLAY_MODE, 0, R.xml.settings, false);
        //PreferenceManager.setDefaultValues(getApplicationContext(), APP_PREFERENCES_PAGE_DISPLAY_MODE, 2, R.xml.settings, false);

        //rootView = new LinearLayout(this);
        //rootView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        //rootView.setOrientation(LinearLayout.VERTICAL);
/*
        Button button = new Button(this);
        ListView v = getListView();
        button.setText(R.string.btn_Reset_Settings);
        //buttonView = new LinearLayout(this);
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.gravity = Gravity.END;
        v.setLayoutParams(lParams);
        button.setGravity(Gravity.CENTER);
        button.setId(R.id.btnReset);
        v.addFooterView(button);
        //v.addView(button, lParams);
        */

    }

    public void onClick(View v) {

        if (v.getId() == R.id.btnCloseSettings)
        {
            this.finish();

            //Boolean resetEvent = sharedPrefs.getBoolean(APP_PREFERENCES_RESET_SETTINGS, false);
            //Log.d(LOG_TAG, "LangDisplayMode= " + langDisplayMode);
            //sharedPrefs.edit().putBoolean(APP_PREFERENCES_RESET_SETTINGS, resetEvent).apply();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (sharedPrefs.contains(APP_PREFERENCES_LANG_DISPLAY_MODE))
        {

            langDisplayMode = sharedPrefs.getString(APP_PREFERENCES_LANG_DISPLAY_MODE, getString(R.string.languages_display_mode_everything));

            Log.d(LOG_TAG, "langDisplayingMode= " + langDisplayMode);
        }

        if (sharedPrefs.contains(APP_PREFERENCES_PAGE_DISPLAY_MODE))
        {

            pageDisplayMode = sharedPrefs.getString(APP_PREFERENCES_PAGE_DISPLAY_MODE, getString(R.string.pageDisplayMode_NoHidden));

            Log.d(LOG_TAG, "pageDisplayingMode= " + pageDisplayMode);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
