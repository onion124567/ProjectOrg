package com.open.face2facemanager.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.widget.ImageView;

import com.open.face2facemanager.R;
import com.open.face2facemanager.business.baseandcommon.TApplication;


/**
 * Created by Administrator on 2015/12/16.
 */
public class DownAndFlurHelper {
    ImageView ivDesc;
    String loadpath;
    int defaultDrawableId = R.mipmap.icon_default;
    public boolean needScalType = false;

    public DownAndFlurHelper(ImageView ivDesc, String path) {
        this.ivDesc = ivDesc;
        this.loadpath = path;
    }

    public DownAndFlurHelper(ImageView ivDesc, String loadpath, int defaultDrawableId) {
        this.ivDesc = ivDesc;
        this.loadpath = loadpath;
        this.defaultDrawableId = defaultDrawableId;
    }

    /*public void downFlurUtil(){
        ImageLoader.getInstance().loadImage(loadpath, imageLoadingListener);
    }
    ImageLoadingListener imageLoadingListener = new ImgLoadListener() {
        @Override
        public void onLoadingStarted(String imageUri, View view) {

        }

        public void onLoadingFailed(String imageUri, View view,
                                    FailReason failReason) {
            ivDesc.setImageResource(defaultDrawableId);
        }

        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            ViewGroup.LayoutParams params = ivDesc.getLayoutParams();
            if (loadedImage == null) {
                ivDesc.setImageResource(defaultDrawableId);
                return;
            }
            if(needScalType){
            params.height = (int) ImageUtils.getPicHeight(loadedImage.getWidth(), loadedImage.getHeight(), params.width);
            ivDesc.setLayoutParams(params);
            ivDesc.setScaleType(ImageView.ScaleType.FIT_XY);
            }
            String path = IOUtils.saveFile(loadedImage, loadpath);
            if (path == null || path.isEmpty()) {
                ivDesc.setImageResource(R.drawable.ad_default_xh);
                return;
            }
            loadedImage = BitmapFactory.decodeFile(path);
            loadedImage = UIUtils.blurBitmap(loadedImage);
//            loadedImage=  FastBlur.doBlur(loadedImage,25,true);
//            ivDesc.setColorFilter(Color.rgb(55,55,55),PorterDuff.Mode.LIGHTEN);
            ivDesc.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            ivDesc.setImageBitmap(loadedImage);
        }

        public void onLoadingCancelled(String imageUri, View view) {
            ivDesc.setImageResource(defaultDrawableId);
        }
    };*/

    public static Bitmap getBlurScreenShoot(View view) {
        Bitmap bitmap = BitmapFactory.decodeFile(ScreenShootHelper.getScreenShoot(view));
        return blurBitmap(bitmap);
    }

    /**
     * @param bitmap
     * @return
     */
    public static Bitmap blurBitmap(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT < 15) return bitmap;
        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        //Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(TApplication.getInstance());

        //Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        //Set the radius of the blur
        blurScript.setRadius(12.f);

        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        //recycle the original bitmap
        bitmap.recycle();

        //After finishing everything, we destroy the Renderscript.
        rs.destroy();

        return outBitmap;
    }
}
