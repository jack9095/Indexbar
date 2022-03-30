package com.fei.indexbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 安卓字母导航view 左边显示对应的悬浮窗口
 */
public class IndexBarTipsView extends RelativeLayout {
    private TextView textview;

    public IndexBarTipsView(Context context) {
        this(context, null);
    }

    public IndexBarTipsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexBarTipsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_floating_window, this, true);
        textview = findViewById(R.id.textview);
    }

    /**
     * 关键代码，可以让悬浮窗随着滑动的字母移动
     * @param text
     * @param poistion
     * @param y
     */
    public void setText(String text,int poistion, float y){
        textview.setText(text);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textview.getLayoutParams();
        layoutParams.topMargin = (int)(y - getWidth()/2.8);
        textview.setLayoutParams(layoutParams);
    }


}
