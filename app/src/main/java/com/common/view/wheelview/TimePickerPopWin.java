package com.common.view.wheelview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.open.face2facemanager.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * PopWindow for Time Pick   by onion 2016/11/21
 */
public class TimePickerPopWin extends PopupWindow implements OnClickListener {

    public TextView cancelBtn;
    public TextView confirmBtn;
    public LoopView hourLoopView,minuteLoopView;
    public View pickerContainerV;
    public View contentView;//root view

    private int hourPos = 0;
    private int minutePos = 0;
    private Context mContext;
    private String textCancel;
    private String textConfirm;
    private int colorCancel;
    private int colorConfirm;
    private int btnTextsize;//text btnTextsize of cancel and confirm button
    private int viewTextSize;
    private boolean showDayMonthYear;

    private int minHour=0;
    private int maxHour=24;
    List<String> hourList = new ArrayList<>();
    List<String> minuteList = new ArrayList<>();

    public static class Builder{

        private int minHour=0;
        private int maxHour=24;
        //Required
        private Context context;
        private OnDatePickedListener listener;

        public Builder(Context context,OnDatePickedListener listener){
            this.context = context;
            this.listener = listener;
        }

        //Option
        private boolean showDayMonthYear = false;
        private String textCancel = "Cancel";
        private String textConfirm = "Confirm";
//        private String dateChose = getStrDate();
        private int colorCancel = Color.parseColor("#999999");
        private int colorConfirm = Color.parseColor("#303F9F");
        private int btnTextSize = 16;//text btnTextsize of cancel and confirm button
        private int viewTextSize = 25;

        public Builder maxHour(int maxHour){
            this.maxHour=maxHour;
            return this;
        }

        public Builder minHour(int minHour){
            this.minHour=minHour;
            return  this;
        }

        public Builder textCancel(String textCancel){
            this.textCancel = textCancel;
            return this;
        }

        public Builder textConfirm(String textConfirm){
            this.textConfirm = textConfirm;
            return this;
        }

//        public Builder dateChose(String dateChose){
//            this.dateChose = dateChose;
//            return this;
//        }

        public Builder colorCancel(int colorCancel){
            this.colorCancel = colorCancel;
            return this;
        }

        public Builder colorConfirm(int colorConfirm){
            this.colorConfirm = colorConfirm;
            return this;
        }

        /**
         * set btn text btnTextSize
         * @param textSize dp
         */
        public Builder btnTextSize(int textSize){
            this.btnTextSize = textSize;
            return this;
        }

        public Builder viewTextSize(int textSize){
            this.viewTextSize = textSize;
            return this;
        }

        public TimePickerPopWin build(){
            if(minHour > maxHour){
                throw new IllegalArgumentException();
            }
            return new TimePickerPopWin(this);
        }

        public Builder showDayMonthYear(boolean useDayMonthYear) {
            this.showDayMonthYear = useDayMonthYear;
            return this;
        }
    }

    public TimePickerPopWin(Builder builder){
        this.maxHour=builder.maxHour;
        this.minHour=builder.minHour;
        this.textCancel = builder.textCancel;
        this.textConfirm = builder.textConfirm;
        this.mContext = builder.context;
        this.mListener = builder.listener;
        this.colorCancel = builder.colorCancel;
        this.colorConfirm = builder.colorConfirm;
        this.btnTextsize = builder.btnTextSize;
        this.viewTextSize = builder.viewTextSize;
        this.showDayMonthYear = builder.showDayMonthYear;
//        setSelectedDate(builder.dateChose);
        initView();
    }

    private OnDatePickedListener mListener;

    private void initView() {

        contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_time_picker, null);
        cancelBtn = (TextView) contentView.findViewById(R.id.btn_cancel);
        confirmBtn = (TextView) contentView.findViewById(R.id.btn_confirm);
        hourLoopView = (LoopView) contentView.findViewById(R.id.picker_hour);
        minuteLoopView = (LoopView) contentView.findViewById(R.id.picker_minute);
        pickerContainerV = contentView.findViewById(R.id.container_picker);

//        //do not loop,default can loop
//        yearLoopView.setNotLoop();
//        monthLoopView.setNotLoop();
//        dayLoopView.setNotLoop();
//
//        //set loopview text size
//        yearLoopView.setTextSize(25);
//        monthLoopView.setTextSize(25);
//        dayLoopView.setTextSize(25);


        hourLoopView.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {
                hourPos = item;
            }
        });

        minuteLoopView.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {
                minutePos = item;
            }
        });
        initTimePickerView();

        cancelBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        contentView.setOnClickListener(this);

        setTouchable(true);
        setFocusable(true);
        // setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(R.style.FadeInPopWin);
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
    }



    private void initTimePickerView(){
        for (int i=0; i<maxHour-minHour; i++){
            hourList.add(format2LenStr(i+minHour));
        }

        for (int i=0 ;i<60; i++){
            minuteList.add(format2LenStr(i));
        }

        hourLoopView.setDataList(hourList);
        hourLoopView.setInitPosition(hourPos);

        minuteLoopView.setDataList(minuteList);
        minuteLoopView.setInitPosition(minutePos);

    }

    /**
     * set selected date position value when initView.
     *
     * @param dateStr
     */
    public void setSelectedDate(String dateStr) {

        if (!TextUtils.isEmpty(dateStr)) {
          String[] sTime=  dateStr.split(":");
                hourPos = Integer.parseInt(sTime[0])-minHour;
            Log.i("onion","hourPos"+hourPos+"/"+Integer.parseInt(sTime[0]));
                minutePos = Integer.parseInt(sTime[1]);
           if(hourLoopView!=null&&hourPos>0)
               hourLoopView.setInitPosition(hourPos);
            if(minuteLoopView!=null)
                minuteLoopView.setInitPosition(minutePos);
        }
    }

    /**
     * Show date picker popWindow
     *
     * @param activity
     */
    public void showPopWin(Activity activity) {

        if (null != activity) {

            TranslateAnimation trans = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
                    0, Animation.RELATIVE_TO_SELF, 1,
                    Animation.RELATIVE_TO_SELF, 0);

            showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM,
                    0, 0);
            trans.setDuration(400);
            trans.setInterpolator(new AccelerateDecelerateInterpolator());

            pickerContainerV.startAnimation(trans);
        }
    }

    /**
     * Dismiss date picker popWindow
     */
    public void dismissPopWin() {

        TranslateAnimation trans = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);

        trans.setDuration(400);
        trans.setInterpolator(new AccelerateInterpolator());
        trans.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                dismiss();
            }
        });

        pickerContainerV.startAnimation(trans);
    }

    @Override
    public void onClick(View v) {

        if (v == contentView || v == cancelBtn) {

            dismissPopWin();
        } else if (v == confirmBtn) {

            if (null != mListener) {
                StringBuffer sb = new StringBuffer();
                sb.append(format2LenStr(hourPos+minHour));
                sb.append(":");
                sb.append(format2LenStr(minutePos));
                mListener.onDatePickCompleted( hourPos,minutePos,sb.toString());
            }

            dismissPopWin();
        }
    }

    /**
     * get long from yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static long getLongFromMMdd(String date) {
        SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date parse = null;
        try {
            parse = mFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (parse != null) {
            return parse.getTime();
        } else {
            return -1;
        }
    }

    public static String getStrDate() {
        SimpleDateFormat dd = new SimpleDateFormat("HH:mm", Locale.CHINA);
        return dd.format(new Date());
    }

    /**
     * Transform int to String with prefix "0" if less than 10
     * @param num
     * @return
     */
    public static String format2LenStr(int num) {

        return (num < 10) ? "0" + num : String.valueOf(num);
    }

    public static int spToPx(Context context, int spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    public interface OnDatePickedListener {

        /**
         * Listener when date has been checked
         *
         * @param dateDesc  yyyy-MM-dd
         */
        void onDatePickCompleted(int hour, int minute,
                                 String dateDesc);
    }
}
