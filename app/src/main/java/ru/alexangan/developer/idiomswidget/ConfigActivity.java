package ru.alexangan.developer.idiomswidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;

public class ConfigActivity extends Activity {

    final static String LOG_TAG = "iw";

    public final static String WIDGET_PREF = "widget_pref";
    public static String WIDGET_PHRASE = "Type your phrase_";

    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    Intent resultValue;
    SharedPreferences sharedPrefs;
    EditText etEditPhrase;
    ImageView imageView;
    static String packageName;
    int curId = 1;
    public static int maxId = 1;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        packageName = getPackageName();

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

        etEditPhrase = (EditText) findViewById(R.id.etEditPhrase);
        imageView = (ImageView) findViewById(R.id.backgroundImage);

        curId = sharedPrefs.getInt("curId" + widgetID, 1);

        String[] Files;

        AssetManager assetsManager = getAssets();

        try {
            Files = assetsManager.list("htm");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "assetsManager.list failed");
            return;
        }

        maxId = Files.length;

        Log.d(LOG_TAG, "arrayHtmPathes size: " + Files.length);

        displayItem();

        //if (cnt == -1) sp.edit().putInt(WIDGET_COUNT + widgetID, 0);
    }

    public void onClick(View v){

        //sharedPrefs.edit().putString(WIDGET_PHRASE + widgetID, etEditPhrase.getText().toString()).commit();

        //MyWidget.updateWidget(this, AppWidgetManager.getInstance(this), widgetID);

        if(v.getId() == R.id.btn_left)
        {
            curId--;

            if (curId < 1)  // cycled show of phrases
            {
                curId = maxId;
            }

            displayItem();
        }

        if(v.getId() == R.id.btn_right)
        {
            curId++;

            if (curId == ConfigActivity.maxId + 1)  // cycled show of phrases
            {
                curId = 1;
            }

            displayItem();
        }

        if(v.getId() == R.id.btn_close_config)
        {
            setResult(RESULT_OK, resultValue);
            finish();
        }
    }

    private void displayItem()
    {
        ViewItem viewItemInstance = new ViewItem(getApplicationContext(), curId);

        String imageFileName = viewItemInstance.getImageFileName();

        Log.d(LOG_TAG, "imageFileName= " + imageFileName);

        int imageId = getResources().getIdentifier(imageFileName, "drawable", packageName);

        Log.d(LOG_TAG, "imageId= " + imageId);

        imageView.setImageResource(imageId);

        String rusIdiom = viewItemInstance.getRusIdiom();
        Log.d(LOG_TAG, "rusIdiom= " + rusIdiom);
        String engIdiom = viewItemInstance.getEngIdiom();
        Log.d(LOG_TAG, "engIdiom= " + engIdiom);
        String translation = viewItemInstance.getTranslation();
        Log.d(LOG_TAG, "translation= " + translation);

        String fullText = rusIdiom + "\n\n" + translation + "\n\n"+ engIdiom;

        etEditPhrase.setText(fullText);
    }
}
