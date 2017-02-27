package com.open.face2facemanager.utils;

/**
 * Created by Administrator on 2016/5/26.
 */
public class DateErrorException extends RuntimeException {

    public DateErrorException() {
    }

    public DateErrorException(String detailMessage) {
        super(detailMessage);
    }

    public DateErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public DateErrorException(Throwable cause) {
        super(cause == null ? null : cause.toString(), cause);
    }
}
