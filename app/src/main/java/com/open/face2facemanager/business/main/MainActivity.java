package com.open.face2facemanager.business.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.view.jeremyfeinstein.slidingmenu.SlidingActivityBase;
import com.common.view.jeremyfeinstein.slidingmenu.SlidingActivityHelper;
import com.common.view.jeremyfeinstein.slidingmenu.SlidingMenu;
import com.open.face2facemanager.R;
import com.open.face2facemanager.business.baseandcommon.SlidingFragmentActivity;
import com.open.face2facemanager.factory.bean.VersionInfo;
import com.open.face2facemanager.utils.DialogManager;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends SlidingFragmentActivity implements SlidingActivityBase {

    private SlidingActivityHelper mHelper;

    FragmentManager fragmentManager;
    private ViewGroup[] mTabs;
    private Fragment[] fragments;
    private int currentTabIndex = 0;
    private int[] menuicon = new int[]{R.drawable.navigation_main, R.drawable.navigation_msg, R.drawable.navigation_group, R.drawable.navigation_mine};
    private String[] menutext = new String[]{"首页", "消息", "师友说", "我"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SlidingActivityHelper(this);
        mHelper.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSlidingMenu(savedInstanceState);
        initView();
    }

    /**
     * 初始化侧边栏
     */
    private void initSlidingMenu(Bundle savedInstanceState) {
        // 如果保存的状态不为空则得到之前保存的Fragment，否则实例化MyFragment
        // 设置左侧滑动菜单
        setBehindContentView(R.layout.menu_frame_left);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, new LeftFragment()).commit();

        // 实例化滑动菜单对象
        SlidingMenu sm = getSlidingMenu();
        // 设置可以左右滑动的菜单
        sm.setMode(SlidingMenu.LEFT);
        // 设置滑动阴影的宽度
        sm.setShadowWidthRes(R.dimen.shadow_width);
        // 设置滑动菜单阴影的图像资源
        sm.setShadowDrawable(null);
        // 设置滑动菜单视图的宽度
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        sm.setFadeDegree(0.3f);
        // 设置触摸屏幕的模式,这里设置为全屏
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        // 设置下方视图的在滚动时的缩放比例
        sm.setBehindScrollScale(0.0f);

    }


    public boolean isBind = false;

    @Override
    protected void onResume() {
        super.onResume();
        if (!isBind && !TextUtils.isEmpty(JPushInterface.getRegistrationID(this))) {

            // getPresenter().bindUser(JPushInterface.getRegistrationID(this));

        }
    }

    private void initView() {
        //指导操作图的准备代码
//        ll_gui= (LinearLayout) findViewById(R.id.ll_gui);
//
//        ll_gui.addView(mSampleView);
        mTabs = new ViewGroup[]{
                (ViewGroup) findViewById(R.id.mainNavigation)
                , (ViewGroup) findViewById(R.id.messageNavigation)
                , (ViewGroup) findViewById(R.id.groupNavigation)
                , (ViewGroup) findViewById(R.id.moreNavigation)
        };
        fragments = new Fragment[]{
                new TestFragment(),
                new TestFragment(),
                new TestFragment(),
                new TestFragment()
        };
        for (int i = 0; i < mTabs.length; i++) {
            mTabs[i].setOnClickListener(onClickListener);
            ImageView icon = (ImageView) mTabs[i].findViewById(R.id.navigationIcon);
            icon.setBackgroundResource(menuicon[i]);
            TextView text = (TextView) mTabs[i].findViewById(R.id.navigationText);
            text.setText(menutext[i]);
        }
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (int i = 0; i < fragments.length; i++) {
            transaction.add(R.id.fr_container, fragments[i]);
            transaction.hide(fragments[i]);
        }
        transaction.show(fragments[0]);
        transaction.commit();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = 0;
            switch (v.getId()) {
                case R.id.mainNavigation:
                    index = 0;
                    break;
                case R.id.messageNavigation:
                    index = 1;
                    break;
                case R.id.groupNavigation:
                    index = 2;
                    break;
                case R.id.moreNavigation:
                    index = 3;
                    break;
            }

            if (currentTabIndex != index) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.hide(fragments[currentTabIndex]);
                transaction.show(fragments[index]);
                transaction.commit();
                updateFocusOutView(currentTabIndex);
                updateFocusView(index);
            }
//        mTabs[currentTabIndex].setSelected(false);

//        mTabs[index].setSelected(true);

            currentTabIndex = index;
        }
    };

    public void updatePoint(int index, int visabllity) {
        mTabs[index].findViewById(R.id.groupNew).setVisibility(visabllity);
    }

    /**
     * 更新选中底部导航栏
     *
     * @param index
     */
    private void updateFocusView(int index) {
        final ImageView icon = (ImageView) mTabs[index].findViewById(R.id.navigationIcon);
        TextView text = (TextView) mTabs[index].findViewById(R.id.navigationText);
        icon.setSelected(true);
        ViewCompat.animate(icon)
                .rotationYBy(360)
//                .alpha(1.0f)
                .setDuration(400)
//                .setInterpolator(LINEAR_OUT_SLOW_IN_INTERPOLATOR)
                .setListener(listener)
                .start();
        text.setSelected(true);

    }

    public static final LinearOutSlowInInterpolator LINEAR_OUT_SLOW_IN_INTERPOLATOR = new LinearOutSlowInInterpolator();

    ViewPropertyAnimatorListener listener = new ViewPropertyAnimatorListener() {
        @Override
        public void onAnimationStart(View view) {
            ((View) view.getParent().getParent()).setClickable(false);
        }

        @Override
        public void onAnimationEnd(View view) {
            ((View) view.getParent().getParent()).setClickable(true);
        }

        @Override
        public void onAnimationCancel(View view) {
            ((View) view.getParent().getParent()).setClickable(true);
        }
    };

    /**
     * 更新未选中底部导航栏
     *
     * @param index
     */
    private void updateFocusOutView(int index) {
        ImageView icon = (ImageView) mTabs[index].findViewById(R.id.navigationIcon);
        TextView text = (TextView) mTabs[index].findViewById(R.id.navigationText);
        icon.setSelected(false);
        text.setSelected(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onNewVersion(final VersionInfo versionInfo) {

        DialogManager.showUpdateDialog(this, "版本更新", versionInfo.getDescription(), "立即更新", "free".equals(versionInfo.getType()), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                downNewVersion(versionInfo.getDownloadUrl());
                dialogInterface.dismiss();
            }
        });

    }

}
