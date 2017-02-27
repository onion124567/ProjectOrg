package com.open.face2facemanager.factory.bean.base;

/**
 * Created by onion on 2016/8/10.
 */
public class PagerAble<T> {
    public int pageNumber = 1;
    public int pageSize = 20;
    public T param;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public T getParam() {
        return param;
    }

    public void setParam(T param) {
        this.param = param;
    }
}
