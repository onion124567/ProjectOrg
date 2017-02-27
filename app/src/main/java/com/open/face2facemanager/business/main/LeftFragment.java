package com.open.face2facemanager.business.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.open.face2facemanager.R;

/**
 * Created by onion on 2016/12/15.
 */
public class LeftFragment extends Fragment implements View.OnClickListener {
    private View todayView;
    private View lastListView;
    private View discussView;
    private View favoritesView;
    private View commentsView;
    private View settingsView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_left, null);
        findViews(view);

        return view;
    }


    public void findViews(View view) {
        todayView = view.findViewById(R.id.tvToday);
        lastListView = view.findViewById(R.id.tvLastlist);
        discussView = view.findViewById(R.id.tvDiscussMeeting);
        favoritesView = view.findViewById(R.id.tvMyFavorites);
        commentsView = view.findViewById(R.id.tvMyComments);
        settingsView = view.findViewById(R.id.tvMySettings);

        todayView.setOnClickListener(this);
        lastListView.setOnClickListener(this);
        discussView.setOnClickListener(this);
        favoritesView.setOnClickListener(this);
        commentsView.setOnClickListener(this);
        settingsView.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvToday: // 今日
                break;
            case R.id.tvLastlist:// 往期列表
                break;
            case R.id.tvDiscussMeeting: // 讨论集会
                break;
            case R.id.tvMyFavorites: // 我的收藏
                break;
            case R.id.tvMyComments: // 我的评论
                break;
            case R.id.tvMySettings: // 设置
                break;
            default:
                break;
        }
    }


}