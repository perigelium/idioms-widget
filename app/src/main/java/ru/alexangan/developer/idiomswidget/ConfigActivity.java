package ru.alexangan.developer.idiomswidget;

import android.app.Activity;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

public class ConfigActivity extends Activity {

    final static String LOG_TAG = "iw";
    final int DIALOG_NOTE = 2;
    ViewPager viewPager;
    PagerAdapter adapter;
    Boolean widgetNoteShown = false;

    public final static String WIDGET_PREF = "widget_prefs";

    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    Intent resultValue;
    SharedPreferences sharedPrefs;
    int curId = 0;
    static Boolean widgetFirstStart = true;
    public static int maxId = 0;
    public static String pageDisplayMode, langDisplayMode;
    //static String HiddenPagesIdsSet = ",";


    protected void onCreate(Bundle savedInstanceState)
    {
        //setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        langDisplayMode = this.getString(R.string.lang_mode_everything);
        pageDisplayMode = this.getString(R.string.pagesDisplayMode_All);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (widgetID != AppWidgetManager.INVALID_APPWIDGET_ID)
        {

        resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);

        setResult(RESULT_CANCELED, resultValue); // initial set negative answer

            sharedPrefs = getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);

            curId = sharedPrefs.getInt("curId" + widgetID, 0);

            if(widgetFirstStart)
            {
                setResult(RESULT_OK, resultValue);
                widgetFirstStart = false;
                finish();
            }
        }

        setContentView(R.layout.config);

        String[] Files;

        AssetManager assetsManager = getAssets();

        try {
            Files = assetsManager.list("htm");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "assetsManager.list failed");
            return;
        }

        //loadPrefs();

        maxId = Files.length - 1;

        Log.d(LOG_TAG, "maxId: " + maxId);

        if(maxId == -1) return;

        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new ViewPagerAdapter(ConfigActivity.this);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(curId);
    }

    public void onCloseClick(View v)
    {
        exit();
    }

    public void onSettingsClick(View v)
    {
        Intent intent = new Intent(this, PrefActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        langDisplayMode = sPrefs.getString(PrefActivity.APP_PREFERENCES_LANG_DISPLAY_MODE, getString(R.string.lang_mode_everything));

        Log.d(LOG_TAG, "langDisplayMode= " + langDisplayMode);

        pageDisplayMode = sPrefs.getString(PrefActivity.APP_PREFERENCES_PAGE_DISPLAY_MODE, getString(R.string.pagesDisplayMode_All));

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

    /*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            exit();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
*/

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        exit();
    }

    @Override
    protected void onUserLeaveHint()
    {
        //exit();

        super.onUserLeaveHint();
    }

    public void exit()
    {
        setResult(RESULT_OK, resultValue);

        if (!widgetNoteShown && widgetID == AppWidgetManager.INVALID_APPWIDGET_ID)
        {
            showDialog(DIALOG_NOTE);
            widgetNoteShown = true;
        }
        else
        {
            finish();
        }

        //savePrefs();
    }

    /*
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
                Log.d(LOG_TAG, "Unable to refresh hidden pages array");
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

        ed.apply();
    }
    */

    protected Dialog onCreateDialog(int id)
    {
        if (id == DIALOG_NOTE) {
            //String aboutTitle = getString(R.string.about_prog_title) + " 1." + BuildConfig.VERSION_CODE;

            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            // заголовок
            adb.setTitle(getString(R.string.dialog_note_title));
            // сообщение
            adb.setMessage(getString(R.string.dialog_note_text));
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
                    break;

                case Dialog.BUTTON_NEGATIVE:
                    break;

                case Dialog.BUTTON_NEUTRAL:
                    break;
            }
        }
    };
}

