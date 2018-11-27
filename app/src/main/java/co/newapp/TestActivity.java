package co.newapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import linechart.Limit;
import linechart.LineChartView;
import linechart.LineChartWrapperView;

/**
 * 类描述： 折线图
 * 创建人： QuZhiJie
 * 创建时间： 2018/6/13
 * 版权： 成都智慧一生约科技有限公司
 */
public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        TextView textView = findViewById(R.id.text_view);
        TextView textView1 = findViewById(R.id.text_view1);

        textView.append("屏幕宽度："+ScreenUtils.getScreenWidth()+"\n; dp:"+SizeUtils.px2dp(this,ScreenUtils.getScreenWidth()));
        textView.append("屏幕高度："+ScreenUtils.getScreenHeight()+"\n; dp:"+SizeUtils.px2dp(this,ScreenUtils.getScreenHeight()));
        textView.append("屏幕Density："+getDensity()+"\n");
        textView.append("屏幕dpi："+getDensity()*160+"\n");


        String txt = getString(R.string.test);
        textView1.setText("--------------------"+txt);
    }

    private float getDensity() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.density;
    }

}
