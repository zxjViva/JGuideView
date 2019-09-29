package com.zxj.jguideview;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

//只是用来计算坐标大小之类的
public class GuideCalculator {
    private final View targetView;
    private final ViewGroup rootView;
    private Rect targetViewRect;
    private CalculatListener listener;


    public interface CalculatListener{
        GuideOptions onResult(Rect tartViewRect);
    }

    public GuideCalculator(View targetView, ViewGroup rootView) {
        this.targetView = targetView;
        this.rootView = rootView;
    }



    public Rect calculateRect(View targetView,ViewGroup rootView) {
        int measuredHeight = targetView.getMeasuredHeight();
        int measuredWidth = targetView.getMeasuredWidth();
        int topAtRoot = getTopAtRoot(targetView, rootView, 0);
        int leftAtRoot = getLeftAtRoot(targetView, rootView, 0);
        Rect rect = new Rect(leftAtRoot,topAtRoot,leftAtRoot + measuredWidth,
                topAtRoot + measuredHeight);
        return rect;
    }

    //计算一下guideview的显示位置，因为如果是动态布局的话，肯定guideview也是动态的
    public GuideCalculator calculate(final CalculatListener listener){
        targetView.post(new Runnable() {
            @Override
            public void run() {
                Rect rect = calculateRect(targetView, rootView);
                listener.onResult(rect);
            }
        });
        return this;
    }

    //获取距离指定布局的top
    private int getTopAtRoot(View targetView,ViewGroup viewGroup,int totalTop){
        totalTop = targetView.getTop() + totalTop;
        ViewGroup parent = (ViewGroup) targetView.getParent();
        if (parent != null){
            if (parent == viewGroup){
                return totalTop;
            }else {
                return getTopAtRoot(parent,viewGroup,totalTop);
            }
        }else {
            return totalTop;
        }

    }
    //获取距离指定布局的left
    private int getLeftAtRoot(View targetView,ViewGroup viewGroup,int totalLeft){
        totalLeft = targetView.getLeft() + totalLeft;
        ViewGroup parent = (ViewGroup) targetView.getParent();
        if (parent != null){
            if (parent == viewGroup){
                return totalLeft;
            }else {
                return getLeftAtRoot(parent,viewGroup,totalLeft);
            }
        }else {
            return totalLeft;
        }

    }
}
