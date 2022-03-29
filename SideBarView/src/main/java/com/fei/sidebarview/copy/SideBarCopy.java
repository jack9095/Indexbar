package com.fei.sidebarview.copy;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.fei.sidebarview.R;
import com.fei.sidebarview.TextDialog;

public class SideBarCopy extends View{
    // 触摸事件
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

    /** sidebar的字符列表 **/
    private CharSequence[] mLetters;

    private int mChoose = -1;// 选中

    private Paint mPaint = new Paint(); // 画笔

    private TextView mTextView;

    private Context mContext;

    /** 字符颜色 **/
    private int mSideTextColor;
    /** 字符选中时的颜色 **/
    private int mSideTextSelectColor;
    /** 字符大小 **/
    private float mSideTextSize;
    /** 滑动时背景颜色 **/
    private Drawable mSideBackground;

    private TextDialog mTextDialog;
    /** 显示弹窗 **/
    private boolean isShowTextDialog = true;
    /** 选中弹窗字符颜色 **/
    private int mDialogTextColor;
    /** 选中弹窗字符大小 **/
    private float mDialogTextSize;
    /** 选中弹窗字符背景颜色 **/
    private Drawable mDialogTextBackground;
    /** 选中弹窗字符背景宽度 **/
    private int mDialogTextBackgroundWidth;
    /** 选中弹窗字符背景高度 **/
    private int mDialogTextBackgroundHeight;

    public SideBarCopy(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext=context;
        initData(attrs);
    }

    public SideBarCopy(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        initData(attrs);
    }

    public SideBarCopy(Context context) {
        super(context);
        mContext=context;
        initData(null);
    }

    private float mItemStartY; // 第一个字母起始位置的 Y 坐标
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mHeight = getMeasuredHeight();
//        mItemStartY = (mHeight - mLetters.length * mItemHeight) / 2;
    }

    /**
     * 重写这个方法
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取焦点改变背景颜色.
        int height = getHeight();// 获取对应高度
        int width = getWidth(); // 获取对应宽度
        int singleHeight = height / mLetters.length;// 获取每一个字母的高度

        for (int i = 0; i < mLetters.length; i++) {
            mPaint.setColor(mSideTextColor);
            mPaint.setTextSize(mSideTextSize);
            mPaint.setTypeface(Typeface.DEFAULT);
            mPaint.setAntiAlias(true);
            // 选中的状态
            if (i == mChoose) {
                mPaint.setColor(mSideTextSelectColor);
                mPaint.setFakeBoldText(true);
            }
            // x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - mPaint.measureText((String) mLetters[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText((String) mLetters[i], xPos, yPos, mPaint);
            mPaint.reset();// 重置画笔
        }

    }

    public CharSequence[] getLetters() {
        return mLetters;
    }

    /**
     * 初始化数据
     * @param attrs
     */
    private void initData(@Nullable AttributeSet attrs) {
        // 加载默认资源
        final Resources res = getResources();
        final CharSequence[] defaultStringArray = res.getTextArray(R.array.side_bar_def_list);
        final int defaultSideTextColor = res.getColor(R.color.default_side_text_color);
        final int defaultSideTextSelectColor = res.getColor(R.color.default_side_text_select_color);
        final float defaultSideTextSize = res.getDimension(R.dimen.default_side_text_size);
        final Drawable defaultSideBackground = res.getDrawable(R.drawable.default_side_background);
        final int defaultDialogTextColor = res.getColor(R.color.sidebar_default_dialog_text_color);
        final float defaultDialogTextSize = res.getDimension(R.dimen.default_dialog_text_size);
        final Drawable defaultDialogTextBackground = res.getDrawable(R.drawable.default_dialog_text_background);
        final int defaultDialogTextBackgroundWidth = res.getDimensionPixelSize(R.dimen.default_dialog_text_background_width);
        final int defaultDialogTextBackgroundHeight = res.getDimensionPixelSize(R.dimen.default_dialog_text_background_height);
        // 读取配置信息
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.SideBar);

        mLetters =  a.getTextArray(R.styleable.SideBar_sideTextArray);

        if (null == mLetters ||mLetters.length <= 0){
            mLetters =  defaultStringArray;
        }
        mSideTextColor = a.getColor(R.styleable.SideBar_sideTextColor, defaultSideTextColor);

        mSideTextSelectColor = a.getColor(R.styleable.SideBar_sideTextSelectColor, defaultSideTextSelectColor);

        mSideTextSize = a.getDimension(R.styleable.SideBar_sideTextSize, defaultSideTextSize);

        mSideBackground = a.getDrawable(R.styleable.SideBar_sideBackground);

        if (null == mSideBackground){
            mSideBackground = defaultSideBackground;
        }

        //弹窗配置
//        isShowTextDialog = a.getBoolean(R.styleable.SideBar_isShowDialog,false);
        mDialogTextColor = a.getColor(R.styleable.SideBar_dialogTextColor, defaultDialogTextColor);
        mDialogTextSize = a.getDimension(R.styleable.SideBar_dialogTextSize, defaultDialogTextSize);
        mDialogTextBackground = a.getDrawable(R.styleable.SideBar_dialogTextBackground);
        if (null == mDialogTextBackground){
            mDialogTextBackground = defaultDialogTextBackground;
        }
        mDialogTextBackgroundWidth = a.getDimensionPixelSize(R.styleable.SideBar_dialogTextBackgroundWidth, defaultDialogTextBackgroundWidth);
        mDialogTextBackgroundHeight = a.getDimensionPixelSize(R.styleable.SideBar_dialogTextBackgroundHeight, defaultDialogTextBackgroundHeight);
        // 释放内存，回收资源
        a.recycle();

        if (isShowTextDialog) mTextDialog = new TextDialog(mContext, mDialogTextBackgroundWidth, mDialogTextBackgroundHeight, mDialogTextColor, mDialogTextSize, mDialogTextBackground);
    }

    @SuppressLint("NewApi")
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();// 点击y坐标
        final int oldChoose = mChoose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * mLetters.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.

        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundDrawable(new ColorDrawable(0x00000000));
                invalidate();
                if (mTextView != null) {
                    mTextView.setVisibility(View.INVISIBLE);
                }
                if (mTextDialog != null) mTextDialog.dismiss();
                break;

            default:
                setBackground(mSideBackground);
                setAlpha((float) 1.0);
                if (oldChoose != c) {
                    if (c >= 0 && c < mLetters.length) {
                        if (listener != null) {
                            //计算位置
//                            Rect rect = new Rect();
//                            mPaint.getTextBounds(mLetters[mChoose], 0, mLetters[mChoose].length(), rect);
//                            float yPos = mItemHeight * mChoose + (int) ((mItemHeight - rect.height()) * 0.5) + mItemStartY;
                            listener.onTouchingLetterChanged((String) mLetters[c], 0);
                        }
                        if (mTextView != null) {
                            mTextView.setText(mLetters[c]);
                            mTextView.setVisibility(View.VISIBLE);
                        }

                        if (mTextDialog != null) mTextDialog.show((String) mLetters[c]);

                        mChoose = c;
                        invalidate();
                    }
                }

                break;
        }
        return true;
    }

    public void setChooseStr(String s){
        int i = 0;
        for (CharSequence b: mLetters){
            if (b.toString().equalsIgnoreCase(s)){
                setChoose(i);
                break;
            }
            i++;
        }
    }

    private void setChoose(int c){
        if (this.mChoose != c) {
            if (c >= 0 && c < mLetters.length) {
                if (mTextView != null) {
                    mTextView.setText(mLetters[c]);
                    mTextView.setVisibility(View.VISIBLE);
                }
//                if (mTextDialog != null) mTextDialog.show((String) letters[c]);

                mChoose = c;
                invalidate();
            }
        }
        this.mChoose = c;
    }

    /**
     * 向外公开的方法
     *
     * @param onTouchingLetterChangedListener
     */
    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    /**
     * 接口
     *
     * @author coder
     *
     */
    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s, float y);
    }

}

