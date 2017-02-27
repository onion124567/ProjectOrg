package com.open.face2facemanager.factory;


/**
 * Created by onion on 2016/7/27.
 */
public class UserIDRequest {
    private long userId;

    public UserIDRequest(long userId) {
        this.userId = userId;
    }

    public UserIDRequest() {
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
