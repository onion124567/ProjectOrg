package com.open.face2facemanager.utils;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * 对头像初始化的util
 * Created by onion on 2016/8/18.
 */
public class AvatarHelper {

    public  void initAvatar(SimpleDraweeView view,String path){
        if(TextUtils.isEmpty(path))path="empty";
        view.setImageURI(Uri.parse(path));
        view.setOnClickListener(onClickListener);
        view.setTag(path);
    }
    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Activity activity= (Activity) view.getContext();
            TongjiUtil.tongJiOnEvent(activity,"id_seeportrait","");
          String path = (String) view.getTag();
            if(!"empty".equals(path)){
                   ScreenUtils.onImgClick(activity,path, (ImageView) view);
            }
        }
    };
}
