package com.open.face2facemanager.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.common.view.ninegrid.ImageInfo;
import com.common.view.ninegrid.preview.ImagePreviewActivity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by onion on 2016/7/29.
 */
public class ScreenUtils {
    private static int windowHeight = -1;
    private static int windowWidth = -1;

    public static int getScreenHeight(Context mContext) {
        if (windowHeight == -1) {
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            windowHeight = wm.getDefaultDisplay().getHeight();
        }
        return windowHeight;
    }

    public static int getScreenWidth(Context mContext) {
        if (windowWidth == -1) {
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            windowWidth = wm.getDefaultDisplay().getWidth();
        }
        return windowWidth;
    }

    public static void forbidSceenCut(Activity activity) {
        Window win = activity.getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SECURE);
    }

    /**
     * 打开软键盘
     *
     * @param mContext 上下文
     */
    public static void openKeybord(EditText mEditText,Activity mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        mEditText.requestFocus();
    }

    /**
     * 关闭软键盘
     *
     * @param mContext 上下文
     */
    public static void closeKeybord(Activity mContext) {
        View view = mContext.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inputmanger.isActive())
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int statusHeight = -1;

    public static void onImgClick(Context context, String url, ImageView imageView) {
        if (statusHeight < 0) statusHeight = ScreenUtils.getStatusHeight(context);
        ImageInfo info = new ImageInfo();
        info.imageViewWidth = imageView.getWidth();
        info.imageViewHeight = imageView.getHeight();
        int[] points = new int[2];
        imageView.getLocationInWindow(points);
        info.imageViewX = points[0];
        info.imageViewY = points[1] - statusHeight;
        info.setUrl(url);
        ArrayList<ImageInfo> imageInfos = new ArrayList<>();
        imageInfos.add(info);
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) imageInfos);
        bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, 0);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    /**
     * 获得状态栏的高度
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
}
