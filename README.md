# JGuideView

**JGuideView 是一种在指定控件附近弹出View的框架，一般用于新功能引导**

*演示demo如下*

![demo](https://github.com/zxjViva/JGuideView/blob/master/demo_20191013190501.gif "demo")

## 使用方法

*显示位置 ： 和relativeLayout的属性一直 比如 alignEnd toStartOf 之类的，在GuideOptions中设置*

*显示内容 ： 由使用者自行创建View，设置给GuideOptions*

*显示动作 ： 支持在三种时机做出不同的动作，显示前，显示时，和隐藏后，在addevent中添加回调实现*

## 示例代码

*
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final View targetView = findViewById(R.id.tv);
        final View targetView2 = findViewById(R.id.tv2);
        View targetView3 = findViewById(R.id.tv3);
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
                View guideView = getLayoutInflater().inflate(R.layout.guide_item1, rootView,false);
                return new GuideOptions(guideView,new int[]{GuideLayouter.Rule.START_OF,GuideLayouter.Rule.ALIGN_BOTTOM});
            }
        }).addEvent(new GuideView.EventCallback() {
            @Override
            public boolean event(int type) {
                if (type == GuideView.EventCallback.EVENT_PRE_SHOW){
                    boolean localVisibleRect = targetView.getLocalVisibleRect(new Rect());
                    return !localVisibleRect;
                }
                return false;
            }
        });

        GuideView guideView1 = new GuideView(targetView2, rootView);
        guideView1.find().calculate(new GuideCalculator.CalculatListener() {
            @Override
            public GuideOptions onResult(Rect tartViewRect) {
                View guideView = getLayoutInflater().inflate(R.layout.guide_item, rootView,false);
                return new GuideOptions(guideView,new int[]{GuideLayouter.Rule.ABOVE,GuideLayouter.Rule.ALIGN_END});
            }
        });

        GuideView guideView3 = new GuideView(targetView3, rootView);
        guideView3.find().calculate(new GuideCalculator.CalculatListener() {
            @Override
            public GuideOptions onResult(Rect tartViewRect) {
                View guideView = getLayoutInflater().inflate(R.layout.guide_item2, rootView,false);
                return new GuideOptions(guideView,new int[]{GuideLayouter.Rule.BELOW,GuideLayouter.Rule.ALIGN_START});
            }
        });
        GuideViews guideViews = new GuideViews(this);
        guideViews.addMaskView(guideView3).addMaskView(maskView).addMaskView(guideView1).apply();
    }
}


*
