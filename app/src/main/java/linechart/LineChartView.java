package linechart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * 类描述： 折线图
 * 创建人： QuZhiJie
 * 创建时间： 2018/7/17
 * 版权： 成都智慧一生约科技有限公司
 */
public class LineChartView extends View {
    // pop全部显示
    public static final int SHOW_POPUPS_All = 1;
    // 显示最小和最大值的pop
    public static final int SHOW_POPUPS_MAX_MIN_ONLY = 2;
    // 默认不显示，手动点击显示pop
    public static final int SHOW_POPUPS_NONE = 3;
    // pop底部三角高度
    private final int bottomTriangleHeight = 12;
    private static final float LINE_SMOOTHNESS = 0.16f;
    // 最小pop宽度
    private final int minPopupWidth = MyUtils.dip2px(getContext(), 20);
    private final int popupTopPadding = MyUtils.dip2px(getContext(), 2);
    private final int popupBottomMargin = MyUtils.dip2px(getContext(), 5);
    private final int bottomTextTopMargin = MyUtils.sp2px(getContext(), 10);
    private final int bottomLineLength = MyUtils.sp2px(getContext(), 0);
    private final int DOT_INNER_CIR_RADIUS = MyUtils.dip2px(getContext(), 3);
    private final int DOT_OUTER_CIR_RADIUS = MyUtils.dip2px(getContext(), 5);
    private final int MIN_VERTICAL_GRID_NUM = 1;
    private final int MIN_HORIZONTAL_GRID_NUM = 1;
    private final int BACKGROUND_LINE_COLOR = Color.parseColor("#EEEEEE");
    private final int BOTTOM_TEXT_COLOR = Color.parseColor("#333333");
    private final Point tmpPoint = new Point();
    private final Paint linePaint;

    private boolean showBackGrid = false;
    public boolean showPopup = true;
    private int mViewHeight;
    private boolean autoSetDataOfGird = true;
    private boolean autoSetGridWidth = true;
    private boolean isSizeChanged = false;
    private int dataOfAGird = 10;
    private int bottomTextHeight = 0;
    private int maxDotNum = 3;
    private int keepDigits = 0;
    private ArrayList<String> bottomTextList = new ArrayList<>();
    private List<List<Float>> dataLists = new ArrayList<>();
    private ArrayList<Integer> xCoordinateList = new ArrayList<>();
    private ArrayList<Integer> yCoordinateList = new ArrayList<>();
    private ArrayList<ArrayList<Dot>> drawDotLists = new ArrayList<>();
    private Paint xAxisPaint = new Paint();
    private int xAxisColor = Color.parseColor("#EEEEEE");
    private Paint limitPaint = new Paint();
    private int limitColor = Color.parseColor("#EEEEEE");
    private Paint limitTextPaint = new Paint();
    private int limitTextColor = Color.parseColor("#666666");
    private Paint bottomTextPaint = new Paint();
    private int bottomTextDescent;
    private Paint popupTextPaint = new Paint();
    private Paint backGridPaint = new Paint();
    private PathEffect effects = new DashPathEffect(new float[]{10, 5, 10, 5}, 1);
    private Dot pointToSelect;
    private Dot selectedDot;
    private int popupBottomPadding = MyUtils.dip2px(getContext(), 2);
    // 顶部预留的空间
    private int topLineLength = MyUtils.dip2px(getContext(), 12);
    // 左右两边预留的空间
    private int sideLineLength = MyUtils.dip2px(getContext(), 30);

    // 单元格宽度
    private int backgroundGridWidth = MyUtils.dip2px(getContext(), 45);
    private int parentWidth;
    private int showPopupType = SHOW_POPUPS_NONE;
    private float scale = 1f;
    private Boolean drawDotLine = false;
    // 是否适应最小limit值，若为true则X轴为limit的最小值
    private Boolean fitMinLimit = false;
    // 是否使用贝塞尔平滑曲线
    private Boolean isCubic = false;

    private int[] colorArray = {
            Color.parseColor("#e74c3c"), Color.parseColor("#2980b9"), Color.parseColor("#1abc9c")
    };
    private List<Limit> limitList = new ArrayList<>();

    private Runnable animator = new Runnable() {
        @Override
        public void run() {
            boolean needNewFrame = false;
            for (ArrayList<Dot> data : drawDotLists) {
                for (Dot dot : data) {
                    dot.update();
                    if (!dot.isAtRest()) {
                        needNewFrame = true;
                    }
                }
            }
            if (needNewFrame) {
                postDelayed(this, 25);
            }
            invalidate();
        }
    };

    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);

        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(MyUtils.dip2px(getContext(), 2));
        linePaint.setColor(Color.RED);

        popupTextPaint.setAntiAlias(true);
        popupTextPaint.setColor(Color.WHITE);
        popupTextPaint.setTextSize(MyUtils.sp2px(getContext(), 13));
        popupTextPaint.setStrokeWidth(5);
        popupTextPaint.setTextAlign(Paint.Align.CENTER);

        bottomTextPaint.setAntiAlias(true);
        bottomTextPaint.setTextSize(MyUtils.sp2px(getContext(), 12));
        bottomTextPaint.setTextAlign(Paint.Align.CENTER);
        bottomTextPaint.setStyle(Paint.Style.FILL);
        bottomTextPaint.setColor(BOTTOM_TEXT_COLOR);

        xAxisPaint.setStyle(Paint.Style.STROKE);
        xAxisPaint.setStrokeWidth(MyUtils.dip2px(getContext(), 1f));
        xAxisPaint.setColor(xAxisColor);

        limitPaint.setStyle(Paint.Style.STROKE);
        limitPaint.setStrokeWidth(MyUtils.dip2px(getContext(), 1f));
        limitPaint.setColor(limitColor);

        limitTextPaint.setAntiAlias(true);
        limitTextPaint.setTextSize(MyUtils.sp2px(getContext(), 12));
        limitTextPaint.setTextAlign(Paint.Align.LEFT);
        limitTextPaint.setStyle(Paint.Style.FILL);
        limitTextPaint.setColor(limitTextColor);

        backGridPaint.setStyle(Paint.Style.STROKE);
        backGridPaint.setStrokeWidth(1);
        backGridPaint.setColor(BACKGROUND_LINE_COLOR);

        refreshTopLineLength();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            pointToSelect = findPointAt((int) event.getX(), (int) event.getY());
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (pointToSelect != null) {
                selectedDot = pointToSelect;
                pointToSelect = null;
                postInvalidate();
            }
        }

        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getParent() != null && getParent() instanceof View) {
            parentWidth = ((View) getParent()).getMeasuredWidth();
            int budgetValue = (parentWidth-2*sideLineLength)/(maxDotNum-1);
            backgroundGridWidth = budgetValue>backgroundGridWidth?budgetValue:backgroundGridWidth;
        }

        int mViewWidth = measureWidth(widthMeasureSpec);
        mViewHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        refreshXCoordinateList(getHorizontalGridNum());
        refreshAfterDataChanged();
        isSizeChanged = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //drawXAxisLine(canvas);
        drawBackgroundLines(canvas);
        //drawLimit(canvas);
        drawLines(canvas);
        drawDots(canvas);

        for (int k = 0; k < drawDotLists.size(); k++) {
            float maxValue = Collections.max(dataLists.get(k));
            float minValue = Collections.min(dataLists.get(k));
            for (Dot d : drawDotLists.get(k)) {
                if (showPopupType == SHOW_POPUPS_All) {
                    drawPopup(canvas, d.data, d.setupPoint(tmpPoint),
                            colorArray[k % colorArray.length]);
                } else if (showPopupType == SHOW_POPUPS_MAX_MIN_ONLY) {
                    if (d.data == maxValue) {
                        drawPopup(canvas, d.data, d.setupPoint(tmpPoint),
                                colorArray[k % colorArray.length]);
                    }
                    if (d.data == minValue) {
                        drawPopup(canvas, d.data, d.setupPoint(tmpPoint),
                                colorArray[k % colorArray.length]);
                    }
                }
            }
        }

        if (showPopup && selectedDot != null) {
            drawPopup(canvas, selectedDot.data, selectedDot.setupPoint(tmpPoint),
                    colorArray[selectedDot.linenumber % colorArray.length]);
        }
    }

    /**
     * 测量宽度
     */
    private int measureWidth(int measureSpec) {
        int horizontalGridNum = getHorizontalGridNum();
        int preferred = backgroundGridWidth * horizontalGridNum + sideLineLength * 2;
        return getMeasurement(measureSpec, preferred);
    }

    /**
     * 测量高度
     */
    private int measureHeight(int measureSpec) {
        int preferred = 0;
        return getMeasurement(measureSpec, preferred);
    }

    /**
     * 绘制背景grid
     */
    private void drawBackgroundLines(Canvas canvas) {
        if (showBackGrid) {
            // draw vertical lines
            for (int i = 0; i < xCoordinateList.size(); i++) {
                canvas.drawLine(xCoordinateList.get(i), 0, xCoordinateList.get(i), getGridBottom(), backGridPaint);
            }

            // draw dotted lines
            backGridPaint.setPathEffect(effects);
            Path dottedPath = new Path();
            for (int i = 0; i < yCoordinateList.size(); i++) {
                if ((yCoordinateList.size() - 1 - i) % dataOfAGird == 0) {
                    dottedPath.moveTo(0, yCoordinateList.get(i));
                    dottedPath.lineTo(getWidth(), yCoordinateList.get(i));
                    canvas.drawPath(dottedPath, backGridPaint);
                }
            }
        }

        //draw bottom text
        if (bottomTextList != null) {
            for (int i = 0; i < bottomTextList.size(); i++) {
                canvas.drawText(bottomTextList.get(i), sideLineLength + backgroundGridWidth * i,
                        mViewHeight - bottomTextDescent, bottomTextPaint);
            }
        }

        if (!drawDotLine) {
            //draw solid lines
            for (int i = 0; i < yCoordinateList.size(); i++) {
                if ((yCoordinateList.size() - 1 - i) % dataOfAGird == 0) {
                    canvas.drawLine(0, yCoordinateList.get(i), getWidth(), yCoordinateList.get(i), backGridPaint);
                }
            }
        }
    }

    /**
     * 绘制X轴线
     */
    private void drawXAxisLine(Canvas canvas) {
        canvas.drawLine(0, getGridBottom(), getWidth(), getGridBottom(), xAxisPaint);
    }

    /**
     * 绘制Limit
     */
    public void drawLimit(Canvas canvas) {
        if (limitList != null && limitList.size()>0) {
            for (int i=0; i<limitList.size(); i++) {
                float limitY = getYAxesOf(limitList.get(i).yValue, getVerticalGridNum());
                if (limitList.get(i).isShowLine){
                    limitPaint.setColor(limitList.get(i).color);
                    canvas.drawLine(0, limitY, parentWidth, limitY, limitPaint);
                }

                Rect rect = new Rect();
                limitPaint.getTextBounds(limitList.get(i).desc, 0, limitList.get(i).desc.length(), rect);
                canvas.drawText(limitList.get(i).desc, 0, limitY-rect.height(), limitTextPaint);
            }
        }
    }

    private void drawLines(Canvas canvas) {
        for (int k = 0; k < drawDotLists.size(); k++) {
            linePaint.setColor(colorArray[k % colorArray.length]);

            if (isCubic) {
                drawLineBySmooth(canvas, drawDotLists.get(k));
            }else {
                for (int i = 0; i < drawDotLists.get(k).size() - 1; i++) {
                    canvas.drawLine(drawDotLists.get(k).get(i).x, drawDotLists.get(k).get(i).y,
                            drawDotLists.get(k).get(i + 1).x, drawDotLists.get(k).get(i + 1).y,
                            linePaint);
                }
            }
        }
    }

    private void drawLineBySmooth(Canvas canvas, ArrayList<Dot> dots){
        final int lineSize = dots.size();
        float prePreviousPointX = Float.NaN;
        float prePreviousPointY = Float.NaN;
        float previousPointX = Float.NaN;
        float previousPointY = Float.NaN;
        float currentPointX = Float.NaN;
        float currentPointY = Float.NaN;
        float nextPointX = Float.NaN;
        float nextPointY = Float.NaN;

        Path path = new Path();
        /*for (int i=0; i<lineSize; i++) {
            Dot dot = dots.get(i);
            if (i == 0) {
                path.moveTo(dot.x, dot.y);
            }else {
                path.lineTo(dot.x, dot.y);
            }

            if (i == lineSize-1){
                canvas.drawPath(path, linePaint);
                path.reset();
            } else if (i%10 == 0) {
                canvas.drawPath(path, linePaint);
                path.reset();
                path.moveTo(dot.x, dot.y);
            }
        }*/

        for (int valueIndex = 0; valueIndex < lineSize; ++valueIndex) {
            if (Float.isNaN(currentPointX)) {
                Dot linePoint = dots.get(valueIndex);
                currentPointX = linePoint.x;
                currentPointY = linePoint.y;
            }
            if (Float.isNaN(previousPointX)) {
                if (valueIndex > 0) {
                    Dot linePoint = dots.get(valueIndex - 1);
                    previousPointX = linePoint.x;
                    previousPointY = linePoint.y;
                } else {
                    previousPointX = currentPointX;
                    previousPointY = currentPointY;
                }
            }

            if (Float.isNaN(prePreviousPointX)) {
                if (valueIndex > 1) {
                    Dot linePoint = dots.get(valueIndex - 2);
                    prePreviousPointX = linePoint.x;
                    prePreviousPointY = linePoint.y;
                } else {
                    prePreviousPointX = previousPointX;
                    prePreviousPointY = previousPointY;
                }
            }

            // nextPoint is always new one or it is equal currentPoint.
            if (valueIndex < lineSize - 1) {
                Dot linePoint = dots.get(valueIndex + 1);
                nextPointX = linePoint.x;
                nextPointY = linePoint.y;
            } else {
                nextPointX = currentPointX;
                nextPointY = currentPointY;
            }

            if (valueIndex == 0) {
                // Move to start point.
                path.moveTo(currentPointX, currentPointY);
            } else {
                // Calculate control points.
                final float firstDiffX = (currentPointX - prePreviousPointX);
                final float firstDiffY = (currentPointY - prePreviousPointY);
                final float secondDiffX = (nextPointX - previousPointX);
                final float secondDiffY = (nextPointY - previousPointY);
                final float firstControlPointX = previousPointX + (LINE_SMOOTHNESS * firstDiffX);
                final float firstControlPointY = previousPointY + (LINE_SMOOTHNESS * firstDiffY);
                final float secondControlPointX = currentPointX - (LINE_SMOOTHNESS * secondDiffX);
                final float secondControlPointY = currentPointY - (LINE_SMOOTHNESS * secondDiffY);
                path.cubicTo(firstControlPointX, firstControlPointY, secondControlPointX, secondControlPointY,
                        currentPointX, currentPointY);
            }

            // 分多个path绘制，解决path连接太多点不显示的问题
            if (valueIndex == lineSize-1){
                canvas.drawPath(path, linePaint);
                path.reset();
            } else if (valueIndex%10 == 0) {
                canvas.drawPath(path, linePaint);
                path.reset();
                path.moveTo(currentPointX, currentPointY);
            }

            // Shift values by one back to prevent recalculation of values that have
            // been already calculated.
            prePreviousPointX = previousPointX;
            prePreviousPointY = previousPointY;
            previousPointX = currentPointX;
            previousPointY = currentPointY;
            currentPointX = nextPointX;
            currentPointY = nextPointY;
        }

//        canvas.drawPath(path, linePaint);
//        path.reset();
    }

    private void drawDots(Canvas canvas) {
        Paint bigCirPaint = new Paint();
        bigCirPaint.setAntiAlias(true);
        Paint smallCirPaint = new Paint(bigCirPaint);
        smallCirPaint.setColor(Color.parseColor("#FFFFFF"));
        if (drawDotLists != null && !drawDotLists.isEmpty()) {
            for (int k = 0; k < drawDotLists.size(); k++) {
                bigCirPaint.setColor(colorArray[k % colorArray.length]);
                for (Dot dot : drawDotLists.get(k)) {
                    canvas.drawCircle(dot.x, dot.y, DOT_OUTER_CIR_RADIUS, bigCirPaint);
                    canvas.drawCircle(dot.x, dot.y, DOT_INNER_CIR_RADIUS, smallCirPaint);
                }
            }
        }
    }

    /**
     * @param canvas The canvas you need to draw on.
     * @param point  The Point consists of the x y coordinates from left bottom to right top.
     *               Like is
     *               <p>
     *               3
     *               2
     *               1
     *               0 1 2 3 4 5
     */
    private void drawPopup(Canvas canvas, float num, Point point, int PopupColor) {
        String numStr = String.format(Locale.getDefault(), "%."+keepDigits+"f", num);
        int sidePadding = MyUtils.dip2px(getContext(), 5);
        int x = point.x;
        int y = point.y - MyUtils.dip2px(getContext(), 5);
        Rect popupTextRect = new Rect();
        popupTextPaint.getTextBounds(numStr, 0, numStr.length(), popupTextRect);
        int popWidth = popupTextRect.width()<minPopupWidth?minPopupWidth:popupTextRect.width();
        int left = x - popWidth / 2 - sidePadding;
        int right = x + popWidth / 2 + sidePadding;
        int top = y - popupTextRect.height() - bottomTriangleHeight - popupTopPadding * 2 - popupBottomMargin;
        int bottom = y-bottomTriangleHeight;
        Rect r = new Rect(left, top, right, bottom);
        /*Rect r = new Rect(x - popupTextRect.width() / 2 - sidePadding, y
                - popupTextRect.height()
                - bottomTriangleHeight
                - popupTopPadding * 2
                - popupBottomMargin, x + popupTextRect.width() / 2 + sidePadding,
                y + popupTopPadding - popupBottomMargin + popupBottomPadding);*/

        /*NinePatchDrawable popup = (NinePatchDrawable) getResources().getDrawable(R.drawable.popup_red);
        popup.setColorFilter(new PorterDuffColorFilter(PopupColor, PorterDuff.Mode.MULTIPLY));
        popup.setBounds(r);*/

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(PopupColor);
        gd.setCornerRadius(MyUtils.dip2px(getContext(), 50));
        //gd.setStroke(2, 0xFFFF0000);
        gd.setBounds(r);
        gd.draw(canvas);
        canvas.drawText(numStr, x, y - bottomTriangleHeight - popupBottomMargin, popupTextPaint);
    }

    /**
     * 获取Y轴方向的grid单元格个数
     */
    private int getVerticalGridNum() {
        int verticalGridNum = MIN_VERTICAL_GRID_NUM;
        if (dataLists != null && !dataLists.isEmpty()) {
            for (List<Float> list : dataLists) {
                for (Float f : list) {
                    if (verticalGridNum < (f + 1)) {
                        verticalGridNum = (int) Math.floor(f + 1);
                    }
                }
            }
        }

        // 如果limit的最大值大于上边计算的值则设置成limit的最大值，保证设置的limit都能看见
        if (limitList != null && limitList.size()>0){
            int limitMaxValue = (int) Math.floor(limitList.get(limitList.size()-1).yValue);
            verticalGridNum = verticalGridNum<limitMaxValue?limitMaxValue:verticalGridNum;
        }

        if (fitMinLimit && limitList != null && limitList.size()>0){
            float minLimitValue = limitList.get(0).yValue;
            verticalGridNum -= minLimitValue;
        }

        return verticalGridNum;
    }

    /**
     * 获取X轴方向的GRID单元格个数
     */
    private int getHorizontalGridNum() {
        int horizontalGridNum = bottomTextList.size() - 1;
        if (horizontalGridNum < MIN_HORIZONTAL_GRID_NUM) {
            horizontalGridNum = MIN_HORIZONTAL_GRID_NUM;
        }
        return horizontalGridNum;
    }

    /**
     * 获取背景Bottom
     */
    private int getGridBottom(){
        return mViewHeight - bottomTextTopMargin - bottomTextHeight - bottomTextDescent;
    }

    private void refreshAfterDataChanged() {
        int verticalGridNum = getVerticalGridNum();
        refreshYCoordinateList(verticalGridNum);
        refreshDrawDotList(verticalGridNum);
    }

    /**
     * 计算x轴坐标列表
     */
    private void refreshXCoordinateList(int horizontalGridNum) {
        xCoordinateList.clear();
        for (int i = 0; i < (horizontalGridNum + 1); i++) {
            xCoordinateList.add(sideLineLength + backgroundGridWidth * i);
        }
    }

    private void refreshYCoordinateList(int verticalGridNum) {
        yCoordinateList.clear();
        for (int i = 0; i < (verticalGridNum + 1); i++) {
            yCoordinateList.add(topLineLength + ((mViewHeight
                    - topLineLength
                    - bottomTextHeight
                    - bottomTextTopMargin
                    - bottomLineLength
                    - bottomTextDescent) * i / (verticalGridNum)));
        }
    }

    private void refreshDrawDotList(int verticalGridNum) {
        if (dataLists != null && !dataLists.isEmpty()) {
            if (drawDotLists.size() == 0) {
                for (int k = 0; k < dataLists.size(); k++) {
                    drawDotLists.add(new ArrayList<Dot>());
                }
            }

            for (int k = 0; k < dataLists.size(); k++) {
                int drawDotSize = drawDotLists.get(k).isEmpty() ? 0 : drawDotLists.get(k).size();

                for (int i = 0; i < dataLists.get(k).size(); i++) {
                    int x = xCoordinateList.get(i);
                    float y = getYAxesOf(dataLists.get(k).get(i), verticalGridNum);
                    if (i > drawDotSize - 1) {
                        drawDotLists.get(k).add(new Dot(getContext(), x, 0, x, y, dataLists.get(k).get(i), k));
                    } else {
                        drawDotLists.get(k)
                                .set(i, drawDotLists.get(k)
                                        .get(i)
                                        .setTargetData(x, y, dataLists.get(k).get(i), k));
                    }
                }

                int temp = drawDotLists.get(k).size() - dataLists.get(k).size();
                for (int i = 0; i < temp; i++) {
                    drawDotLists.get(k).remove(drawDotLists.get(k).size() - 1);
                }
            }
        }

        removeCallbacks(animator);
        post(animator);
    }

    private float getYAxesOf(float value, int verticalGridNum) {
        float y = verticalGridNum - value;
        if (fitMinLimit && limitList != null && limitList.size()>0){
            float minLimitValue = limitList.get(0).yValue;
            y += minLimitValue;
        }

        return topLineLength + ((mViewHeight
                - topLineLength
                - bottomTextHeight
                - bottomTextTopMargin
                - bottomLineLength
                - bottomTextDescent) * y / (getVerticalGridNum()));
    }

    private void refreshTopLineLength() {
        // For prevent popup can't be completely showed when backgroundGridHeight is too small.
        topLineLength = getPopupHeight() + DOT_OUTER_CIR_RADIUS + DOT_INNER_CIR_RADIUS + 2;
    }

    private int getPopupHeight() {
        Rect popupTextRect = new Rect();
        popupTextPaint.getTextBounds("9", 0, 1, popupTextRect);
        Rect r = new Rect(-popupTextRect.width() / 2, -popupTextRect.height()
                - bottomTriangleHeight
                - popupTopPadding * 2
                - popupBottomMargin, +popupTextRect.width() / 2,
                +popupTopPadding - popupBottomMargin + popupBottomPadding);
        return r.height();
    }

    private int getMeasurement(int measureSpec, int preferred) {
        int specSize = MeasureSpec.getSize(measureSpec);
        int measurement;
        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.EXACTLY:
                measurement = specSize;
                break;
            case MeasureSpec.AT_MOST:
                measurement = Math.min(preferred, specSize);
                break;
            default:
                measurement = preferred;
                break;
        }
        return measurement;
    }

    private Dot findPointAt(int x, int y) {
        if (drawDotLists.isEmpty()) {
            return null;
        }

        final int width = backgroundGridWidth / 2;
        final Region r = new Region();

        for (ArrayList<Dot> data : drawDotLists) {
            for (Dot dot : data) {
                final int pointX = dot.x;
                final int pointY = (int) dot.y;

                r.set(pointX - width, pointY - width, pointX + width, pointY + width);
                if (r.contains(x, y)) {
                    return dot;
                }
            }
        }

        return null;
    }

    /**
     * 这种方式只支持一条线，多条线使用setBottomTextList & setDataList
     */
    public void setEntityList(List<LineEntity> entityList) {
        if (entityList != null && entityList.size()>0) {
            List<Float> data = new ArrayList<>();
            for (LineEntity entity : entityList) {
                bottomTextList.add(entity == null || TextUtils.isEmpty(entity.xAxisText)?"":entity.xAxisText);
                data.add(entity==null?0f:entity.yValue);
            }
            dataLists.add(data);

            setBottomTextList(bottomTextList);
            setDataList(dataLists);
        }
    }

    /**
     * X轴上显示的列表
     *
     * @param bottomTextList The String ArrayList in the bottom.
     */
    public void setBottomTextList(ArrayList<String> bottomTextList) {
        this.bottomTextList = bottomTextList;

        Rect r = new Rect();
        int longestWidth = 0;
        String longestStr = "";
        bottomTextDescent = 0;
        for (String s : bottomTextList) {
            bottomTextPaint.getTextBounds(s, 0, s.length(), r);
            if (bottomTextHeight < r.height()) {
                bottomTextHeight = r.height();
            }

            if (autoSetGridWidth && (longestWidth < r.width())) {
                longestWidth = r.width();
                longestStr = s;
            }

            if (bottomTextDescent < (Math.abs(r.bottom))) {
                bottomTextDescent = Math.abs(r.bottom);
            }
        }

        if (autoSetGridWidth) {
            if (backgroundGridWidth < longestWidth) {
                backgroundGridWidth = longestWidth + (int) bottomTextPaint.measureText(longestStr, 0, 1);
            }

            if (sideLineLength < longestWidth / 2) {
                sideLineLength = longestWidth / 2;
            }
        }
    }

    public void setDataList(List<List<Float>> dataLists) {
        selectedDot = null;
        this.dataLists = dataLists;

        for (List<Float> list : dataLists) {
            if (list.size() > bottomTextList.size()) {
                throw new RuntimeException("dacer.LineView error:" + " dataList.size() > bottomTextList.size() !!!");
            }
        }

        float biggestData = 0;
        for (List<Float> list : dataLists) {
            if (autoSetDataOfGird) {
                for (Float i : list) {
                    if (biggestData < i) {
                        biggestData = i;
                    }
                }
            }

            dataOfAGird = 1;
            while (biggestData / 10 > dataOfAGird) {
                dataOfAGird *= 10;
            }
        }

        if (isSizeChanged) {
            refreshAfterDataChanged();
        }
        showPopup = true;
        setMinimumWidth(0); // It can help the LineView reset the Width,
        // I don't know the better way..
        postInvalidate();
    }

    // pop显示模式
    public void setShowPopup(int popupType) {
        this.showPopupType = popupType;
    }

    // 背景辅助横线是否为虚线
    public void setDrawDotLine(Boolean drawDotLine) {
        this.drawDotLine = drawDotLine;
    }

    // 折线的颜色组
    public void setColorArray(int[] colors) {
        this.colorArray = colors;
    }

    // 设置X坐标轴的颜色
    public void setxAxisColor(int xAxisColor) {
        this.xAxisColor = xAxisColor;
        xAxisPaint.setColor(xAxisColor);
    }

    // 设置临界值列表
    public void setLimitList(List<Limit> limitList) {
        if (limitList != null && limitList.size()>0) {
            this.limitList.addAll(limitList);
            Collections.sort(this.limitList);
        }
    }

    // 设置是否显示背景表格，默认不显示
    public void setShowBackGrid(boolean showBackGrid) {
        this.showBackGrid = showBackGrid;
    }

    // 保留小数位数
    public void setKeepDigits(int keepDigits) {
        this.keepDigits = keepDigits;
    }

    public void setFitMinLimit(Boolean fitMinLimit) {
        this.fitMinLimit = fitMinLimit;
    }

    // 设置一屏显示的坐标点个数
    public void setMaxDotNum(int maxDotNum) {
        this.maxDotNum = maxDotNum;
    }

    // 设置是否显示平滑曲线
    public void setCubic(Boolean cubic) {
        isCubic = cubic;
    }
}
