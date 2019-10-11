package com.zxj.jguideview;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.WeakHashMap;

public class GuideViews {
    private static final String MASK_TAG = "com.zxj.jGuideView.mask";
    private int curIndex = 0;
    private Activity activity;

    public GuideViews(Activity activity) {
        this.activity = activity;
    }

    private WeakHashMap<Integer, GuideView> map = new WeakHashMap<>();
    public GuideViews addMaskView(GuideView guideView){
        map.put(map.size(), guideView);
        return this;
    }
    public void apply(){
        View maskView = showMask();
        final GuideView guideView = map.get(curIndex);
        if (guideView == null){
            boolean b = applyNext();
            if (!b){
                dismissMask();
            }
            return;
        }
        maskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guideView.dismiss();
                boolean b = applyNext();
                if (!b){
                    dismissMask();
                }

            }
        });
        Rect rect = guideView.preCalculate();
        if (curIndex != 0 && rect.equals(new Rect(0,0,0,0))){
            applyNext();
        }else {
            guideView.apply();
        }

    }
    private boolean applyNext(){
        curIndex++;
        if (curIndex < map.size()){
            apply();
            return true;
        }
        return false;
    }
    private View showMask(){
        ViewGroup rootview = (ViewGroup) activity.getWindow().getDecorView();
        View clickEventView = rootview.findViewWithTag(MASK_TAG);
        if (clickEventView == null){
            clickEventView = new FrameLayout(activity);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            clickEventView.setLayoutParams(layoutParams);
            clickEventView.setTag(MASK_TAG);
            rootview.addView(clickEventView);
        }
        return clickEventView;
    }

    private void dismissMask(){
        ViewGroup rootview = (ViewGroup) activity.getWindow().getDecorView();
        View clickEventView = rootview.findViewWithTag(MASK_TAG);
        if (clickEventView != null){
            rootview.removeView(clickEventView);
        }
    }
}
