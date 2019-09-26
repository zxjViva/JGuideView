package com.zxj.jguideview;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

//只是用来计算坐标大小之类的
public class MaskCalculator {
    private final View targetView;
    private final ViewGroup rootView;
    private Rect targetViewRect;
    private CalculatListener listener;


    public interface CalculatListener{
        MaskOptions onResult(Rect tartViewRect);
    }

    public MaskCalculator(View targetView, ViewGroup rootView) {
        this.targetView = targetView;
        this.rootView = rootView;
    }

    MaskCalculator find(){
        targetView.post(new Runnable() {
            @Override
            public void run() {
                targetViewRect = calculateRect(targetView,rootView);
                if (listener != null){
                    listener.onResult(targetViewRect);
                }

            }
        });
        return this;
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
    public MaskCalculator calculate(CalculatListener listener){
        this.listener = listener;
        if (targetViewRect != null){
            listener.onResult(targetViewRect);
        }
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
                return getTopAtRoot(parent,viewGroup,totalLeft);
            }
        }else {
            return totalLeft;
        }

    }
}
