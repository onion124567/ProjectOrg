package com.open.face2facemanager.business.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.open.face2facemanager.R;
import com.open.face2facemanager.business.baseandcommon.TApplication;
import com.open.face2facemanager.utils.Config;
import com.open.face2facemanager.utils.StrUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * 下载新版本及wps应用
 */
public class OBVersionDownloadService extends Service {
    private NotificationManager notificationMrg;
    public String appName = "教师秘书";
    private String mDownloadUrl;
    private String TAG = "onion";
    int flag = 99;
    File apkFile;

    public void onCreate() {
        super.onCreate();
        notificationMrg = (NotificationManager) this
                .getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        appName = getResources().getResourceName(R.string.app_name);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        System.out.println("Get intent:" + intent);
        // 注:此处url必须声明为final常量，否则是不会被子线程中读取到的
        mDownloadUrl = intent.getStringExtra("filePath");
        System.out.println("Get url from intent:" + mDownloadUrl);
        appName = intent.getStringExtra(Config.INTENT_String);
        final int appLogo = intent.getIntExtra(Config.INTENT_PARAMS1, R.mipmap.ic_logo);
//        Observable.just(mDownloadUrl).subscribe(new Action1<String>() {
//            @Override
//            public void call(String s) {
//
//            }
//        });
        loadFile(mDownloadUrl, appName, appLogo);
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // 状态栏视图更新
    private Notification displayNotificationMessage(int count) {
        RemoteViews contentView1 = notification.contentView;
        contentView1.setTextViewText(R.id.n_title, appName);
        contentView1.setTextViewText(R.id.n_text, "当前进度：" + count + "% ");
        contentView1.setProgressBar(R.id.n_progress, 100, count, false);
        notification.contentView = contentView1;
        // 提交一个通知在状态栏中显示。如果拥有相同标签和相同id的通知已经被提交而且没有被移除，该方法会用更新的信息来替换之前的通知。
        notificationMrg.notify(flag, notification);
        return notification;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    Notification notification;

    public void loadFile(final String url, final String appName, int appLogo) {
        Intent notificationIntent = new Intent(getApplicationContext(),
                this.getClass());
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // addflag设置跳转类型
        PendingIntent contentIntent = PendingIntent.getActivity(
                getApplicationContext(), 0, notificationIntent, 0);
        // 创建Notifcation对象，设置图标，提示文字
        long number = 100;
        notification = new Notification(appLogo,
                appName, number);// 设定Notification出现时的声音，一般不建议自定义
        // System.currentTimeMillis()
        notification.flags |= Notification.FLAG_ONGOING_EVENT;// 出现在 “正在运行的”栏目下面
        RemoteViews contentView1 = new RemoteViews(getPackageName(),
                R.layout.notify_updateprogress);
        contentView1.setTextViewText(R.id.n_title, "准备下载");
        contentView1.setTextViewText(R.id.n_text, "当前进度：" + 0 + "% ");
        contentView1.setProgressBar(R.id.n_progress, 100, 0, false);
        contentView1.setImageViewResource(R.id.iv_logo, appLogo);
        notification.contentView = contentView1;
        notification.contentIntent = contentIntent;
        double m = 0.0;
        Call<ResponseBody> call = TApplication.getServerAPI().downloadFileWithDynamicUrlSync(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,final  Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "server contacted and has file");
                    Log.i(TAG, "name" + Thread.currentThread().getName());

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean writtenToDisk = writeResponseBodyToDisk(response.body());
                            Log.d(TAG, "file download was a success? " + writtenToDisk);
                            if(!writtenToDisk){
                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                                        getApplicationContext())
                                        .setSmallIcon(R.mipmap.ic_logo)
                                        .setContentTitle(getResources().getString(R.string.app_name))
                                        .setContentText("下载失败");
                                mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                        R.mipmap.ic_logo));
                                mBuilder.setAutoCancel(true);//自己维护通知的消失
                                // 提交一个通知在状态栏中显示。如果拥有相同标签和相同id的通知已经被提交而且没有被移除，该方法会用更新的信息来替换之前的通知。
                                notificationMrg.notify(flag, mBuilder.build());
                            }else {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                Uri uri = Uri.fromFile(apkFile);
                                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                                        getApplicationContext())
                                        .setSmallIcon(R.mipmap.ic_logo)
                                        .setContentTitle(getResources().getString(R.string.app_name))
                                        .setContentText("下载完成");
                                mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                        R.mipmap.ic_logo));
                                mBuilder.setAutoCancel(true);
                                mBuilder.setContentIntent(pendingIntent);
                                //自己维护通知的消失
                                // 提交一个通知在状态栏中显示。如果拥有相同标签和相同id的通知已经被提交而且没有被移除，该方法会用更新的信息来替换之前的通知。
                                notificationMrg.notify(flag, mBuilder.build());
                            }
                        }
                    }).start();



                } else {
                    Log.d(TAG, "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "error");
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                        getApplicationContext())
                        .setSmallIcon(R.mipmap.ic_small_logo)
                        .setContentTitle(getResources().getString(R.string.app_name))
                        .setContentText("下载失败");
                mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_logo));
                mBuilder.setAutoCancel(true);//自己维护通知的消失
                // 提交一个通知在状态栏中显示。如果拥有相同标签和相同id的通知已经被提交而且没有被移除，该方法会用更新的信息来替换之前的通知。
                notificationMrg.notify(flag, mBuilder.build());
            }
        });


        notificationMrg.notify(flag, notification);
    }


    public void openfile(Uri url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(url, "application/vnd.android.package-archive");
        startActivity(intent);
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            apkFile = new File(Environment.getExternalStorageDirectory(),
                    appName + ".apk");
                       InputStream inputStream = null;
            OutputStream outputStream = null;
           int  count =0;
            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(apkFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    int progress=(int) (fileSizeDownloaded * 100.0f / fileSize);
                    if(progress>count){
                        count=progress;
                        displayNotificationMessage(count);
                    }

                }

                outputStream.flush();
               // TApplication.getInstance().showToast("下载成功");
                Uri uri = Uri.fromFile(apkFile);
                openfile(uri);
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
