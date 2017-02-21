package com.android.daniel.mobile_challenge.utils;

import android.support.v7.widget.RecyclerView;

import com.fivehundredpx.greedolayout.GreedoLayoutManager;

// Abstract class needed in order to do the Endless RecyclerView
public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private int visibleThreshold = 5;
    private int currentPage = 1;
    private int previousTotalItemCount = 0;
    private boolean loading = true;
    private int startingPageIndex = 1;

    RecyclerView.LayoutManager mLayoutManager;

    public EndlessRecyclerViewScrollListener(GreedoLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    public int findLastVisibleItemPosition(GreedoLayoutManager greedoLayoutManager) {
        if (greedoLayoutManager.getItemCount() == 0) {
            return RecyclerView.NO_POSITION;
        } else {
            return greedoLayoutManager.findFirstVisibleItemPosition() + greedoLayoutManager.getChildCount();
        }
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        int lastVisibleItemPosition = 0;
        int totalItemCount = mLayoutManager.getItemCount();

        lastVisibleItemPosition = findLastVisibleItemPosition((GreedoLayoutManager) mLayoutManager);

        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }

        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
            currentPage++;
            onLoadMore(currentPage, totalItemCount);
            loading = true;
        }
    }

    public abstract void onLoadMore(int page, int totalItemsCount);

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }


}
