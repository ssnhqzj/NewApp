package co.newapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * 类描述：
 * 创建人： 张力
 * 创建时间： 2018/11/12
 * 版权： 成都智慧一生约科技有限公司
 */
public class BackgroundTransparentLayout extends FrameLayout {
    private float radius = 10;
    private RectF roundRect;
    private Paint mPaint;
    private Paint lastPaint;
    public BackgroundTransparentLayout(@NonNull Context context) {
        this(context,null);
    }

    public BackgroundTransparentLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context,attrs ,0);
    }

    public BackgroundTransparentLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BackgroundTransparentLayout);
        radius = typedArray.getDimension(R.styleable.BackgroundTransparentLayout_radius, dp2px(10));




        typedArray.recycle();

        roundRect = new RectF();
        mPaint = new Paint();
        lastPaint = new Paint();
        lastPaint.setAntiAlias(true);
        lastPaint.setColor(Color.BLUE);
        lastPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);

    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int w = getWidth();
        int h = getHeight();

        roundRect.set(0, 0, w, h);

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.saveLayer(roundRect, mPaint, Canvas.ALL_SAVE_FLAG);
        canvas.drawRoundRect(roundRect, radius, radius, mPaint);
        canvas.saveLayer(roundRect, lastPaint, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        canvas.restore();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

    }




    public int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}

