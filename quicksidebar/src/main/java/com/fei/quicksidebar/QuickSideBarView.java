package com.fei.quicksidebar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.fei.quicksidebar.listener.OnQuickSideBarTouchListener;

import java.util.Arrays;
import java.util.List;

/**
 * 快速选择侧边栏
 * Letter 字母的意思
 */
public class QuickSideBarView extends View {

    private OnQuickSideBarTouchListener listener;
    private List<String> mLetters;
    private int mChoose = -1;
    private final Paint mPaint = new Paint();
    private float mTextSize; // 默认文字大小
    private float mTextSizeChoose; // 选中文字大小
    private int mTextColor;  // 默认文字颜色
    private int mTextColorChoose;  // 选中文字颜色
    private int mWidth;  // SideBarVie 控件的宽度
    private float mItemHeight; // 每一个字母的高度
    private float mItemStartY; // 第一个字母起始位置的 Y 坐标
    private int mPadding; // 顶部和底部内边距

    public QuickSideBarView(Context context) {
        this(context, null);
    }

    public QuickSideBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickSideBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mLetters = Arrays.asList(context.getResources().getStringArray(R.array.quickSideBarLetters));

        mTextColor = context.getResources().getColor(android.R.color.black);
        mTextColorChoose = context.getResources().getColor(android.R.color.black);
        mTextSize = context.getResources().getDimensionPixelSize(R.dimen.textSize_quicksidebar);
        mTextSizeChoose = context.getResources().getDimensionPixelSize(R.dimen.textSize_quicksidebar_choose);
        mItemHeight = context.getResources().getDimension(R.dimen.height_quicksidebaritem);
        // 这个值至关重要
        mPadding = context.getResources().getDimensionPixelSize(R.dimen.height_quicksidebaritem);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.QuickSideBarView);
            mTextColor = a.getColor(R.styleable.QuickSideBarView_sidebarTextColor, mTextColor);
            mTextColorChoose = a.getColor(R.styleable.QuickSideBarView_sidebarTextColorChoose, mTextColorChoose);
            mTextSize = a.getDimension(R.styleable.QuickSideBarView_sidebarTextSize, mTextSize);
            mTextSizeChoose = a.getDimension(R.styleable.QuickSideBarView_sidebarTextSizeChoose, mTextSizeChoose);
            mItemHeight = a.getDimension(R.styleable.QuickSideBarView_sidebarItemHeight, mItemHeight);
            a.recycle();
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        if (mLetters != null && mLetters.size() > 0) {
            int itemHeight = (mHeight - mPadding) / mLetters.size();
            if (itemHeight < (int) mItemHeight) {

//                mItemHeight = (float) itemHeight; // 在固定高度缩小范围内展示的 itemHeight
            }
            mItemStartY = (mHeight - mLetters.size() * mItemHeight) / 2;
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mLetters.size(); i++) {
            mPaint.setColor(mTextColor);

            mPaint.setAntiAlias(true);
            mPaint.setTextSize(mTextSize);
            if (i == mChoose) {
                mPaint.setColor(mTextColorChoose);
                mPaint.setFakeBoldText(true); //true为粗体，false为非粗体
                mPaint.setTypeface(Typeface.DEFAULT_BOLD); // 设置为粗体
                mPaint.setTextSize(mTextSizeChoose);
            }

            //计算位置
            @SuppressLint("DrawAllocation") Rect rect = new Rect();
            // 由调用者返回在边界(分配)的最小矩形包含所有的字符,以隐含原点(0,0)
            mPaint.getTextBounds(mLetters.get(i), 0, mLetters.get(i).length(), rect);

            float xPos = (float) ((mWidth - rect.width()) * 0.5);
            float yPos = (float) (mItemHeight * i + ((mItemHeight - rect.height()) * 0.5) + mItemStartY);

            canvas.drawText(mLetters.get(i), xPos, yPos, mPaint);
            mPaint.reset();
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = mChoose;
        final int newChoose = (int) ((y - mItemStartY) / mItemHeight);
        if (action == MotionEvent.ACTION_UP) {
            mChoose = -1;
            if (listener != null) {
                listener.onLetterTouching(false);
            }
            invalidate();
        } else {
            if (oldChoose != newChoose) {
                if (newChoose >= 0 && newChoose < mLetters.size()) {
                    mChoose = newChoose;
                    if (listener != null) {
                        //计算位置
                        Rect rect = new Rect();
                        mPaint.getTextBounds(mLetters.get(mChoose), 0, mLetters.get(mChoose).length(), rect);
                        float yPos = mItemHeight * mChoose + (int) ((mItemHeight - rect.height()) * 0.5) + mItemStartY;
                        listener.onLetterChanged(mLetters.get(newChoose), mChoose, yPos);
                    }
                }
                invalidate();
            }
            //如果是cancel也要调用onLetterUpListener 通知
            if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                if (listener != null) {
                    listener.onLetterTouching(false);
                }
            } else if (event.getAction() == MotionEvent.ACTION_DOWN) {//按下调用 onLetterDownListener
                if (listener != null) {
                    listener.onLetterTouching(true);
                }
            }
        }
        return true;
    }

    public void setOnQuickSideBarTouchListener(OnQuickSideBarTouchListener listener) {
        this.listener = listener;
    }

    /**
     * 设置字母表
     *
     * @param letters 字母集合
     */
    public void setLetters(List<String> letters) {

        int measuredHeight = getMeasuredHeight();
        if (letters != null && letters.size() > 0) {
            int itemHeight = (measuredHeight - mPadding) / letters.size();
            if (itemHeight < (int) mItemHeight) {

//                mItemHeight = (float) itemHeight; // 在固定高度缩小范围内展示的 itemHeight
            }
        }
//        requestLayout();

        this.mLetters = letters;
        invalidate();
    }
}

