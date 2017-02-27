package com.open.face2facemanager.factory;

/**
 * Created by onion on 2016/7/27.
 */
public class ToUserIDRequest {
    private long toUserId;

    public ToUserIDRequest(long toUserId) {
        this.toUserId = toUserId;
    }

    public ToUserIDRequest() {
    }

    public long getToUserId() {
        return toUserId;
    }

    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }
}
