package co.newapp;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * 创建人： 张力
 * 创建时间： 2018/10/18
 * 版权： 成都智慧一生约科技有限公司
 */
public class CircleChangeView extends View {

    Paint[] paints = new Paint[5];
    int[] colors = new int[5];

    public CircleChangeView(Context context) {
        super(context);
        initTools();
    }

    public CircleChangeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initTools();
    }

    public CircleChangeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTools();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(100, 100, 10, paints[0]);
        canvas.drawCircle(150, 100, 10, paints[1]);
        canvas.drawCircle(200, 100, 10, paints[2]);
        canvas.drawCircle(250, 100, 10, paints[3]);
        canvas.drawCircle(300, 100, 10, paints[4]);
    }

    private void initTools() {
        colors[0] = ContextCompat.getColor(getContext(), R.color.color10blue);
        colors[1] = ContextCompat.getColor(getContext(), R.color.color30blue);
        colors[2] = ContextCompat.getColor(getContext(), R.color.color50blue);
        colors[3] = ContextCompat.getColor(getContext(), R.color.color70blue);
        colors[4] = ContextCompat.getColor(getContext(), R.color.color507DFF);

        for (int i = 0; i < paints.length; i++) {
            paints[i] = new Paint();
            paints[i].setColor(colors[i]);
            paints[i].setAntiAlias(true);
            paints[i].setStyle(Paint.Style.FILL);
        }

        waveAnim();
    }


    public void waveAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(5, 0);
        valueAnimator.setDuration(2000);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int aFloat = Integer.valueOf(animation.getAnimatedValue().toString());

                for (int i = 0; i < paints.length; i++) {
                    paints[i].setColor(colors[(aFloat + i) % 5]);
                }
                invalidate();
            }
        });
        valueAnimator.start();
    }
}
