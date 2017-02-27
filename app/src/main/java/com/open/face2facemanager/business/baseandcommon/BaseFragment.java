package com.open.face2facemanager.business.baseandcommon;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.common.view.PresenterLifecycleDelegate;
import com.common.view.ViewWithPresenter;
import com.open.face2facemanager.R;
import com.open.face2facemanager.factory.PresenterFactory;
import com.open.face2facemanager.factory.ReflectionPresenterFactory;
import com.open.face2facemanager.presenter.Presenter;
import com.open.face2facemanager.utils.ScreenUtils;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/5/16.
 */
public class BaseFragment<P extends Presenter> extends Fragment implements ViewWithPresenter<P> {

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
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null)
            presenterDelegate.onRestoreInstanceState(bundle.getBundle(PRESENTER_STATE_KEY));
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBundle(PRESENTER_STATE_KEY, presenterDelegate.onSaveInstanceState());
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onResume(this);
        presenterDelegate.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
        presenterDelegate.onPause(getActivity().isFinishing());
    }

    public void showToast(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }
    public void showToast(int ids) {
        Toast.makeText(getActivity(), ids, Toast.LENGTH_SHORT).show();
    }

    /**
     * 下拉刷新模塊
     */
    protected PtrClassicFrameLayout mPtrFrame;

    /**
     * for loadmore
     * @param action1
     * @param contentView
     */
    protected void initPtrFrameLayout(final Action1<String> action1,final View contentView) {
        mPtrFrame = (PtrClassicFrameLayout) getView().findViewById(R.id.ptr_layout);
//        StoreHouseHeader header = new StoreHouseHeader(this);
//        header.setPadding(0, 40, 0, 40);
//        header.initWithString("ZHAOSHENG");
        // header
        final MaterialHeader header = new MaterialHeader(getActivity());
//        final OpenDIYHeader header = new OpenDIYHeader(getActivity());
        int[] colors = getResources().getIntArray(R.array.loading_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, ScreenUtils.dip2px(getActivity(), 15), 0, ScreenUtils.dip2px(getActivity(), 10));
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

}
