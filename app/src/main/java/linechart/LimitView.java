package linechart;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

/**
 * 类描述：
 * 创建人： QuZhiJie
 * 创建时间： 2018/7/18$
 * 版权： 成都智慧一生约科技有限公司
 */
public class LimitView extends View {

    private LineChartView lineChartView;

    public LimitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (lineChartView != null) {
            lineChartView.drawLimit(canvas);
        }
    }

    public void setLineChartView(LineChartView lineChartView) {
        this.lineChartView = lineChartView;
    }
}
