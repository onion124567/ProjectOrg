package com.open.face2facemanager.business.baseandcommon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.common.view.avatar.ClipSquareImageView;
import com.open.face2facemanager.R;
import com.open.face2facemanager.utils.BitmapHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;


/**
 * Created by Administrator on 2015/11/4.
 */
public class ShowPicActivity extends BaseActivity {
    private String photoName;


    private ImageView iv_submit;
    private ImageView iv_back;
    private Bitmap oldbitmap;
    private Bitmap bitmap;

    private String path;

    private File fileFolder = null;
    private File jpgFile = null;
    private byte[] data = null;
    private ClipSquareImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showpicc);
        photoName = getIntent().getStringExtra("photoName");
        try {
            oldbitmap=   BitmapHelper.revitionImageSizeBydegree(photoName);
        } catch (IOException e) {
            e.printStackTrace();
        }
//         = BitmapFactory.decodeFile(photoName);
        imageView = (ClipSquareImageView) findViewById(R.id.cimg);
        imageView.setImageBitmap(oldbitmap);
        Log.i("onion", "photoName: " + photoName+"/"+oldbitmap);
        intView();

    }


    private void intView() {
        iv_submit = (ImageView) findViewById(R.id.iv_submit);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra("photoName", "");
                setResult(RESULT_CANCELED, data);
                finish();
            }
        });
        iv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 此处获取剪裁后的bitmap
                bitmap = imageView.clip();

                // 由于Intent传递bitmap不能超过40k,此处使用二进制数组传递
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
               // photoName = saveToSDCard(baos);
                Intent data = new Intent();
                data.putExtra("photoName", photoName);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent data = new Intent();
            data.putExtra("photoName", "");
            setResult(RESULT_CANCELED, data);
        }
        return true;

    }
//    private String saveToSDCard(final ByteArrayOutputStream baos) {
//        String fileName;
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        data = baos.toByteArray();
//        fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
//
//        if (Environment.MEDIA_MOUNTED.equals(Environment
//                .getExternalStorageState())) {
//            path = Environment.getExternalStorageDirectory().getAbsolutePath()
//                    + File.separator + Config.BASE_GROUP_CACHE +
//                    File.separator + "image";
//            fileFolder = new File(path);
//        } else {
//            path = ShowPicActivity.this.getCacheDir()
//                    + File.separator + Config.BASE_GROUP_CACHE +
//                    File.separator + "image";
//            fileFolder = new File(path);
//        }
//        if (!fileFolder.exists()) { // 如果目录不存在，则创建一个名为"finger"的目录
//            fileFolder.mkdirs();
//        }
//        jpgFile = new File(fileFolder, fileName);
//        FileOutputStream outputStream = null;
//        try {
//            jpgFile.createNewFile();
//            outputStream = new FileOutputStream(jpgFile); // 文件输出流
//            outputStream.write(data); // 写入sd卡中
//        } catch (FileNotFoundException e) {
//
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {// 关闭输出流
//            if (outputStream != null)
//                try {
//                    outputStream.close();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//        }
//        return path + File.separator + fileName;
//    }


}
