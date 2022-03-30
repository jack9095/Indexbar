package com.fei.indexbar.copy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fei.indexbar.MyRecyclerView;
import com.fei.indexbar.R;
import com.fei.indexbar.adapter.IndexBarAdapter;
import com.fei.indexbar.model.IndexBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IndexBarCopy extends FrameLayout {

    private MyRecyclerView mRecyclerView;
    private IndexBarAdapter mIndexBarAdapter;
    private List<IndexBean> mLetters = new ArrayList<>();

    public IndexBarCopy(@NonNull Context context) {
        this(context, null);
    }

    public IndexBarCopy(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexBarCopy(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public IndexBarCopy(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.index_layout, this, true);
        mRecyclerView = findViewById(R.id.recycler_view);
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
        List<String> strings = Arrays.asList(getResources().getStringArray(R.array.quickSideBarLetters));
        for (String string : strings) {
            mLetters.add(new IndexBean(string));
        }
        setData(mLetters);

//        mRecyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(mRecyclerView) {
//
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//            }
//
//            @Override
//            public void onItemClick(RecyclerView.ViewHolder vh) {
//                Log.e("fei.wang", "点击事件");
//            }
//
//            @Override
//            public void onItemLongClick(RecyclerView.ViewHolder vh) {
//
//            }
//        });
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
