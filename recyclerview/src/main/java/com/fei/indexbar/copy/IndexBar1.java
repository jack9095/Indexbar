package com.fei.indexbar.copy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fei.indexbar.IndexBarTipsView;
import com.fei.indexbar.MyRecyclerView;
import com.fei.indexbar.R;
import com.fei.indexbar.adapter.IndexBarAdapter;
import com.fei.indexbar.model.IndexBean;
import com.fei.indexbar.util.SpellingUtils;
import com.fei.indexbar.util.UDisplayUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 目前功能性问题，字母最上方的热区没实现，还有就是 合并缩略功能的实现，和 ListView 的结合使用
 */
public class IndexBar1 extends RelativeLayout implements MyRecyclerView1.OnTouchListener, IndexBarTipsView.OnTouchListener {

    private RecyclerView mRecyclerView;
    private MyRecyclerView1 mMyRecyclerView;
    private IndexBarTipsView mIndexBarTipsView;
    private IndexBarAdapter mIndexBarAdapter;
    private OnTouchListener mOnTouchListener;
    private List<IndexBean> mLetters = new ArrayList<>();
    private Map<String, List<IndexBean>> mMap = new HashMap<>();
    private boolean mIsTouch; // 是否在触摸 索引条 IndexBar
    private static final Handler mHandler = new Handler(Looper.getMainLooper());
    private List<String> realData;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean isInit; // 是否是第一次近来初始化

    public IndexBar1(@NonNull Context context) {
        this(context, null);
    }

    public IndexBar1(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexBar1(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public IndexBar1(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        isInit = true;
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.u_index_bar_layout_1, this, true);
        mIndexBarTipsView = findViewById(R.id.tips_view);
        mIndexBarTipsView.setOnTouchListener(this);
        mMyRecyclerView = findViewById(R.id.recycler_view);
        mMyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mMyRecyclerView.setOnTouchListener(this);
        mIndexBarAdapter = new IndexBarAdapter();
        mMyRecyclerView.adapter(mIndexBarAdapter);
        List<String> strings = Arrays.asList(getResources().getStringArray(R.array.quickSideBarLetters));
        for (String string : strings) {
            mLetters.add(new IndexBean(string));
        }
        mIndexBarAdapter.setData(mLetters);
    }

    /**
     * 标题的数据集合，这个方法会把 标题数据集合的首字母提取出来，归类，排序 a,b,c...z
     *
     * @param lists 标题的数据集合
     */
    public void setData(List<String> lists) {
        if (lists == null) {
            return;
        }
        realData = SpellingUtils.stringSort(lists);
        List<String> strings = Arrays.asList(getResources().getStringArray(R.array.quickSideBarLetters));
        if (strings == null) {
            return;
        }
        for (int j = 0; j < strings.size(); j++) {
            List<IndexBean> temps = new LinkedList<>();
            for (int i = 0; i < realData.size(); i++) {
                String firstLetter = SpellingUtils.getFirstLetter(realData.get(i).substring(0, 1));
                if (TextUtils.equals(strings.get(j), firstLetter)) {
                    temps.add(new IndexBean(firstLetter, realData.get(i), realData.get(i).substring(0, 1)));
                }
            }
            mMap.put(strings.get(j), temps);
        }

        mLetters.clear();
        for (String string : strings) {
            mLetters.add(new IndexBean(string));
        }
        mIndexBarAdapter.setData(mLetters);
    }

    /**
     * 标题的数据集合，这个方法会把 标题数据集合的首字母提取出来，归类，排序 a,b,c...z
     *
     * @param lists      标题的数据集合
     * @param headLetter 放到队列头部特殊符号集合
     */
    public void setData(List<String> lists, List<String> headLetter) {
        if (lists == null) {
            return;
        }
        realData = SpellingUtils.stringSort(lists);
        List<String> strings = new ArrayList<>(headLetter);
        strings.addAll(Arrays.asList(getResources().getStringArray(R.array.quickSideBarLetters)));

        for (int j = 0; j < strings.size(); j++) {
            List<IndexBean> temps = new LinkedList<>();
            for (int i = 0; i < realData.size(); i++) {
                String firstLetter = SpellingUtils.getFirstLetter(realData.get(i).substring(0, 1));
                if (TextUtils.equals(strings.get(j), firstLetter)) {
                    temps.add(new IndexBean(firstLetter, realData.get(i), realData.get(i).substring(0, 1)));
                }
            }
            mMap.put(strings.get(j), temps);
        }

//        mLetters.clear();
//        for (String string : strings) {
//            mLetters.add(new IndexBean(string));
//        }
//        mIndexBarAdapter.setData(mLetters);
        Log.e("fei.wang", "-- lists--> " + lists.size());
        setLetters(lists, headLetter, null);
    }

    /**
     * 标题的数据集合，这个方法会把 标题数据集合的首字母提取出来，归类，排序 a,b,c...z
     *
     * @param lists      标题的数据集合
     * @param headLetter 放到队列头部特殊符号集合
     * @param tailLetter 放到队列尾部特殊符号集合
     */
    public void setData(List<String> lists, List<String> headLetter, List<String> tailLetter) {
        if (lists == null) {
            return;
        }
        realData = SpellingUtils.stringSort(lists);
        List<String> strings = new ArrayList<>(headLetter);
        strings.addAll(Arrays.asList(getResources().getStringArray(R.array.quickSideBarLetters)));
        strings.addAll(tailLetter);

        for (int j = 0; j < strings.size(); j++) {
            List<IndexBean> temps = new LinkedList<>();
            for (int i = 0; i < realData.size(); i++) {
                String firstLetter = SpellingUtils.getFirstLetter(realData.get(i).substring(0, 1));
                if (TextUtils.equals(strings.get(j), firstLetter)) {
                    temps.add(new IndexBean(firstLetter, realData.get(i), realData.get(i).substring(0, 1)));
                }
            }
            mMap.put(strings.get(j), temps);
        }

        mLetters.clear();
        for (String string : strings) {
            mLetters.add(new IndexBean(string));
        }
        mIndexBarAdapter.setData(mLetters);
    }

    /**
     * 设置字母表
     *
     * @param strs 字母集合
     */
    private void setLetters(List<String> strs, List<String> headLetter, List<String> tailLetter) {
//        if (strings == null) {
//            return;
//        }


        post(new Runnable() {
            @Override
            public void run() {
                int finalSize = 0;
                if (headLetter != null) {
                    finalSize += headLetter.size();
                }

                if (tailLetter != null) {
                    finalSize += tailLetter.size();
                }
                List<String> strings = Arrays.asList(getResources().getStringArray(R.array.quickSideBarLetters));
                Log.e("fei.wang", "size -> " + finalSize);
                Log.e("fei.wang", "strings.size() -> " + strings.size());
                List<String> letters = new ArrayList<>();
                List<String> tempLetter = null;
                int measuredHeight = getHeight();
                Log.e("fei.wang", "measuredHeight -> " + measuredHeight);
                Log.e("fei.wang", "one -> " + getIndexBarHeight(strings.size() + finalSize));
                Log.e("fei.wang", "two -> " + getIndexBarHeight(finalSize + 17));
                if (measuredHeight >= getIndexBarHeight(strings.size() + finalSize)) {
//                    letters.addAll(strings);
                    tempLetter = strings;
                } else if (measuredHeight >= getIndexBarHeight(finalSize + 17)) {
                    tempLetter = Arrays.asList(getContext().getResources().getStringArray(R.array.zoomTwo));
//                    letters.addAll(Arrays.asList(getContext().getResources().getStringArray(R.array.zoomTwo)));
                } else if (measuredHeight >= getIndexBarHeight(finalSize + 13)) {
                    tempLetter = Arrays.asList(getContext().getResources().getStringArray(R.array.zoomThree));
//                    letters.addAll(Arrays.asList(getContext().getResources().getStringArray(R.array.zoomThree)));
                } else if (measuredHeight >= getIndexBarHeight(finalSize + 11)) {
                    tempLetter = Arrays.asList(getContext().getResources().getStringArray(R.array.zoomFour));
//                    letters.addAll(Arrays.asList(getContext().getResources().getStringArray(R.array.zoomFour)));
                } else if (measuredHeight >= getIndexBarHeight(finalSize + 9)) {
                    tempLetter = Arrays.asList(getContext().getResources().getStringArray(R.array.zoomFive));
//                    letters.addAll(Arrays.asList(getContext().getResources().getStringArray(R.array.zoomFive)));
                } else if (measuredHeight >= getIndexBarHeight(finalSize + 7)) {
                    tempLetter = Arrays.asList(getContext().getResources().getStringArray(R.array.zoomSix));
//                    letters.addAll(Arrays.asList(getContext().getResources().getStringArray(R.array.zoomSix)));
                }

                if (headLetter != null) {
                    letters.addAll(headLetter);
                }
                if (tempLetter != null) {
                    letters.addAll(tempLetter);
                }
                if (tailLetter != null) {
                    letters.addAll(tailLetter);
                }

                mLetters.clear();
                for (String string : letters) {
                    mLetters.add(new IndexBean(string));
                }
                mIndexBarAdapter.setData(mLetters);
//                mItemStartY = (measuredHeight - mLetters.size() * mItemHeight) / 2;
//                invalidate();
            }
        });
    }


    private void setFilterData() {

    }

    /**
     * 获取 IndexBar 的实际高度
     *
     * @param size
     * @return
     */
    private int getIndexBarHeight(int size) {
        Log.e("fei.wang", "size 16dp -> " + UDisplayUtil.dp2Px(getContext(), 16));
        Log.e("fei.wang", "16dp -> " + UDisplayUtil.dp2Px(getContext(), 16));
        return size * UDisplayUtil.dp2Px(getContext(), 16) + UDisplayUtil.dp2Px(getContext(), 16);
    }

    /**
     * 与列表关联参数的回掉
     *
     * @param bean     数据模型
     * @param position 选中字母的下标
     * @param y        Y轴偏移的距离
     */
    @Override
    public void onChanged(IndexBean bean, int position, float y) {
        if (mIndexBarTipsView != null) {
            mIndexBarTipsView.setText(bean.getLetter(), position, y);
            mIndexBarTipsView.setData(mMap.get(bean.getLetter()));
        }
        if (mOnTouchListener != null) {
            mOnTouchListener.onChanged(bean, position, y, false);
        } else {
            setLocation(bean, false);
        }
    }

    @Override
    public void onDispatchTouch(boolean touching) {
        setTipsViewHide(touching);
    }

    private void setLocation(IndexBean bean, boolean secondaryIndex) {
        for (int i = 0; i < realData.size(); i++) {
            if (secondaryIndex) {
                if (TextUtils.equals(realData.get(i), bean.getName())) {
                    mLinearLayoutManager.scrollToPositionWithOffset(i, 0);
                    return;
                }
            } else {
                String key = SpellingUtils.getFirstLetter(realData.get(i).substring(0, 1));
                if (("#".equals(bean.getLetter()) && key.startsWith("#")) || TextUtils.equals(key, bean.getLetter())) {
                    mLinearLayoutManager.scrollToPositionWithOffset(i, 0);
                    return;
                }
            }
        }
    }

    @Override
    public void onClick(View view, IndexBean bean, int position, boolean isLetter) {
        if (mOnTouchListener != null) {
            mOnTouchListener.onChanged(bean, position, 0, isLetter);
        } else {
            setLocation(bean, isLetter);
        }
    }

    @Override
    public void onTouching(boolean touching) {
        isInit = false;
        mIsTouch = touching;
        setTipsViewHide(touching);
    }

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mIndexBarTipsView != null) {
                mIndexBarTipsView.setVisibility(View.GONE);
            }
        }
    };

    private void setTipsViewHide(boolean touching) {
        if (mIndexBarTipsView != null) {
            // 可以自己加入动画效果渐显渐隐
            if (touching) {
                mIndexBarTipsView.setVisibility(View.VISIBLE);
                mHandler.removeCallbacks(mRunnable);
            } else {
                mHandler.postDelayed(mRunnable, 2000);
            }
        }
    }

    public void setOnTouchListener(OnTouchListener listener) {
        this.mOnTouchListener = listener;
    }

    public interface OnTouchListener {
        void onChanged(IndexBean bean, int position, float y, boolean secondaryIndex);

        void onTouching(boolean touching);
    }

    public void release() {
        mHandler.removeCallbacksAndMessages(null);
    }

    public void setCurrentIndex(String key) {
        // TODO 2022/3.29 这里要加上这个判断 否则 这几行代码绘制选中背景会错乱,但是滑动的时候要和列表联动起来就必须得使用下面的代码
        if (!mIsTouch && !isInit) {
            if (!mIndexBarAdapter.getData().isEmpty()) {
                for (int i = 0; i < mIndexBarAdapter.getData().size(); i++) {
                    mIndexBarAdapter.getData().get(i).setSelect(TextUtils.equals(mIndexBarAdapter.getData().get(i).getLetter(), key));
                }
            }
            mIndexBarAdapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setRecyclerView(RecyclerView recyclerView, LinearLayoutManager layoutManager) {
        this.mRecyclerView = recyclerView;
        this.mLinearLayoutManager = layoutManager;
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
                final int position = layoutManager.findFirstCompletelyVisibleItemPosition();
                String key = SpellingUtils.getFirstLetter(realData.get(position).substring(0, 1));
                setCurrentIndex(key);
            }
        });

        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideTipsView();
                isInit = false;
                return false;
            }
        });
    }

    public void hideTipsView() {
        mIndexBarTipsView.setVisibility(View.GONE);
    }

}
