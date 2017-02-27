package com.common.view.ninegrid;

import java.io.Serializable;

/**
 * by onion
 */
public class ImageInfo implements Serializable {

    /**
     * imgId : 38685
     * thumbnailUrl : http://www.pp3.cn/uploads/allimg/111124/155ASV8-8.jpg
     * thumbWidth : 40
     * thumbHeight : 50
     * bigWidth : 1920
     * bigHeight : 1080
     * bigImageUrl : http://www.pp3.cn/uploads/allimg/111124/155ASV8-8.jpg
     */
    private  long orderList;
    public int identification;
    public String thu_url;
    //原图宽度
    public int thu_width;
    public int thu_height;
    public int width;
    public int height;
    public String url;

    public long getOrderList() {
        return orderList;
    }

    public void setOrderList(long orderList) {
        this.orderList = orderList;
    }

    public int getIdentification() {
        return identification;
    }

    public void setIdentification(int identification) {
        this.identification = identification;
    }

    public String getThu_url() {
        return thu_url;
    }

    public void setThu_url(String thu_url) {
        this.thu_url = thu_url;
    }

    public int getThu_width() {
        return thu_width;
    }

    public void setThu_width(int thu_width) {
        this.thu_width = thu_width;
    }

    public int getThu_height() {
        return thu_height;
    }

    public void setThu_height(int thu_height) {
        this.thu_height = thu_height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //用来做跳转放大动画
    public int imageViewWidth;
    public int imageViewHeight;
    public int imageViewX;
    public int imageViewY;
   //用来做单图时，全部显示
    public int viewWidth;
    public int viewHeight;

}
