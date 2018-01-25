package net.jiaobaowang.visitor.custom_view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by rocka on 2018/1/25.
 */

public class VisitViewPager extends ViewPager {
    private boolean isCanScroll = false;

    public VisitViewPager(Context context) {
        super(context);
    }

    public VisitViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isCanScroll && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isCanScroll && super.onInterceptTouchEvent(ev);

    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item,false);
    }


    public void setCanScroll(boolean canScroll) {
        isCanScroll = canScroll;
    }
}
