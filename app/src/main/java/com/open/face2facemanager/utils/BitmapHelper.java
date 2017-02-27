package com.open.face2facemanager.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BitmapHelper {
    public static int imageCount = 0;
    public static boolean act_bool = true;
    public static List<Bitmap> bitmapListMemory = new ArrayList<Bitmap>();

    //图片sd地址  上传服务器时把图片调用下面方法压缩后 保存到临时文件夹 图片压缩后小于100KB，失真度不明显
    public static List<String> imageListStorage = new ArrayList<String>();
    public static List<String> bitmapChatListStorage = new ArrayList<String>();
    public static HashMap<String, String> uploadImageStorage = new HashMap<>();
    public static RevitionOption defaultRevitionOptoion = new RevitionOption(300);

    /**
     *
     */
    public static class RevitionOption {
        public int revsize;

        public RevitionOption(int revsize) {
            this.revsize = revsize;
        }
    }

    public static boolean removeBitmap(String path) {
        if (imageListStorage.contains(path)) {
            bitmapListMemory.remove(imageListStorage.indexOf(path));
            imageListStorage.remove(path);
            //不删除已生成的图片，最后统一删除
//            uploadImageStorage.remove(path);
            imageCount--;
            return true;
        }
        return false;
    }

    public static boolean removeBitmap(int position) {
        if (position > BitmapHelper.imageCount) return false;
        BitmapHelper.bitmapListMemory.remove(position);
        String path = BitmapHelper.imageListStorage.remove(position);
        if (path != null)
            BitmapHelper.uploadImageStorage.remove(path);
        BitmapHelper.imageCount--;
        return true;

    }

    public static int addAll(List<String> imageStorages, int imageCount) {
        int j = 0;
        for (int i = 0; i < imageStorages.size(); i++) {
            if (BitmapHelper.imageListStorage.size() < imageCount && !BitmapHelper.imageListStorage.contains(imageStorages.get(i))) {
                BitmapHelper.imageListStorage.add(imageStorages.get(i));
                j++;
            }
        }
        return j;
    }

    public static Bitmap revitionImageSize(String path) throws IOException {
        File file = new File(path);
        if (file == null) {
            Log.i("onion", path + "/" + file);
            return null;
        }
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                file));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= 1000)
                    && (options.outHeight >> i <= 1000)) {
                in = new BufferedInputStream(
                        new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
        return bitmap;
    }

    public static Bitmap revitionImageSizeBydegree(String path) throws IOException {
        return revitionImageSizeBydegree(path, defaultRevitionOptoion);
    }

    public static Bitmap revitionImageSizeBydegree(String path, RevitionOption revitionOption) throws IOException {
        File oldFile = new File(path);
        File file = getFileName(path);
        try {
            file.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
//            DebugMessage.put("在保存图片时出错："+e.toString());
        }
        if (file == null) {
            Log.i("onion", path + "/" + file);
            return null;
        }
        int rotate = 0;
        ExifInterface exif = new ExifInterface(oldFile.getAbsolutePath());
        int orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotate = 270;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotate = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotate = 90;
                break;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
//
        Bitmap bitmap = getSmallBitmap(path);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
        compressBmpToFile(bitmap, file, revitionOption);
        return bitmap;
    }
   /* public static Bitmap revitionImageSizeBydegree(String path) throws IOException {
        File file=new File(path);
        if(file==null){
            Log.i("onion",path+"/"+file);
            return null;}
        int rotate = 0;
        ExifInterface exif = new ExifInterface(file.getAbsolutePath());
        int orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);

        switch (orientation)
        {
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotate = 270;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotate = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotate = 90;
                break;
        }
        Log.i("onion","rotate"+rotate);
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                file));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= 1000)
                    && (options.outHeight >> i <= 1000)) {
                in = new BufferedInputStream(
                        new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        bitmap= Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
        return bitmap;
    }*/


    public static void cleanBitmap() {
//       for(int i=0;i<imageListStorage.size();i++)
//        new File(imageListStorage.get(0)).delete();
        imageListStorage.clear();
        bitmapListMemory.clear();
        ArrayList<String> paths = new ArrayList<>(uploadImageStorage.values());
        if (paths.size() > 0) {
            for (int i = 0; i < paths.size(); i++) {
                File file = new File(paths.get(i));
                file.delete();
            }
        }
        uploadImageStorage.clear();
        imageCount = 0;
    }

    public static void copyfile(File fromFile, File toFile, Boolean rewrite) {

        if (!fromFile.exists()) {

            return;

        }

        if (!fromFile.isFile()) {

            return;

        }

        if (!fromFile.canRead()) {

            return;

        }

        if (!toFile.getParentFile().exists()) {

            toFile.getParentFile().mkdirs();

        }

        if (toFile.exists() && rewrite) {

            toFile.delete();

        }

        //当文件不存时，canWrite一直返回的都是false

        // if (!toFile.canWrite()) {

        // MessageDialog.openError(new Shell(),"错误信息","不能够写将要复制的目标文件" + toFile.getPath());

        // Toast.makeText(this,"不能够写将要复制的目标文件", Toast.LENGTH_SHORT);

        // return ;

        // }

        try {

            FileInputStream fosfrom = new FileInputStream(fromFile);

            FileOutputStream fosto = new FileOutputStream(toFile);

            byte bt[] = new byte[1024];

            int c;

            while ((c = fosfrom.read(bt)) > 0) {

                fosto.write(bt, 0, c); //将内容写到新文件当中

            }

            fosfrom.close();

            fosto.close();

        } catch (Exception ex) {

            Log.e("readfile", ex.getMessage());

        }

    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }


    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     *
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        return bitmap;
    }

    private static int compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 300) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return options + 10;
    }

    public static void compressBmpToFile(Bitmap bmp, File file, RevitionOption revitionOption) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 90;
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > revitionOption.revsize) {
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static String getFileName() {
//        String fileName = String.valueOf(System.currentTimeMillis());
//        String path;
//        File sdcardPath = Environment.getExternalStorageDirectory();
//        path = sdcardPath.getAbsolutePath() + File.separator + Config.BASE_GROUP_CACHE +
//                File.separator + "image";
//        File file = new File(path);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        return path + File.separator + fileName + ".jpg";
//    }

    /**
     * @param data      返回的intent
     * @param photoName 初始路径
     * @param listener  处理后的回调
     */
    public static void onResultGetPhoto(Intent data, String photoName, onGetPhotoListener listener) {
        Log.i("onion", "data" + data);
        Bitmap thumbnail = null;
        if (data != null) { //可能尚未指定intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            //返回有缩略图
            if (data.hasExtra("data")) {

                thumbnail = data.getParcelableExtra("data");
                rototeAndSave(thumbnail,photoName);
                listener.onGetPhoto();
            } else  {
                File f = new File(photoName);
                if (f.exists()) {
                    rototeAndSave(BitmapFactory.decodeFile(photoName), photoName);
                    listener.onGetPhoto();
                }
            }
        } else {
            //由于指定了目标uri，存储在目标uri，intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            // 通过目标uri，找到图片
            // 对图片的缩放处理
            // 操作
            rototeAndSave(BitmapFactory.decodeFile(photoName),photoName);
            listener.onGetPhoto();
        }
    }
   public static void rototeAndSave(Bitmap bitmap,String path){
       bitmap = ImageUtils.rotate(bitmap, ImageUtils.readPictureDegree(path));
       FileUtils.saveBitmap(bitmap, path);

   }
    /**
     * 处理完photo后，回调
     */
    public static interface onGetPhotoListener {
        void onGetPhoto();
    }

    /***
     * @param realpath
     * @return
     */
    private static File getFileName(String realpath) {
        String fileName = String.valueOf(System.currentTimeMillis());
        String path;
        File sdcardPath = Environment.getExternalStorageDirectory();
        path = sdcardPath.getAbsolutePath() + File.separator + Config.BASE_IMAGE_CACHE +
                File.separator + "image";

        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        File newFile = new File(f, fileName + ".jpg");
        if (!newFile.exists()) {
            try {
                newFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        uploadImageStorage.put(realpath, path + File.separator + fileName + ".jpg");
        return newFile;
    }
}
