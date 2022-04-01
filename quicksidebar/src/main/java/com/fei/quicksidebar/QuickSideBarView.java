package com.fei.quicksidebar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.fei.quicksidebar.listener.OnQuickSideBarTouchListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 快速选择侧边栏
 * Letter 字母的意思
 *
 * 缩进两个 17个字母 + 拓展的
 * 缩进三个 13个字母 + 拓展的
 * 缩进四个 11个字母 + 拓展的
 * 缩进五个 9个字母 + 拓展的
 * 缩进六个 7个字母 + 拓展的
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
        mTextColor = context.getResources().getColor(R.color.color_text);
        mTextColorChoose = context.getResources().getColor(R.color.color_text_choose);
        mTextSize = context.getResources().getDimensionPixelSize(R.dimen.textSize_quicksidebar);
        mTextSizeChoose = context.getResources().getDimensionPixelSize(R.dimen.textSize_quicksidebar_choose);
        mItemHeight = context.getResources().getDimension(R.dimen.height_quicksidebaritem);
        // 这个值至关重要
        mPadding = context.getResources().getDimensionPixelSize(R.dimen.height_quicksidebaritem);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        Log.e("fei.wang","onMeasure  -》 measuredHeight -> " + mHeight);
        if (mLetters != null && mLetters.size() > 0) {
            int itemHeight = (mHeight - mPadding) / mLetters.size();
            if (itemHeight < (int) mItemHeight) {

//                mItemHeight = (float) itemHeight; // 在固定高度缩小范围内展示的 itemHeight
            }
            mItemStartY = (mHeight - mLetters.size() * mItemHeight) / 2;
            Log.e("fei.wang","onMeasure  -》 mItemStartY -> " + mItemStartY);
        }
    }

    /**
     * 获取 IndexBar 的实际高度
     * @param size
     * @return
     */
    private int getIndexBarHeight(int size) {
        return size*UDisplayUtil.dp2Px(getContext(),16) + UDisplayUtil.dp2Px(getContext(),16);
    }

    /**
     * 设置字母表
     *
     * @param letters 字母集合
     */
    public void setLetters(List<String> letters) {
        if (letters == null) {
            return;
        }

        post(new Runnable() {
            @Override
            public void run() {
                int measuredHeight = getMeasuredHeight();
                Log.e("fei.wang","measuredHeight -> " + measuredHeight);
                List<String> strings = filterString(letters);
                if (measuredHeight >= getIndexBarHeight(letters.size())) {
                    mLetters = letters;
                } else if (measuredHeight >= getIndexBarHeight(strings.size() + 17)) {
                    mLetters = Arrays.asList(getContext().getResources().getStringArray(R.array.zoomTwo));
                } else if (measuredHeight >= getIndexBarHeight(strings.size() + 13)) {
                    mLetters = Arrays.asList(getContext().getResources().getStringArray(R.array.zoomThree));
                } else if (measuredHeight >= getIndexBarHeight(strings.size() + 11)) {
                    mLetters = Arrays.asList(getContext().getResources().getStringArray(R.array.zoomFour));
                } else if (measuredHeight >= getIndexBarHeight(strings.size() + 9)) {
                    mLetters = Arrays.asList(getContext().getResources().getStringArray(R.array.zoomFive));
                } else if (measuredHeight >= getIndexBarHeight(strings.size() + 7)) {
                    mLetters = Arrays.asList(getContext().getResources().getStringArray(R.array.zoomSix));
                }

                mItemStartY = (measuredHeight - mLetters.size() * mItemHeight) / 2;
                invalidate();
            }
        });
    }

    // 字母背景画笔
    private Paint mLettersPaint = new Paint();
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mLetters.size(); i++) {



            mPaint.setColor(mTextColor);
            mPaint.setAntiAlias(true);
            mPaint.setTextSize(mTextSize);
            if (i == mChoose) {
                mPaint.setColor(mTextColorChoose);
                mPaint.setTextSize(mTextSizeChoose);
            }

            //计算位置
            @SuppressLint("DrawAllocation") Rect rect = new Rect();
            // 由调用者返回在边界(分配)的最小矩形包含所有的字符,以隐含原点(0,0)
            mPaint.getTextBounds(mLetters.get(i), 0, mLetters.get(i).length(), rect);
            float xPos = (float) ((mWidth - rect.width()) * 0.5);
            float yPos = (float) (mItemHeight * i + ((mItemHeight - rect.height()) * 0.5) + mItemStartY);

            if (i == mChoose) {

                RectF rectF = new RectF();
                rectF.left = xPos - mTextSize/2;
                rectF.right = xPos + mTextSize/2;
                rectF.top = mTextSize / 2;
                rectF.bottom = mTextSize / 2;

                // 绘制背景 start
                mLettersPaint.reset();
                mLettersPaint.setStyle(Paint.Style.FILL);
                mLettersPaint.setColor(ContextCompat.getColor(getContext(),R.color.color_background_choose));
                mLettersPaint.setAntiAlias(true);
//                mPaint.setColor(ContextCompat.getColor(getContext(),R.color.color_background_choose));
                canvas.drawCircle(xPos, yPos-mTextSize/3, (float) (mTextSize * 0.6), mLettersPaint);
            }

            canvas.drawText(mLetters.get(i), xPos, yPos, mPaint);

//            canvas.drawRoundRect(rectF, xPos, yPos, mPaint);
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
//            mChoose = -1;
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
     * 正则去掉数组字母
     */
    private List<String> filterLetter(List<String> array) {
        List<String> lists = new ArrayList<>();
        Pattern p = Pattern.compile("[a-zA-z]");
        for (String s : array) {
            //如果包含英文字母我这边不做处理,如果有需求,可以自己添加
            if (p.matcher(s).find()) {
                lists.add(s);
            }
        }
        return lists;
    }

    /**
     * 正则过滤出数组字母
     */
    private List<String> filterString(List<String> array) {
        List<String> lists = new ArrayList<>();
        Pattern p = Pattern.compile("[a-zA-z]");
        for (String s : array) {
            //如果包含英文字母我这边不做处理,如果有需求,可以自己添加
            if (!p.matcher(s).find()) {
                lists.add(s);
            }
        }
        return lists;
    }
}

