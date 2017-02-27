package com.common.view.recyclerview.loadmore;

import android.content.Context;
import android.view.View;

/**
 * 项目名称：BaseRecyclerViewAdapterHelper-master
 * 类描述：
 * 创建人：zhougl
 * 创建时间：2016/7/18 16:12
 * 修改人：zhougl
 * 修改时间：2016/7/18 16:12
 * 修改备注：
 */
public class LoadMoreContainerBase  implements LoadMoreContainer {

    private LoadMoreHandler mLoadMoreHandler;

    private boolean mIsLoading;
    private boolean mHasMore = false;
    private boolean mAutoLoadMore = true;
    private boolean mLoadError = false;
    private int pageNum = 1;
    public static final int pageSize=20;
    private boolean mListEmpty = true;
    private boolean mShowLoadingForFirstPage = false;
    private LoadMoreUIHandler mLoadMoreUIHandler;
    private View mFooterView;
    private Context context;

    public LoadMoreContainerBase(Context context) {
        this.context = context;
    }

    @Override
    public void setShowLoadingForFirstPage(boolean showLoading) {
        mShowLoadingForFirstPage = showLoading;
    }

    @Override
    public void setAutoLoadMore(boolean autoLoadMore) {
        mAutoLoadMore = autoLoadMore;
    }

    @Override
    public void setOnScrollListener() {
        onReachBottom();
    }

    @Override
    public void setLoadMoreView(View view) {
//        if (mFooterView != null && mFooterView != view) {
//            removeFooterView(view);
//        }

        // add current
        mFooterView = view;
        mFooterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryToPerformLoadMore();
            }
        });

    }

    @Override
    public void setLoadMoreUIHandler(LoadMoreUIHandler handler) {
        mLoadMoreUIHandler = handler;
    }

    @Override
    public void setLoadMoreHandler(LoadMoreHandler handler) {
        mLoadMoreHandler = handler;
    }

    public void loadMoreFinish(boolean hasMore) {

        loadMoreFinish(false,hasMore);
    }
    public void loadMoreFinish(int num) {

        loadMoreFinish(false,num>=pageSize);
    }

    @Override
    public void loadMoreFinish(boolean emptyResult, boolean hasMore) {
        mLoadError = false;
        mListEmpty = emptyResult;
        mIsLoading = false;
        mHasMore = hasMore;

        if(hasMore){
            pageNum++;
        }

        if (mLoadMoreUIHandler != null) {
            mLoadMoreUIHandler.onLoadFinish(this, emptyResult, hasMore);
        }
    }

    @Override
    public void loadMoreError(int errorCode, String errorMessage) {
        mIsLoading = false;
        mLoadError = true;
        if (mLoadMoreUIHandler != null) {
            mLoadMoreUIHandler.onLoadError(this, errorCode, errorMessage);
        }
    }

    @Override
    public int getPageNum() {
        return pageNum;
    }

    private void tryToPerformLoadMore() {
        if (mIsLoading) {
            return;
        }

        // no more content and also not load for first page
        if (!mHasMore && !(mListEmpty && mShowLoadingForFirstPage)) {
            return;
        }

        mIsLoading = true;
        if (mLoadMoreUIHandler != null) {
            mLoadMoreUIHandler.onLoading(this);
        }
        if (null != mLoadMoreHandler) {
            mLoadMoreHandler.onLoadMore(this);
        }
    }

    private void onReachBottom() {
        // if has error, just leave what it should be
        if (mLoadError) {
            return;
        }
        if (mAutoLoadMore) {
            tryToPerformLoadMore();
        } else {
            if (mHasMore) {
                mLoadMoreUIHandler.onWaitToLoadMore(this);
            }
        }
    }

    public void useDefaultFooter() {
        LoadMoreDefaultFooterRecyclerView footerView = new LoadMoreDefaultFooterRecyclerView(context);
        footerView.setVisibility(View.GONE);
        setLoadMoreView(footerView);
        setLoadMoreUIHandler(footerView);
    }

    @Override
    public View getFooterView() {
        return mFooterView;
    }
}
