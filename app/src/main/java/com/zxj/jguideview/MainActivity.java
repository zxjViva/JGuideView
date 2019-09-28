package com.zxj.jguideview;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View targetView = findViewById(R.id.tv);
        final View targetView2 = findViewById(R.id.tv2);
        final ScrollView rootView = findViewById(R.id.sv);
        MaskView maskView = new MaskView(targetView, rootView);
        maskView.find().calculate(new MaskCalculator.CalculatListener() {
            @Override
            public MaskOptions onResult(Rect tartViewRect) {
                View guideView = getLayoutInflater().inflate(R.layout.guide_item, null);
                return new MaskOptions(guideView);
            }
        }).addEvent(new MaskView.EventCallback() {
            @Override
            public void event() {
                boolean globalVisibleRect = targetView2.getGlobalVisibleRect(new Rect());
                if (!globalVisibleRect){
                    rootView.smoothScrollTo(0,3000);
                }

            }
        });

        MaskView maskView1 = new MaskView(targetView2, rootView);
        maskView1.find().calculate(new MaskCalculator.CalculatListener() {
            @Override
            public MaskOptions onResult(Rect tartViewRect) {
                View guideView = getLayoutInflater().inflate(R.layout.guide_item, rootView,false);
                MaskOptions maskOptions = new MaskOptions(guideView);
                maskOptions.orientation = MaskLayouter.AlignOrientation.RIGHT;
                return maskOptions;
            }
        });
        MaskViews maskViews = new MaskViews();
        maskViews.addMaskView(maskView).addMaskView(maskView1).apply();
    }
}
