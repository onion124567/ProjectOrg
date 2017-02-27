package com.open.face2facemanager.business.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.open.face2facemanager.business.baseandcommon.BaseFragment;

/**
 * Created by onion on 2016/12/15.
 */
public class TestFragment extends BaseFragment {
    static int num=1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView textView=new TextView(getActivity());
        num++;
        textView.setText("num"+num);
        return textView;
    }
}
