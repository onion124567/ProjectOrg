package com.open.face2facemanager.utils;

import android.content.Context;
import android.view.View;

import com.common.view.recyclerview.loadmore.LoadMoreContainer;
import com.common.view.recyclerview.loadmore.LoadMoreDefaultFooterRecyclerView;
import com.common.view.recyclerview.loadmore.LoadMoreHandler;
import com.common.view.recyclerview.loadmore.LoadMoreUIHandler;
import com.open.face2facemanager.factory.bean.base.PagerAble;

import java.util.List;

/**
 * T pageable里的bean
 * V 返回的数据列表
 * 2016/8/10 by onion
 */
public class OpenLoadMoreDefault<T, V> implements LoadMoreContainer {

    private LoadMoreHandler mLoadMoreHandler;

    private boolean mIsLoading;
    private boolean mHasMore = false;
    private boolean mAutoLoadMore = true;
    private boolean mLoadError = false;
    //考虑与服务端对接，封装可分页bean
    public PagerAble<T> pagerAble;
    //考虑Presenter处理数据，保留数据
    public List<V> datas;
    //标示目前是缓存数据
    private boolean isCache;
    //    private int pageNum = 1;
//    public static final int pageSize=20;
    private boolean mListEmpty = true;
    private boolean mShowLoadingForFirstPage = false;
    private LoadMoreUIHandler mLoadMoreUIHandler;
    private View mFooterView;
    private Context context;

    public OpenLoadMoreDefault(Context context, List<V> list) {
        this.context = context;
        datas = list;
        pagerAble = new PagerAble<T>();
        useDefaultFooter();

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

    //    public void loadMoreFinish(boolean hasMore) {
//
//        loadMoreFinish(false,hasMore);
//    }
//    public void loadMoreFinish(int num) {
//        loadMoreFinish(false,num>=pagerAble.pageSize);
//    }
    //
    public void loadMoreFinish(List<V> list) {
        if (isCache) {
            datas.clear();
            isCache=false;
        }
        datas.addAll(list);
        loadMoreFinish(datas.isEmpty(), list.size() >= pagerAble.pageSize);
    }

    public void loadCache(List<V> list) {
        datas.addAll(list);
        isCache = true;
        pagerAble.pageNumber=1;
    }

    @Override
    public void loadMoreFinish(boolean emptyResult, boolean hasMore) {
        mLoadError = false;
        mListEmpty = emptyResult;
        mIsLoading = false;
        mHasMore = hasMore;

        if (hasMore) {
            pagerAble.pageNumber++;
        }

        if (mLoadMoreUIHandler != null) {
            mLoadMoreUIHandler.onLoadFinish(this, emptyResult, hasMore);
        }
    }

    @Override
    public void loadMoreError(int errorCode, String errorMessage) {
        mIsLoading = false;
        mLoadError = true;
        mHasMore = true;
        if (mLoadMoreUIHandler != null) {
            mLoadMoreUIHandler.onLoadError(this, errorCode, errorMessage);
        }
    }

    public void loadMoreError() {
        mIsLoading = false;
        mLoadError = true;
        mHasMore = true;
        if (mLoadMoreUIHandler != null) {
            mLoadMoreUIHandler.onLoadError(this, 99, "网络异常,请重新加载");
        }
    }

    @Override
    public int getPageNum() {
        return pagerAble.pageNumber;
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

    //检测页码，并清理数据
    public void fixNumAndClear() {
        if (pagerAble.pageNumber == 1 && datas != null) {
            datas.clear();
        }
    }

}
