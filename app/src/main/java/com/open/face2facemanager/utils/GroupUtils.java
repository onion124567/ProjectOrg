package com.open.face2facemanager.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

/**
 * Created by onion on 2016/11/14.
 */
public class GroupUtils {

    public static void setCrowdName(String crowdType,long crowdExtId,String crowdName,TextView textView){
        if("CLAZZ".equals(crowdType)&&crowdExtId!=0){
            //从map里获取
          String realName=  ACache.get(textView.getContext(),"groupCache").getAsString("crowName"+crowdExtId);
            if(!TextUtils.isEmpty(realName)){
                crowdName=realName+"班级圈";
            }
        }
        textView.append("\t来自:"+crowdName);
    }


    public static void saveCrowId(Context context,long clazzId,String clazzNikeName){
        ACache.get(context,"groupCache").put("crowName"+clazzId,clazzNikeName);
    }
}
