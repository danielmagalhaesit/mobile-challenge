package com.android.daniel.mobile_challenge.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.daniel.mobile_challenge.R;
import com.android.daniel.mobile_challenge.adapters.ImagePagerAdapter;

import java.util.ArrayList;

public class FullScreenPhotoActivity extends Activity {

    private ViewPager mViewPager;
    private ImagePagerAdapter mPagerAdapter;

    private ArrayList<String> mURLs;
    private ArrayList<String> mDescriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_photo);

        // Getting information from previous activity
        Intent intent = getIntent();
        int position = intent.getExtras().getInt("position");
        mURLs = intent.getStringArrayListExtra("urls");
        mDescriptions = intent.getStringArrayListExtra("descriptions");

        // Creating ViewPager to swipe among the photos
        mViewPager = (ViewPager) findViewById(R.id.view_pager_full_screen);
        mPagerAdapter = new ImagePagerAdapter(this, mURLs, mDescriptions );
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(position);
    }

}
