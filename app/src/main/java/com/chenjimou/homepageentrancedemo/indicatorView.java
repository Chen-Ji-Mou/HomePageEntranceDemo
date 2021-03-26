package com.chenjimou.homepageentrancedemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class indicatorView extends View {
    // 指示器背景的画笔
    private Paint mIndicatorBackgroundPaint;
    // 指示器的画笔
    private Paint mIndicatorPaint;
    private RectF mIndicatorBackground;
    private RectF mIndicator;
    // 指示器长度占显示宽度的比例（默认为1）
    private float proportion = 1f;
    // 滑动位置占显示宽度的比例
    private float progress = 0f;
    // View的宽度（默认100）
    private int totalWidth = 100;
    // View的高度（默认10）
    private int totalHeight = 10;
    // 矩形圆角的半径
    private float radius = 0f;
    // 指示器背景的颜色（默认灰色）
    private int indicatorBackgroundColor = Color.GRAY;
    // 指示器背景的颜色（默认黑色）
    private int indicatorColor = Color.BLACK;

    public indicatorView(Context context) {
        this(context, null);
    }

    public indicatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public indicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 初始化
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {

        // 通过TypedArray获取布局文件中设置的属性值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.indicatorView);
        indicatorBackgroundColor = typedArray.getColor(R.styleable.indicatorView_indicator_background_color,
                indicatorBackgroundColor);
        indicatorColor = typedArray.getColor(R.styleable.indicatorView_indicator_color, indicatorColor);
        typedArray.recycle();

        mIndicatorBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIndicatorBackgroundPaint.setStyle(Paint.Style.FILL);
        mIndicatorBackgroundPaint.setColor(indicatorBackgroundColor);

        mIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIndicatorPaint.setStyle(Paint.Style.FILL);
        mIndicatorPaint.setColor(indicatorColor);

        mIndicatorBackground = new RectF();
        mIndicator = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 测量View的宽高
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
        // 记录View的宽度
        totalWidth = getMeasuredWidth();
        // 设置指示器背景的画布大小
        mIndicatorBackground.set(0f, 0f, getMeasuredWidth() * 1f, getMeasuredHeight() * 1f);
        // 设置圆角的半径
        radius = getMeasuredHeight() / 2f;
    }

    private int measureWidth(final int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int result = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result = totalWidth + getPaddingLeft() + getPaddingRight();
                break;
        }
        // 如果是AT_MOST,不能超过父布局的尺寸
        result = (mode == MeasureSpec.AT_MOST) ? Math.min(result, size) : result;
        return result;
    }

    private int measureHeight(final int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int result = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result = totalHeight + getPaddingTop() + getPaddingBottom();
                break;
        }
        // 如果是AT_MOST,不能超过父布局的尺寸
        result = (mode == MeasureSpec.AT_MOST) ? Math.min(result, size) : result;
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画指示器背景
        canvas.save();
        canvas.drawRoundRect(mIndicatorBackground, radius, radius, mIndicatorBackgroundPaint);
        canvas.restore();
        // 计算指示器的位置，设置指示器的画布大小
        float left = mIndicatorBackground.left + totalWidth * (1f - proportion) * progress;
        float right = left + totalWidth * proportion;
        mIndicator.set(left, mIndicatorBackground.top, right, mIndicatorBackground.bottom);
        // 画指示器
        canvas.save();
        canvas.drawRoundRect(mIndicator, radius, radius, mIndicatorPaint);
        canvas.restore();
    }

    public void bindRecyclerView(RecyclerView recyclerView){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                // 已经向水平方向滚动的距离，为0时表示已处于边界
                int offsetX = recyclerView.computeHorizontalScrollOffset();
                // 整体的宽度，包括在显示区域之外的
                int range = recyclerView.computeHorizontalScrollRange();
                // 当前RecyclerView显示区域的宽度
                int extend = recyclerView.computeHorizontalScrollExtent();
                // 计算滑动位置占显示范围的比例
                float progress = (float) offsetX / (range - extend);
                // 设置比例，刷新视图
                setProgress(progress);
            }
        });
        recyclerView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                // 整体的宽度，包括在显示区域之外的
                int range = recyclerView.computeHorizontalScrollRange();
                // 当前RecyclerView显示区域的宽度
                int extend = recyclerView.computeHorizontalScrollExtent();
                // 定死指示器长度占显示宽度的比例为 extend / range
                float proportion = (float) extend / range;
                // 设置比例，刷新视图
                setProportion(proportion);
            }
        });
    }

    private void setProportion(float proportion) {
        this.proportion = proportion;
        invalidate();
    }

    private void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    public void setTotalWidth(int totalWidth) {
        this.totalWidth = totalWidth;
        requestLayout();
        invalidate();
    }

    public void setTotalHeight(int totalHeight) {
        this.totalHeight = totalHeight;
        requestLayout();
        invalidate();
    }
}
