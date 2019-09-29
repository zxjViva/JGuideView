package com.zxj.jguideview;

import android.view.View;

public class GuideOptions {
    public int gravity;
    public View guideView;
    public GuideLayouter.AlignOrientation orientation = GuideLayouter.AlignOrientation.LEFT;
    public boolean openAnimation = true;

    public void setOpenAnimation(boolean openAnimation) {
        this.openAnimation = openAnimation;
    }

    public void setOrientation(GuideLayouter.AlignOrientation orientation) {
        this.orientation = orientation;
    }

    private static final int DEFAULT_GRAVITY = GuideLayouter.Gravity.BOTTOM
            | GuideLayouter.Gravity.CENTER_HORZ;
    public GuideOptions(View guideView) {
        this(guideView,DEFAULT_GRAVITY);
    }

    public GuideOptions(View guideView, int gravity) {
        this.gravity = gravity;
        this.guideView = guideView;
    }
}
