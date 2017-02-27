package com.open.face2facemanager.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/6/30.
 */
public class ScreenShootHelper {
    public interface MeassureHelper {
        public View onMeasure(int position);
    }

    /**
     * 截图listview
     * <p>
     * *
     */

    public static String getScreenShoot(View convertView, MeassureHelper meassureHelper, int size) {
        Bitmap bitmap = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        /*String fname = Environment.getExternalStorageDirectory().getAbsolutePath() +
                ".png";*/
        File sdcardPath = Environment.getExternalStorageDirectory();
        final String fname = sdcardPath.getAbsolutePath() + File.separator + sdf.format(new Date()) + ".png";
        // 创建对应大小的bitmap
        Bitmap totalBitmap = Bitmap.createBitmap(convertView.getWidth(), convertView.getHeight() * size,
                Bitmap.Config.ARGB_8888);
        Canvas totalCanvas = new Canvas(totalBitmap);
        Paint paint = new Paint();
        // 获取listView实际高度
        for (int i = 0; i < size; i++) {
            bitmap = Bitmap.createBitmap(convertView.getWidth(), convertView.getHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            convertView = meassureHelper.onMeasure(i);
            convertView.draw(canvas);
            totalCanvas.drawBitmap(bitmap, 0, convertView.getHeight() * i, paint);
            bitmap.recycle();
        }
        // 测试输出
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(fname);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (null != out) {
                totalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return fname;
    }

    /**
     * 获取截屏
     *
     * @param v
     * @return
     */
   public static String getScreenShoot(View v) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        /*String fname = Environment.getExternalStorageDirectory().getAbsolutePath() +
                ".png";*/
        File sdcardPath = Environment.getExternalStorageDirectory();
        final String fname = sdcardPath.getAbsolutePath() + File.separator + "screenshoot.png";
        Log.i("onion", "fname" + fname);
//        View   view = v.getRootView();
       v.setDrawingCacheEnabled(true);
       v.buildDrawingCache();
        Bitmap bitmap = v.getDrawingCache();
        if (bitmap
                != null) {
            try {
                FileOutputStream out = new FileOutputStream(fname);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100,
                        out);
                return fname;
            } catch (Exception
                    e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }
}
