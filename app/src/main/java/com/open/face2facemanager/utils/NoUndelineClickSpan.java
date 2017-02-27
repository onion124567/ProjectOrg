package com.open.face2facemanager.utils;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by Administrator on 2015/9/9.
 */
public class NoUndelineClickSpan extends ClickableSpan {
    public int color=0xffff0000;
    @Override
    public void onClick(View widget) {
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(color);
        ds.setUnderlineText(false);
        ds.setFakeBoldText(true);
    }
}
