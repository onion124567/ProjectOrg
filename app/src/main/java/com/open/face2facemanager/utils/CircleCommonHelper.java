package com.open.face2facemanager.utils;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.common.view.MyPopWindow;
import com.open.face2facemanager.R;
import com.open.face2facemanager.business.baseandcommon.TApplication;
import com.open.face2facemanager.factory.bean.base.OpenResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * 举报通用类
 * Created by Administrator on 2015/12/21.
 */
public class CircleCommonHelper {
    MyPopWindow pop;

    public void report(final Activity context, final Integer commentId, final long topicId) {
        pop = new MyPopWindow(context,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        switch (v.getId()) {
                            // 举报
                            case R.id.pop_xi:
                                pop.dismiss();
//                                BaseRequest<ReportRequest> baseRequest = new BaseRequest();
//                                ReportRequest reportRequest = new ReportRequest();
//                                reportRequest.setTopicId(topicId);
//                                reportRequest.setCommentId(commentId);
//                                reportRequest.setUserId(TApplication.getInstance().request.getUserId());
//                                baseRequest.setParams(reportRequest);
//                                Observable<OpenResponse> responseObservable= TApplication.getServerAPI().reportSpeak(baseRequest);
//                                responseObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<OpenResponse>() {
//                                    @Override
//                                    public void call(OpenResponse openResponse) {
//                                        TApplication.getInstance().showToast(openResponse.getMessage()+"");
//                                    }
//                                });
//                                Toast.makeText(context, "感谢您的举报，我们会及时处理", Toast.LENGTH_SHORT).show();
                                break;
                            // 取消
                            case R.id.pop_no:
                                pop.dismiss();
                                break;
                        }
                    }
                });
        pop.setMessage("举报", null, "取消");
        ScreenUtils.closeKeybord(context);
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        pop.showAtLocation(context.getWindow().getDecorView(), Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0);
    }



}
