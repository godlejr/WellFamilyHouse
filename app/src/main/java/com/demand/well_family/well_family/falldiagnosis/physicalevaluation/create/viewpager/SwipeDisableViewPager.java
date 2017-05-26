package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by ㅇㅇ on 2017-05-26.
 */

public class SwipeDisableViewPager extends ViewPager {
    public SwipeDisableViewPager(Context context) {
        super(context);
    }

    public SwipeDisableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }
}
