package com.open.face2facemanager.business.baseandcommon;

import android.widget.Toast;

import com.open.face2facemanager.factory.bean.base.OpenResponse;
import com.open.face2facemanager.utils.ACache;
import com.open.face2facemanager.utils.DialogManager;
import com.open.face2facemanager.utils.StrUtils;

import org.json.JSONObject;

/**
 * 直接返回bean
 * Created by Administrator on 2016/5/24.
 */
public abstract class NetCallBack<View, T> extends CacheAble<View, OpenResponse<T>> {

    @Override
    public void call(View v, OpenResponse<T> tOpenResponse) {
        DialogManager.dismissNetLoadingView();
        switch (tOpenResponse.getCode()) {
            case 200:
                callBack(v, tOpenResponse.getData());
                if (getCacheType() != CacheType.None) {//需要缓存
                    ACache.get(TApplication.getInstance()).put(StrUtils.string2md5(cacheKey), TApplication.gson.toJson(tOpenResponse));
                }
                break;
            default:
            case 800:
                if (getCacheType() == CacheType.After) {//异常后缓存
                    JSONObject object = ACache.get(TApplication.getInstance()).getAsJSONObject(StrUtils.string2md5(cacheKey));
                    if (object != null) {
                        OpenResponse o = TApplication.gson.fromJson(object.toString(), OpenResponse.class);
                        onCache.call(v, o);
                    }
                }
                Toast.makeText(TApplication.getInstance(), tOpenResponse.getMessage(), Toast.LENGTH_SHORT).show();
                callBackServerError(v, tOpenResponse);
                break;
            case 700:
                TApplication.getInstance().exit();
                TApplication.getInstance().startLogin();
                Toast.makeText(TApplication.getInstance(), "您的账号在别处登陆。", Toast.LENGTH_SHORT).show();
                break;

        }

    }

    public abstract void callBack(View v, T t);

    public void callBackServerError(View v, OpenResponse t) {

    }


}
