package com.popo.cuterecyclerview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class BoomView extends View {
    private int middleX;
    private int middleY;
    private int recHeight;
    private int recWidth;
    private int progress=0;
    public BoomView(Context context) {
        super(context);
        middleX=(getRight()-getLeft())/2;
        middleY=(getBottom()-getTop())/2;

    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    public BoomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        middleX=(getRight()-getLeft())/2;
        middleY=(getBottom()-getTop())/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint rRecPaint=new Paint();
        rRecPaint.setStyle(Paint.Style.STROKE);
        rRecPaint.setAntiAlias(true);
        rRecPaint.setStrokeWidth(5);
        canvas.drawRoundRect(65,7,getRight()-65,7+13,50,50,rRecPaint);

        Paint progressPaint=new Paint();
        progressPaint.setStyle(Paint.Style.FILL);
        progressPaint.setAntiAlias(true);
        progressPaint.setColor(getResources().getColor(R.color.colorAccent));
        float length=getRight()-135;
        canvas.drawRoundRect(67,10,67+(length*progress)/100,18,50,50,progressPaint);
    }
    public void animateStart(){
        ObjectAnimator animator=ObjectAnimator.ofInt(this,"progress",0,100).setDuration(1500);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }
}
