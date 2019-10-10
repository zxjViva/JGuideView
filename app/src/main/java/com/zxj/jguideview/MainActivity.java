package com.zxj.jguideview;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final View targetView = findViewById(R.id.tv);
        final View targetView2 = findViewById(R.id.tv2);
        final ScrollView rootView = findViewById(R.id.sv);
        targetView.postDelayed(new Runnable() {
            @Override
            public void run() {
                targetView.setVisibility(View.VISIBLE);
            }
        },2000);
        GuideView maskView = new GuideView(targetView, rootView);
        maskView.find().calculate(new GuideCalculator.CalculatListener() {
            @Override
            public GuideOptions onResult(Rect tartViewRect) {
                View guideView = getLayoutInflater().inflate(R.layout.guide_item, rootView,false);
                return new GuideOptions(guideView);
            }
        }).addEvent(new GuideView.EventCallback() {
            @Override
            public void event(int type) {

            }
        });

        GuideView guideView1 = new GuideView(targetView2, rootView);
        guideView1.find().calculate(new GuideCalculator.CalculatListener() {
            @Override
            public GuideOptions onResult(Rect tartViewRect) {
                View guideView = getLayoutInflater().inflate(R.layout.guide_item, rootView,false);
                GuideOptions guideOptions = new GuideOptions(guideView,new int[]{GuideLayouter.Rule.BELOW,GuideLayouter.Rule.ALIGN_PARENT_END});
                return guideOptions;
            }
        });
        View targetView3 = findViewById(R.id.tv3);
        GuideView guideView3 = new GuideView(targetView3, rootView);
        guideView3.find().calculate(new GuideCalculator.CalculatListener() {
            @Override
            public GuideOptions onResult(Rect tartViewRect) {
                View guideView = getLayoutInflater().inflate(R.layout.guide_item, rootView,false);
                GuideOptions guideOptions = new GuideOptions(guideView);
                return guideOptions;
            }
        });
        GuideViews guideViews = new GuideViews(this);
        guideViews.addMaskView(guideView3).addMaskView(maskView).addMaskView(guideView1).apply();
    }
}
