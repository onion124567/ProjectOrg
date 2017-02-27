package com.open.face2facemanager.factory.bean.base;

import com.open.face2facemanager.business.baseandcommon.TApplication;

/**
 * 通用响应，解析外层数据。
 * Created by Administrator on 2016/5/19.
 */
public class OpenResponse<P> {


    /**
     * code : 200  成功  800 服务器异常
     * message : 操作成功
     * result : {"id":1,"loginname":"ceshi","phone":"123456789","token":"C4CA4238A0B923820DCC509A6F75849B","path":"","idcard":"370811199909099999","bigpath":"","teachername":"测试"}
     */

    private int code;
    private String message;
    private P data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public P getData() {
        return data;
    }

    public void setData(P data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "OpenResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * 為了緩存  缓存后拿数据用此方法
     * @param c
     * @return
     */
    public P parseBean(Class<P> c) {
        if (data == null) return null;
        // 在三星 note3 有适配问题
//       LinkedTreeMap map= (LinkedTreeMap) data;
//
//        JSONObject obj=new JSONObject(map);
      /*  Log.i("onion","data"+data);
        JSONObject obj= null;
        try {
            obj = new JSONObject(data.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Log.i("debbug","map"+map);
        if(obj==null)return null;*/
            return TApplication.gson.fromJson(TApplication.gson.toJsonTree(data), c);

    }
}
