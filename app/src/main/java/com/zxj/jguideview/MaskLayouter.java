package com.zxj.jguideview;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

public class MaskLayouter implements ViewTreeObserver.OnGlobalLayoutListener{
    private final ViewGroup rootView;
    private final View targetView;
    MaskOptions maskOptions;
    private Rect targetViewRect;
    private MaskCalculator calculator;
    public MaskLayouter(View targetView, ViewGroup rootView) {
        this.targetView = targetView;
        this.rootView = rootView;
    }

    public void setCalculator(MaskCalculator calculator) {
        this.calculator = calculator;
    }

    public void setTargetViewRect(Rect targetViewRect) {
        this.targetViewRect = targetViewRect;
    }
    public void addOnGlobalLayoutListener(){
        ViewTreeObserver viewTreeObserver = targetView.getViewTreeObserver();
        if (viewTreeObserver != null){
            viewTreeObserver.removeOnGlobalLayoutListener(this);
            viewTreeObserver.addOnGlobalLayoutListener(this);
        }
    }
    public void removeOnGlobalLayoutListener(){
        ViewTreeObserver viewTreeObserver = targetView.getViewTreeObserver();
        if (viewTreeObserver != null){
            viewTreeObserver.removeOnGlobalLayoutListener(this);
        }

    }

    @Override
    public void onGlobalLayout() {
        if (calculator != null){
            targetViewRect = calculator.calculateRect(targetView, rootView);
            layout();
        }
    }

    public interface Gravity{
        int LEFT = 1;
        int RIGHT = 1 << 1;
        int TOP = 1 << 2;
        int BOTTOM = 1 << 3;
        int CENTER_HORZ = 1 << 4;
        int CENTER_VERT = 1 << 5;
        int CENTER = 11 << 4;
        int COVER = 0b1001;

    }

    public void setMaskOptions(MaskOptions maskOptions) {
        this.maskOptions = maskOptions;
    }

    public void layout(){
        View guideView = maskOptions.guideView;
        if (guideView.getParent() == null){
            rootView.addView(guideView);
        }
        int gravity = maskOptions.gravity;

        if ((gravity & Gravity.LEFT) == Gravity.LEFT){
            guideView.setX(targetViewRect.left);
        }
        if ((gravity & Gravity.RIGHT) == Gravity.RIGHT){
            guideView.setX(targetViewRect.right);
        }
        if ((gravity & Gravity.TOP) == Gravity.TOP){
            guideView.setY(targetViewRect.top);
        }
        if ((gravity & Gravity.BOTTOM) == Gravity.BOTTOM){
            guideView.setY(targetViewRect.bottom);

        }
        if ((gravity & Gravity.CENTER_HORZ) == Gravity.CENTER_HORZ){
            guideView.setX((targetViewRect.left + targetViewRect.right)/2);
        }
        if ((gravity & Gravity.CENTER_VERT) == Gravity.CENTER_VERT){
            guideView.setY((targetViewRect.top + targetViewRect.bottom)/2);
        }

    }

}
