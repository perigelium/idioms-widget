package ru.alexangan.developer.idiomswidget;

/**
 * Created by Administrator on 23.08.16.
 *
 */
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Random;

public class ViewPagerAdapter extends PagerAdapter //implements CompoundButton.OnCheckedChangeListener
{
    private Context mContext;
    LayoutInflater mInflater;
    final static String LOG_TAG = "iw";
    static String packageName;
    EditText etEditPhrase;
    ImageView imageView;
    final static int maxId = ConfigActivity.maxId;
    public static ArrayList<Integer> arrPageDisplayMode;
    //private static ArrayList<Integer> toggleBtnIds;
    //ToggleButton toggleBtnHidePage;
    static int indexCurId;

    public ViewPagerAdapter(Context context)
    {
        this.mContext = context;
        packageName = mContext.getPackageName();

        arrPageDisplayMode = new ArrayList<>(maxId);

        /*
        toggleBtnIds = new ArrayList<>(maxId);

        for (int i = 0; i <= maxId; i++)
        {
            arrPageDisplayMode.add(0);
            toggleBtnIds.add(i, null);
        }

        Log.d(LOG_TAG, "views.size= " + toggleBtnIds.size());
        */
    }

    @Override
    public int getCount()
    {
        return maxId;
    } // This parameter is mandatory

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int curId)
    {
        curId = calculateCurId(curId);

        indexCurId = curId;
        Log.d(LOG_TAG, "curId= " + indexCurId);

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = mInflater.inflate(R.layout.pager, container, false);

        etEditPhrase = (EditText) itemView.findViewById(R.id.etEditPhrase);
        imageView = (ImageView) itemView.findViewById(R.id.backgroundImage);
        //toggleBtnHidePage = (ToggleButton) itemView.findViewById(R.id.toggleBtnHidePage);


            //toggleBtnHidePage.setId(curId);
            //toggleBtnIds.set(curId, toggleBtnHidePage.getId());
            //Log.d(LOG_TAG, "btnViews.add(curId, itemView) " + curId);

        //setPageDisplayMode(indexCurId);

        setItemContentView(curId);

        container.addView(itemView);

        return itemView;
    }

    private int calculateCurId(int curId)
    {
        Log.d(LOG_TAG, "ConfigActivity.pageDisplayMode= " + ConfigActivity.pageDisplayMode);

        if(ConfigActivity.pageDisplayMode != null
                && ConfigActivity.pageDisplayMode.equals(mContext.getString(R.string.pagesDisplayMode_Shuffle)))
        {
            Random rnd = new Random();
            curId = rnd.nextInt(maxId + 1);
        }
/*
        if(ConfigActivity.pageDisplayMode != null
                && ConfigActivity.pageDisplayMode.equals(mContext.getString(R.string.pageDisplayMode_NoHidden)))
        {
            Random rnd = new Random();

                while(arrPageDisplayMode.get(curId) == -1)
                {
                    Log.d(LOG_TAG, "curId= " + curId);
                    Log.d(LOG_TAG, "arrPageDisplayMode.get(curId)= " + arrPageDisplayMode.get(curId));

                    curId = rnd.nextInt(maxId + 1);

                    Log.d(LOG_TAG, "curId= " + curId);
                }
        }
*/

        if(curId < 0)
        {
            curId = ConfigActivity.maxId;
        }

        if(curId > ConfigActivity.maxId)
        {
            curId = 0;
        }

        Log.d(LOG_TAG, "calculateCurId(int curId) returned curId " + curId);

        return curId;
    }

    /*
    private void setPageDisplayMode(int indexCurId)
    {

        int pageDisplayModeInteger = arrPageDisplayMode.get(indexCurId);

        switch (pageDisplayModeInteger)
        {
            case -1:
            {
                Log.d(LOG_TAG, "toggleBtnHidePage.setChecked(true), index: " + indexCurId);
                toggleBtnHidePage.setChecked(true);
                break;
            }
            default:
            {
                Log.d(LOG_TAG, "toggleBtnHidePage.setChecked(false), index: " + indexCurId);
                toggleBtnHidePage.setChecked(false);
                break;
            }

        }

        toggleBtnHidePage.setOnCheckedChangeListener(this);
    }
*/

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        Log.d(LOG_TAG, "Removing view with position: " + position);

        (container).removeView((FrameLayout) object);
    }

    private void setItemContentView(int curId)
    {
        String rusIdiom = "", engIdiom = "", translation = "", fullText = "";

        ItemContent itemContentInstance = new ItemContent(mContext, curId);

        String imageFileName = itemContentInstance.getImageFileName();

        Log.d(LOG_TAG, "ConfigActivity.langDisplayMode= " + ConfigActivity.langDisplayMode);

        int imageId = mContext.getResources().getIdentifier(imageFileName, "drawable", packageName);

        imageView.setImageResource(imageId);

        if(ConfigActivity.langDisplayMode != null
                && !ConfigActivity.langDisplayMode.equals(mContext.getString(R.string.lang_mode_only_English)))
        {
            rusIdiom = itemContentInstance.getRusIdiom();
            fullText += rusIdiom + "\n\n";
        }

        if(ConfigActivity.langDisplayMode != null
                && !ConfigActivity.langDisplayMode.equals(mContext.getString(R.string.lang_mode_only_Russian)))
        {
            translation = itemContentInstance.getTranslation();
            engIdiom = itemContentInstance.getEngIdiom();

            if(!translation.equals(engIdiom))
            {
                fullText += translation + "\n\n";
            }

            fullText += engIdiom;
        }

        etEditPhrase.setText(fullText);
    }

    /*
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        int btnPos = buttonView.getId();

        if (isChecked)
        {
            Log.d(LOG_TAG, "arrPageDisplayMode.add -1: " + btnPos);
            arrPageDisplayMode.set(btnPos, -1);
        }
        else
        {
            arrPageDisplayMode.set(btnPos, 0);
            Log.d(LOG_TAG, "arrPageDisplayMode.add 0: " + btnPos);
        }
    }
    */

}
