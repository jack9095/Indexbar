package com.fei.indexbar;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fei.indexbar.adapter.IndexBarAdapter;
import com.fei.indexbar.model.IndexBean;
import com.fei.indexbar.util.SpellingUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IndexBar extends FrameLayout implements MyRecyclerView.OnTouchListener {

    private MyRecyclerView mMyRecyclerView;
    private IndexBarAdapter mIndexBarAdapter;
    private IndexBarTipsView indexBarTipsView;
    private OnTouchListener mOnTouchListener;
    private List<IndexBean> mLetters = new ArrayList<>();
    public boolean isTouch; // 是否在触摸 索引条 IndexBar

    public IndexBar(@NonNull Context context) {
        this(context, null);
    }

    public IndexBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public IndexBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.index_layout, this, true);
        indexBarTipsView = findViewById(R.id.tips_view);
        mMyRecyclerView = findViewById(R.id.recycler_view);
        mMyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mMyRecyclerView.setOnTouchListener(this);
        mIndexBarAdapter = new IndexBarAdapter();
        mMyRecyclerView.adapter(mIndexBarAdapter);
        List<String> strings = Arrays.asList(getResources().getStringArray(R.array.quickSideBarLetters));
        for (String string : strings) {
            mLetters.add(new IndexBean(string));
        }
        setData(mLetters);
    }

    public void setData(List<IndexBean> lists) {
        mLetters = lists;
        mIndexBarAdapter.setData(lists);
    }

    public void setCurrentIndex(int currentIndex) {
        // TODO 2022/3.29 这里要加上这个判断 否则 这几行代码绘制选中背景会错乱,但是滑动的时候要和列表联动起来就必须得使用下面的代码
        if (!isTouch) {
            if (!mIndexBarAdapter.getData().isEmpty()) {
                for (int i = 0; i < mIndexBarAdapter.getData().size(); i++) {
                    mIndexBarAdapter.getData().get(i).isSelect = i == currentIndex;
                }
            }
            mIndexBarAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 与列表关联参数的回掉
     * @param letter 选中的字母
     * @param position 选中字母的下标
     * @param y Y轴偏移的距离
     */
    @Override
    public void onChanged(String letter, int position, float y) {
        if (indexBarTipsView != null) {
            indexBarTipsView.setText(letter, position, y);
        }

        if (mOnTouchListener != null) {
            mOnTouchListener.onChanged(letter, position, y);
        }
    }

    @Override
    public void onTouching(boolean touching) {
        isTouch = touching;
        if (indexBarTipsView != null) {
            //可以自己加入动画效果渐显渐隐
            indexBarTipsView.setVisibility(touching ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void setOnTouchListener(OnTouchListener listener) {
        this.mOnTouchListener = listener;
    }

    public interface OnTouchListener {
        void onChanged(String letter, int position, float y);

        void onTouching(boolean touching);
    }
}
