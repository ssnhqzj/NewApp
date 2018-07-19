package co.newapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人： QuZhiJie
 * 创建时间： 2018/6/13$
 * 版权： 成都智慧一生约科技有限公司
 */
public class AutoScrollRVAdapter extends RecyclerView.Adapter<AutoScrollRVAdapter.AutoScrollHolder> {
    private Context context;
    private List<String> list;

    AutoScrollRVAdapter(Context context){
        this.context = context;
    }

    @Override
    public AutoScrollHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AutoScrollHolder(View.inflate(context, R.layout.layout_auto_scroll_item, null));
    }

    @Override
    public void onBindViewHolder(final AutoScrollHolder holder, int position) {
        String item = list.get(position);
        if (item == null) return;

        holder.textView.setText(item+"--"+position);
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public void addItem(String item){
        if (list == null) {
            list = new ArrayList<>();
        }

        list.add(0, item);
        notifyItemInserted(0);
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }

    class AutoScrollHolder extends RecyclerView.ViewHolder {

        TextView textView;

        AutoScrollHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.auto_scroll_text);
        }
    }
}
