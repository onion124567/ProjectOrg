package com.open.face2facemanager.business.baseandcommon;

import com.open.face2facemanager.factory.bean.base.OpenResponse;
import com.open.face2facemanager.utils.ACache;
import com.open.face2facemanager.utils.DialogManager;
import com.open.face2facemanager.utils.StrUtils;

/**
 * 联网错误回调
 * 如果网络异常不需要其他操作，只Toast可以
 * Created by Administrator on 2016/5/24.
 */
public class BaseToastNetError<View> extends CacheAble<View, Throwable> {

    @Override
    public void call(View v, Throwable throwable) {
        DialogManager.dismissNetLoadingView();
        if (v instanceof BaseFragment) {
            if (((BaseFragment) v).mPtrFrame != null)
                ((BaseFragment) v).mPtrFrame.autoRefresh();
        } else if (v instanceof BaseActivity) {
            if (((BaseActivity) v).mPtrFrame != null)
                ((BaseActivity) v).mPtrFrame.autoRefresh();
        }
        if (getCacheType() == CacheType.After) {//异常后获取缓存
            OpenResponse o = TApplication.gson.fromJson(ACache.get(TApplication.getInstance()).getAsString(StrUtils.string2md5(cacheKey)), OpenResponse.class);
            onCache.call(v, o);
        }
        TApplication.getInstance().showToast(throwable.getMessage());
//        Log.e("error", "error" + throwable.getMessage().toString());
       /* if (throwable instanceof RetrofitError) {
            RetrofitError error= (RetrofitError) throwable;
            switch (error.getKind()){
                case HTTP:
                    Toast.makeText(TApplication.getInstance(), "服务器异常，请重试", Toast.LENGTH_SHORT).show();
                    break;
                case NETWORK:
                    Toast.makeText(TApplication.getInstance(), "网络连接错误，请重试", Toast.LENGTH_SHORT).show();
                    break;
                case CONVERSION:
                    break;
                case UNEXPECTED:
                    break;

            }

        } else {
            Toast.makeText(TApplication.getInstance(),  throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }*/
    }

}
