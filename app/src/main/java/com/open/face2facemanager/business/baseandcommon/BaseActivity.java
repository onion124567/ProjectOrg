package com.open.face2facemanager.business.baseandcommon;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.common.view.PresenterLifecycleDelegate;
import com.common.view.ScrollViewWithListener;
import com.common.view.ViewWithPresenter;
import com.common.view.shortcutbadger.ShortcutBadger;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.open.face2facemanager.R;
import com.open.face2facemanager.business.service.OBVersionDownloadService;
import com.open.face2facemanager.factory.PresenterFactory;
import com.open.face2facemanager.factory.ReflectionPresenterFactory;
import com.open.face2facemanager.factory.bean.VersionInfo;
import com.open.face2facemanager.presenter.Presenter;
import com.open.face2facemanager.utils.Config;
import com.open.face2facemanager.utils.ImageLoaderConfig;
import com.open.face2facemanager.utils.ImageUtils;
import com.open.face2facemanager.utils.ScreenUtils;
import com.open.face2facemanager.utils.TongjiUtil;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import rx.Observable;
import rx.functions.Action1;


/**
 * This class is an example of how an activity could controls it's presenter.
 * You can inherit from this class or copy/paste this class's code to
 * create your own view implementation.
 * <p>
 * copy from com.common.view NucleusActivity   @2016.5.9 by onion
 *
 * @param <P> a type of presenter to return with {@link #getPresenter}.
 */
public abstract class BaseActivity<P extends Presenter> extends AppCompatActivity implements ViewWithPresenter<P> {

    private static final String PRESENTER_STATE_KEY = "presenter_state";

    private PresenterLifecycleDelegate<P> presenterDelegate =
            new PresenterLifecycleDelegate<>(ReflectionPresenterFactory.<P>fromViewClass(getClass()));

    /**
     * Returns a current presenter factory.
     */
    public PresenterFactory<P> getPresenterFactory() {
        return presenterDelegate.getPresenterFactory();
    }

    /**
     * Sets a presenter factory.
     * Call this method before onCreate/onFinishInflate to override default {@link ReflectionPresenterFactory} presenter factory.
     * Use this method for presenter dependency injection.
     */
    @Override
    public void setPresenterFactory(PresenterFactory<P> presenterFactory) {
        presenterDelegate.setPresenterFactory(presenterFactory);
    }

    /**
     * Returns a current attached presenter.
     * This method is guaranteed to return a non-null value between
     * onResume/onPause and onAttachedToWindow/onDetachedFromWindow calls
     * if the presenter factory returns a non-null value.
     *
     * @return a currently attached presenter or null.
     */
    public P getPresenter() {
        return presenterDelegate.getPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TApplication.getInstance().addActivity(this);
        if (getIntent().getBooleanExtra(Config.INTENT_TONGJI, false))
            TongjiUtil.tongJiOnEvent(this, "id_pushinapp", "");
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (!ImageLoader.getInstance().isInited()) {
            ImageLoaderConfig.initImageLoader(this, Config.BASE_IMAGE_CACHE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    View view = getWindow().getDecorView();
                    ImageUtils.WindowHeight = view.getHeight();
                    ImageUtils.WindowWidth = view.getWidth();
                }
            }, 1000);
        }
        if (savedInstanceState != null) {
            //删除上次的fragment
            String FRAGMENTS_TAG = "Android:support:fragments";
            savedInstanceState.remove(FRAGMENTS_TAG);
            presenterDelegate.onRestoreInstanceState(savedInstanceState.getBundle(PRESENTER_STATE_KEY));
        }

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        //5.0以后展示沉浸状态栏，避免页面被状态栏覆盖做一下操作
        ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= 14) {
            parentView.setFitsSystemWindows(true);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(PRESENTER_STATE_KEY, presenterDelegate.onSaveInstanceState());
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
        //删掉红点
        boolean success = ShortcutBadger.removeCount(this);
        presenterDelegate.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
        presenterDelegate.onPause(isFinishing());
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    protected Toolbar toolbar;

    protected void initTitle(String title) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar == null) return;
        TextView titleView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titleView.setText(title);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationIcon(R.mipmap.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenUtils.closeKeybord(BaseActivity.this);
                finish();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    public void showToast(int id) {
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
    }

    public void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }


    /**
     * 積分動畫 通過Subscriber返回
     *
     * @param scoer
     * @param sub
     */
    protected void scoerChangeAnimation(final double scoer, final Action1<Double> sub) {
        double begin = scoer / 3;
        final double i = scoer / 3 * 2 / 20;
        for (double d = 0; d < 20; d++) {
            double v = begin + i * d;
        }
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Observable.just((double) msg.what).subscribe(sub);
                if (msg.what + i < scoer)
                    sendEmptyMessageDelayed((int) (msg.what + i), 30);
                else {
                    Observable.just(scoer).subscribe(sub);
                }
            }
        };
        handler.sendEmptyMessage((int) begin);
    }

    /**
     * 通過listview的headview計算距離，講應該漸變的色值 及headview是否顯示
     * 通過Subscriber返回
     *
     * @param listView
     * @param sub
     */
    public AbsListView.OnScrollListener initListviewHeadAndTopColor(final View headView, ListView listView, final Action1<Integer> colorSub, final Action1<Boolean> neeVisable) {
        AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {//headview顯示
                    if (!isHeadViewVisable) {
                        isHeadViewVisable = true;
                    }
                    //考慮漸變
                    //1計算比率
                    double privet = (headView.getHeight() + headView.getY()) * 1D / headView.getHeight();
                    //r 色值
                    double r = 240 + (225 - 240) * privet;
                    double g = 84 + (77 - 84) * privet;
                    double b = 80 + (46 - 80) * privet;
                    int color = Color.rgb((int) r, (int) g, (int) b);

                    Observable.just(color).subscribe(colorSub);
                } else if (firstVisibleItem == 1) {
                    if (isHeadViewVisable) {
                        isHeadViewVisable = false;
                    }
                }
                Observable.just(isHeadViewVisable).subscribe(neeVisable);
            }
        };
        listView.setOnScrollListener(scrollListener);
        return scrollListener;
    }

    /**
     * 通過listview的headview計算距離，講應該漸變的色值 及headview是否顯示
     * 通過Subscriber返回
     */
    public void initScrollHeadAndTopColor(final View headView, final View title, final ScrollViewWithListener scrollView, final Action1<Integer> colorSub, final Action1<Boolean> neeVisable) {
        scrollView.setOnScrollListener(new ScrollViewWithListener.OnScrollListener() {
                                           @Override
                                           public void onScroll(int scrollY) {
                                               if (headView.getHeight() - title.getHeight() >= scrollY) {//headview顯示
                                                   if (!isHeadViewVisable) {
                                                       isHeadViewVisable = true;

                                                   }
                                                   //考慮漸變
                                                   //1計算比率
                                                   double privet = scrollY * 1D / (headView.getHeight() - title.getHeight());
                                                   //r 色值
                                                   double r = 225 + (240 - 225) * privet;
                                                   double g = 77 + (84 - 77) * privet;
                                                   double b = 46 + (80 - 46) * privet;
                                                   int color = Color.rgb((int) r, (int) g, (int) b);
                                                   Observable.just(color).subscribe(colorSub);
                                               } else {
                                                   if (isHeadViewVisable) {
                                                       isHeadViewVisable = false;
                                                   }
                                               }
                                               Observable.just(isHeadViewVisable).subscribe(neeVisable);

                                           }
                                       }
        );

    }

    public void initScrollHeadAndTopAlpha(final View headView, final View title, final ScrollViewWithListener scrollView, final Action1<Integer> colorSub, final Action1<Boolean> neeVisable) {
        scrollView.setOnScrollListener(new ScrollViewWithListener.OnScrollListener() {
                                           @Override
                                           public void onScroll(int scrollY) {
                                               if (headView.getHeight() - title.getHeight() >= scrollY) {//headview顯示
                                                   if (!isHeadViewVisable) {
                                                       isHeadViewVisable = true;

                                                   }
                                                   //考慮漸變
                                                   //1計算比率
                                                   float color = scrollY * 1f / (headView.getHeight() - title.getHeight()) * 255;
                                                   Observable.just((int) color).subscribe(colorSub);
                                               } else {
                                                   if (isHeadViewVisable) {
                                                       isHeadViewVisable = false;
                                                   }
                                               }
                                               Observable.just(isHeadViewVisable).subscribe(neeVisable);

                                           }
                                       }
        );
    }

    //    private int titile_alpha_down=255;
//    private int titile_alpha_up=255;
//    private int titile_red_down=255;
//    private int titile_red_up=255;
//    private int titile_green_down=255;
//    private int titile_green_up=255;
//    private int titile_blue_down=255;
//    private int titile_blue_up=255;
    boolean isHeadViewVisable = true;
    int main_red = 0xffe14d2e;//225,77,46
    int main_light_red = 0xfff05450;//240,84,80


    /**
     * 下拉刷新模塊
     */
    protected PtrClassicFrameLayout mPtrFrame;

    /**
     * for loadmore
     *
     * @param action1
     * @param contentView
     */
    protected void initPtrFrameLayout(final Action1<String> action1, final View contentView) {
        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.ptr_layout);
//        StoreHouseHeader header = new StoreHouseHeader(this);
//        header.setPadding(0, 40, 0, 40);
//        header.initWithString("ZHAOSHENG");
        // header
        final MaterialHeader header = new MaterialHeader(this);
//        final OpenDIYHeader header = new OpenDIYHeader(this);
        int[] colors = getResources().getIntArray(R.array.loading_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, ScreenUtils.dip2px(this, 15), 0, ScreenUtils.dip2px(this, 10));
        header.setPtrFrameLayout(mPtrFrame);
        mPtrFrame.setDurationToClose(100);
        mPtrFrame.setPinContent(false);
        mPtrFrame.setHeaderView(header);
        mPtrFrame.addPtrUIHandler(header);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, contentView == null ? content : contentView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                action1.call("用戶下拉了");
            /*    mPtrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    }
                }, 1500);*/
            }
        });
        // the following are default settings
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        // default is false
        mPtrFrame.setPullToRefresh(false);
        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);
    }

    protected void initPtrFrameLayout(final Action1<String> action1) {
        initPtrFrameLayout(action1, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TApplication.getInstance().removeActivity(this);
    }


    /**
     * 处理版本更新
     *
     * @param response
     */
    public void handVersionUpdate(VersionInfo info) {

        PackageManager manager = this.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (packageInfo != null) {
            int version_num = packageInfo.versionCode;
            // 如果当前的版本号小于服务器上的版本号，则弹出更新对话框
            if (info.getiVersion() > version_num) {
                showVersionUpdateDialog(info);
            } else {
            }
        }
    }

    /**
     * @param versionInfo
     */
    public void showVersionUpdateDialog(VersionInfo versionInfo) {
//        mApkUrl = versionInfo.jUpdateUrl;
//        UIUtils.getInstance().showVersionUpdateDialog(this,
//                OBUtil.getString(this, R.string.ob_version_update_title),
//                versionInfo.jVerDes, mUpdateClickListener, versionInfo.force);
    }

    private DialogInterface.OnClickListener mUpdateClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            // TODO Auto-generated method stub
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    // 更新开始跳转，即要计算课程积分，如果不跳转，不计算课程积分
                    // Log.d(TAG, "The update type is exit");
                    // integralCourse();
                    dialog.cancel();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.cancel();
                    break;
            }
        }
    };

    protected void downNewVersion(String mApkUrl) {
//        mApkUrl="http://tongxue.open.com.cn:17020/2016/0721/18/apk20160721183627781rin.apk";
        Intent updateIntent = new Intent(BaseActivity.this,
                OBVersionDownloadService.class);
        // updateIntent.setData(Uri.parse(Constants.VERSION_PATH));
        // updateIntent.putExtra("filePath", Constants.VERSION_PATH);

        updateIntent.setData(Uri.parse(mApkUrl));
        updateIntent.putExtra("filePath", mApkUrl);
        updateIntent.putExtra(Config.INTENT_String, getResources().getString(R.string.app_name));
        startService(updateIntent);

    }


}
