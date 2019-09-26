package com.zxj.jguideview;

import android.view.View;

public class MaskOptions {
    public int gravity;
    public View guideView;
    private static final int DEFAULT_GRAVITY = MaskLayouter.Gravity.BOTTOM
            | MaskLayouter.Gravity.CENTER_HORZ;
    public MaskOptions(View guideView) {
        this(guideView,DEFAULT_GRAVITY);
    }

    public MaskOptions(View guideView,int gravity) {
        this.gravity = gravity;
        this.guideView = guideView;
    }
}
