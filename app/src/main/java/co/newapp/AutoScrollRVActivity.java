package co.newapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.VERTICAL;

/**
 * 类描述： 自动滑动到底部RV
 * 创建人： QuZhiJie
 * 创建时间： 2018/6/13
 * 版权： 成都智慧一生约科技有限公司
 */
public class AutoScrollRVActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AutoScrollRVAdapter adapter;
    private Button addItemBT;

    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_scroll_rv);

        addItemBT = findViewById(R.id.add_item);
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new ScrollLinearLayoutManager(this);
        //linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new AutoScrollRVAdapter(this);
        recyclerView.setAdapter(adapter);

        addItemBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.addItem("测试哈哈哈加到加到开导开导");
                if (recyclerView.getChildCount() > 0) {
                    recyclerView.getChildAt(recyclerView.getChildCount()-1).post(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.smoothScrollToPosition(0);
                        }
                    });
                }else {
                    recyclerView.smoothScrollToPosition(0);
                }
            }
        });
    }
}
