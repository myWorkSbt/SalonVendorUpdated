package com.vendor.salon.utilityMethod;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.vendor.salon.R;

public class HalfCurvedProgressBar extends View {
    private Drawable mBackgroundDrawable;
    private Drawable mProgressDrawable;
    private int mMax;
    private int mProgress;

    public HalfCurvedProgressBar(Context context) {
        super(context);
        init();
    }

    public HalfCurvedProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HalfCurvedProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mMax = 100;
        mProgress = 0;
        mBackgroundDrawable = getResources().getDrawable(R.drawable.bgprogresslay);
//        mProgressDrawable = mBackgroundDrawable.findDrawableByLayerId(android.R.id.progress);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = mBackgroundDrawable.getIntrinsicWidth();
        int height = mBackgroundDrawable.getIntrinsicHeight();
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mBackgroundDrawable.draw(canvas);
        Rect progressBounds = mBackgroundDrawable.getBounds();
        int progressWidth = (int) ((float) mProgress / (float) mMax * progressBounds.width());
        mProgressDrawable.setBounds(progressBounds.left, progressBounds.top, progressBounds.left + progressWidth, progressBounds.bottom);
        mProgressDrawable.draw(canvas);
    }

    public void setMax(int max) {
        mMax = max;
    }

    public void setProgress(int progress) {
        mProgress = progress;
        invalidate();
    }
}

