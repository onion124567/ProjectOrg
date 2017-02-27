package com.open.face2facemanager.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.open.face2facemanager.business.baseandcommon.TApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;


/**
 * Created by Administrator on 2015/7/21.
 */
public class IOUtils {
    public static String getFromAssets(String fileName){
        try {
            InputStreamReader inputReader = new InputStreamReader(TApplication.getInstance(). getResources().getAssets().open(fileName) );
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            String Result="";
            while((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
    return "";
    }
//    public String getFromRaw(){
//        try {
//            InputStreamReader inputReader = new InputStreamReader( getResources().openRawResource(R.raw.test1));
//            BufferedReader bufReader = new BufferedReader(inputReader);
//            String line="";
//            String Result="";
//            while((line = bufReader.readLine()) != null)
//                Result += line;
//            return Result;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static String saveFile(Bitmap loadedImage,String url){
        File sdcardPath = Environment.getExternalStorageDirectory();
        final String path = sdcardPath.getAbsolutePath() + File.separator + TApplication.getInstance().getPackageName() + File.separator + StrUtils.string2md5(url) + ".png";
        File file = new File(path);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                loadedImage.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "";
            }
        }
        return  path;
    }

    public static void printLongString(String content){
        StringReader stringReader=new StringReader(content);
        BufferedReader buffreader = new BufferedReader(stringReader);
        String line;
        //分行读取
        try{
            while (( line = buffreader.readLine()) != null) {
                Log.i("onion", line);
            }
            stringReader.close();
            buffreader.close();}
        catch (IOException exception){

        }
    }
}
