package com.liberal.young.tuomanprivatecloud.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 *
 * com.liberal.young.tuomanprivatecloud.view.MyFormScrollView
 * Created by Administrator on 2017/3/16.
 */

public class MyFormScrollView extends ScrollView{

    private ScrollViewListener scrollViewListener = null;

    public MyFormScrollView(Context context) {
        super(context);
    }

    public MyFormScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFormScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    //回调接口
    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    @Override
    public void fling(int velocityY) {
        super.fling(velocityY/1000);         // 除以1000，是为了取消ScrollView的滑动惯性
    }
}
