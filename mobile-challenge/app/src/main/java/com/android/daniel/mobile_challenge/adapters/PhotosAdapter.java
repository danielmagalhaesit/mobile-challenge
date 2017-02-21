package com.android.daniel.mobile_challenge.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.daniel.mobile_challenge.models.Image500px;
import com.android.daniel.mobile_challenge.utils.OnItemClickListener;
import com.fivehundredpx.greedolayout.GreedoLayoutSizeCalculator;
import com.squareup.picasso.Picasso;

import java.util.List;


public class PhotosAdapter extends RecyclerView.Adapter <PhotosAdapter.PhotoViewHolder>
        implements GreedoLayoutSizeCalculator.SizeCalculatorDelegate {

    private List<Image500px> mImage500pxList;

    private Context mContext;
    private OnItemClickListener mClickListener;

    @Override
    public double aspectRatioForIndex(int index) {

        if (index >= getItemCount()){
            return 1.0;
        }
        // Returning the correct proportion of the images
        return mImage500pxList.get(getLoopedIndex(index)).getmRatio();

    }

    public PhotosAdapter(List<Image500px> Image500pxList ,Context context) {
        mImage500pxList = Image500pxList;
        mContext = context;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Creating the ImageView
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        return new PhotoViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {

        // Using picasso to display the photos in the grid
        String url = mImage500pxList.get(getLoopedIndex(position)).getmImageURL();

        Picasso.with(mContext)
                .load(url)
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mImage500pxList.size();
    }

    // It return zero when it reaches the last image on the page
    private int getLoopedIndex(int index) {
        return index % mImage500pxList.size();
    }

    // Listener of the RecyclerView
    public void setClickListener(OnItemClickListener itemClickListener) {
        mClickListener = itemClickListener;
    }

        public class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;

        public PhotoViewHolder(ImageView imageView) {
            super(imageView);
            mImageView = imageView;
            imageView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onClick(v, getAdapterPosition());

        }
    }

}
