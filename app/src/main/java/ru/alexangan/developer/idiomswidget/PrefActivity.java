package ru.alexangan.developer.idiomswidget;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 24.08.16.
 *
 */
public class PrefActivity extends PreferenceActivity
{
    public final static String LOG_TAG = "iw";
    static SharedPreferences sharedPrefs;
    public static final String APP_PREFERENCES_LANG_DISPLAY_MODE = "lang_display_mode";
    public static final String APP_PREFERENCES_PAGE_DISPLAY_MODE = "pages_display_mode";
    public static String langDisplayMode, pageDisplayMode;
    final int DIALOG_ABOUT = 1;

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

            langDisplayMode = sharedPrefs.getString(APP_PREFERENCES_LANG_DISPLAY_MODE, getString(R.string.lang_mode_everything));

            Log.d(LOG_TAG, "langDisplayingMode= " + langDisplayMode);
        }

        if (sharedPrefs.contains(APP_PREFERENCES_PAGE_DISPLAY_MODE))
        {

            pageDisplayMode = sharedPrefs.getString(APP_PREFERENCES_PAGE_DISPLAY_MODE, getString(R.string.pagesDisplayMode_All));

            Log.d(LOG_TAG, "pageDisplayingMode= " + pageDisplayMode);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void onAboutClick(View view)
    {
        showDialog(DIALOG_ABOUT);
    }

    protected Dialog onCreateDialog(int id)
    {
        if (id == DIALOG_ABOUT) {
            String aboutTitle = getString(R.string.about_prog_title) + " 1." + BuildConfig.VERSION_CODE;

            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            // заголовок
            adb.setTitle(aboutTitle);
            // сообщение
            adb.setMessage(R.string.about_prog_text);
            // иконка
            adb.setIcon(android.R.drawable.ic_dialog_info);
            // кнопка положительного ответа
            //adb.setPositiveButton(R.string.Yes, myClickListener);
            // кнопка отрицательного ответа
            //adb.setNegativeButton(R.string.No, myClickListener);
            // кнопка нейтрального ответа
            adb.setNeutralButton(R.string.Close, myClickListener);
            // создаем диалог
            return adb.create();
        }
        return super.onCreateDialog(id);
    }

    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case Dialog.BUTTON_POSITIVE:
                    finish();
                    break;

                case Dialog.BUTTON_NEGATIVE:
                    finish();
                    break;

                case Dialog.BUTTON_NEUTRAL:
                    break;
            }
        }
    };
}
