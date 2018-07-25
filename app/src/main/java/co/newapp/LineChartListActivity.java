package co.newapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

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
public class LineChartListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart_list);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ChartAdapter adapter = new ChartAdapter();
        recyclerView.setAdapter(adapter);
    }


    class ChartAdapter extends RecyclerView.Adapter<ChartAdapter.VHolder> {

        @Override
        public ChartAdapter.VHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VHolder(View.inflate(LineChartListActivity.this,R.layout.layout_line_chart_item,null));
        }

        @Override
        public void onBindViewHolder(ChartAdapter.VHolder holder, int position) {
            holder.wrapperView.setTitleText("腰围");
            holder.wrapperView.setSubTitleText("（正常：男 73.35cm 女 65.79cm）");
            holder.wrapperView.setTitleColor(0xFFFF0000);
            initLineView(holder.wrapperView.getChartView(),position);
            randomSet(holder.wrapperView.getChartView());
        }

        @Override
        public int getItemCount() {
            return 6;
        }

        private void initLineView(LineChartView lineView,int pos) {
            ArrayList<String> test = new ArrayList<>();
            for (int i = 0; i < 1; i++) {
                test.add(String.format(Locale.getDefault(),"7月%d日"+pos,i+1));
            }
            lineView.setBottomTextList(test);
            lineView.setColorArray(new int[] {
                    Color.parseColor("#F44336"), Color.parseColor("#9C27B0"),
                    Color.parseColor("#2196F3"), Color.parseColor("#009688")
            });
            lineView.setDrawDotLine(true);
            lineView.setShowPopup(LineChartView.SHOW_POPUPS_All);

            List<Limit> limits = new ArrayList<>();
            limits.add(new Limit(0, "0次/分", 0xFFFF0000));
            limits.add(new Limit(80, "60-100次/分", 0xFF00FF00));
            lineView.setLimitList(limits);
        }

        private void randomSet(LineChartView lineView) {
            List<Float> dataList = new ArrayList<>();
            float random = (float) (Math.random() * 100 + 1);
            for (int i = 0; i < 1; i++) {
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

        class VHolder extends RecyclerView.ViewHolder{

            LineChartWrapperView wrapperView;

            public VHolder(View itemView) {
                super(itemView);
                wrapperView = itemView.findViewById(R.id.line_chart_wrapper);
            }
        }
    }

}
