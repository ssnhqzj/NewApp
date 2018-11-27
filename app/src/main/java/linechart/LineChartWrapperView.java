package linechart;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.newapp.R;

/**
 * 类描述： LineChartView的包装类
 * 创建人： QuZhiJie
 * 创建时间： 2018/7/18$
 * 版权： 成都智慧一生约科技有限公司
 */
public class LineChartWrapperView extends LinearLayout {

    private ImageView titleIV;
    private TextView titleTV;
    private TextView subTitleTV;
    private LineChartView chartView;
    private HorizontalScrollView horizontalScrollView;

    public LineChartWrapperView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View.inflate(getContext(), R.layout.layout_line_chart_wrapper, this);
        titleIV = findViewById(R.id.line_chart_wrapper_title_img);
        titleTV = findViewById(R.id.line_chart_wrapper_title_txt);
        subTitleTV = findViewById(R.id.line_chart_wrapper_sub_title);
        chartView = findViewById(R.id.line_chart_wrapper_chart);
        horizontalScrollView = findViewById(R.id.line_chart_wrapper_scroll);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (chartView.getDrawFromEnd()){
            horizontalScrollView.post(new Runnable() {
                @Override
                public void run() {
                    horizontalScrollView.scrollTo(chartView.getMeasuredWidth(),0);
                }
            });
        }
    }

    private Drawable getTitleLeftDrawable(int color){
        int l = MyUtils.dip2px(getContext(),12);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(color);
        //gd.setBounds(0,0, l, l);
        return gd;
    }

    public void setTitleText(String titleText){
        titleTV.setText(titleText);
    }

    public void setTitleColor(int color){
        titleIV.setImageDrawable(getTitleLeftDrawable(color));
    }

    public void setTitleDrawable(Drawable drawable) {
        titleIV.setImageDrawable(drawable);
    }

    public void setSubTitleText(String titleText){
        subTitleTV.setText(titleText);
    }

    public LineChartView getChartView() {
        return chartView;
    }
}
