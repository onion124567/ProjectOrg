package com.common.view.adapter;

import android.widget.ImageView;

import com.common.view.recyclerview.BaseQuickAdapter;
import com.common.view.recyclerview.BaseViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.open.face2facemanager.R;
import com.open.face2facemanager.business.baseandcommon.TApplication;
import com.open.face2facemanager.factory.annotation.ImageField;
import com.open.face2facemanager.factory.annotation.TextField;

import java.lang.reflect.Field;
import java.util.List;

import rx.functions.Action2;

/**
 * Created by Administrator on 2016/7/21.
 */
public class OnionRecycleAdapter<T> extends BaseQuickAdapter<T> {
    DisplayImageOptions displayImageOptions;
    public OnionRecycleAdapter(int layoutId, List<T> list) {
        super(layoutId, list);
    }
    public OnionRecycleAdapter(int layoutId, List<T> list,DisplayImageOptions displayImageOptions) {
        super(layoutId, list);
        this.displayImageOptions=displayImageOptions;
    }
    Action2<BaseViewHolder, T> callBack = null;


    public Action2<BaseViewHolder, T> getCallBack() {
        return callBack;
    }

    public void setCallBack(Action2<BaseViewHolder, T> callBack) {
        this.callBack = callBack;
    }


    @Override
    protected void convert(BaseViewHolder helper, T item) {
        Field[] fields = item.getClass().getDeclaredFields();
        for (Field f : fields) {
            //认为是文本
            TextField textField = f.getAnnotation(TextField.class);
            try {
                if (textField != null && helper.getView(textField.value()) != null) {
                    helper.setText(textField.value(), f.get(item) + "");
                } else {
                    ImageField imageField = f.getAnnotation(ImageField.class);
                    if (imageField != null && helper.getView(imageField.value()) != null) {
                        ImageView imageView = helper.getView(imageField.value());
                        disPlayImg(f.get(item) + "", imageView);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (callBack != null) {
            callBack.call(helper, item);
        }
    }


    private void disPlayImg(String url,ImageView imageView){
        boolean picisNative=false;
        if (url.contains("http")) {
            picisNative = false;
        } else {
            picisNative = true;
        }
        if (picisNative) {//加载本地图片
            if (url.contains("."))//去掉本地图片后缀
                url = url.substring(0, url.lastIndexOf("."));
//                    ImageLoader.getInstance().displayImage(
//                            "drawable://" + context.getResources().getIdentifier(path, "drawable",
//                                    packageName), vh.ivs.get(i));
            int id = mContext.getResources().getIdentifier(url, "drawable",
                    TApplication.packageName);
            if (id != 0) {//本地图片存在
                imageView.setImageResource(id);
            } else {//本地图片不存在,加载默认图片
                if (displayImageOptions == null) {
                    imageView.setImageResource(R.mipmap.icon_default);
                } else {
                    ImageLoader.getInstance().displayImage("", imageView, displayImageOptions);
                }
            }
        } else {
            if (displayImageOptions == null)
                ImageLoader.getInstance().displayImage(url, imageView);
            else
                ImageLoader.getInstance().displayImage(url, imageView, displayImageOptions);
        }
    }
}
