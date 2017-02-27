package com.common.view.recyclerview.loadmore;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.open.face2facemanager.R;
import com.open.face2facemanager.utils.ScreenUtils;


public class LoadMoreDefaultFooterRecyclerView extends RelativeLayout implements LoadMoreUIHandler {

    private TextView mTextView;
    private View ll_empty_view;
    private TextView tv_empty;
    public LoadMoreDefaultFooterRecyclerView(Context context) {
        this(context, null);
    }

    public LoadMoreDefaultFooterRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreDefaultFooterRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupViews();
    }

    private void setupViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.cube_views_load_more_default_footer, this);
        mTextView = (TextView) findViewById(R.id.cube_views_load_more_default_footer_text_view);
        ll_empty_view = findViewById(R.id.ll_empty_view);
        tv_empty= (TextView) findViewById(R.id.tv_empty);
        //
        int emptyHeight= ScreenUtils.getScreenHeight(getContext())- ScreenUtils.getStatusHeight(getContext())-ScreenUtils.dip2px(getContext(),44);
        RelativeLayout.LayoutParams params=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,emptyHeight);
        ll_empty_view.setLayoutParams(params);
    }
    public void setEmptyMessage(String msg){
        tv_empty.setText(msg);
    }
    @Override
    public void onLoading(LoadMoreContainer container) {
        setVisibility(VISIBLE);
        mTextView.setText(R.string.cube_views_load_more_loading);
    }

    @Override
    public void onLoadFinish(LoadMoreContainer container, boolean empty, boolean hasMore) {

        if (!hasMore) {

//            if (empty) {
//                setVisibility(GONE);
//                mTextView.setText(R.string.cube_views_load_more_loaded_empty);
//            } else {
            setVisibility(VISIBLE);
            if (empty) {
                ll_empty_view.setVisibility(VISIBLE);
                mTextView.setVisibility(GONE);
            } else {
                mTextView.setVisibility(VISIBLE);
                ll_empty_view.setVisibility(GONE);
                mTextView.setText(R.string.cube_views_load_more_loaded_no_more);
            }

//            }
        } else {
            setVisibility(INVISIBLE);
        }
    }

    @Override
    public void onWaitToLoadMore(LoadMoreContainer container) {
        Log.i("debbug", "onWaitToLoadMore()");
        setVisibility(VISIBLE);
        mTextView.setText(R.string.cube_views_load_more_click_to_load_more);
    }

    @Override
    public void onLoadError(LoadMoreContainer container, int errorCode, String errorMessage) {
        Log.i("debbug", "onLoadError()");
        setVisibility(VISIBLE);
        mTextView.setText(R.string.cube_views_load_more_error);
//        mTextView.setVisibility(GONE);
    }
}
