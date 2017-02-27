package com.open.face2facemanager.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.common.view.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import rx.functions.Action0;
import rx.functions.Action1;

/**
 * 项目名称：face2facemanager
 * 类描述：图片压缩
 * 创建人：zhougl
 * 创建时间：2016/8/15 15:02
 * 修改人：zhougl
 * 修改时间：2016/8/15 15:02
 * 修改备注：
 */
public class PictureCompressUtils {


    public static ArrayList<String> compressSelecterImager(final Action1<ArrayList<String>> action1){
        final ArrayList<String> returnList = new ArrayList<>();
        final ImagePicker imagePicker = ImagePicker.getInstance();
        new Thread(new Runnable() {//开启多线程进行压缩处理
            @Override
            public void run() {
                // TODO Auto-generated method stub
                for (int i = 0; i < imagePicker.getSelectImageCount(); i++) {
                    if (!"net".equals(imagePicker.getSelectedImages().get(i).mimeType)) {
                        String compressPath = getFileName();
                        final String path = imagePicker.getSelectedImages().get(i).path;
                        compressImageByPixel(path, compressPath, new Action0() {
                            @Override
                            public void call() {

                            }
                        });
                        returnList.add(compressPath);
                    }
                }
                action1.call(returnList);
            }
        }).start();
        return returnList;
    }


    /**
     * 按比例缩小图片的像素以达到压缩的目的
     * @author JPH
     * @param imgPath
     * @date 2014-12-5下午11:30:59
     */
    public static void compressImageByPixel(String imgPath, String compressPath ,Action0 action0) {
        File newFile = new File(compressPath);
        if (!newFile.exists()) {
            try {
                newFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
        newOpts.inJustDecodeBounds = false;
        int width = newOpts.outWidth;
        int height = newOpts.outHeight;
        float maxSize = 1080f;//默认1000px
        int be = 1;
        if ( width > maxSize) {//缩放比,用高或者宽其中较大的一个数据进行计算
            be = (int) (newOpts.outWidth / maxSize);
        }
//        if (width > height && width > maxSize) {//缩放比,用高或者宽其中较大的一个数据进行计算
//            be = (int) (newOpts.outWidth / maxSize);
//        } else if (width < height && height > maxSize) {
//            be = (int) (newOpts.outHeight / maxSize);
//        }
        if(be <=0)
            be =1;
        newOpts.inSampleSize = be;//设置采样率
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收
        bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
        compressImageByQuality(bitmap,compressPath,action0);//压缩好比例大小后再进行质量压缩
    }


    /**
     * 压缩图片的质量
     * @author JPH
     * @param bitmap 内存中的图片
     * @param imgPath 图片的保存路径
     * @date 2014-12-5下午11:30:43
     */
    public static void compressImageByQuality(final Bitmap bitmap, final String imgPath, final Action0 action0) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//质量压缩方法，把压缩后的数据存放到baos中 (100表示不压缩，0表示压缩到最小)
        while (baos.toByteArray().length / 1024 > 500) {//循环判断如果压缩后图片是否大于kb,大于继续压缩
            baos.reset();//重置baos即让下一次的写入覆盖之前的内容
            options -= 10;//图片质量每次减少10
            if (options < 0) options = 0;//如果图片质量小于10，则将图片的质量压缩到最小值
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//将压缩后的图片保存到baos中
            if (options == 0) break;//如果图片的质量已降到最低则，不再进行压缩
        }
        try {
            FileOutputStream fos = new FileOutputStream(new File(imgPath));//将压缩后的图片保存的本地上指定路径中
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFileName() {
        String fileName = String.valueOf(System.currentTimeMillis());
        String path;
        File sdcardPath = Environment.getExternalStorageDirectory();
        try {
            path = StrUtils.getPackgePath("Compress");
        } catch (Exception e) {
            e.printStackTrace();
            path = sdcardPath.getAbsolutePath() + File.separator + Config.BASE_IMAGE_CACHE +
                    File.separator + "image";
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path + File.separator + fileName + ".jpg";
    }

    public static void clearCompressFilse(){
        File dir = null;
        try {
            dir = new File(StrUtils.getPackgePath("Compress"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;

        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                clearCompressFilse(); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }
}
