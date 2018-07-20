package com.popo.cuterecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class SlidingMenu extends HorizontalScrollView{
    private static final float radio=0.2f;
    private final int screenWidth;
    private final int menuWidth;
    private boolean once =true;
    private boolean isOpen=false;

    public SlidingMenu(final Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        screenWidth=outMetrics.widthPixels;
        menuWidth=(int)(screenWidth*radio);
        setOverScrollMode(View.OVER_SCROLL_NEVER);
        setHorizontalScrollBarEnabled(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(once){
            LinearLayout wrapper=(LinearLayout)getChildAt(0);
            wrapper.getChildAt(0).getLayoutParams().width=screenWidth;
            wrapper.getChildAt(1).getLayoutParams().width=menuWidth;
            once=false;
        }
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_UP:
                int scrollX=getScrollX();
                if(Math.abs(scrollX)>menuWidth/2){
                    this.smoothScrollTo(menuWidth,0);
                    onOpenMenu();
                }else {
                    View view = this;
                    while (true) {
                        view = (View) view.getParent();
                        if (view instanceof RecyclerView) {
                            break;
                        }
                    }
                    ((CuteAdapter) ((RecyclerView) view).getAdapter()).closeOpenMenu();
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }

    public void closeMenu(){
        this.smoothScrollTo(0,0);
        isOpen=false;
    }

    public boolean isOpen(){
        return isOpen;
    }

    private void onOpenMenu() {
        if (!isOpen) {
            View view = this;
            while (true) {
                view = (View) view.getParent();
                if (view instanceof RecyclerView) {
                    break;
                }
            }
            ((CuteAdapter) ((RecyclerView) view).getAdapter()).closeOpenMenu();
            ((CuteAdapter) ((RecyclerView) view).getAdapter()).holdOpenMenu(this);
            isOpen = true;
        }
    }
}
