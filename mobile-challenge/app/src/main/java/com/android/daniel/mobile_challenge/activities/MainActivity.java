package com.android.daniel.mobile_challenge.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.android.daniel.mobile_challenge.R;
import com.android.daniel.mobile_challenge.models.Image500px;
import com.android.daniel.mobile_challenge.utils.EndlessRecyclerViewScrollListener;
import com.android.daniel.mobile_challenge.utils.JSONToImage;
import com.android.daniel.mobile_challenge.utils.MeasUtils;
import com.android.daniel.mobile_challenge.utils.OnItemClickListener;
import com.android.daniel.mobile_challenge.adapters.PhotosAdapter;
import com.android.daniel.mobile_challenge.utils.Tasks500px;
import com.fivehundredpx.greedolayout.GreedoLayoutManager;
import com.fivehundredpx.greedolayout.GreedoSpacingItemDecoration;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends Activity implements OnItemClickListener{

    private RecyclerView mRecyclerView;
    private PhotosAdapter mRecyclerViewAdapter;
    private EndlessRecyclerViewScrollListener mScrollListener;

    private List<Image500px> mImage500pxList = new ArrayList<>();
    private int mPage = 1;
    private int mSpacing;
    private GreedoLayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_images);

        final GetJSON getJSON = new GetJSON();
        getJSON.execute(Integer.toString(mPage));

    }

    // Click event in the images
    @Override
    public void onClick(View view, int position) {
        try{
            Intent intent = new Intent(this, FullScreenPhotoActivity.class);
            intent.putExtra("position", position);
            intent.putStringArrayListExtra("urls", getArrayURL(mImage500pxList));
            intent.putStringArrayListExtra("descriptions", getArrayDescription(mImage500pxList));
            startActivity(intent);
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private class GetJSON extends AsyncTask<String, Void, List<Image500px>> {

        private ProgressDialog progressDialog;

        // Method to display the progress dialog while the application get the photos.
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this
                    , getString(R.string.progress_title)
                    , getString(R.string.progress_message)
                    , true
                    , true);
        }

        // Method to get the photos running in background
        @Override
        protected List<Image500px> doInBackground(String... params) {

            return loadPhotos(Integer.parseInt(params[0]));
        }

        // Method that receives the the result and load the Views
        @Override
        protected void onPostExecute(List<Image500px> result) {
            progressDialog.dismiss();

            if (result != null) {
                try{
                    // Setting up RecyclerView, Adapter and GreedoLayout
                    mRecyclerViewAdapter = new PhotosAdapter(result, MainActivity.this);
                    mLayoutManager = new GreedoLayoutManager(mRecyclerViewAdapter);
                    mRecyclerView.setAdapter(mRecyclerViewAdapter);
                    mRecyclerView.setLayoutManager(mLayoutManager);

                    if(mPage == 1){
                        mSpacing = MeasUtils.dpToPx(4, MainActivity.this);
                        mRecyclerView.addItemDecoration(new GreedoSpacingItemDecoration(mSpacing));
                    }

                    mRecyclerViewAdapter.setClickListener(MainActivity.this);

                    mScrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
                        @Override
                        public void onLoadMore(int page, int totalItemsCount) {

                            new GetJSON().execute(Integer.toString(page));
                            mPage ++;

                        }
                    };
                    mRecyclerView.addOnScrollListener(mScrollListener);
                    mScrollListener.setCurrentPage(JSONToImage.currentPage);

                }catch (Exception e){
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            } else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false)
                        .setTitle(R.string.alert_title)
                        .setMessage(R.string.alert_error)
                        .setPositiveButton(R.string.alert_ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                return;
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    private List<Image500px> loadPhotos(int page) {

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            Tasks500px tasks500px = new Tasks500px();
            tasks500px.generateAccessToken();
            String url = tasks500px.getURL(page);

            JSONToImage jsonToImage = new JSONToImage();
            List<Image500px> images = jsonToImage.parseJSON(url);

            // This task must run in background
            // This task calculate the exact ratio of the photos
            for (int i = 0; i < images.size(); i++) {
                InputStream inputStream;
                Rect rect = new Rect();
                inputStream = new URL(images.get(i).getmImageURL()).openStream();
                BitmapFactory.decodeStream(inputStream, rect, options);
                double ratio = options.outWidth / (double) options.outHeight;
                images.get(i).setmRatio(ratio);
                inputStream.close();
            }
            mPage = page;
            mImage500pxList.addAll(images);
            return mImage500pxList;
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();;
            return null;
        }
    }

    // Putting URLs in array to send to the next Activity
    private ArrayList<String> getArrayURL(List<Image500px> list){
        ArrayList<String> urlArray = new ArrayList<>();
        for (Image500px image : list){
            urlArray.add(image.getmImageURL());
        }
        return urlArray;
        }

    // Putting URLs in array to send to the next Activity
    private ArrayList<String> getArrayDescription(List<Image500px> list){
        ArrayList<String> descriptionArray = new ArrayList<>();
        for (Image500px image : list){
            descriptionArray.add(image.getmDescription());
        }
        return descriptionArray;
    }
}


