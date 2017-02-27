package com.open.face2facemanager.utils;

/**
 * Created by Administrator on 2016/5/26.
 */
public class InputNullException extends Exception {

    public InputNullException() {
    }

    public InputNullException(String detailMessage) {
        super(detailMessage);
    }

    public InputNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public InputNullException(Throwable cause) {
        super(cause == null ? null : cause.toString(), cause);
    }
}
