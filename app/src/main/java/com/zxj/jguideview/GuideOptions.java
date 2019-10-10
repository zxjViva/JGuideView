package com.zxj.jguideview;

import android.view.View;

public class GuideOptions {
    public int[] rules;
    public View guideView;
    public boolean openAnimation = true;

    public void setOpenAnimation(boolean openAnimation) {
        this.openAnimation = openAnimation;
    }


    public GuideOptions(View guideView) {
        this(guideView,new int[]{GuideLayouter.Rule.BELOW,GuideLayouter.Rule.ALIGN_PARENT_END});
    }

    public GuideOptions(View guideView, int[] rules) {
        this.rules = rules;
        this.guideView = guideView;
    }
}
