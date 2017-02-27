package com.open.face2facemanager.factory.bean.base;

import java.util.List;

/**
 * 分页父类
 * 子类须指定 T，否则影响缓存
 * Created by onion on 2016/8/10.
 */
public class OpenListBean<T> {
    public  List<T> pager;

    public List<T> getPager() {
        return pager;
    }

    public void setPager(List<T> pager) {
        this.pager = pager;
    }

    @Override
    public String toString() {
        return "BroadSpeakListBean{" +
                "pager=" + pager +
                '}';
    }
}
