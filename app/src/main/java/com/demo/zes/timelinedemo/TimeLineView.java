package com.demo.zes.timelinedemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class TimeLineView extends View {


    private Paint mPaint;
    /**
     * 第一个节点的外半径
     */
    private float timelineHeadRadius;
    /**
     * 第一个节点的颜色值
     */
    private int timelineHeadColor;
    /**
     * 第二个节点的颜色值
     */
    private int timelineOtherColor;
    /**
     * 时间线的节点数
     */
    private int timelineCount;
    /**
     * 时间轴的位置
     */
    private int viewWidth;
    /**
     * 时间轴到距离顶部的距离
     */
    private int marginTop;
    /**
     * 时间轴的节点的半径
     */
    private int timelineRadius;
    /**
     * 时间轴节点之间的距离
     */
    private int timelineRadiusDistance;
    /**
     * 时间轴的宽度
     */
    private int timelineWidth;
    /**
     * 时间轴的高度
     */
    private float timeLineViewHeight;

    public TimeLineView(Context context) {
        this(context, null);
    }

    public TimeLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeLineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    private void init(Context context, AttributeSet attrs, int defStyle) {

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TimeLineView, defStyle, 0);
        timelineRadiusDistance = (int) a.getDimension(R.styleable.TimeLineView_timelineRadiusDistance, convertDIP2PX(context, 20));
        timelineHeadRadius = a.getDimension(R.styleable.TimeLineView_timelineHeadRadius, convertDIP2PX(context, 10));
        timelineRadius = (int) a.getDimension(R.styleable.TimeLineView_timelineRadius, convertDIP2PX(context, 5));
        timelineHeadColor = a.getColor(R.styleable.TimeLineView_timelineHeadColor, Color.parseColor("#73be36"));
        timelineOtherColor = a.getColor(R.styleable.TimeLineView_timelineOtherColor, Color.parseColor("#e0e0e0"));
        timelineCount = a.getInteger(R.styleable.TimeLineView_timelineCount, 0);
        timelineWidth = (int) a.getDimension(R.styleable.TimeLineView_timelineWidth, convertDIP2PX(context, 1));
        marginTop = (int) a.getDimension(R.styleable.TimeLineView_timelineMarginTop, convertDIP2PX(context, 50));
        a.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        //默认设置时间轴的位置位于view的中间
        viewWidth = getMeasuredWidth() / 2;
        //设置第一个节点的颜色
        mPaint.setColor(timelineHeadColor);
        /**
         * 根据时间轴的节点数目，画对应的节点和轴
         */
        for (int j = 1; j <= timelineCount; j++) {

            /**
             * 当j==1，画第一个节点的时候，有点特殊，我们需要在节点的外面再换一个圆环
             */
            if (j == 1) {
                //画笔设置为空心
                canvas.drawCircle(viewWidth, timelineHeadRadius + marginTop, timelineRadius, mPaint);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(5.0f);
                //画第一个节点外围的圆环
                canvas.drawCircle(viewWidth, timelineHeadRadius + marginTop, timelineHeadRadius, mPaint);
                //设置画笔颜色，画其他时间节点的颜色
                mPaint.setColor(timelineOtherColor);
                //画笔设置为实心
                mPaint.setStyle(Paint.Style.FILL);
                /**
                 * 画第一个节点下面的轴
                 */
                canvas.drawRect(new Rect(viewWidth - timelineWidth / 2, (int) (2 * timelineHeadRadius + marginTop) + 5, viewWidth + timelineWidth / 2, (int) (2 * timelineHeadRadius + timelineRadiusDistance + marginTop + 5)), mPaint);
                continue;
            }
            /**
             * 画时间轴的节点,即画圆形
             * 圆心的x都是一样的,view的中间
             * 圆心的y的计算是根据节点的位置来计算的,例如:第一个节点的y是根据第一个节点距离上面的距离加上第一个节点的半径:timelineHeadRadius + marginTop
             * 其余的节点就是在一个节点的y的基础上,加上两倍半径和节点之间的轴的长度＊节点数:(2 * timelineRadius + timelineRadiusDistance) * (j - 1) + timelineHeadRadius - timelineRadius + marginTop
             *
             */
            canvas.drawCircle(viewWidth, (2 * timelineRadius + timelineRadiusDistance) * (j - 1) + 2 * timelineHeadRadius - timelineRadius + marginTop, timelineRadius, mPaint);
            /**
             * 　
             *  画其余的轴
             *  left:每个轴距离左边距离都是一样的　　　时间轴的中心位置-1/2的时间轴的宽度  viewWidth - timelineWidth / 2
             *  top:   (int) (j * (2 * timelineRadius + timelineRadiusDistance) - timelineRadiusDistance + 2 * (timelineHeadRadius-timelineRadius)+ marginTop)　
             *  right:每个轴距离右边距离都是一样的　　　时间轴的中心位置+1/2的时间轴的宽度  viewWidth + timelineWidth / 2
             *  bottom: (int) (j * (2 * timelineRadius + timelineRadiusDistance) + 2 * (timelineHeadRadius-timelineRadius)+ marginTop)
             */

            canvas.drawRect(new Rect(viewWidth - timelineWidth / 2, (int) (j * (2 * timelineRadius + timelineRadiusDistance) - timelineRadiusDistance + 2 * (timelineHeadRadius - timelineRadius) + marginTop), viewWidth + timelineWidth / 2, (int) (j * (2 * timelineRadius + timelineRadiusDistance) + 2 * (timelineHeadRadius - timelineRadius) + marginTop)), mPaint);
        }
    }

    public float getTimelineHeadRadius() {
        return timelineHeadRadius;
    }

    public void setTimelineHeadRadius(float timelineHeadRadius) {

        this.timelineHeadRadius = timelineHeadRadius;
        invalidate();
    }

    public int getTimelineHeadColor() {
        return timelineHeadColor;
    }

    public void setTimelineHeadColor(int timelineHeadColor) {

        this.timelineHeadColor = timelineHeadColor;
        invalidate();
    }

    public int getTimelineOtherColor() {
        return timelineOtherColor;
    }

    public void setTimelineOtherColor(int timelineOtherColor) {
        this.timelineOtherColor = timelineOtherColor;
        invalidate();
    }

    public int getTimelineCount() {
        return timelineCount;
    }

    public void setTimelineCount(int timelineCount) {
        this.timelineCount = timelineCount;
        invalidate();
    }

    public int getMarginTop() {
        return marginTop;
    }

    public void setMarginTop(int marginTop) {

        this.marginTop = marginTop;
        invalidate();
    }

    public int getTimelineRadius() {
        return timelineRadius;
    }

    public void setTimelineRadius(int timelineRadius) {

        this.timelineRadius = timelineRadius;
        invalidate();
    }

    public int getTimelineRadiusDistance() {
        return timelineRadiusDistance;
    }

    public void setTimelineRadiusDistance(int timelineRadiusDistance) {

        this.timelineRadiusDistance = timelineRadiusDistance;
        invalidate();
    }

    public int getTimelineWidth() {
        return timelineWidth;
    }

    public void setTimelineWidth(int timelineWidth) {
        this.timelineWidth = timelineWidth;
    }

    public float getTimeLineViewHeight() {
        this.timeLineViewHeight = getMarginTop() + getTimelineCount() * (2 * getTimelineRadius() + getTimelineRadiusDistance());
        return timeLineViewHeight;
    }

    public void setTimeLineViewHeight(float timeLineViewHeight) {
        this.timeLineViewHeight = timeLineViewHeight;
        invalidate();

    }

    public int getViewWidth() {
        return viewWidth;
    }

    public void setViewWidth(int viewWidth) {
        this.viewWidth = viewWidth;
        invalidate();
    }

    /**
     * 转换dip为px
     */
    public static int convertDIP2PX(Context context, int dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

}