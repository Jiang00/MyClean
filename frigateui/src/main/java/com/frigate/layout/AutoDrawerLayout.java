package com.frigate.layout;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;

import com.frigate.event.IAutoEvent;
import com.frigate.event.IFrigateEventListener;
import com.frigate.utils.AutoLayoutHelper;

/**
 * Created by ${} on 2018/1/24.
 */

public class AutoDrawerLayout extends DrawerLayout {
    public AutoLayoutHelper mHelper;
    Context mContext;

    public AutoDrawerLayout(Context context) {
        this(context, null);
    }

    public AutoDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        mHelper = new AutoLayoutHelper(this, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHelper.onFinishInflate(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public interface AutoLayoutEventListener extends IAutoEvent {

    }
    private AutoLayoutEventListener autoLayoutEventListener;


    public IAutoEvent getEventListener() {
        return autoLayoutEventListener;
    }

    public void setEventListener(IFrigateEventListener autoLayoutEventListener) {
        this.autoLayoutEventListener = autoLayoutEventListener;
    }

}
