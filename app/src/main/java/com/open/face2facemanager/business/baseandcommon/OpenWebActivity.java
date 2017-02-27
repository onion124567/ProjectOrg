package com.open.face2facemanager.business.baseandcommon;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;

import com.open.face2facemanager.R;
import com.open.face2facemanager.utils.MyWebViewClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

@SuppressLint("SetJavaScriptEnabled")
public class OpenWebActivity extends BaseActivity {

    protected WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guide);
        mWebView = (WebView) this.findViewById(R.id.webview);
        if (savedInstanceState != null) {
            mWebView.restoreState(savedInstanceState);
        }
        initWebView();
    }

    private void initWebView() {

        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(false);
        mWebView.setWebViewClient(new MyWebViewClient(this));
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        //        mWebView.setWebChromeClient(new MyWebChromeClient());
//        mWebView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        // 设置能响应超链接
//        mWebView.setWebViewClient(new MyWebViewClient(this){
//            public void onPageFinished(WebView view, String url) {
//
//                Log.d("WebView", "onPageFinished ");
//                view.loadUrl("javascript:window.local_obj.showSource('<head>'+" +
//                        "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
//                super.onPageFinished(view, url);
//
//            }
//        });
    }
    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
           // IOUtils.printLongString(html);
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack(); // goBack()表示返回WebView的上一页面
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.loadData("", "text/html; charset=UTF-8", null);
    }
}
