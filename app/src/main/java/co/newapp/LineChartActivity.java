package co.newapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
public class LineChartActivity extends AppCompatActivity {

    private int randomInt = 30;
    private LineChartWrapperView lineChartWrapperView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);

        lineChartWrapperView = findViewById(R.id.line_chart_wrapper);

        lineChartWrapperView.setTitleText("腰围");
        lineChartWrapperView.setSubTitleText("（正常：男 73.35cm 女 65.79cm）");
        lineChartWrapperView.setTitleColor(0xFFFF0000);
        initLineView(lineChartWrapperView.getChartView());
        randomSet(lineChartWrapperView.getChartView());
    }

    private void initLineView(LineChartView lineView) {
        ArrayList<String> test = new ArrayList<>();
        for (int i = 0; i < randomInt; i++) {
            test.add(String.format(Locale.getDefault(),"7月%d日",i+1));
        }
        lineView.setBottomTextList(test);
        lineView.setColorArray(new int[] {
                Color.parseColor("#F44336"), Color.parseColor("#9C27B0"),
                Color.parseColor("#2196F3"), Color.parseColor("#009688")
        });
        lineView.setDrawDotLine(true);
        lineView.setShowPopup(LineChartView.SHOW_POPUPS_All);

        List<Limit> limits = new ArrayList<>();
        limits.add(new Limit(10, "10次/分", 0xFFFF0000));
        // 是否显示limit线,false只显示文字
        limits.add(new Limit(55, "55次/分", 0xFFFF0000, false));
        limits.add(new Limit(80, "60-100次/分", 0xFF00FF00));
        lineView.setLimitList(limits);
        // 设置一屏显示的坐标点个数
        lineView.setMaxDotNum(7);
        // 设置是否开启平滑曲线
        lineView.setCubic(true);
    }

    private void randomSet(LineChartView lineView) {
        List<Float> dataList = new ArrayList<>();
        float random = (float) (Math.random() * 100 + 1);
        for (int i = 0; i < randomInt; i++) {
            dataList.add((float) (Math.random() * random));
        }

        /*ArrayList<Integer> dataList2 = new ArrayList<>();
        random = (int) (Math.random() * 9 + 1);
        for (int i = 0; i < randomInt; i++) {
            dataList2.add((int) (Math.random() * random));
        }

        ArrayList<Integer> dataList3 = new ArrayList<>();
        random = (int) (Math.random() * 9 + 1);
        for (int i = 0; i < randomInt; i++) {
            dataList3.add((int) (Math.random() * random));
        }*/

        List<List<Float>> dataLists = new ArrayList<>();
        dataLists.add(dataList);
        //dataLists.add(dataList2);
        //dataLists.add(dataList3);

        lineView.setDataList(dataLists);
    }

}
