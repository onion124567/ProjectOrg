package com.open.face2facemanager.utils;

import android.content.Context;

import com.baidu.mobstat.StatService;

/**
 * Created by onion on 2016/8/17.
 */
public class TongjiUtil {
    /**
     * 统计事件方法
     *
     * @param context
     * @param key
     * @param value
     */
    public static void tongJiOnEvent(Context context, String key, String value) {
        StatService.onEvent(context, key, value);
    }
}
