package com.common.view.recyclerview.loadmore;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.open.face2facemanager.R;


public class LoadMoreGroupRecyclerView extends RelativeLayout implements LoadMoreUIHandler {

    private TextView mTextView;
    private View ll_empty_view;

    public LoadMoreGroupRecyclerView(Context context) {
        this(context, null);
    }

    public LoadMoreGroupRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreGroupRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupViews();
    }

    private void setupViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.cube_views_load_more_group_footer, this);
        mTextView = (TextView) findViewById(R.id.cube_views_load_more_default_footer_text_view);
        ll_empty_view = findViewById(R.id.ll_empty_view);
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
