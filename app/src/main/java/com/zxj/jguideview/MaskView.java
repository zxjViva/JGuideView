package com.zxj.jguideview;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class MaskView {

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
        FrameLayout frameLayout = new FrameLayout(rootView.getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        frameLayout.setLayoutParams(layoutParams);
        rootView.addView(frameLayout);
        maskOptions.guideView.setVisibility(View.VISIBLE);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventCallback != null){
                    eventCallback.event();
                }
            }
        });
    }

    private void startLayout(MaskOptions maskOptions) {
        maskLayouter.setMaskOptions(maskOptions);
        maskLayouter.setCalculator(maskCalculator);
        maskLayouter.layout();
        maskLayouter.addOnGlobalLayoutListener();
    }
}
