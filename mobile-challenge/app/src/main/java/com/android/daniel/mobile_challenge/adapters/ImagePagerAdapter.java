package com.android.daniel.mobile_challenge.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

// It is needed in order to swipe among the images
public class ImagePagerAdapter extends PagerAdapter {

    private ArrayList<String> mURLList;
    private ArrayList<String> mDescriptionList;
    private Context mContext;

    public ImagePagerAdapter(Context ctx , ArrayList<String> urls, ArrayList<String> descriptions){
        this.mContext = ctx;
        this.mURLList = urls;
        this.mDescriptionList = descriptions;
    }

    @Override
    public int getCount()
    {
        return mURLList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == object;
    }

    // Rendering the views.
    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        FrameLayout fl = new FrameLayout(mContext);
        FrameLayout.LayoutParams params =
        new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        fl.setLayoutParams(params);
        container.addView(fl);

        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        Picasso.with(mContext)
                .load(mURLList.get(position))
                .into(imageView);
        fl.addView(imageView);

        TextView textView = new TextView(mContext);
        textView.setText(mDescriptionList.get(position));
        textView.setGravity(Gravity.BOTTOM);
        fl.addView(textView);

        return fl;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView((View) object);
    }
}
