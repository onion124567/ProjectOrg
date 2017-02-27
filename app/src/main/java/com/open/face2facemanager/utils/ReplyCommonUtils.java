package com.open.face2facemanager.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;


/**
 * 处理圈子的公共类
 * Created by Administrator on 2015/10/9.
 */
public class ReplyCommonUtils {
    public static final int COLOR_TIME_DEFAULT = 0xFF999999;
    public static final int COLOR_NAME_DEFAULT = 0xFF48C55C;
//    public static void initEva(Context context,String fromNike,String fromEmobid,String toNike,String toEmobid,String content,String time,TextView tv){
//       initEva(context,fromNike,fromEmobid,toNike,toEmobid,content,time,tv,new int[]{COLOR_NAME_DEFAULT,COLOR_NAME_DEFAULT});
//    }

    /**
     * 展示回复
     * 人名可点击的版本
     * textview会抢占点击事件
     *
     * @param context
     * @param fromNike
     * @param fromEmobid
     * @param toNike
     * @param toEmobid
     * @param content
     * @param time
     * @param tv
     */
    public static void initEvaClick(Context context, String fromNike, String fromEmobid, String toNike, String toEmobid, String content, String time, TextView tv) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(fromNike);
        NoUndelineClickSpan toClickSpan = null;
        if ("0".equals(toEmobid) || fromEmobid.equals(toEmobid)) {
            toEmobid = null;
        }
        if (toEmobid != null && toNike != null) {
            stringBuilder.append("回复");
            stringBuilder.append(toNike);
            toClickSpan = new NoUndelineClickSpan();
            toClickSpan.setContext(context);
            toClickSpan.setEmobid(toEmobid);
        }
        if (time != null) {
            stringBuilder.append("\t" + time);

        }
        stringBuilder.append("\n" + content);
        SpannableString sp = new SpannableString(stringBuilder.toString());
//        sp.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        NoUndelineClickSpan fromSpan = new NoUndelineClickSpan();
        fromSpan.setContext(context);
        fromSpan.setEmobid(fromEmobid);
        sp.setSpan(fromSpan, 0, fromNike.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        sp.setSpan(new ForegroundColorSpan(nameColors[0]),0,fromNike.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        int timestartindex = fromNike.length() + 1;
        if (toClickSpan != null) {
            sp.setSpan(toClickSpan, fromNike.length() + 2, fromNike.length() + 2 + toNike.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//            sp.setSpan(new ForegroundColorSpan(nameColors[1]), fromNike.length() + 2, fromNike.length() + 2 + toNike.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            timestartindex = fromNike.length() + 2 + toNike.length() + 1;
        }
        if (time != null) {
            sp.setSpan(new AbsoluteSizeSpan(11, true), timestartindex, timestartindex + time.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            sp.setSpan(new ForegroundColorSpan(COLOR_TIME_DEFAULT), timestartindex, timestartindex + time.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        tv.setText(sp);
        tv.getPaint().setUnderlineText(false);
        tv.setLinkTextColor(COLOR_NAME_DEFAULT);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setFocusable(false);
//        tv.setClickable(false);
        tv.setLongClickable(false);
    }

    /**
     *
     * @param context
     * @param fromNike
     * @param fromEmobid
     * @param toNike
     * @param toEmobid
     * @param content
     * @param time
     * @param tv
     */
    public static void initEva(Context context, String fromNike, String fromEmobid, String toNike, String toEmobid, String content, String time, TextView tv) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(fromNike);
        ForegroundColorSpan toClickSpan = null;
        if ("0".equals(toEmobid) || fromEmobid.equals(toEmobid)) {
            toEmobid = null;
        }
        if (toEmobid != null && toNike != null) {
            stringBuilder.append("回复");
            stringBuilder.append(toNike);
            toClickSpan = new ForegroundColorSpan(COLOR_NAME_DEFAULT);
        }
        if (time != null) {
            stringBuilder.append("\t" + time);

        }
        stringBuilder.append("\n" + content);
        SpannableString sp = new SpannableString(stringBuilder.toString());
//        sp.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan fromSpan = new ForegroundColorSpan(COLOR_NAME_DEFAULT);
        sp.setSpan(fromSpan, 0, fromNike.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        int timestartindex = fromNike.length() + 1;
        if (toClickSpan != null) {
            sp.setSpan(toClickSpan, fromNike.length() + 2, fromNike.length() + 2 + toNike.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//            sp.setSpan(new ForegroundColorSpan(nameColors[1]), fromNike.length() + 2, fromNike.length() + 2 + toNike.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            timestartindex = fromNike.length() + 2 + toNike.length() + 1;
        }
        if (time != null) {
            sp.setSpan(new AbsoluteSizeSpan(11, true), timestartindex, timestartindex + time.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            sp.setSpan(new ForegroundColorSpan(COLOR_TIME_DEFAULT), timestartindex, timestartindex + time.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        tv.setText(sp);
    }

    static class NoUndelineClickSpan extends ClickableSpan {
        Context context;
        String emobid;

        public String getEmobid() {
            return emobid;
        }

        public void setEmobid(String emobid) {
            this.emobid = emobid;
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View widget) {
//            Intent intent=new Intent(context, UserGroupInfoActivity.class);
//            intent.putExtra(Config.INTENT_PARMAS2,emobid);
//            context.startActivity(intent);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ds.linkColor);
            ds.setUnderlineText(false);
        }
    }


}
