package com.example.jguideviewlib;

import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.Locale;

public class GuideLayouter implements ViewTreeObserver.OnGlobalLayoutListener {
    private static final int TAG_TEMPLATE_VIEW_ID1 = 1;
    private static final int TAG_TEMPLATE_VIEW_ID2 = 2;
    private final ViewGroup rootView;
    private final View targetView;
    GuideOptions guideOptions;
    private Rect targetViewRect;
    private Rect lastTargetViwRect;
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

    public interface Rule {
        /**
         * Rule that aligns a child's right edge with another child's left edge.
         */
        public static final int LEFT_OF = 0;
        /**
         * Rule that aligns a child's left edge with another child's right edge.
         */
        public static final int RIGHT_OF = 1;
        /**
         * Rule that aligns a child's bottom edge with another child's top edge.
         */
        public static final int ABOVE = 2;
        /**
         * Rule that aligns a child's top edge with another child's bottom edge.
         */
        public static final int BELOW = 3;

        /**
         * Rule that aligns a child's baseline with another child's baseline.
         */
        public static final int ALIGN_BASELINE = 4;
        /**
         * Rule that aligns a child's left edge with another child's left edge.
         */
        public static final int ALIGN_LEFT = 5;
        /**
         * Rule that aligns a child's top edge with another child's top edge.
         */
        public static final int ALIGN_TOP = 6;
        /**
         * Rule that aligns a child's right edge with another child's right edge.
         */
        public static final int ALIGN_RIGHT = 7;
        /**
         * Rule that aligns a child's bottom edge with another child's bottom edge.
         */
        public static final int ALIGN_BOTTOM = 8;

        /**
         * Rule that aligns the child's left edge with its RelativeLayout
         * parent's left edge.
         */
        public static final int ALIGN_PARENT_LEFT = 9;
        /**
         * Rule that aligns the child's top edge with its RelativeLayout
         * parent's top edge.
         */
        public static final int ALIGN_PARENT_TOP = 10;
        /**
         * Rule that aligns the child's right edge with its RelativeLayout
         * parent's right edge.
         */
        public static final int ALIGN_PARENT_RIGHT = 11;
        /**
         * Rule that aligns the child's bottom edge with its RelativeLayout
         * parent's bottom edge.
         */
        public static final int ALIGN_PARENT_BOTTOM = 12;

        /**
         * Rule that centers the child with respect to the bounds of its
         * RelativeLayout parent.
         */
        public static final int CENTER_IN_PARENT = 13;
        /**
         * Rule that centers the child horizontally with respect to the
         * bounds of its RelativeLayout parent.
         */
        public static final int CENTER_HORIZONTAL = 14;
        /**
         * Rule that centers the child vertically with respect to the
         * bounds of its RelativeLayout parent.
         */
        public static final int CENTER_VERTICAL = 15;
        /**
         * Rule that aligns a child's end edge with another child's start edge.
         */
        public static final int START_OF = 16;
        /**
         * Rule that aligns a child's start edge with another child's end edge.
         */
        public static final int END_OF = 17;
        /**
         * Rule that aligns a child's start edge with another child's start edge.
         */
        public static final int ALIGN_START = 18;
        /**
         * Rule that aligns a child's end edge with another child's end edge.
         */
        public static final int ALIGN_END = 19;
        /**
         * Rule that aligns the child's start edge with its RelativeLayout
         * parent's start edge.
         */
        public static final int ALIGN_PARENT_START = 20;
        /**
         * Rule that aligns the child's end edge with its RelativeLayout
         * parent's end edge.
         */
        public static final int ALIGN_PARENT_END = 21;
    }


    public void setGuideOptions(GuideOptions guideOptions) {
        this.guideOptions = guideOptions;
    }

    public void layout() {
        if (guideOptions == null || (lastTargetViwRect != null && lastTargetViwRect.equals(targetViewRect))
                || targetView.getMeasuredWidth() == 0) {
            return;
        }
        Rect tempRect = new Rect(targetViewRect);
        View guideView = guideOptions.guideView;
        if (guideView.getParent() == null) {
            addToRoot(guideView);
        }
        if (isVisible) {
            visible();
        } else {
            invisible();
        }
        RelativeLayout parent = (RelativeLayout) guideView.getParent();
        View tagview1 = parent.findViewById(TAG_TEMPLATE_VIEW_ID1);
        View tagview2 = parent.findViewById(TAG_TEMPLATE_VIEW_ID2);
        if (tagview1 == null) {
            tagview1 = new View(guideView.getContext());
            tagview2 = new View(guideView.getContext());
            RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(tempRect.right - tempRect.left,
                    tempRect.bottom - tempRect.top);
            int marginStart = isRtl() ? (rootView.getMeasuredWidth() - tempRect.right) : tempRect.left;
            layoutParams1.setMarginStart(marginStart);
            layoutParams1.topMargin = tempRect.top;
            tagview1.setLayoutParams(layoutParams1);
            tagview1.setId(TAG_TEMPLATE_VIEW_ID1);

            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(tempRect.right - tempRect.left,
                    tempRect.bottom - tempRect.top);
            parent.addView(tagview1);
            tagview2.setLayoutParams(layoutParams2);
            tagview2.setId(TAG_TEMPLATE_VIEW_ID2);
            layoutParams2.addRule(Rule.ALIGN_START,tagview1.getId());
            layoutParams2.addRule(Rule.ALIGN_TOP,tagview1.getId());
            parent.addView(tagview2);
        } else {
            RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(tempRect.right - tempRect.left,
                    tempRect.bottom - tempRect.top);
            int marginStart = isRtl() ? (rootView.getMeasuredWidth() - tempRect.right) : tempRect.left;
            layoutParams1.setMarginStart(marginStart);
            layoutParams1.topMargin = tempRect.top;
            tagview1.setLayoutParams(layoutParams1);
            ViewGroup.LayoutParams layoutParams2 = tagview2.getLayoutParams();
            layoutParams2.height = tempRect.bottom - tempRect.top;
            layoutParams2.width = tempRect.right - tempRect.left;
            tagview2.setLayoutParams(layoutParams2);
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) guideView.getLayoutParams();
        for (int rule : guideOptions.rules) {
            if (9 <= rule && rule <= 15){
                layoutParams.addRule(rule);
            }else {
                layoutParams.addRule(rule, tagview2.getId());
            }
        }

        lastTargetViwRect = new Rect(targetViewRect);
    }

    private void addToRoot(View view) {
        RelativeLayout relativeLayout = new RelativeLayout(view.getContext());
        relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        if (rootView instanceof ScrollView) {
            FrameLayout frameLayout = new FrameLayout(view.getContext());
            View childAt = rootView.getChildAt(0);
            rootView.removeView(childAt);
            frameLayout.addView(childAt);
            relativeLayout.addView(view);
            frameLayout.addView(relativeLayout);
            rootView.addView(frameLayout);
        } else {
            relativeLayout.addView(view);
            rootView.addView(relativeLayout);
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
            guideOptions.guideView.setVisibility(View.GONE);
            ViewGroup parent = (ViewGroup) guideOptions.guideView.getParent();
            View templateView = parent.findViewById(TAG_TEMPLATE_VIEW_ID1);
            View templateView2 = parent.findViewById(TAG_TEMPLATE_VIEW_ID2);
            parent.removeView(templateView2);
            parent.removeView(templateView);
            parent.removeView(guideOptions.guideView);
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
