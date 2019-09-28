package com.zxj.jguideview;

import java.util.WeakHashMap;

public class MaskViews {
    private int curIndex = 0;
    private WeakHashMap<Integer,MaskView> map = new WeakHashMap<>();
    public MaskViews addMaskView(MaskView maskView){
        map.put(map.size(),maskView);
        return this;
    }
    public void apply(){
        MaskView maskView = map.get(curIndex);
        final MaskView.EventCallback origEventCallback = maskView.getEventCallback();
        maskView.addEvent(new MaskView.EventCallback() {
            @Override
            public void event() {
                if (origEventCallback != null){
                    origEventCallback.event();
                }
                boolean b = applyNext();
            }
        }).apply();
    }
    private boolean applyNext(){
        curIndex++;
        if (curIndex < map.size()){
            apply();
            return true;
        }
        return false;
    }

}
