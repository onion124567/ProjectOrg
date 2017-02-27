package com.open.face2facemanager.utils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.open.face2facemanager.R;
import com.open.face2facemanager.business.baseandcommon.ServerAPI;

/**
 * Created by Administrator on 2015/7/16.
 */
public class Config {
    public static final String BASE_IMAGE_CACHE = "ONION";
    public static final String IMG_IRL = "";
    public static final String INTENT_PARAMS1 = "params1";
    public static final String INTENT_PARAMS2 = "params2";
    public static final String INTENT_PARAMS3 = "params3";
    public static final String INTENT_OrderId = "orderId";
    public static final String INTENT_String = "intentstring";
    public static final String INTENT_Boolean = "intentboolean";
    public static final String INTENT_TONGJI = "tongji";

    public static final String INTENT_ClazzId = "intentClazzId";
    public static final String INTENT_CourseId = "intentCourseId";
    public static final String INTENT_WeekIndex = "intentWeekIndex";
    public static final String INTENT_LessonIndex = "intentLessonIndex";
    public static final String INTENT_SectionTime = "intentSectionTime";
    public static final String INTENT_Date = "intentDate";
    public static final String INTENT_ClazzName = "intentClazzName";
    public static final String INTENT_CourseName = "intentCourseName";
    public static final int FINISH = 123;
    public static final int START = 456;
    public static final int RESULT_QUIT_CLASS = 789;
    public static final int RESULT_DELETE_MEMBER = 790;
    public static final String QQ_APP_ID = "1105559150";
    public static final int NICKNAME_MAX_LEGTH = 12;

    public static final String LESSON_NOTES_TYPE = "lessonNotesType";
    public static final int LESSON_NOTE_SCHEDULE = 1;
    public static final int LESSON_NOTE_THING = 2;
    public static final int LESSON_NOTE_FAULT = 3;

    public static final String SIGN_AGREEMENT_URL = "https://www.baidu.com/";
//    public static final String LOGIN_TORUIST="loginist";//目前状态游客登录

    public static final String CLAZZKEY = "open_clazz";
    public static final String ORDERLIST_KEY = "orderlist_key";


    //消息小红点的key
    public final static String ORDERLIST_CLAZZTOPIC = "clazzTopic";   //班级圈
    public final static String ORDERLIST_CLAZZNOTICEMESSAGE = "clazzNoticeMessage";//我收到的通知
    public final static String ORDERLIST_COMMENTMESSAGE = "commentMessage";
    public final static String ORDERLIST_LIKEMESSAGE = "likeMessage";
    public final static String ORDERLIST_SYSTEMMESSAGE = "systemMessage";
    public final static String SPEAK_MAIN="speakmain";

    public final static int IMAGE_PICKER = 99;
    public final static long crowdId = 1;

    public final static boolean isVersionFirstMode = true;
    public final static String WEIXIN_APPID = "wx4ce560f0c26d3318";
   /* public static DisplayImageOptions logo_options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.img_learningbar_laucher)
            .showImageForEmptyUri(R.drawable.img_learningbar_laucher)
            .showImageOnFail(R.drawable.img_learningbar_laucher)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
//            .displayer(new RoundedBitmapDisplayer(20))
            .build();*/

    public static DisplayImageOptions circleIconDefault = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.icon_default)
            .showImageForEmptyUri(R.mipmap.icon_default)
            .showImageOnFail(R.mipmap.icon_default)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true).build();
    public static double defaultLat = 0;//39.92;
    public static double defaultLon = 0;//116.43;
    public static String shareUrl = ServerAPI.ENDPOINT + "business/clazz_group/clazz_share.xhtml";
}
