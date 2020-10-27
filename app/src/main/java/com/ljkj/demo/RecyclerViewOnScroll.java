package com.ljkj.demo;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

public class RecyclerViewOnScroll extends RecyclerView.OnScrollListener {
    private PullToRefreshRecyclerView mPullToRefreshRecyclerView;

    public RecyclerViewOnScroll(PullToRefreshRecyclerView pullToRefreshRecyclerView) {
        this.mPullToRefreshRecyclerView = pullToRefreshRecyclerView;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int lastItem = 0;
        int firstItem = 0;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int totalItemCount = layoutManager.getItemCount();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = ((GridLayoutManager) layoutManager);
            firstItem = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
            //Position to find the final item of the current LayoutManager
            lastItem = gridLayoutManager.findLastCompletelyVisibleItemPosition();
            if (lastItem == -1) lastItem = gridLayoutManager.findLastVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) layoutManager);
            firstItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
            lastItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
            if (lastItem == -1) lastItem = linearLayoutManager.findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = ((StaggeredGridLayoutManager) layoutManager);
            // since may lead to the final item has more than one StaggeredGridLayoutManager the particularity of the so here that is an array
            // this array into an array of position and then take the maximum value that is the last show the position value
            int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
            staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(lastPositions);
            lastItem = findMax(lastPositions);
            firstItem = staggeredGridLayoutManager.findFirstVisibleItemPositions(lastPositions)[0];
        }
        if (firstItem == 0 || firstItem == RecyclerView.NO_POSITION) {
            if (mPullToRefreshRecyclerView.getPullRefreshEnable())
                mPullToRefreshRecyclerView.setSwipeRefreshEnable(true);
        } else {
            mPullToRefreshRecyclerView.setSwipeRefreshEnable(false);
        }
        if (mPullToRefreshRecyclerView.getPushRefreshEnable()
                && !mPullToRefreshRecyclerView.isRefresh()
                && mPullToRefreshRecyclerView.isHasMore()
                && (lastItem == totalItemCount - 1)
                && !mPullToRefreshRecyclerView.isLoadMore()
                && (dx > 0 || dy > 0)) {
            mPullToRefreshRecyclerView.setIsLoadMore(true);
            mPullToRefreshRecyclerView.loadMore();
        }

    }

    //To find the maximum value in the array
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}
