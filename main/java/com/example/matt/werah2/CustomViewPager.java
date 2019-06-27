package com.example.matt.werah2;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by matt on 12/22/2017.
 */

public class CustomViewPager extends ViewPager {
    private boolean swipeable;

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSwipeable(boolean swipeable){
        this.swipeable = swipeable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (this.swipeable){
            return super.onTouchEvent(ev);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.swipeable){
            return super.onInterceptTouchEvent(ev);
        }
        return false;
    }
}
