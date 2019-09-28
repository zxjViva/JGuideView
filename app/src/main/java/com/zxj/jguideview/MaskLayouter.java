package com.zxj.jguideview;

import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import java.util.Locale;

public class MaskLayouter implements ViewTreeObserver.OnGlobalLayoutListener {
    private final ViewGroup rootView;
    private final View targetView;
    MaskOptions maskOptions;
    private Rect targetViewRect;
    private MaskCalculator calculator;
    private boolean isVisible;

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

    public void addOnGlobalLayoutListener() {
        ViewTreeObserver viewTreeObserver = targetView.getViewTreeObserver();
        if (viewTreeObserver != null) {
            viewTreeObserver.removeOnGlobalLayoutListener(this);
            viewTreeObserver.addOnGlobalLayoutListener(this);
        }
    }

    public void removeOnGlobalLayoutListener() {
        ViewTreeObserver viewTreeObserver = targetView.getViewTreeObserver();
        if (viewTreeObserver != null) {
            viewTreeObserver.removeOnGlobalLayoutListener(this);
        }

    }

    @Override
    public void onGlobalLayout() {
        if (calculator != null) {
            targetViewRect = calculator.calculateRect(targetView, rootView);
            layout();
        }
    }

    public interface Gravity {
        int LEFT = 1;//0b01
        int RIGHT = 1 << 1;//0b10
        int CENTER_HORZ = LEFT | RIGHT;//0b11
        int TOP = 1 << 2;//0b0100
        int BOTTOM = 1 << 3;//0b1000
        int CENTER_VERT = TOP | BOTTOM;//0b1100
        int CENTER = CENTER_HORZ | CENTER_VERT;
        int COVER = LEFT | TOP;
    }
    //对齐方式，左对齐，右对齐
    public enum AlignOrientation{
        LEFT,
        RIGHT
    }

    public void setMaskOptions(MaskOptions maskOptions) {
        this.maskOptions = maskOptions;
    }

    public void layout() {
        if (maskOptions == null){
            return;
        }
        View guideView = maskOptions.guideView;
        if (guideView.getParent() == null) {
            addToRoot(guideView);
        }
        if (isVisible){
            visible();
        }else {
            invisible();
        }
        int gravity = maskOptions.gravity;
        int x = 0;
        int y = 0;

        if ((gravity & Gravity.CENTER_HORZ) == Gravity.CENTER_HORZ) {
            x = (targetViewRect.left + targetViewRect.right) / 2;
        } else if ((gravity & Gravity.LEFT) == Gravity.LEFT) {
            x = targetViewRect.left;
        } else if ((gravity & Gravity.RIGHT) == Gravity.RIGHT) {
            x = targetViewRect.right;
        }

        if ((gravity & Gravity.CENTER_VERT) == Gravity.CENTER_VERT) {
            y = (targetViewRect.top + targetViewRect.bottom) / 2;
        } else if ((gravity & Gravity.TOP) == Gravity.TOP) {
            y = targetViewRect.top;
        } else if ((gravity & Gravity.BOTTOM) == Gravity.BOTTOM) {
            y = targetViewRect.bottom;
        }
        if (maskOptions.orientation == AlignOrientation.RIGHT){
            x = x - guideView.getMeasuredWidth();
        }
        Log.e("zxj", "coordinate: " + guideView.getX() + "," + guideView.getY());
        Log.e("zxj", "visible: " + guideView.getVisibility());
        guideView.setX(x);
        guideView.setY(y);
    }
    private void addToRoot(View view){
        if (rootView instanceof ScrollView){
            FrameLayout frameLayout = new FrameLayout(view.getContext());
            View childAt = rootView.getChildAt(0);
            rootView.removeView(childAt);
            frameLayout.addView(childAt);
            frameLayout.addView(view);
            rootView.addView(frameLayout);
        }else {
            rootView.addView(view);
        }

    }

    public void gone() {
        invisible();
        removeOnGlobalLayoutListener();
    }

    void invisible() {
        isVisible = false;
        if (maskOptions != null && maskOptions.guideView.getVisibility() == View.VISIBLE) {
            if (maskOptions.openAnimation) {
                AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
                alphaAnimation.setDuration(200);
                maskOptions.guideView.startAnimation(alphaAnimation);
            }
            maskOptions.guideView.setVisibility(View.INVISIBLE);
        }
    }

    public void visible() {
        isVisible = true;
        if (maskOptions != null && maskOptions.guideView.getVisibility() != View.VISIBLE) {
            if (maskOptions.openAnimation) {
                AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
                alphaAnimation.setDuration(200);
                maskOptions.guideView.startAnimation(alphaAnimation);
            }
            maskOptions.guideView.setVisibility(View.VISIBLE);
        }
    }
    private boolean isRtl() {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_RTL;
    }
}
