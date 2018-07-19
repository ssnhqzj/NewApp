package co.newapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by qzj on 2018/1/11.
 * 水质净化效果图表
 */

public class WaterCleanChatView extends View{

    private Point mRadiusCenterPoint;
    // 外圆弧半径
    private int mOutRadius;
    // 内圆弧半径
    private int mInRadius;
    // 外圆弧宽度
    private int mArcWidth = 20;
    // 指标标题高度
    private int mIndexLabelSize = 30;
    // 净化前后高度
    private int mCleanLabelSize = 20;
    // 净化前后数值高度
    private int mCleanValueSize = 60;
    // 净化前字体颜色
    private int cleanBeforeColor = 0xFFFA52C6;
    // 净化后字体颜色
    private int cleanAfterColor = 0xFF4A90E2;
    private int mInFillRadius;
    private float mIndexLabelBaseLine;
    private float mCleanLabelBaseLine;
    private float mCleanValueBaseLine;
    private RectF mOutArcRectF;
    private RectF mInArcRectF;
    private RectF mIndexLabelRectF;

    // 圆弧画笔
    private Paint mArcPaint;
    private Paint mHintArcPaint;
    private TextPaint mIndexLabelPaint;
    private TextPaint mCleanLabelPaint;
    private TextPaint mCleanValuePaint;

    // 渐变
    private SweepGradient sweepGradient;
    private float mMaxValue = 250;
    private float mBeforeValue = 130;
    private float mAfterValue = 20;


    public WaterCleanChatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    private void initView(AttributeSet attrs){
        // 圆弧画笔
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setColor(0xFFA4C739);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(mArcWidth);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);

        mHintArcPaint = new Paint();
        mHintArcPaint.setAntiAlias(true);
        mHintArcPaint.setColor(0xFFE8E8E8);
        mHintArcPaint.setStyle(Paint.Style.STROKE);
        mHintArcPaint.setStrokeWidth(mArcWidth/3);
        mHintArcPaint.setStrokeCap(Paint.Cap.ROUND);

        mIndexLabelPaint = new TextPaint();
        mIndexLabelPaint.setAntiAlias(true);
        mIndexLabelPaint.setColor(0xFF4A90E2);
        mIndexLabelPaint.setTextSize(mIndexLabelSize);
        mIndexLabelPaint.setTextAlign(Paint.Align.CENTER);

        mCleanLabelPaint = new TextPaint();
        mCleanLabelPaint.setAntiAlias(true);
        mCleanLabelPaint.setTextSize(mCleanLabelSize);
        mCleanLabelPaint.setTextAlign(Paint.Align.CENTER);

        mCleanValuePaint = new TextPaint();
        mCleanValuePaint.setAntiAlias(true);
        mCleanValuePaint.setTextSize(mCleanValueSize);
        mCleanValuePaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasureSize(-1, widthMeasureSpec);
        int height = getMeasureSize(-1, heightMeasureSpec);

        if (width>0 && height>0){
            mOutRadius = Math.min(width/2, height);
        }else if (width==-1 && height>0){
            mOutRadius = height;
        }else if (width>0 && height==-1){
            mOutRadius = width/2;
        }

        setMeasuredDimension(mOutRadius*2, mOutRadius+mArcWidth/2);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadiusCenterPoint = new Point();
        mRadiusCenterPoint.x = w/2;
        mRadiusCenterPoint.y = h;
        mInRadius = mOutRadius-mArcWidth*3/2;
        mInFillRadius = mInRadius-mArcWidth/2;
        int inFillLeftY = 5*mArcWidth/2+mInRadius/4;
        int inFillLeftX = w/2-(int) Math.sqrt(mInFillRadius*mInFillRadius-((mInRadius*3/4)*(mInRadius*3/4)));

        // 指标title
        mIndexLabelRectF = new RectF(
                inFillLeftX,
                inFillLeftY,
                w-inFillLeftX,
                inFillLeftY+ mIndexLabelSize
        );
        Paint.FontMetricsInt fontMetrics = mIndexLabelPaint.getFontMetricsInt();
        mIndexLabelBaseLine = (mIndexLabelRectF.bottom + mIndexLabelRectF.top - fontMetrics.bottom - fontMetrics.top)/2;
        Paint.FontMetricsInt fontCleanLabelMetrics = mCleanLabelPaint.getFontMetricsInt();
        mCleanLabelBaseLine = (mOutRadius + mOutRadius- mCleanLabelSize - fontCleanLabelMetrics.bottom - fontCleanLabelMetrics.top)/2;
        mCleanValueBaseLine = (mOutRadius- mCleanLabelSize + mOutRadius- mCleanLabelSize - mCleanValueSize - fontCleanLabelMetrics.bottom - fontCleanLabelMetrics.top)/2;

        mOutArcRectF = new RectF(
                mArcWidth/2,
                mArcWidth/2,
                w-mArcWidth/2,
                mOutRadius*2-mArcWidth/2
        );
        mInArcRectF = new RectF(
                mArcWidth*2,
                mArcWidth*2,
                w-mArcWidth*2,
                mOutRadius*2-mArcWidth*2
        );

        sweepGradient = new SweepGradient(mRadiusCenterPoint.x, mRadiusCenterPoint.y, new int[]{0xFF00C467,0xFF4A90E2,0xFFFA52C6}, new float[]{0.5f,0.75f,1f});
        mArcPaint.setShader(sweepGradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制外层粗细圆弧
        canvas.drawArc(mOutArcRectF, 180, 180, false, mHintArcPaint);
        canvas.drawArc(mOutArcRectF, 180, 180*(mBeforeValue/mMaxValue), false, mArcPaint);
        // 绘制内层粗细圆弧
        canvas.drawArc(mInArcRectF, 180, 180, false, mHintArcPaint);
        canvas.drawArc(mInArcRectF, 180, 180*(mAfterValue/mMaxValue), false, mArcPaint);
        // 绘制指标标题
        canvas.drawText("水质纯净值DTS",mIndexLabelRectF.centerX(),mIndexLabelBaseLine,mIndexLabelPaint);
        mCleanLabelPaint.setColor(cleanBeforeColor);
        mCleanValuePaint.setColor(cleanBeforeColor);
        canvas.drawText("净化前",mRadiusCenterPoint.x-mInFillRadius/2,mCleanLabelBaseLine,mCleanLabelPaint);
        canvas.drawText("186",mRadiusCenterPoint.x-mInFillRadius/2,mCleanValueBaseLine,mCleanValuePaint);
        mCleanLabelPaint.setColor(cleanAfterColor);
        mCleanValuePaint.setColor(cleanAfterColor);
        canvas.drawText("净化后",mRadiusCenterPoint.x+mInFillRadius/2,mCleanLabelBaseLine,mCleanLabelPaint);
        canvas.drawText("86",mRadiusCenterPoint.x+mInFillRadius/2,mCleanValueBaseLine,mCleanValuePaint);
    }

    private int getMeasureSize(int defaultSize, int measureSpec) {
        int mySize = defaultSize;

        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode) {
            //如果没有指定大小，就设置为默认大小
            case MeasureSpec.UNSPECIFIED: {
                mySize = defaultSize;
                break;
            }
            //如果测量模式是最大取值为size
            case MeasureSpec.AT_MOST: {
                //我们将大小取最大值,你也可以取其他值
                mySize = size;
                break;
            }
            //如果是固定的大小，那就不要去改变它
            case MeasureSpec.EXACTLY: {
                mySize = size;
                break;
            }
        }

        return mySize;
    }

    public void setArcWidth(int mArcWidth) {
        this.mArcWidth = mArcWidth;
    }

    public void setIndexLabelSize(int mIndexLabelHeight) {
        this.mIndexLabelSize = mIndexLabelHeight;
    }

    public void setCleanLabelSize(int mCleanLabelHeight) {
        this.mCleanLabelSize = mCleanLabelHeight;
    }

    public void setCleanValueSize(int mCleanValueHeight) {
        this.mCleanValueSize = mCleanValueHeight;
    }

    public void setMaxValue(float mMaxValue) {
        this.mMaxValue = mMaxValue;
    }

    public void setBeforeValue(float mBeforeValue) {
        this.mBeforeValue = mBeforeValue;
        invalidate();
    }

    public void setAfterValue(float mAfterValue) {
        this.mAfterValue = mAfterValue;
        invalidate();
    }
}
