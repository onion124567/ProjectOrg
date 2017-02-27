package com.open.face2facemanager.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.telephony.TelephonyManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.functions.Action1;


/**
 * Created by Administrator on 2016/2/19.
 */
public class SMSUtil {
    public String ImeI = "";

    /**
     *
     * @param context
     * @param handler    收到验证码的操作
     */
    public SMSUtil(Context context, Action1<String> smsAction) {
        this.smsAction = smsAction;
        SmsObserver smsObserver = new SmsObserver(context);
        TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        ImeI = tm.getDeviceId();
        context.getContentResolver().registerContentObserver(SMS_INBOX, true, smsObserver);
    }
    public Action1 smsAction =null;
//    public Handler smsHandler = new Handler() {
//        // 这里可以进行回调的操作
//        // TODO
//        public void handleMessage(android.os.Message msg) {
//            System.out.println("smsHandler 执行了.....");
//        }
//    };

    private Uri SMS_INBOX = Uri.parse("content://sms/");

    public void getSmsFromPhone(Context context) {
        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[]{"body", "address", "person"};// "_id", "address",
        // "person",, "date",
        // "type
        String where = " date >  "
                + (System.currentTimeMillis() - 10 * 60 * 1000);
        Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
        if (null == cur)
            return;
        if (cur.moveToNext()) {
            String number = cur.getString(cur.getColumnIndex("address"));// 手机号
            String name = cur.getString(cur.getColumnIndex("person"));// 联系人姓名列表
            String body = cur.getString(cur.getColumnIndex("body"));

            System.out.println(">>>>>>>>>>>>>>>>手机号：" + number);
            System.out.println(">>>>>>>>>>>>>>>>联系人姓名列表：" + name);
            System.out.println(">>>>>>>>>>>>>>>>短信的内容：" + body);

            // 这里我是要获取自己短信服务号码中的验证码~~
            Pattern pattern = Pattern.compile("[a-zA-Z0-9]{4}");
            Matcher matcher = pattern.matcher(body);//String body="测试验证码2346ds";
            if (matcher.find()) {
                String res = matcher.group().substring(0, 4);// 获取短信的内容
                smsAction.call(res);
            }
        }
    }


    class SmsObserver extends ContentObserver {
        Context context;

        public SmsObserver(Context context) {
            super(new Handler());
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            // 每当有新短信到来时，使用我们获取短消息的方法
            getSmsFromPhone(context);
        }
    }


}
