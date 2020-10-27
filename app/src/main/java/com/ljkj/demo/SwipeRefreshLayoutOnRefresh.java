package com.ljkj.demo;

import android.support.v4.widget.SwipeRefreshLayout;

public class SwipeRefreshLayoutOnRefresh implements SwipeRefreshLayout.OnRefreshListener {
    private PullToRefreshRecyclerView mPullToRefreshRecyclerView;

    public SwipeRefreshLayoutOnRefresh(PullToRefreshRecyclerView pullToRefreshRecyclerView) {
        this.mPullToRefreshRecyclerView = pullToRefreshRecyclerView;
    }

    @Override
    public void onRefresh() {
        if (!mPullToRefreshRecyclerView.isRefresh()) {
            mPullToRefreshRecyclerView.setIsRefresh(true);
            mPullToRefreshRecyclerView.refresh();
        }
    }
}
