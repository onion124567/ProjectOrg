package com.open.face2facemanager.utils;

import android.os.CountDownTimer;

import rx.functions.Action1;

/**
 * Created by Administrator on 2016/5/26.
 */
public class CountDownTimeUtil {
    /**
     * 默认60秒的 倒计时回调
     *
     * @param countDownTimer
     */
    public static void start(Action1<Integer> countDownTimer) {
        start(60000, countDownTimer);
    }

    /**
     * @param totalTime      总倒计时
     * @param countDownTimer 倒计时每秒回调
     */
    public static void start(int totalTime, final Action1<Integer> countDownTimer) {
        new CountDownTimer(totalTime, 1000) {
            @Override
            public void onFinish() {//计时完毕时触发
                countDownTimer.call(0);
            }

            @Override
            public void onTick(long millisUntilFinished) {//计时过程显示
                countDownTimer.call((int) (millisUntilFinished / 1000));
            }
        }.start();
    }
}
