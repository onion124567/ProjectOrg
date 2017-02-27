package com.open.face2facemanager.business.baseandcommon;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.view.PagerSlidingTabStrip;
import com.open.face2facemanager.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/16.
 */
public class TableActivity<View extends MPresenter> extends BaseActivity<View> {
    Map<Integer, Fragment> contentMap = new HashMap<>();
    protected PagerSlidingTabStrip tabs;
    protected ViewPager pager;
    //如果改了layout,可能为空
    protected  ImageView iv_back,iv_arrow,iv_right;
    protected TextView tv_title,tv_right;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getTabsRootView());
        iv_back= (ImageView) findViewById(R.id.iv_back);
        iv_arrow= (ImageView) findViewById(R.id.iv_arrow);
        iv_right= (ImageView) findViewById(R.id.iv_right);
        tv_title= (TextView) findViewById(R.id.tv_title);
        tv_right= (TextView) findViewById(R.id.tv_right);
    }

    protected int getTabsRootView() {
        return R.layout.activity_tabs;
    }

    protected void initTab(String[] titles, Fragment[] fragments) {
        if (titles == null || titles.length < 1 || fragments == null || titles.length != fragments.length)
            return;
        contentMap.clear();
        ArrayList<String> title = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            contentMap.put(i, fragments[i]);
            title.add(titles[i]);
        }
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyAdapter(getSupportFragmentManager(), title));
        tabs.setViewPager(pager);
        //为了不重复加载，开始就请求所有的
        pager.setOffscreenPageLimit(fragments.length);
    }


    public class MyAdapter extends FragmentPagerAdapter {
        ArrayList<String> _titles;

        public MyAdapter(FragmentManager fm, ArrayList<String> titles) {
            super(fm);
            _titles = titles;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return _titles.get(position);
        }

        @Override
        public int getCount() {
            return _titles.size();
        }

        @Override
        public Fragment getItem(int position) {
            return contentMap.get(position);
        }


    }

    public ViewPager getPager() {
        return pager;
    }
}
