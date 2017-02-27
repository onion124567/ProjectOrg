package com.open.face2facemanager.utils;

import android.app.Activity;
import android.util.Log;

import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.lang.ref.SoftReference;

/**
 * 统一处理web里点击事件的client
 */
public class MyWebViewClient extends WebViewClient {

    SoftReference<Activity> contextSoftReference;

    public MyWebViewClient(Activity activity) {
        this.contextSoftReference = new SoftReference<Activity>(activity);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Activity activity= contextSoftReference.get();
        if(null==activity){
            return true;
        }
        //通过url取值
        //判断是否load
        Log.i("onion", "url=====================" + url);
        if (url.contains("com_open_tongxue?")) {
            view.loadUrl(url);
        } else
            view.loadUrl(url);
        return true;
    }

}
