package ru.alexangan.developer.idiomswidget;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 22.08.16.
 *
 */

public class ItemContent {

    private int itemId;
    private String rusIdiom;
    private String engIdiom;
    private String translation;
    private String fileName;
    private static final String parentFolder = "htm/";
    private static final String fileExtension = ".htm";
    final static String LOG_TAG = "iw";

    public ItemContent(Context context, int id)
    {
        this.itemId = id;

        fileName = makeFileName();

        String strRawHtm = readAssetsTextFile(context, parentFolder + fileName + fileExtension);

        rusIdiom =  extractRusIdiomFromHtm(strRawHtm);
        engIdiom = extractEngIdiomFromHtm(strRawHtm);
        translation = extractTranslationFromHtm(strRawHtm);
    }

    private String makeFileName()
    {
        String imageFileName;

        if( itemId < 10)
        {
            imageFileName = "_00" + itemId;
        }
        else if( itemId < 100)
        {
            imageFileName = "_0" + itemId;
        }
        else
        {
            imageFileName = "_" + itemId;
        }
        return imageFileName;
    }

    static String extractTranslationFromHtm(String strFile)
    {
        String phraseEn = " ";

        Pattern pattEngDirectTrans = Pattern.compile("((?<=(<P><I>)).+(?=(</I>)))", Pattern.UNICODE_CASE);

        Matcher matcher2 = pattEngDirectTrans.matcher(strFile);

        if (matcher2.find())
        {
            phraseEn += matcher2.group(0);
        }

        return phraseEn;
    }

    static String extractRusIdiomFromHtm(String strFile)
    {
        String phraseRu = " ";

        Pattern pattRusPhraseol = Pattern.compile("((?<=<TITLE>).+(?=</TITLE>))", Pattern.UNICODE_CASE);

        Matcher matcher1 = pattRusPhraseol.matcher(strFile);

        if (matcher1.find())
        {
            phraseRu += matcher1.group(0);
        }

        return phraseRu;
    }

    static String extractEngIdiomFromHtm(String strFile)
    {
        String phraseEn = " ";

        Pattern pattEngCf = Pattern.compile("((?<=(<P><B>Cf\\..)).+(?=(</B>)))", Pattern.UNICODE_CASE);

        Matcher matcher0 = pattEngCf.matcher(strFile);

        if (matcher0.find())
        {
            phraseEn += matcher0.group(0);
        }

        return phraseEn;
    }

    public static String readAssetsTextFile(Context context, String filePath)
    {
        InputStream inputStream;
        AssetManager assetsManager = context.getAssets();

        try {
            inputStream = assetsManager.open(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "Error opening file: " + filePath);
            return null;
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

    public String getRusIdiom() {
        return rusIdiom;
    }

    public String getEngIdiom() {
        return engIdiom;
    }

    public String getTranslation() {
        return translation;
    }

    public String getImageFileName() {
        return fileName;
    }
}
