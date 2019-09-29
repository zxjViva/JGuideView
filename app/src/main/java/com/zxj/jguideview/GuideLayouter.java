package com.zxj.jguideview;

import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import java.util.Locale;

public class GuideLayouter implements ViewTreeObserver.OnGlobalLayoutListener {
    private final ViewGroup rootView;
    private final View targetView;
    GuideOptions guideOptions;
    private Rect targetViewRect;
    private GuideCalculator calculator;
    private boolean isVisible;

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public GuideLayouter(View targetView, ViewGroup rootView) {
        this.targetView = targetView;
        this.rootView = rootView;
    }

    void setCalculator(GuideCalculator calculator) {
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
        RIGHT,
    }

    public void setGuideOptions(GuideOptions guideOptions) {
        this.guideOptions = guideOptions;
    }

    public void layout() {
        if (guideOptions == null){
            return;
        }
        Rect tempRect = new Rect(targetViewRect);
        AlignOrientation tempOri = guideOptions.orientation;
        View guideView = guideOptions.guideView;
        if (guideView.getParent() == null) {
            addToRoot(guideView);
        }
        if (isVisible){
            visible();
        }else {
            invisible();
        }

        if (isRtl()){
            tempRect = new Rect(tempRect.right,tempRect.top,tempRect.left,tempRect.bottom);
            if (tempOri == AlignOrientation.RIGHT){
                tempOri = AlignOrientation.LEFT;
            }else {
                tempOri = AlignOrientation.RIGHT;
            }

        }
        int gravity = guideOptions.gravity;
        int x = tempRect.left;
        int y = tempRect.bottom;

        if ((gravity & Gravity.CENTER_HORZ) == Gravity.CENTER_HORZ) {
            x = (tempRect.left + tempRect.right) / 2;
        } else if ((gravity & Gravity.LEFT) == Gravity.LEFT) {
            x = tempRect.left;
        } else if ((gravity & Gravity.RIGHT) == Gravity.RIGHT) {
            x = tempRect.right;
        }

        if ((gravity & Gravity.CENTER_VERT) == Gravity.CENTER_VERT) {
            y = (tempRect.top + tempRect.bottom) / 2;
        } else if ((gravity & Gravity.TOP) == Gravity.TOP) {
            y = tempRect.top - guideView.getMeasuredHeight();
        } else if ((gravity & Gravity.BOTTOM) == Gravity.BOTTOM) {
            y = tempRect.bottom;
        }
        if (tempOri == AlignOrientation.RIGHT){
            x = x - guideView.getMeasuredWidth();
        }
        guideView.setX(x);
        guideView.setY(y);

        Log.e("zxj", "target: " + tempRect.toString() );
        Log.e("zxj", "coordinate: " + guideView.getX() + "," + guideView.getY());
        Log.e("zxj", "visible: " + guideView.getVisibility());

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
        if (guideOptions != null && guideOptions.guideView.getVisibility() == View.VISIBLE) {
            if (guideOptions.openAnimation) {
                AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
                alphaAnimation.setDuration(200);
                guideOptions.guideView.startAnimation(alphaAnimation);
            }
            guideOptions.guideView.setVisibility(View.INVISIBLE);
        }
    }

    public void visible() {
        isVisible = true;
        if (guideOptions != null && guideOptions.guideView.getVisibility() != View.VISIBLE) {
            if (guideOptions.openAnimation) {
                AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
                alphaAnimation.setDuration(200);
                guideOptions.guideView.startAnimation(alphaAnimation);
            }
            guideOptions.guideView.setVisibility(View.VISIBLE);
        }
    }
    private boolean isRtl() {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_RTL;
    }
}
