package com.fei.indexbar.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.fei.indexbar.adapter.IndexBarAdapter;
import com.fei.indexbar.model.IndexBean;
import com.fei.indexbar.util.UDisplayUtil;

import java.util.List;

public class MyRecyclerView extends RecyclerView {
    private int mChoose = -1;
    private float mItemHeight; // 每一个字母的高度
    private float mItemStartY; // 第一个字母起始位置的 Y 坐标
    private IndexBarAdapter mIndexBarAdapter;
    private OnTouchListener mOnTouchListener;
    private List<String> mLetters;
    private float mCurrentItemY; // 当前 item 的 Y 轴偏移距离

    public MyRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public MyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mItemHeight = dp2px(16);
        ((DefaultItemAnimator) getItemAnimator()).setSupportsChangeAnimations(false);
    }

    public void adapter(IndexBarAdapter indexBarAdapter) {
        mIndexBarAdapter = indexBarAdapter;
        setAdapter(indexBarAdapter);
    }

    public void setData(List<String> lists) {
        this.mLetters = lists;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mHeight = getMeasuredHeight();
//        Log.e("fei.wang", "onMeasure  -》 measuredHeight -> " + mHeight);
        if (mLetters != null && mLetters.size() > 0) {
            mItemStartY = (mHeight - mLetters.size() * mItemHeight) / 2;
            Log.e("fei.wang","onMeasure  -》 mItemStartY -> " + mItemStartY);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = mChoose;
//        Log.e("fei.wang", " dispatchTouchEvent  mItemStartY -> " + mItemStartY);
        // TODO 这里减去的 dp2px(16) 是因为，布局中距离顶部 paddingTop = 8dp, paddingBottom = 8dp
        final int newChoose = (int) ((y - mItemStartY - dp2px(16)) / mItemHeight);
                Log.e("fei.wang", "newChoose -> " + newChoose);
        if (action == MotionEvent.ACTION_UP) {
            if (mOnTouchListener != null) {
                mOnTouchListener.onTouching(false);
            }
//            Log.e("fei.wang", "mChoose ACTION_UP -> " + mChoose);
        } else {
            if (oldChoose != newChoose) {
                mCurrentItemY = y - mItemStartY;
                if (mIndexBarAdapter != null && newChoose >= 0 && newChoose < mIndexBarAdapter.getItemCount()) {
                    mChoose = newChoose;
                    if (mOnTouchListener != null) {
                        //计算位置
                        Rect rect = new Rect();
                        float yPos = mItemHeight * mChoose + (int) ((mItemHeight - rect.height()) * 0.5) + mItemStartY;
                        mOnTouchListener.onChanged(mIndexBarAdapter.getData().get(mChoose), mChoose, yPos);
                    }
                }
//                Log.e("fei.wang", "mChoose -> " + mChoose);
                if (mIndexBarAdapter != null && mIndexBarAdapter.getData() != null && mChoose >= 0 && mChoose < mIndexBarAdapter.getData().size()) {
                    List<IndexBean> data = mIndexBarAdapter.getData();
                    if (!data.isEmpty()) {
                        for (int i = 0; i < data.size(); i++) {
                            data.get(i).setSelect(i == mChoose);
                        }
                    }
                    mIndexBarAdapter.notifyDataSetChanged();
//                    Log.e("fei.wang", "mChoose letter -> " + mIndexBarAdapter.getData().get(mChoose).getLetter());
                }
            } else {
                if (mOnTouchListener != null) {
                    if (mChoose == 2) {
                        float v = y - mItemStartY - mCurrentItemY;
//                        Log.e("fei.wang", "item Y 轴距离 ->" + mCurrentItemY);
                        int moietyHeight = UDisplayUtil.dp2Px(getContext(), 16) / 10;
                        Log.e("fei.wang", "item内偏移距离 ->" + v + " item 10等份大小 -> " + moietyHeight);
                    }

                    mOnTouchListener.onMoving(mIndexBarAdapter.getData().get(mChoose),y - mItemStartY, mCurrentItemY, mChoose);
                }
            }
            //如果是cancel也要调用onLetterUpListener 通知
            if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                if (mOnTouchListener != null) {
                    mOnTouchListener.onTouching(false);
                }
            } else if (event.getAction() == MotionEvent.ACTION_DOWN) { //按下调用 OnTouchListener
                if (mOnTouchListener != null) {
                    mOnTouchListener.onTouching(true);
//                    Log.e("fei.wang", "mChoose ACTION_DOWN -> " + mChoose);
                }
                if (mOnTouchListener != null) {
                    //计算位置
                    Rect rect = new Rect();
                    float yPos = mItemHeight * mChoose + (int) ((mItemHeight - rect.height()) * 0.5) + mItemStartY;
                    mOnTouchListener.onChanged(mIndexBarAdapter.getData().get(newChoose), mChoose, yPos);
                }
                if (mIndexBarAdapter != null && mIndexBarAdapter.getData() != null && mChoose >= 0 && mChoose < mIndexBarAdapter.getData().size()) {
                    List<IndexBean> data = mIndexBarAdapter.getData();
                    if (!data.isEmpty()) {
                        for (int i = 0; i < data.size(); i++) {
                            data.get(i).setSelect(i == mChoose);
                        }
                    }
                    mIndexBarAdapter.notifyDataSetChanged();
//                    Log.e("fei.wang", "mChoose letter -> " + mIndexBarAdapter.getData().get(mChoose).getLetter());
                }
            }
        }
        return true;
    }

    private float dp2px(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
    }

    private float sp2px(int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getContext().getResources().getDisplayMetrics());
    }

    public void setOnTouchListener(OnTouchListener listener) {
        this.mOnTouchListener = listener;
    }

    public interface OnTouchListener {

        /**
         * 滑动到 item 的回掉事件
         * @param bean item 对应的实体
         * @param position item 对应的下标
         * @param y 在 item 上的偏移距离
         */
        void onChanged(IndexBean bean, int position, float y);

        /**
         * 手指按下和抬起的回掉
         * @param touching true 手指在屏幕上，false 手指离开屏幕
         */
        void onTouching(boolean touching);

        /**
         * 滑动到省略的点二级索引展示的列表
         * @param bean item 对应的实体
         * @param y 在 item 上的偏移距离
         * @param index item 对应的下标
         * @param currentItemY 当前 item 的 Y 轴偏移距离
         */
        void onMoving(IndexBean bean,float y, float currentItemY, int index);
    }

}
