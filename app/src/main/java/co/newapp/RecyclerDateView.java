package co.newapp;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Date selector
 */

public class RecyclerDateView extends LinearLayout{

    private RecyclerView recyclerView;
    private TextView yearView;

    private MonthAdapter monthAdapter;
    private YearPopAdapter yearPopAdapter;
    private List<YearMonthBean> yearMonthBeans = new ArrayList<>();
    private int selectedYear;
    private YearMonthBean selectedMonthBean = null;
    private OnMonthCheckListener onMonthCheckListener;
    private YearBubblePopup customBubblePopup;

    public RecyclerDateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        View.inflate(getContext(), R.layout.layout_rv_date_view, this);
        recyclerView = findViewById(R.id.recycler_view);
        yearView = findViewById(R.id.year);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        monthAdapter = new MonthAdapter(getContext());
        recyclerView.setAdapter(monthAdapter);
        getMonthData(Calendar.getInstance().get(Calendar.YEAR));
        monthAdapter.setList(yearMonthBeans);
        setCheckMonth(yearMonthBeans.size()-1);

        yearView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectYearPop();
            }
        });
    }

    private void getMonthData(int year){
        yearMonthBeans.clear();
        int currYear = Calendar.getInstance().get(Calendar.YEAR);
        int currMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int maxMonth = year==currYear?currMonth:12;
        for (int i=1; i<=maxMonth; i++) {
            YearMonthBean bean = new YearMonthBean(year, i);
            bean.isChecked = selectedMonthBean != null
                    && selectedMonthBean.year==year
                    && selectedMonthBean.month==i;
            yearMonthBeans.add(bean);
        }
    }

    private List<Integer> getYearList(){
        List<Integer> list = new ArrayList<>();
        int currYear = Calendar.getInstance().get(Calendar.YEAR);
        list.add(currYear);
        list.add(currYear-1);
        return list;
    }

    public void setCheckMonth(int position){
        if (position<yearMonthBeans.size()){
            if (selectedMonthBean != null && yearMonthBeans.get(position).month==selectedMonthBean.month)
                return;

            for (int i=0; i<yearMonthBeans.size(); i++) {
                if (position == i) {
                    selectedMonthBean = yearMonthBeans.get(position);
                    selectedMonthBean.isChecked = true;
                }else {
                    yearMonthBeans.get(i).isChecked = false;
                }
            }

            monthAdapter.notifyDataSetChanged();
            if (onMonthCheckListener != null && selectedMonthBean != null) {
                onMonthCheckListener.onMonthChecked(selectedMonthBean);
            }
        }
    }

    public void setCheckMonthByMonth(int month){
        if (selectedMonthBean != null && month == selectedMonthBean.month) return;
        for (YearMonthBean bean : yearMonthBeans) {
            if (month==bean.month){
                bean.isChecked = true;
                selectedMonthBean = bean;
            }else {
                bean.isChecked = false;
            }
        }
        monthAdapter.notifyDataSetChanged();
    }

    private void showSelectYearPop(){
        if (customBubblePopup == null) {
            customBubblePopup = new YearBubblePopup(getContext());
            customBubblePopup
                    .bubbleColor(Color.parseColor("#FFFFFF"))
                    .gravity(Gravity.BOTTOM)
                    .anchorView(yearView)
                    .triangleWidth(20)
                    .triangleHeight(10)
                    .showAnim(null)
                    .dismissAnim(null)
                    .show();
        }else {
            customBubblePopup.show();
        }
    }

    public void setOnMonthCheckListener(OnMonthCheckListener onMonthCheckListener) {
        this.onMonthCheckListener = onMonthCheckListener;
    }

    class YearBubblePopup extends CusBaseBubblePopup<YearBubblePopup> {
        RecyclerView popRV;

        public YearBubblePopup(Context context) {
            super(context);
        }

        @Override
        public View onCreateBubbleView() {
            View popView = View.inflate(getContext(), R.layout.layout_rv_date_year_pop, null);
            popRV = popView.findViewById(R.id.year_pop_recycler_view);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setAutoMeasureEnabled(true);
            popRV.setLayoutManager(layoutManager);
            yearPopAdapter = new YearPopAdapter(getContext());
            yearPopAdapter.setList(getYearList());
            popRV.setAdapter(yearPopAdapter);
            return popView;
        }

        @Override
        public void setUiBeforShow() {
            super.setUiBeforShow();
        }
    }

    class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.MonthHolder> {

        private Context context;
        private List<YearMonthBean> list;

        MonthAdapter(Context context){
            this.context = context;
        }

        @Override
        public MonthHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MonthHolder(View.inflate(context, R.layout.layout_rv_date_month_item, null));
        }

        @Override
        public void onBindViewHolder(final MonthHolder holder, int position) {
            YearMonthBean item = list.get(position);
            if (item == null) return;

            holder.monthView.setText(String.format(Locale.getDefault(),"%d月",item.month));
            holder.monthView.setSelected(item.isChecked);
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    setCheckMonth(holder.getAdapterPosition());
                }
            });
        }

        @Override
        public int getItemCount() {
            return list==null?0:list.size();
        }

        void setList(List<YearMonthBean> list) {
            this.list = list;
        }

        class MonthHolder extends RecyclerView.ViewHolder {

            TextView monthView;

            MonthHolder(View itemView) {
                super(itemView);
                itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                monthView = itemView.findViewById(R.id.month_item);
            }
        }
    }

    class YearPopAdapter extends RecyclerView.Adapter<YearPopAdapter.YearPopHolder> {

        private Context context;
        private List<Integer> list;

        YearPopAdapter(Context context){
            this.context = context;
        }

        @Override
        public YearPopHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new YearPopHolder(View.inflate(context, R.layout.layout_rv_date_year_pop_item, null));
        }

        @Override
        public void onBindViewHolder(final YearPopHolder holder, int position) {
            final Integer item = list.get(position);
            if (item == null) return;

            holder.yearTxtTV.setText(String.format(Locale.getDefault(),"%d年",item));
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    yearView.setText(String.format(Locale.getDefault(),"%d年",item));
                    getMonthData(item);
                    monthAdapter.setList(yearMonthBeans);
                    monthAdapter.notifyDataSetChanged();
                    if (customBubblePopup != null) {
                        customBubblePopup.dismiss();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return list==null?0:list.size();
        }

        public void setList(List<Integer> list) {
            this.list = list;
        }

        class YearPopHolder extends RecyclerView.ViewHolder {

            TextView yearTxtTV;

            YearPopHolder(View itemView) {
                super(itemView);
                yearTxtTV = itemView.findViewById(R.id.year_pop_item_txt);
            }
        }
    }

    class YearMonthBean {
        public int year;
        public int month;
        public boolean isChecked;

        public YearMonthBean(int year, int month) {
            this.year = year;
            this.month = month;
        }
    }

    public interface OnMonthCheckListener {
        void onMonthChecked(YearMonthBean bean);
    }

}
