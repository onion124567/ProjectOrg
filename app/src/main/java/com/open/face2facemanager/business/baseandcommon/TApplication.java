package com.open.face2facemanager.business.baseandcommon;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.common.view.imagepicker.ImagePicker;
import com.common.view.imagepicker.loader.ImageLoader;
import com.common.view.imagepicker.view.CropImageView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.google.gson.Gson;
import com.open.face2facemanager.BuildConfig;
import com.open.face2facemanager.factory.bean.UserInfoResponse;
import com.open.face2facemanager.utils.Config;
import com.open.face2facemanager.utils.ImagePipelineConfigUtils;
import com.open.face2facemanager.utils.PreferencesHelper;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class TApplication extends Application {

    private static ServerAPI serverAPI;
    private static TApplication instance;
    private Location location;
    private ArrayList<Activity> mList = new ArrayList<>();
    public static Gson gson = new Gson();
    public static String packageName;
    private ImagePicker imagePicker;
    public String token;
    public long userId;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        packageName = getPackageName();
        ActiveAndroid.initialize(this);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        initHttpClient();
        ImagePipelineConfig config = ImagePipelineConfigUtils.getDefaultImagePipelineConfig(this);
      /*  ImagePipelineConfig config  = ImagePipelineConfig.newBuilder(this)
                .setProgressiveJpegConfig(pjpegConfig)
                .setBitmapMemoryCacheParamsSupplier(bitmapCacheParamsSupplier)
                .setCacheKeyFactory(cacheKeyFactory)
                .setEncodedMemoryCacheParamsSupplier(encodedCacheParamsSupplier)
                .setExecutorSupplier(executorSupplier)
                .setImageCacheStatsTracker(imageCacheStatsTracker)
                .setMainDiskCacheConfig(mainDiskCacheConfig)
                .setMemoryTrimmableRegistry(memoryTrimmableRegistry)
                .setNetworkFetchProducer(networkFetchProducer)
                .setPoolFactory(poolFactory)
                .setProgressiveJpegConfig(progressiveJpegConfig)
                .setRequestListeners(requestListeners)
                .setSmallImageDiskCacheConfig(smallImageDiskCacheConfig)
                .build();*/
        Fresco.initialize(this, config);
        initImagePicker();
        initX5Web();
    }

    private void initX5Web() {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                Log.e("apptbs", " onViewInitFinished is " + arg0);
            }
            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub

            }
        };
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                Log.d("apptbs", "onDownloadFinish");
            }

            @Override
            public void onInstallFinish(int i) {
                Log.d("apptbs", "onInstallFinish");
            }

            @Override
            public void onDownloadProgress(int i) {
                Log.d("apptbs", "onDownloadProgress:" + i);
            }
        });
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }


    //登陆成功，退出后，都调用此方法，用来刷新token
    public void initHttpClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerAPI.ENDPOINT)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(genericClient())
                .build();
        serverAPI = retrofit.create(ServerAPI.class);
        UserInfoResponse loginbean = PreferencesHelper.getInstance().getBean(UserInfoResponse.class);
        if (PreferencesHelper.getInstance().isLogin() && loginbean != null) {
            try {
//                token = loginbean.getToken();
//                request = new UserIDRequest();
//                request.setUserId(loginbean.getUser().getIdentification());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void initImagePicker() {
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
                if (imageView instanceof SimpleDraweeView) {
                    SimpleDraweeView simpleDraweeView = (SimpleDraweeView) imageView;
                    simpleDraweeView.setImageURI(Uri.parse(path + ""));
                } else {
                    if (path.contains("http")) {
                        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(path, imageView, Config.circleIconDefault);
                    } else {
                        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage("file://" + path, imageView, Config.circleIconDefault);
                    }
                }

            }

            @Override
            public void clearMemoryCache() {

            }
        });   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(9);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素

    }

    public void changePickerMode(boolean isMultiMode, int selectLimit) {
        imagePicker.setMultiMode(isMultiMode);
        imagePicker.setCrop(!isMultiMode);
        imagePicker.setSelectLimit(selectLimit);
    }

    public OkHttpClient genericClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {//测试版本看log
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {//线上版本 看log会影响传图
            logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                /*MediaType mediaType = request.body().contentType();
                try {
                    Field field = mediaType.getClass().getDeclaredField("mediaType");
                    field.setAccessible(true);
                    field.set(mediaType, "application/json");
//                    field.set("latait","0.0");
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }*/
                TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                Request compressedRequest = originalRequest.newBuilder()
//                        .header("Content-Encoding", "gzip")
                        .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                        .addHeader("device", "android")
                        .addHeader("imei", tm.getDeviceId())
                        .addHeader("User-Agent", "teacher")
                        .addHeader("version", getVersion())
                        .addHeader("push_token", JPushInterface.getRegistrationID(instance))
//                        .method(originalRequest.method(), gzip(originalRequest.body()))
                        .build();
                return chain.proceed(compressedRequest);
            }
        }).addInterceptor(logging);
//        setCertificates(builder,new Buffer()
//                .writeUtf8("sdadsada")
//                .inputStream());
        return builder.build();
    }

    //证书 现在服务端没做验证
    public void setCertificates(  OkHttpClient.Builder builder,InputStream... certificates){
        try
        {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates)
            {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

                try
                {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e)
                {
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            sslContext.init
                    (
                            null,
                            trustManagerFactory.getTrustManagers(),
                            new SecureRandom()
                    );
            builder.sslSocketFactory(sslContext.getSocketFactory(),(X509TrustManager)trustManagerFactory.getTrustManagers()[0]);


        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    /**
     * 做gzip压缩
     *
     * @param body
     * @return
     */
    private RequestBody gzip(final RequestBody body) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return body.contentType();
            }

            @Override
            public long contentLength() {
                return -1; // 无法知道压缩后的数据大小
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                body.writeTo(gzipSink);
                gzipSink.close();
            }
        };
    }

    public static ServerAPI getServerAPI() {
        return serverAPI;
    }

    public static TApplication getInstance() {
        return instance;
    }

    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public boolean removeActivity(Activity activity) {
        return mList.remove(activity);
    }

    public boolean isActivityIn(Activity acitivity) {
        return mList.contains(acitivity);
    }

    public void exit() { // 遍历List，退出每一个Activity
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
            mList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        PreferencesHelper.getInstance().clearBean(UserInfoResponse.class);
        PreferencesHelper.getInstance().saveLogin(false);
        UserInfoResponse phoneLoginResponse = PreferencesHelper.getInstance().getBean(UserInfoResponse.class);
        Log.i("onion", "phoneLoginResponse" + phoneLoginResponse);
        userId=0;
        token = null;
        //QQlogout
//        PreferencesHelper.getInstance().saveUserLoginName("");
//		finally {//2.5.3版本以前退出是直接推出软件的，后来进入登录注册页，此段代码注释
//			System.exit(0);
//			MobclickAgent.onKillProcess(this);
//		}
    }

    public void startLogin() {
       /* Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);*/
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
        instance = null;
    }


    public String getVersion() {
        PackageManager manager = this.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (packageInfo != null) {
//            return packageInfo.versionCode;
            return packageInfo.versionName;
        }
        return "1.0";
    }

    public int getVersionCode() {
        PackageManager manager = this.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (packageInfo != null) {
//            return packageInfo.versionCode;
            return packageInfo.versionCode;
        }
        return 1;
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
