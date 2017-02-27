package com.open.face2facemanager.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.open.face2facemanager.business.baseandcommon.TApplication;

/**
 * Created by Administrator on 2015/7/20.
 */
public class PreferencesHelper {
    final static String SPNAME = "ONION2015720";
    final static String SPAPPINFO = "appinfo";
    final static String SCHEDULE = "schedule";
    final static Gson gson = new Gson();
    private static PreferencesHelper instance = new PreferencesHelper();

    private PreferencesHelper() {
    }

    public static PreferencesHelper getInstance() {
        return instance;
    }

    //存bean
    public void saveBean(Object p) {
        final SharedPreferences spf = TApplication.getInstance().getSharedPreferences(
                SPNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString(p.getClass().getName(), gson.toJson(p));
        edit.commit();
    }

    //取bean
    public <T> T getBean(Class<T> c) {
        final SharedPreferences spf = TApplication.getInstance().getSharedPreferences(
                SPNAME, Context.MODE_PRIVATE);
        return gson.fromJson(spf.getString(c.getName(), ""), c);
    }

    public void clearBean(Class c) {
        final SharedPreferences spf = TApplication.getInstance().getSharedPreferences(
                SPNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString(c.getName(), "");
        edit.commit();
    }

    public void saveUserLoginName(String userLoginName){
        final SharedPreferences spf = TApplication.getInstance().getSharedPreferences(
                SPAPPINFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("userLoginName", userLoginName);
        editor.commit();
    }

    public String getUserLoginName(){
        final SharedPreferences spf = TApplication.getInstance().getSharedPreferences(
                SPAPPINFO, Context.MODE_PRIVATE);
        return spf.getString("userLoginName", "");
    }

    public void saveIsFirst(boolean isFirst){
        final SharedPreferences spf = TApplication.getInstance().getSharedPreferences(
                SPAPPINFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putBoolean("first", isFirst);
        editor.commit();
    }

//    public  void saveUpdateDialogTime(long time) {
//        final SharedPreferences spf = TApplication.getInstance().getSharedPreferences(
//                SPNAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor edit = spf.edit();
//        edit.putLong("updateDialogShowTime", time);
//        edit.commit();
//    }
//    public long getUpdateDialogTime() {
//        final SharedPreferences spf = TApplication.getInstance().getSharedPreferences(
//                SPNAME, Context.MODE_PRIVATE);
//        return   spf.getLong("updateDialogShowTime", 0);
//    }
    public boolean  getFirstWithMode(){
        if(Config.isVersionFirstMode){
          return   getIsVersionFirst();
        }else
            return getIsFirst();
    }
    public void saveFirstWithMode(boolean flag){
        if(Config.isVersionFirstMode){
            saveIsVersionFirst(flag);
        }
        saveIsFirst(flag);
    }
    public boolean getIsFirst(){
        final SharedPreferences spf = TApplication.getInstance().getSharedPreferences(
                SPAPPINFO, Context.MODE_PRIVATE);
        return spf.getBoolean("first", false);
    }
    public void saveScheduleFirstIsFirst(boolean isFirst){
        final SharedPreferences spf = TApplication.getInstance().getSharedPreferences(
                SCHEDULE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putBoolean("first", isFirst);
        editor.commit();
    }
    public void saveIsVersionFirst(boolean isFirst){
        final SharedPreferences spf = TApplication.getInstance().getSharedPreferences(
                "first", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putBoolean(TApplication.getInstance().getVersion(), isFirst);
        editor.commit();
    }
    public boolean getIsVersionFirst(){
        final SharedPreferences spf = TApplication.getInstance().getSharedPreferences(
                "first", Context.MODE_PRIVATE);
        return spf.getBoolean(TApplication.getInstance().getVersion(), false);
    }


    public boolean getScheduleFirst(){
        final SharedPreferences spf = TApplication.getInstance().getSharedPreferences(
                SCHEDULE, Context.MODE_PRIVATE);
        return spf.getBoolean("first", true);
    }

    public void saveLogin(boolean login){
        final SharedPreferences spf = TApplication.getInstance().getSharedPreferences(
                SPAPPINFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putBoolean("login", login);
        editor.commit();
    }

    public boolean isLogin(){
        final SharedPreferences spf = TApplication.getInstance().getSharedPreferences(
                SPAPPINFO, Context.MODE_PRIVATE);
        return spf.getBoolean("login", false);
    }


    public void saveMainClazzsMax(int size){
        final SharedPreferences spf = TApplication.getInstance().getSharedPreferences(
                SPAPPINFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putInt("mainClazzsMax", size);
        editor.commit();
    }

    public int getMainClazzsMax(){
        final SharedPreferences spf = TApplication.getInstance().getSharedPreferences(
                SPAPPINFO, Context.MODE_PRIVATE);
        return spf.getInt("mainClazzsMax", -1);
    }
//    public void saveBindSuccess(boolean login,String userId){
//        final SharedPreferences spf = TApplication.getInstance().getSharedPreferences(
//                userId, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = spf.edit();
//        editor.putBoolean("isBindSuccess", login);
//        editor.commit();
//    }
//
//    public boolean isBindSuccess(String userId){
//        final SharedPreferences spf = TApplication.getInstance().getSharedPreferences(
//                userId, Context.MODE_PRIVATE);
//        return spf.getBoolean("isBindSuccess", false);
//    }

}
