package co.newapp;

import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * 类描述：
 * 创建人： QuZhiJie
 * 创建时间： 2018/6/13$
 * 版权： 成都智慧一生约科技有限公司
 */
public class ScrollLinearLayoutManager extends LinearLayoutManager {
    private static final float MILLISECONDS_PER_INCH = 1000f;
    public ScrollLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, final int position) {
        LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext())
        {

            @Nullable
            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return ScrollLinearLayoutManager.this.computeScrollVectorForPosition(targetPosition);
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                View view = getChildAt(0);
                if(view != null) {
                    final int firstChildPos = getPosition(getChildAt(0)); //获取当前item的position
                    int delta = Math.abs(position - firstChildPos);//算出需要滑动的item数量
                    if(delta == 0)
                        delta = 1;
                    return (MILLISECONDS_PER_INCH/delta) / displayMetrics.densityDpi;
                }
                else
                {
                    return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
                }
            }

        };
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }
}
