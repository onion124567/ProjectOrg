package com.open.face2facemanager.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.common.view.imagepicker.bean.ImageItem;
import com.common.view.ninegrid.ImageInfo;
import com.common.view.photoview.PhotoView;
import com.common.view.photoview.PhotoViewAttacher;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class ImageUtils {
    public static int WindowWidth;
    public static int WindowHeight;
    public static float scale;

    public static void setWindowDisplay(Activity ac) {

        WindowWidth = ac.getWindowManager().getDefaultDisplay().getWidth();
        WindowHeight = ac.getWindowManager().getDefaultDisplay().getHeight();

    }

    public static void setScale(Context context) {
        scale = context.getResources().getDisplayMetrics().density;
    }

    public static int dip2px(float dipValue) {
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(float pxValue) {
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据宽度等比例缩放图
     *
     * @param defaultBitmap
     * @param width
     * @return
     */
    public static Bitmap resizeImageByWidth(Bitmap defaultBitmap,
                                            int targetWidth) {
        int rawWidth = defaultBitmap.getWidth();
        int rawHeight = defaultBitmap.getHeight();
        float targetHeight = targetWidth * (float) rawHeight / (float) rawWidth;
        float scaleWidth = targetWidth / (float) rawWidth;
        float scaleHeight = targetHeight / (float) rawHeight;
        Matrix localMatrix = new Matrix();
        localMatrix.postScale(scaleHeight, scaleWidth);
        return Bitmap.createBitmap(defaultBitmap, 0, 0, rawWidth, rawHeight,
                localMatrix, true);
    }

    public static byte[] getByte4UpLoading(Uri uri, Context context)
            throws FileNotFoundException, IOException {
        byte[] buf = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                context.getContentResolver(), uri);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        buf = out.toByteArray();
        return buf;
    }

    public static byte[] comp(Uri uri, Context context)
            throws FileNotFoundException, IOException {
        Bitmap image = MediaStore.Images.Media.getBitmap(
                context.getContentResolver(), uri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    public static byte[] compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 50) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        // ByteArrayInputStream isBm = new
        // ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        // Bitmap bitmap = BitmapFactory.decodeStream(isBm, null,
        // null);//把ByteArrayInputStream数据生成图片
        return baos.toByteArray();
    }

    // 让图片宽度充满全屏，得到等比例高度。
    public static float getPicHeight(int picw, int pich, int viewW) {
        if (WindowWidth == 0)
            return pich;
        if (viewW == -2) {// 默认值
            return pich;
        }
        if (viewW <= 0)// 充满父容器
            return ((float) WindowWidth / picw) * pich;
        return ((float) viewW / picw) * pich;
    }

    /**
     * 读取本地或网络图片，转换为Bitmap
     *
     * @param url
     * @return
     */
    public static Bitmap GetLocalOrNetBitmap(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new URL(url).openStream(), 2 * 1024);
            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, 2 * 1024);
            copy(in, out);
            out.flush();
            byte[] data = dataStream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            data = null;
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void copy(InputStream in, OutputStream out)
            throws IOException {
        byte[] b = new byte[2 * 1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    public static Bitmap changeSizeBitmap(String pathName, int width, int height) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, opts);
        // 计算收缩比例
        int xScale = opts.outWidth / width;
        int yScale = opts.outHeight / height;
        opts.inSampleSize = xScale > yScale ? xScale : yScale;
        // 取消仅加载边界的设置
        opts.inJustDecodeBounds = false;
        // 按选项设置加载图片
        Bitmap bm = BitmapFactory.decodeFile(pathName, opts);
        return bm;
    }

    public static Bitmap getBitmap(Resources res, int id, int width, int height) {
        Bitmap bm = null;
        if (res != null && id != 0) {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, id, opts);
            int y = opts.outHeight / height;
            int x = opts.outWidth / width;
            opts.inSampleSize = x > y ? x : y;
            opts.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeResource(res, id, opts);
        }
        return bm;
    }

    public static Bitmap getBitmap(String path) {
        Bitmap bm = null;
        if (path != null) {
            bm = BitmapFactory.decodeFile(path);
        }
        return bm;
    }

    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        float scalesize = scaleHeight < scaleWidth ? scaleHeight : scaleWidth;
        matrix.postScale(scalesize, scalesize);
//            matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
        return newbm;
    }

    public static void save(Bitmap bm, File f) throws FileNotFoundException {
        bm.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(f));
    }

    public static Bitmap chopBitmap(Bitmap bm, int poisition, int width,
                                    int height) {
        return Bitmap.createBitmap(bm, 0, poisition * height, width, height);
    }

/*	public static String getImagePath(String remoteUrl)
    {
		String imageName= remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1, remoteUrl.length());
		String path =PathUtil.getInstance().getImagePath()+"/"+ imageName;
		EMLog.d("msg", "image path:" + path);
		return path;

	}


	public static String getThumbnailImagePath(String thumbRemoteUrl) {
		String thumbImageName= thumbRemoteUrl.substring(thumbRemoteUrl.lastIndexOf("/") + 1, thumbRemoteUrl.length());
		String path =PathUtil.getInstance().getImagePath()+"/"+ "th"+thumbImageName;
		EMLog.d("msg", "thum image path:" + path);
		return path;
	}*/


    public static void changeLight(ImageView imageView, int brightness) {
        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(new float[]{1, 0, 0, 0, brightness,
                0, 1, 0, 0, brightness,// 改变亮度
                0, 0, 1, 0, brightness,
                0, 0, 0, 1, 0});
        imageView.setColorFilter(new ColorMatrixColorFilter(cMatrix));
    }

    public static void changeColorByRGB(ImageView imageView, float R, float G, float B) {
        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(new float[]{R, 0, 0, 0, 1,
                0, G, 0, 0, 1,// 改变亮度
                0, 0, B, 0, 1,
                0, 0, 0, 1, 0});
        imageView.setColorFilter(new ColorMatrixColorFilter(cMatrix));
    }

    public static void changeColorByRGB(ImageView imageView, String R, String G, String B) {
        changeColorByRGB(imageView, Float.parseFloat(R) / 100, Float.parseFloat(G) / 100, Float.parseFloat(B) / 100);
    }


    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap rotate(Bitmap b, int degrees) {
        if (degrees == 0) {
            return b;
        }
        if (degrees != 0 && b != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) b.getWidth(),
                    (float) b.getHeight());
            try {
                Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
                        b.getHeight(), m, true);
                if (b != b2) {
                    b.recycle(); // Android开发网再次提示Bitmap操作完应该显示的释放
                    b = b2;
                }
            } catch (OutOfMemoryError ex) {
                ex.printStackTrace();
// Android123建议大家如何出现了内存不足异常，最好return 原始的bitmap对象。.
            }
        }
        return b;
    }
   //网络图片的review拉伸
    public static void setPhtotScale(PhotoView imageView, ImageInfo info, float screenWidth, float screenHeight) {
        //设置最大拉伸值  以短边与屏幕宽高计算
        float maxScale = 4.0f;
        if (info.width > 0 && info.height > 0) {
            if (info.getHeight() > info.getWidth()) {//以宽计算
                float widthScale = info.getHeight() > screenHeight ? info.getHeight() * 1.0f / screenHeight : 1.0f;
                maxScale = info.getWidth() > screenWidth ? maxScale : screenWidth / info.getWidth() * widthScale;
            } else {//以高计算
                float heightScale = info.getWidth() > screenWidth ? info.getWidth() * 1.0f / screenWidth : 1.0f;
                maxScale = info.getHeight() > screenHeight ? maxScale : screenHeight / info.getHeight() * heightScale;
            }
            if(maxScale> PhotoViewAttacher.DEFAULT_MAX_SCALE)
            imageView.setMaxScale(maxScale);
        }
    }

    //本地的revuew拉伸
    public static void setPhtotScale(PhotoView imageView, ImageItem info, float screenWidth, float screenHeight) {
        //设置最大拉伸值  以短边与屏幕宽高计算
        float maxScale = 4.0f;
        if (info.width > 0 && info.height > 0) {
            if (info.height > info.width) {//以宽计算
                float widthScale = info.height > screenHeight ? info.height * 1.0f / screenHeight : 1.0f;
                maxScale = info.width > screenWidth ? maxScale : screenWidth / info.width * widthScale;
            } else {//以高计算
                float heightScale = info.width > screenWidth ? info.width * 1.0f / screenWidth : 1.0f;
                maxScale = info.height > screenHeight ? maxScale : screenHeight / info.height * heightScale;
            }
            imageView.setMaxScale(maxScale);
        }
    }
}
