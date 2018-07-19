package linechart;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;

/**
 * 类描述：
 * 创建人： QuZhiJie
 * 创建时间： 2018/7/18$
 * 版权： 成都智慧一生约科技有限公司
 */
public class LineChartViewGroup extends RelativeLayout {

    private LineChartView lineChartView;
    private LimitView limitView;

    public LineChartViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findNeedView();
        if (limitView != null && lineChartView != null) {
            limitView.setLineChartView(lineChartView);
        }
    }

    private void findNeedView(){
        if (getChildCount()>0){
            for (int index=0; index<getChildCount(); index++){
                View view = getChildAt(index);
                if (view instanceof LimitView) {
                    limitView = (LimitView) view;
                    continue;
                }

                if (view instanceof HorizontalScrollView) {
                    ViewGroup scrollView = (ViewGroup) view;
                    if (scrollView.getChildCount()>0){
                        for (int subIndex=0; subIndex<scrollView.getChildCount(); subIndex++) {
                            View subView = scrollView.getChildAt(subIndex);
                            if (subView instanceof LineChartView){
                                lineChartView = (LineChartView) subView;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}
