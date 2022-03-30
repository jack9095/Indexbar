package com.fei.indexbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fei.indexbar.adapter.IndexBarAdapter;
import com.fei.indexbar.model.IndexBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IndexBar extends FrameLayout {

    private MyRecyclerView mRecyclerView;
    private IndexBarAdapter mIndexBarAdapter;
    private List<IndexBean> mLetters = new ArrayList<>();

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
        List<String> strings = Arrays.asList(getResources().getStringArray(R.array.quickSideBarLetters));
        for (String string : strings) {
            mLetters.add(new IndexBean(string));
        }

        LayoutInflater.from(getContext()).inflate(R.layout.index_layout, this, true);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setData(strings);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mIndexBarAdapter = new IndexBarAdapter();
        mIndexBarAdapter.setOnItemClickListener(new IndexBarAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, String str, int position) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(view, str, position);
                }
            }
        });
        mRecyclerView.adapter(mIndexBarAdapter);

        setData(mLetters);
    }

    public void setData(List<IndexBean> lists) {
        mIndexBarAdapter.setData(lists);
    }

    public interface OnItemClickListener {
        void onClick(View view, String str, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnTouchListener(MyRecyclerView.OnTouchListener listener) {
        mRecyclerView.setOnTouchListener(listener);
    }
}
