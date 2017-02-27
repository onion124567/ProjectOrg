package com.common.view.imagepicker.bean;

/**
 * 项目名称：face2facemanager
 * 类描述：
 * 创建人：zhougl
 * 创建时间：2016/8/12 10:38
 * 修改人：zhougl
 * 修改时间：2016/8/12 10:38
 * 修改备注：
 */
public class ImageItemAndNet extends ImageItem {
    private boolean isLocal = true; //是否是本地图片,默认为本地图片

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }
}
