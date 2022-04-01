package com.fei.indexbar;

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
import java.util.Objects;

/**
 * 目前功能性问题，字母最上方的热区没实现，还有就是 合并缩略功能的实现，和 ListView 的结合使用
 */
public class IndexBar extends RelativeLayout implements MyRecyclerView.OnTouchListener, IndexBarTipsView.OnTouchListener {
    private final static int INDEX_BAR_MIN_HEIGHT = 144;

    private final static int TIPS_DISMISS_TIME = 2000;

    private final static int INDEX_BAR_ONE = 1;

    private final static int INDEX_BAR_THREE = 3;

    private final static int INDEX_BAR_FIVE = 5;

    private final static int INDEX_BAR_SEVEN = 7;

    private final static int INDEX_BAR_NINE = 9;

    private final static int INDEX_BAR_ELEVEN = 11;

    private final static int INDEX_BAR_THIRTEEN = 13;

    private final static int INDEX_BAR_FIFTEEN = 15;

    private final static int INDEX_BAR_SEVENTEEN = 17;

    private final static int ZOOM_TYPE_ONE = 0x00;

    private final static int ZOOM_TYPE_TWO = 0x01;

    private final static int ZOOM_TYPE_THREE = 0x02;

    private final static int ZOOM_TYPE_FOUR = 0x03;

    private final static int ZOOM_TYPE_FIVE = 0x04;

    private final static int SUBSTRING_END_INDEX = 1;

    private final static int ITEM_HEIGHT = 16;

    private final static int ITEM_MARGIN_TOP = 8;

    private final static int ITEM_MARGIN_BOTTOM = 8;

    private RecyclerView mRecyclerView;

    private MyRecyclerView mMyRecyclerView;

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
        isInit = true;
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.u_index_bar_layout, this, true);
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
                String firstLetter = SpellingUtils.getFirstLetter(realData.get(i).substring(0, SUBSTRING_END_INDEX));
                if (TextUtils.equals(strings.get(j), firstLetter)) {
                    temps.add(new IndexBean(firstLetter, realData.get(i), realData.get(i).substring(0, SUBSTRING_END_INDEX)));
                }
            }
            mMap.put(strings.get(j), temps);
        }

        syncData(lists, null, null);
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
                String firstLetter = SpellingUtils.getFirstLetter(realData.get(i).substring(0, SUBSTRING_END_INDEX));
                if (TextUtils.equals(strings.get(j), firstLetter)) {
                    temps.add(new IndexBean(firstLetter, realData.get(i), realData.get(i).substring(0, SUBSTRING_END_INDEX)));
                }
            }
            mMap.put(strings.get(j), temps);
        }

        Log.e("fei.wang", "-- lists--> " + lists.size());
        syncData(lists, headLetter, null);
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
                String firstLetter = SpellingUtils.getFirstLetter(realData.get(i).substring(0, SUBSTRING_END_INDEX));
                if (TextUtils.equals(strings.get(j), firstLetter)) {
                    temps.add(new IndexBean(firstLetter, realData.get(i), realData.get(i).substring(0, SUBSTRING_END_INDEX)));
                }
            }
            mMap.put(strings.get(j), temps);
        }

        syncData(lists, headLetter, tailLetter);
    }

    /**
     * 设置字母表
     *
     * @param strs 字母集合
     */
    private void syncData(List<String> strs, List<String> headLetter, List<String> tailLetter) {

        post(new Runnable() {
            @Override
            public void run() {
                int tempType = Integer.MAX_VALUE; // 使用的那个省略规则
                int finalSize = 0;
                if (headLetter != null) {
                    finalSize += headLetter.size();
                }

                if (tailLetter != null) {
                    finalSize += tailLetter.size();
                }
                setVisibility(VISIBLE);
                List<String> strings = Arrays.asList(getResources().getStringArray(R.array.quickSideBarLetters));
                List<String> letters = new ArrayList<>();
                List<String> tempLetter = null;
                int measuredHeight = getHeight();
                if (measuredHeight < UDisplayUtil.dp2Px(getContext(), INDEX_BAR_MIN_HEIGHT)) {
                    setVisibility(GONE);
                } else if (measuredHeight >= getIndexBarHeight(strings.size() + finalSize)) {
                    tempLetter = strings;
                } else if (measuredHeight >= getIndexBarHeight(finalSize + INDEX_BAR_SEVENTEEN)) {
                    tempType = ZOOM_TYPE_ONE;
                    tempLetter = Arrays.asList(getContext().getResources().getStringArray(R.array.zoomTwo));
                } else if (measuredHeight >= getIndexBarHeight(finalSize + INDEX_BAR_THIRTEEN)) {
                    tempType = ZOOM_TYPE_TWO;
                    tempLetter = Arrays.asList(getContext().getResources().getStringArray(R.array.zoomThree));
                } else if (measuredHeight >= getIndexBarHeight(finalSize + INDEX_BAR_ELEVEN)) {
                    tempType = ZOOM_TYPE_THREE;
                    tempLetter = Arrays.asList(getContext().getResources().getStringArray(R.array.zoomFour));
                } else if (measuredHeight >= getIndexBarHeight(finalSize + INDEX_BAR_NINE)) {
                    tempType = ZOOM_TYPE_FOUR;
                    tempLetter = Arrays.asList(getContext().getResources().getStringArray(R.array.zoomFive));
                } else if (measuredHeight >= getIndexBarHeight(finalSize + INDEX_BAR_SEVEN)) {
                    tempType = ZOOM_TYPE_FIVE;
                    tempLetter = Arrays.asList(getContext().getResources().getStringArray(R.array.zoomSix));
                }

                int headSize = 0;
                if (headLetter != null) {
                    headSize = headLetter.size();
                    letters.addAll(headLetter);
                }
                if (tempLetter != null) {
                    letters.addAll(tempLetter);
                }
                if (tailLetter != null) {
                    letters.addAll(tailLetter);
                }

                mLetters.clear();
//                if (headLetter != null) {
//                    for (String string : headLetter) {
//                        mLetters.add(new IndexBean(string));
//                    }
//                }
                IndexBean indexBean;
                for (int i = 0; i < letters.size(); i++) {

                    indexBean = new IndexBean(letters.get(i));
                    if (tempType == ZOOM_TYPE_ONE) {
                        if (i - headSize == INDEX_BAR_ONE) {
                            indexBean.setLists(Arrays.asList("B", "C"));
                        } else if (i - headSize == INDEX_BAR_THREE) {
                            indexBean.setLists(Arrays.asList("E", "F"));
                        } else if (i - headSize == INDEX_BAR_FIVE) {
                            indexBean.setLists(Arrays.asList("H", "I"));
                        } else if (i - headSize == INDEX_BAR_SEVEN) {
                            indexBean.setLists(Arrays.asList("K", "L"));
                        } else if (i - headSize == INDEX_BAR_NINE) {
                            indexBean.setLists(Arrays.asList("N", "O"));
                        } else if (i - headSize == INDEX_BAR_ELEVEN) {
                            indexBean.setLists(Arrays.asList("Q", "R"));
                        } else if (i - headSize == INDEX_BAR_THIRTEEN) {
                            indexBean.setLists(Arrays.asList("T", "U"));
                        } else if (i - headSize == INDEX_BAR_FIFTEEN) {
                            indexBean.setLists(Arrays.asList("W", "X", "Y"));
                        }
                    } else if (tempType == ZOOM_TYPE_TWO) {
                        if (i - headSize == INDEX_BAR_ONE) {
                            indexBean.setLists(Arrays.asList("B", "C", "D"));
                        } else if (i - headSize == INDEX_BAR_THREE) {
                            indexBean.setLists(Arrays.asList("F", "G", "H"));
                        } else if (i - headSize == INDEX_BAR_FIVE) {
                            indexBean.setLists(Arrays.asList("J", "K", "L"));
                        } else if (i - headSize == INDEX_BAR_SEVEN) {
                            indexBean.setLists(Arrays.asList("N", "O", "P"));
                        } else if (i - headSize == INDEX_BAR_NINE) {
                            indexBean.setLists(Arrays.asList("R", "S", "T"));
                        } else if (i - headSize == INDEX_BAR_ELEVEN) {
                            indexBean.setLists(Arrays.asList("V", "W", "X", "Y"));
                        }
                    } else if (tempType == ZOOM_TYPE_THREE) {
                        if (i - headSize == INDEX_BAR_ONE) {
                            indexBean.setLists(Arrays.asList("B", "C", "D", "E"));
                        } else if (i - headSize == INDEX_BAR_THREE) {
                            indexBean.setLists(Arrays.asList("G", "H", "I", "J"));
                        } else if (i - headSize == INDEX_BAR_FIVE) {
                            indexBean.setLists(Arrays.asList("L", "M", "N", "O"));
                        } else if (i - headSize == INDEX_BAR_SEVEN) {
                            indexBean.setLists(Arrays.asList("Q", "R", "S", "T"));
                        } else if (i - headSize == INDEX_BAR_NINE) {
                            indexBean.setLists(Arrays.asList("V", "W", "X", "Y"));
                        }
                    } else if (tempType == ZOOM_TYPE_FOUR) {
                        if (i - headSize == INDEX_BAR_ONE) {
                            indexBean.setLists(Arrays.asList("B", "C", "D", "E", "F"));
                        } else if (i - headSize == INDEX_BAR_THREE) {
                            indexBean.setLists(Arrays.asList("H", "I", "J", "K", "L"));
                        } else if (i - headSize == INDEX_BAR_FIVE) {
                            indexBean.setLists(Arrays.asList("N", "O", "P", "Q", "R"));
                        } else if (i - headSize == INDEX_BAR_SEVEN) {
                            indexBean.setLists(Arrays.asList("T", "U", "V", "W", "X", "Y"));
                        }
                    } else if (tempType == ZOOM_TYPE_FIVE) {
                        if (i - headSize == INDEX_BAR_ONE) {
                            indexBean.setLists(Arrays.asList("B", "C", "D", "E", "F", "G"));
                        } else if (i - headSize == INDEX_BAR_THREE) {
                            indexBean.setLists(Arrays.asList("I", "J", "K", "L", "M", "N"));
                        } else if (i - headSize == INDEX_BAR_FIVE) {
                            indexBean.setLists(Arrays.asList("P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y"));
                        }
                    }
                    mLetters.add(indexBean);
                }

//                if (tailLetter != null) {
//                    for (String string : tailLetter) {
//                        mLetters.add(new IndexBean(string));
//                    }
//                }
                mIndexBarAdapter.setData(mLetters);
            }
        });
    }

    /**
     * 获取 IndexBar 的实际高度
     *
     * @param size
     * @return
     */
    private int getIndexBarHeight(int size) {
        return size * UDisplayUtil.dp2Px(getContext(), ITEM_HEIGHT) + UDisplayUtil.dp2Px(getContext(), ITEM_MARGIN_TOP + ITEM_MARGIN_BOTTOM);
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
            if (TextUtils.equals("·", bean.getLetter())) {
                if (bean.getLists() != null && bean.getLists().size() > 0) {
                    mIndexBarTipsView.setText(bean.getLists().get(0), position, y);
                    List<IndexBean> tips = new ArrayList<>();
                    for (int i = 0; i < bean.getLists().size(); i++) {
                        String s = bean.getLists().get(i);
                        if (!TextUtils.isEmpty(s) && mMap.get(s) != null) {
                            tips.addAll(Objects.requireNonNull(mMap.get(s)));
                        }
                    }
                    mIndexBarTipsView.setData(tips);
                }
            } else {
                mIndexBarTipsView.setText(bean.getLetter(), position, y);
                mIndexBarTipsView.setData(mMap.get(bean.getLetter()));
            }
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
                String key = SpellingUtils.getFirstLetter(realData.get(i).substring(0, SUBSTRING_END_INDEX));
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
                mHandler.postDelayed(mRunnable, TIPS_DISMISS_TIME);
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
                String key = SpellingUtils.getFirstLetter(realData.get(position).substring(0, SUBSTRING_END_INDEX));
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
