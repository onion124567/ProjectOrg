package com.open.face2facemanager.factory.bean.base;

/**
 * Created by onion on 2016/8/10.
 */
public class TwoFrontPagerAble<T> {
    public Long anchor=null;
    public int pageSize = 20;
    public Direction direction = Direction.DOWN;
    public T param;

    public Long getAnchor() {
        return anchor;
    }

    public void setAnchor(Long anchor) {
        this.anchor = anchor;
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


    public enum Direction {
        DOWN, UP
    }

    @Override
    public String toString() {
        return "TwoFrontPagerAble{" +
                "anchor=" + anchor +
                ", pageSize=" + pageSize +
                ", direction=" + direction +
                ", param=" + param +
                '}';
    }
}
