package com.zxj.jguideview;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class MaskView {
    private static final String MASK_TAG = "com.zxj.jGuideView.mask";

    private final MaskLayouter maskLayouter;
    private MaskCalculator maskCalculator;
    private View targetView;
    private ViewGroup rootView;
    private EventCallback eventCallback;
    private MaskOptions maskOptions;

    public EventCallback getEventCallback() {
        return eventCallback;
    }

    public interface EventCallback{
        void event();
    }

    public MaskView(View targetView, ViewGroup rootView) {
        maskLayouter = new MaskLayouter(targetView,rootView);
        this.targetView = targetView;
        this.rootView = rootView;
    }

    public MaskView find(){
        maskCalculator = new MaskCalculator(targetView,rootView);
        maskCalculator.find();
        return this;

    }

    public MaskView calculate(final MaskCalculator.CalculatListener listener){
        MaskCalculator.CalculatListener proxyListener = new MaskCalculator.CalculatListener(){

            @Override
            public MaskOptions onResult(Rect tartViewRect) {
                maskOptions = listener.onResult(tartViewRect);
                maskLayouter.setTargetViewRect(tartViewRect);
                maskOptions.guideView.setVisibility(View.INVISIBLE);
                startLayout(maskOptions);
                return maskOptions;
            }
        };
        maskCalculator.calculate(proxyListener);
        return this;

    }

    public MaskView addEvent(EventCallback eventCallback){
        this.eventCallback = eventCallback;
        return this;
    }
    public void apply(){
        ViewGroup realRoot = (ViewGroup) this.rootView.getRootView();
        View clickEventView = realRoot.findViewWithTag(MASK_TAG);
        if (clickEventView == null){
            clickEventView = new FrameLayout(realRoot.getContext());
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            clickEventView.setLayoutParams(layoutParams);
            clickEventView.setTag(MASK_TAG);
            realRoot.addView(clickEventView);
        }
        clickEventView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("zxj", "onClick: ");
                if (eventCallback != null){
                    eventCallback.event();
                }
                dismiss();
            }
        });
        Log.e("zxj", "apply: ");
        maskLayouter.visible();
        maskLayouter.layout();
    }

    private void startLayout(MaskOptions maskOptions) {
        maskLayouter.setMaskOptions(maskOptions);
        maskLayouter.setCalculator(maskCalculator);
        maskLayouter.layout();
        maskLayouter.addOnGlobalLayoutListener();
    }
    void dismiss(){
        if (maskLayouter != null){
            maskLayouter.gone();
        }
    }
}
