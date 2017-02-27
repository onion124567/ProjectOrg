package com.open.face2facemanager.business.baseandcommon;


import com.open.face2facemanager.factory.bean.base.OpenResponse;

import rx.functions.Action2;

/**
 * Created by Administrator on 2016/7/20.
 */
public abstract class CacheAble<T1, T2> implements Action2<T1, T2> {
    public enum CacheType {
        None, Before, After
    }


    CacheType cacheType = CacheType.None;
    String cacheKey="cacheDefault";
    public Action2<T1,OpenResponse> onCache;
    public void setTypeAndKey(CacheType cacheType,String key,Action2 onCache) {
        this.cacheType = cacheType;
        cacheKey=key;
        this.onCache=onCache;
    }

    public CacheType getCacheType() {
        return cacheType;
    }
}
