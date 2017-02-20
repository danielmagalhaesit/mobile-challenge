package com.android.daniel.mobile_challenge.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.android.daniel.mobile_challenge.R;
import com.android.daniel.mobile_challenge.models.Image500px;
import com.android.daniel.mobile_challenge.utils.JSONToImage;
import com.android.daniel.mobile_challenge.utils.MeasUtils;
import com.android.daniel.mobile_challenge.utils.OnItemClickListener;
import com.android.daniel.mobile_challenge.adapters.PhotosAdapter;
import com.fivehundredpx.greedolayout.GreedoLayoutManager;
import com.fivehundredpx.greedolayout.GreedoSpacingItemDecoration;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnItemClickListener{

    private RecyclerView mRecyclerView;
    private PhotosAdapter mRecyclerViewAdapter;
    private List<Image500px> mImage500pxList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_images);

        final GetJSON getJSON = new GetJSON();
        getJSON.execute();

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

    private class GetJSON extends AsyncTask<Void, Void, List<Image500px>> {

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
        protected List<Image500px> doInBackground(Void... params) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;

                JSONToImage jsonToImage = new JSONToImage();
                List<Image500px> images = jsonToImage.parseJSON();

                // This task must run in background
                for (int i = 0; i < images.size(); i++) {
                    InputStream inputStream;
                    Rect rect = new Rect();
                    inputStream = new URL(images.get(i).getmImageURL()).openStream();
                    BitmapFactory.decodeStream(inputStream, rect, options);
                    double ratio = options.outWidth / (double) options.outHeight;
                    images.get(i).setmRatio(ratio);
                    inputStream.close();
                }
                mImage500pxList.addAll(images);
                return images;
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();;
                return null;
            }

        }

        // Method that receives the the result and load the Views
        @Override
        protected void onPostExecute(List<Image500px> result) {
            progressDialog.dismiss();

            if (result != null) {
                try{
                    // Setting up RecyclerView, Adapter and GreedoLayout
                    mRecyclerViewAdapter = new PhotosAdapter(result, MainActivity.this);
                    final GreedoLayoutManager layoutManager = new GreedoLayoutManager(mRecyclerViewAdapter);
                    mRecyclerView.setAdapter(mRecyclerViewAdapter);
                    mRecyclerView.setLayoutManager(layoutManager);

                    int spacing = MeasUtils.dpToPx(4, MainActivity.this);
                    mRecyclerView.addItemDecoration(new GreedoSpacingItemDecoration(spacing));

                    mRecyclerViewAdapter.setClickListener(MainActivity.this);
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
        ArrayList<String> urlArray = new ArrayList<>();
        for (Image500px image : list){
            urlArray.add(image.getmDescription());
        }
        return urlArray;
    }

}


