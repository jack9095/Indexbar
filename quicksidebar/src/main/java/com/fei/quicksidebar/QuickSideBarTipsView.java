package com.fei.quicksidebar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fei.quicksidebar.tipsview.QuickSideBarTipsItemView;

/**
 * Created by Sai on 16/3/26.
 */
public class QuickSideBarTipsView extends RelativeLayout {
    private QuickSideBarTipsItemView mTipsView;
    private TextView textview;

    public QuickSideBarTipsView(Context context) {
        this(context, null);
    }

    public QuickSideBarTipsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickSideBarTipsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_floating_window, this, true);
        mTipsView = findViewById(R.id.tipsView);
        textview = findViewById(R.id.textview);
//        mTipsView = new QuickSideBarTipsItemView(context,attrs);
//        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        addView(mTipsView,layoutParams);
    }


    public void setText(String text,int poistion, float y){
        mTipsView.setText(text);
        textview.setText(text);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textview.getLayoutParams();
        layoutParams.topMargin = (int)(y - getWidth()/2.8 + 200);
        textview.setLayoutParams(layoutParams);
    }


}
