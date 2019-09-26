package com.zxj.jguideview;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View targetView = findViewById(R.id.tv);
        ViewGroup rootView = findViewById(android.R.id.content);
        MaskView maskView = new MaskView(targetView, rootView);
        maskView.find().calculate(new MaskCalculator.CalculatListener() {
            @Override
            public MaskOptions onResult(Rect tartViewRect) {
                View guideView = getLayoutInflater().inflate(R.layout.guide_item, null);
                return new MaskOptions(guideView);
            }
        });
        View targetView2 = findViewById(R.id.tv2);
        MaskView maskView1 = new MaskView(targetView2, rootView);
        maskView1.find().calculate(new MaskCalculator.CalculatListener() {
            @Override
            public MaskOptions onResult(Rect tartViewRect) {
                View guideView = getLayoutInflater().inflate(R.layout.guide_item, null);
                return new MaskOptions(guideView);
            }
        });
        MaskViews maskViews = new MaskViews();
        maskViews.addMaskView(maskView).addMaskView(maskView1).apply();
    }
}
