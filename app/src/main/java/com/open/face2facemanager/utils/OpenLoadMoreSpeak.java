package com.open.face2facemanager.utils;

import android.content.Context;
import android.view.View;

import com.common.view.recyclerview.loadmore.LoadMoreContainer;
import com.common.view.recyclerview.loadmore.LoadMoreGroupRecyclerView;
import com.common.view.recyclerview.loadmore.LoadMoreHandler;
import com.common.view.recyclerview.loadmore.LoadMoreUIHandler;
import com.open.face2facemanager.factory.bean.base.OrderListAble;
import com.open.face2facemanager.factory.bean.base.TwoFrontPagerAble;

import java.util.List;

/**
 * T pageable里的bean
 * V 返回的数据列表
 * 2016/8/10 by onion
 */
public class OpenLoadMoreSpeak<T> implements LoadMoreContainer {

    private LoadMoreHandler mLoadMoreHandler;

    private boolean mIsLoading;
    private boolean mAutoLoadMore = true;
    private boolean mLoadError = false;
    //考虑与服务端对接，封装可分页bean
    public TwoFrontPagerAble<T> pagerAble;
    //记录上下两方向的 orderList
//    public long upOrderList;
//    public long downOrderList;
    private boolean downHasMore = false;
    private boolean upHasMore = false;
    //考虑Presenter处理数据，保留数据
    public List<OrderListAble> datas;
    //标示目前是缓存数据
//    private boolean isCache;
    //    private int pageNum = 1;
//    public static final int pageSize=20;
    private boolean mListEmpty = true;
    private boolean mShowLoadingForFirstPage = false;
    private LoadMoreUIHandler mLoadMoreUIHandler;
    private View mFooterView;
    private View tv_see_more_up;
    private Context context;
    public int updateSize;

    public OpenLoadMoreSpeak(Context context, List<OrderListAble> list, View tv_see_more_up, Long orderListId) {
        this.context = context;
        datas = list;
        pagerAble = new TwoFrontPagerAble<T>();
        this.tv_see_more_up = tv_see_more_up;
        useDefaultFooter();
        if (orderListId == 0 || orderListId == null) {
            tv_see_more_up.setVisibility(View.GONE);
            pagerAble.anchor = null;
        } else {
            upHasMore = true;
            tv_see_more_up.setVisibility(View.VISIBLE);
            tv_see_more_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (upHasMore) {
                        pagerAble.direction = TwoFrontPagerAble.Direction.UP;
                        pagerAble.anchor = ++datas.get(0).orderList;
                        mLoadMoreHandler.onLoadMore(OpenLoadMoreSpeak.this);
                    }
                }
            });
            pagerAble.anchor = orderListId;
        }

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

    public void loadMoreFinish(List<? extends OrderListAble> list) {
        if (datas.isEmpty() && (list == null || list.isEmpty())) {
            loadMoreFinish(true, false);
        } else if (pagerAble.direction.equals(TwoFrontPagerAble.Direction.DOWN)) {
            datas.addAll(list);
            downHasMore = list.size() >= pagerAble.pageSize;
            updateSize = list.size();
            loadMoreFinish(false, downHasMore);
        } else {
            datas.addAll(0, list);
            updateSize = list.size();
            upHasMore = list.size() >= pagerAble.pageSize;
            if (!upHasMore) tv_see_more_up.setVisibility(View.GONE);
        }
    }

    //    public void loadCache(List<V> list) {
//        datas.addAll(list);
//        isCache = true;
//        pagerAble.pageNumber = 1;
//    }
    //朝下
    @Override
    public void loadMoreFinish(boolean emptyResult, boolean hasMore) {
        mLoadError = false;
        mListEmpty = emptyResult;
        mIsLoading = false;
        downHasMore = hasMore;

        if (hasMore) {
            pagerAble.anchor = --datas.get(datas.size() - 1).orderList;
        }
        if (mLoadMoreUIHandler != null) {
            mLoadMoreUIHandler.onLoadFinish(this, emptyResult, hasMore);
        }
    }

    @Override
    public void loadMoreError(int errorCode, String errorMessage) {
        mIsLoading = false;
        mLoadError = true;
        downHasMore = true;
        if (mLoadMoreUIHandler != null) {
            mLoadMoreUIHandler.onLoadError(this, errorCode, errorMessage);
        }
    }

    public void loadMoreError() {
        mIsLoading = false;
        mLoadError = true;
        downHasMore = true;
        if (mLoadMoreUIHandler != null) {
            mLoadMoreUIHandler.onLoadError(this, 99, "网络异常,请重新加载");
        }
    }

    @Override
    public int getPageNum() {
        return 0;
    }

    private void tryToPerformLoadMore() {
        if (mIsLoading) {
            return;
        }

        // no more content and also not load for first page
        if (!downHasMore && !(mListEmpty && mShowLoadingForFirstPage)) {
            return;
        }
        pagerAble.direction = TwoFrontPagerAble.Direction.DOWN;
        mIsLoading = true;
        if (mLoadMoreUIHandler != null) {
            mLoadMoreUIHandler.onLoading(this);
        }
        if (null != mLoadMoreHandler) {
            mLoadMoreHandler.onLoadMore(this);
        }
    }
   public void onLoading(){
       mLoadMoreUIHandler.onLoading(this);
   }
    private void onReachBottom() {
        // if has error, just leave what it should be
        if (mLoadError) {
            return;
        }
        if (mAutoLoadMore) {
            tryToPerformLoadMore();
        } else {
            if (downHasMore) {
                mLoadMoreUIHandler.onWaitToLoadMore(this);
            }
        }
    }

    public void useDefaultFooter() {
        LoadMoreGroupRecyclerView footerView = new LoadMoreGroupRecyclerView(context);
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
        if (pagerAble.anchor == null && datas != null) {
            datas.clear();
        }
    }

}
