package com.open.face2facemanager.factory;

/**
 * Created by Administrator on 2016/6/13.
 */
public class PushNotice {
    public String pushTarget;
    public int pusherId;
    public int pType;
    public int jType;
    public String pusherName;
    /*enum Target{
            PushTo->messge_list;
        }*/
    public String getPushTarget() {
        return pushTarget;
    }

    public void setPushTarget(String pushTarget) {
        this.pushTarget = pushTarget;
    }

    public int getPusherId() {
        return pusherId;
    }

    public void setPusherId(int pusherId) {
        this.pusherId = pusherId;
    }

    public int getpType() {
        return pType;
    }

    public void setpType(int pType) {
        this.pType = pType;
    }

    public int getjType() {
        return jType;
    }

    public void setjType(int jType) {
        this.jType = jType;
    }

    public String getPusherName() {
        return pusherName;
    }

    public void setPusherName(String pusherName) {
        this.pusherName = pusherName;
    }
}
