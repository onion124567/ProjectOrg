package com.open.face2facemanager.utils;

import android.content.Context;
import android.content.Intent;

/**
 * Created by onion on 2016/7/26.
 */
public class PermissionUtil {
    /**
     * 开魅族手机的权限
     * @param context
     */
    public static void openMeiZuPermission(Context context) {
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("packageName", context.getPackageName());
        context.startActivity(intent);
    }
}
