package com.open.face2facemanager.business.baseandcommon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.common.view.imagepicker.ImagePicker;
import com.common.view.imagepicker.bean.ImageItem;
import com.common.view.imagepicker.ui.ImageGridActivity;
import com.common.view.imagepicker.ui.ImagePreviewDelActivity;
import com.open.face2facemanager.R;
import com.open.face2facemanager.business.adapter.ImagePickerAdapter;
import com.open.face2facemanager.utils.TongjiUtil;

import java.util.ArrayList;

import rx.functions.Action1;

/**
 * Created by onion on 2016/8/2.
 */
public abstract class ImgPickWithTxtActivity <View extends  MPresenter>extends BaseActivity<View>implements ImagePickerAdapter.OnRecyclerViewItemClickListener {

    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    protected ImagePickerAdapter adapter;
    //private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    protected int maxImgCount = 6;               //允许选择图片最大数
    protected EditText edit_speak_content;
    protected TextView text_num;
    protected ImagePicker imagePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getCotentViewId());
        //最好放到 Application oncreate执行
        initImagePicker();
        initWidget();
    }

    protected abstract int getCotentViewId();

    private void initImagePicker() {
        /*ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(false);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素*/
        imagePicker = ImagePicker.getInstance();
        imagePicker.clear();
        TApplication.getInstance().changePickerMode(true, maxImgCount);
    }

    protected Action1<MenuItem> menuClick = null;
    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (menuClick != null)
                menuClick.call(menuItem);
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_imgpick, menu);
//        ImageView imageView = new ImageView(this);
//        imageView.setImageResource(R.mipmap.icon_default);
//        menu.findItem(R.id.action_collect).setActionView(imageView);
        return super.onCreateOptionsMenu(menu);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int num = 500 - s.length();

            SpannableString sp = new SpannableString(num+"/"+500);
            if(num<0){
                ForegroundColorSpan fromSpan = new ForegroundColorSpan(0xffff0000);
                sp.setSpan(fromSpan, 0, String.valueOf(num).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            text_num.setText(sp);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void initTitle(String title) {
        super.initTitle(title);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
    }

    private void initWidget() {
        text_num = (TextView) findViewById(R.id.text_num);
        edit_speak_content = (EditText) findViewById(R.id.edit_speak_content);
        edit_speak_content.addTextChangedListener(textWatcher);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, imagePicker.getSelectedImages(), maxImgCount);
        adapter.setOnItemClickListener(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onItemClick(android.view.View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                //打开选择,本次允许选择的数量
//                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                TongjiUtil.tongJiOnEvent(this,"id_post_photo","");
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT);
                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("onion", "actigvity" + this);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
//                selImageList.addAll(images);
//                imagePicker.getSelectedImages().addAll(images);
                adapter.setImages(imagePicker.getSelectedImages());
            }

        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
//                selImageList.clear();
//                selImageList.addAll(images);
                imagePicker.clearSelectedImages();
                imagePicker.getSelectedImages().addAll(images);
                adapter.setImages(imagePicker.getSelectedImages());
            }
        }
    }
}
