package com.open.face2facemanager.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.view.loadingview.LevelLoadingRenderer;
import com.common.view.loadingview.LoadingDrawable;
import com.open.face2facemanager.R;

/**
 * 项目名称：zhaosheng1.0
 * 类描述：dialog通用类
 * 创建人：zhougl
 * 创建时间：2016/5/27 15:42
 * 修改人：zhougl
 * 修改时间：2016/5/27 15:42
 * 修改备注：
 */
public class DialogManager {
    private static Dialog netDialog;

    public static void showNormalDialog(Context context, String title, String info, String btnOkStr,String btnDissmissStr, final DialogInterface.OnClickListener listener){
        final Dialog dialog = new Dialog(context, R.style.MyDialog2);
        dialog.setContentView(R.layout.dialog_normal_layout);
        LinearLayout ll_main_layout = (LinearLayout) dialog.findViewById(R.id.ll_main_layout);
        TextView tv_dialog_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);
        TextView tv_dialog_text = (TextView) dialog.findViewById(R.id.tv_dialog_text);
        Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
        Button btn_dismiss = (Button) dialog.findViewById(R.id.btn_dismiss);
        tv_dialog_title.setText("" + title);
        tv_dialog_text.setText("" + info);

        if(btnOkStr!=null){
            btn_ok.setText("" + btnOkStr);
        }else {
            btn_ok.setVisibility(View.GONE);
        }

        if(btnDissmissStr!=null){
            btn_dismiss.setText(""+btnDissmissStr);
        }else {
            btn_dismiss.setVisibility(View.GONE);
        }

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(dialog,DialogInterface.BUTTON_POSITIVE);
            }
        });

        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // 设置全屏大小
        int[] screenRect = getScreenRect((Activity) context);
        dialog.getWindow().setLayout(screenRect[0], screenRect[1]);
        dialog.show();
    }
    public static void showUpdateDialog(Context context, String title, String info, String btnOkStr,boolean canmissStr, final DialogInterface.OnClickListener listener){
        final Dialog dialog = new Dialog(context, R.style.MyDialog2);
        dialog.setContentView(R.layout.dialog_updateapk_layout);
        TextView tv_dialog_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);
        TextView tv_dialog_text = (TextView) dialog.findViewById(R.id.tv_dialog_text);
        TextView btn_ok = (TextView) dialog.findViewById(R.id.btn_dialog_submit);
        View btn_dismiss =  dialog.findViewById(R.id.btn_dialog_cancle);
        tv_dialog_title.setText("" + title);
        tv_dialog_text.setText("" + info);
            btn_ok.setText("" + btnOkStr);
        if(canmissStr){
            btn_dismiss.setVisibility(View.VISIBLE);
            btn_dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }else {
            btn_dismiss.setVisibility(View.GONE);
            dialog.setCancelable(false);
        }

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
            }
        });


        dialog.show();
    }
    public static void showNetLoadingView(Context context){
        netDialog = new Dialog(context, R.style.CustomDialog);
        netDialog.setContentView(R.layout.dialog_net_loading_layout);
        ImageView iv_net_loading = (ImageView) netDialog.findViewById(R.id.iv_net_loading);
        LinearLayout ll_main_layout = (LinearLayout) netDialog.findViewById(R.id.ll_main_layout);
        final LoadingDrawable mLevelDrawable = new LoadingDrawable( new LevelLoadingRenderer(context));
        iv_net_loading.setImageDrawable(mLevelDrawable);
        mLevelDrawable.start();
        netDialog.show();
        netDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mLevelDrawable.stop();
            }
        });
        ll_main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netDialog.dismiss();
            }
        });
        // 设置全屏大小
        int[] screenRect = getScreenRect((Activity) context);
        netDialog.getWindow().setLayout(screenRect[0], screenRect[1]);
    }

    public static  void dismissNetLoadingView(){
        if(netDialog!=null){
            netDialog.dismiss();
        }
    }

    /**
     * 获取当前屏幕的全屏大小
     *
     * @param activity
     * @return
     */
    public static int[] getScreenRect(Activity activity) {

        WindowManager manager = activity.getWindowManager();
        int sreenW = manager.getDefaultDisplay().getWidth();
        int sreenH = manager.getDefaultDisplay().getHeight();
        return new int[]{sreenW, sreenH};
    }


    /**
     * @param context
     * @param text
     * @param message
     * @param click
     */
    public void showVersionUpdateDialog(Context context, String text,
                                        String message, DialogInterface.OnClickListener click, int isforce) {

    }



}
