package com.open.face2facemanager.utils;

import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;

/**
 * Created by Administrator on 2016/7/21.
 */
public class AlarmUtil {
    public static void setAlarm(Context context) {
        Intent alarmas = new Intent(AlarmClock.ACTION_SET_ALARM);
        alarmas.putExtra(AlarmClock.EXTRA_MESSAGE, "New Alarm");
        alarmas.putExtra(AlarmClock.EXTRA_HOUR, 10);
        alarmas.putExtra(AlarmClock.EXTRA_MINUTES, 30);
        context.startActivity(alarmas);
    }
}
