package com.open.face2facemanager.business.baseandcommon;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.open.face2facemanager.factory.bean.base.OpenResponse;
import com.open.face2facemanager.presenter.RxPresenter;
import com.open.face2facemanager.utils.ACache;
import com.open.face2facemanager.utils.StrUtils;

import org.json.JSONObject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Created by onion on 2016/5/10.
 * for cache
 * <View>  type View's className and restartableId for cache key
 * <T>  T's Gsonformate is cache value
 */
public class MPresenter<View> extends RxPresenter<View> {
    private String mPresenterName = getClass().getName();

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
    }

    @Override
    protected void onTakeView(View view) {
        super.onTakeView(view);

    }

    /**
     * 联网在后台，回调在UI   添加缓存
     *
     * @param restartableId     an id of the restartable.
     * @param observableFactory a factory that should return an Observable when the restartable should run.
     * @param onNext            a callback that will be called when received data should be delivered to view.
     * @param onError           a callback that will be called if the source observable emits onError.
     * @param <T>
     */
    public <T> void restartableLatestCache(final int restartableId, final Func0<Observable<T>> observableFactory, final CacheAble<View, T> onNext, @Nullable final CacheAble<View, Throwable> onError, CacheAble.CacheType type, Action2<View, OpenResponse> onCache) {
        initCache(restartableId, onNext, onError, type, onCache);
        restartable(restartableId, new Func0<Subscription>() {
            @Override
            public Subscription call() {
                return observableFactory.call()
                        .compose(MPresenter.this.<T>deliverLatestCache()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(split(onNext, onError));
            }
        });

    }

    /**
     * 不带缓存的 联网在后台 展示在UI线程
     *
     * @param restartableId     an id of the restartable.
     * @param observableFactory a factory that should return an Observable when the restartable should run.
     * @param onNext            a callback that will be called when received data should be delivered to view.
     * @param onError           a callback that will be called if the source observable emits onError.
     * @param <T>
     */
    @Override
    public <T> void restartableFirst(int restartableId, final Func0<Observable<T>> observableFactory,
                                     final Action2<View, T> onNext, @Nullable final Action2<View, Throwable> onError) {
        restartable(restartableId, new Func0<Subscription>() {
            @Override
            public Subscription call() {
                return observableFactory.call()
                        .compose(MPresenter.this.<T>deliverFirst()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(split(onNext, onError));
            }
        });
    }

    /**
     * @param restartableId
     * @param observableFactory
     * @param onNext
     * @param onError
     * @param type
     * @param onCache
     * @param <T>
     */
    public <T> void restartableFirst(int restartableId, final Func0<Observable<T>> observableFactory,
                                     final CacheAble<View, T> onNext, @Nullable final CacheAble<View, Throwable> onError, CacheAble.CacheType type, Action2<View, OpenResponse> onCache) {
        initCache(restartableId, onNext, onError, type, onCache);

        restartable(restartableId, new Func0<Subscription>() {
            @Override
            public Subscription call() {
                return observableFactory.call()
                        .compose(MPresenter.this.<T>deliverFirst()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(split(onNext, onError));
            }
        });
    }

    /**
     * 缓存模块
     */

    private <T> void initCache(int restartableId, final CacheAble<View, T> onNext, @Nullable final CacheAble<View, Throwable> onError, CacheAble.CacheType type, @Nullable final Action2<View, OpenResponse> onCache) {
        if (onCache == null || type == CacheAble.CacheType.None) return;
        final String key = mPresenterName + restartableId;
        if (type == CacheAble.CacheType.Before) {
            doInView(new Action1<View>() {
                @Override
                public void call(View view) {
                    OpenResponse response = getCache(key);
                    if (response != null)
                        onCache.call(view, response);

                }
            });

        }
        onNext.setTypeAndKey(type, key, onCache);
        onError.setTypeAndKey(type, key, onCache);
    }

    /**
     * 获取cache的时候
     *
     * @param restartableId
     */
    public OpenResponse getCache(String cacheKey) {
        //get cache
        JSONObject cache = ACache.get(TApplication.getInstance()).getAsJSONObject(StrUtils.string2md5(cacheKey));
        if (cache == null) return null;
        return TApplication.gson.fromJson(cache.toString(), OpenResponse.class);
    }


    protected void doInView(Action1<View> action1) {
        view().take(1).subscribe(action1);
    }
}
