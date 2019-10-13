package com.example.jguideviewlib;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

public class GuideView {


    private final GuideLayouter guideLayouter;
    private GuideCalculator guideCalculator;
    private View targetView;
    private ViewGroup rootView;
    private EventCallback eventCallback;
    private GuideOptions guideOptions;
    private GuideCalculator.CalculatListener calculatListener;

    public EventCallback getEventCallback() {
        return eventCallback;
    }

    public interface EventCallback{
        int EVENT_PRE_SHOW = 1;
        int EVENT_SHOW = 2;
        int EVENT_DISMISS = 3;
        boolean event(int type);
    }



    public GuideView(View targetView, ViewGroup rootView) {
        guideLayouter = new GuideLayouter(targetView,rootView);
        this.targetView = targetView;
        this.rootView = rootView;
    }

    public GuideView find(){
        guideCalculator = new GuideCalculator(targetView,rootView);
        return this;

    }

    public GuideView calculate(final GuideCalculator.CalculatListener listener){
        this.calculatListener = listener;
        return this;

    }

    public GuideView addEvent(EventCallback eventCallback){
        this.eventCallback = eventCallback;
        return this;
    }

    public void apply(){
        GuideCalculator.CalculatListener proxyListener = new GuideCalculator.CalculatListener(){

            @Override
            public GuideOptions onResult(Rect tartViewRect) {
                guideOptions = calculatListener.onResult(tartViewRect);
                guideLayouter.setTargetViewRect(tartViewRect);
                guideOptions.guideView.setVisibility(View.INVISIBLE);
                startLayout(guideOptions);
                return guideOptions;
            }
        };
        guideCalculator.calculate(proxyListener);
    }

    Rect preCalculate(){
        return guideCalculator.calculateRect(targetView,rootView);
    }
    private void startLayout(GuideOptions guideOptions) {
        if (eventCallback != null){
            boolean event = eventCallback.event(EventCallback.EVENT_PRE_SHOW);
            if (event){
                return;
            }
        }
        guideLayouter.setGuideOptions(guideOptions);
        guideLayouter.setCalculator(guideCalculator);
        guideLayouter.setVisible(true);
        guideLayouter.layout();
        guideLayouter.addOnGlobalLayoutListener();
        if (eventCallback != null){
            eventCallback.event(EventCallback.EVENT_SHOW);
        }
    }
    void dismiss(){
        if (guideLayouter != null){
            guideLayouter.gone();
        }
        if (eventCallback != null){
            eventCallback.event(EventCallback.EVENT_DISMISS);
        }
    }
}
