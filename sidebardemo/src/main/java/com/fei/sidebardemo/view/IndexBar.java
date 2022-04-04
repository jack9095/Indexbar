package com.fei.sidebardemo.view;

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

import com.fei.sidebardemo.R;
import com.fei.sidebardemo.adapter.IndexBarAdapter;
import com.fei.sidebardemo.model.IndexBean;
import com.fei.sidebardemo.util.UDisplayUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 目前功能性问题，滑动的过程中把合并的效果也实现出来，和 ListView 的结合使用
 */
public class IndexBar extends RelativeLayout implements MyRecyclerView.OnTouchListener, IndexBarTipsView.OnTouchListener {
    private final static int INDEX_BAR_MIN_HEIGHT = 144;

    private final static int TIPS_DISMISS_TIME = 2000;

    private final static int INDEX_BAR_ONE_POSITION = 1;

    private final static int INDEX_BAR_TWO_POSITION = 2;

    private final static int INDEX_BAR_THREE_POSITION = 3;

    private final static int INDEX_BAR_FOUR_POSITION = 4;

    private final static int INDEX_BAR_FIVE_POSITION = 5;

    private final static int INDEX_BAR_SIX_POSITION = 6;

    private final static int INDEX_BAR_SEVEN_POSITION = 7;

    private final static int INDEX_BAR_EIGHT_POSITION = 8;

    private final static int INDEX_BAR_NINE_POSITION = 9;

    private final static int INDEX_BAR_TEN_POSITION = 10;

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

    private List<IndexBean> realData; // 列表对应的真实数据

    private LinearLayoutManager mLinearLayoutManager;

    private boolean isInit; // 是否是第一次近来初始化

    private int mTempType = Integer.MAX_VALUE; // 使用的那个省略规则

    private int headSize = 0; // 添加到头部的特殊字符数据集合大小

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
     * @param lists 真实的列表数据集合 排好顺序
     */
    public void setData(List<IndexBean> lists) {
        List<String> headLetter = new ArrayList<>();
        List<String> tailLetter = new ArrayList<>();
        boolean isHead = true; // 头部字符串
        boolean isTail = true; // 尾部字符串

        Pattern p = Pattern.compile("[a-zA-Z]");

//        !p.matcher(list.getLetter()).find() // 表示不包含字母

        for (IndexBean list : lists) {
            if (list.getLetter().equalsIgnoreCase("A")) {
                isHead = false;
            }
            if (isHead && !headLetter.contains(list.getLetter()) && !p.matcher(list.getLetter()).find()) {
                headLetter.add(list.getLetter());
            }

            if (!isTail && !tailLetter.contains(list.getLetter()) && !p.matcher(list.getLetter()).find()) {
                tailLetter.add(list.getLetter());
            }
            if (list.getLetter().equalsIgnoreCase("Z")) {
                isTail = false;
            }
        }

        setData(lists, headLetter, tailLetter);
    }

    /**
     * 标题的数据集合，这个方法会把 标题数据集合的首字母提取出来，归类，排序 a,b,c...z
     *
     * @param lists      标题的数据集合
     * @param headLetter 放到队列头部特殊符号集合
     */
    public void setData(List<IndexBean> lists, List<String> headLetter) {
        setData(lists, headLetter, null);
//        if (lists == null) {
//            return;
//        }
//        realData = SpellingUtils.stringSort(lists);
//        List<String> strings = new ArrayList<>(headLetter);
//        strings.addAll(Arrays.asList(getResources().getStringArray(R.array.quickSideBarLetters)));
//
//        for (int j = 0; j < strings.size(); j++) {
//            List<IndexBean> temps = new LinkedList<>();
//            for (int i = 0; i < realData.size(); i++) {
//                String firstLetter = SpellingUtils.getFirstLetter(realData.get(i).substring(0, SUBSTRING_END_INDEX));
//                if (TextUtils.equals(strings.get(j), firstLetter)) {
//                    temps.add(new IndexBean(firstLetter, realData.get(i), realData.get(i).substring(0, SUBSTRING_END_INDEX)));
//                }
//            }
//            mMap.put(strings.get(j), temps);
//        }
//
//        Log.e("fei.wang", "-- lists--> " + lists.size());
//        syncData(lists, headLetter, null);
    }

    /**
     * 标题的数据集合，这个方法会把 标题数据集合的首字母提取出来，归类，排序 a,b,c...z
     *
     * @param lists      标题的数据集合，总共的数据集合 按自然顺序排好序的
     * @param headLetter 放到队列头部特殊符号集合, 方便计算头部高度
     * @param tailLetter 放到队列尾部特殊符号集合，方便计算尾部的高度
     */
    public void setData(List<IndexBean> lists, List<String> headLetter, List<String> tailLetter) {
        if (lists == null) {
            return;
        }
        realData = lists;
//        realData = SpellingUtils.stringSort(lists);
        List<String> strings = new ArrayList<>();
        if (headLetter != null && headLetter.size() > 0) {
            strings.addAll(headLetter);
        }

        if (Arrays.asList(getResources().getStringArray(R.array.quickSideBarLetters)) != null) {
            strings.addAll(Arrays.asList(getResources().getStringArray(R.array.quickSideBarLetters)));
        }

        if (tailLetter != null && tailLetter.size() > 0) {
            strings.addAll(tailLetter);
        }

        for (int j = 0; j < strings.size(); j++) {
            List<IndexBean> temps = new LinkedList<>();
            for (int i = 0; i < realData.size(); i++) {
//                String firstLetter = SpellingUtils.getFirstLetter(realData.get(i).substring(0, SUBSTRING_END_INDEX));
                String firstLetter = realData.get(i).getLetter();
                if (TextUtils.equals(strings.get(j), firstLetter)) {
//                    temps.add(new IndexBean(firstLetter, realData.get(i).getName().substring(0, SUBSTRING_END_INDEX)));
//                    temps.add(new IndexBean(realData.get(i).getName().substring(0, SUBSTRING_END_INDEX)));
                    String substring = realData.get(i).getName().substring(0, SUBSTRING_END_INDEX);
                    temps.add(realData.get(i).setNameFirst(substring));
                }
            }
            mMap.put(strings.get(j), temps); // TODO key 字母， value 字母对应标题首字母的集合
        }

        syncData(lists, headLetter, tailLetter);
    }

    /**
     * 设置字母表
     *
     * @param strs 字母集合
     */
    private void syncData(List<IndexBean> strs, List<String> headLetter, List<String> tailLetter) {

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
                setVisibility(VISIBLE);
                // 26 个英文字母
                List<String> strings = Arrays.asList(getResources().getStringArray(R.array.quickSideBarLetters));
                List<String> tempLetter = null;
                int measuredHeight = getHeight();
                if (measuredHeight < UDisplayUtil.dp2Px(getContext(), INDEX_BAR_MIN_HEIGHT)) {
                    setVisibility(GONE);
                } else if (measuredHeight >= getIndexBarHeight(strings.size() + finalSize)) {
                    tempLetter = strings;
                } else if (measuredHeight >= getIndexBarHeight(finalSize + INDEX_BAR_SEVENTEEN)) {
                    mTempType = ZOOM_TYPE_ONE;
                    tempLetter = Arrays.asList(getContext().getResources().getStringArray(R.array.zoomTwo));
                } else if (measuredHeight >= getIndexBarHeight(finalSize + INDEX_BAR_THIRTEEN)) {
                    mTempType = ZOOM_TYPE_TWO;
                    tempLetter = Arrays.asList(getContext().getResources().getStringArray(R.array.zoomThree));
                } else if (measuredHeight >= getIndexBarHeight(finalSize + INDEX_BAR_ELEVEN)) {
                    mTempType = ZOOM_TYPE_THREE;
                    tempLetter = Arrays.asList(getContext().getResources().getStringArray(R.array.zoomFour));
                } else if (measuredHeight >= getIndexBarHeight(finalSize + INDEX_BAR_NINE_POSITION)) {
                    mTempType = ZOOM_TYPE_FOUR;
                    tempLetter = Arrays.asList(getContext().getResources().getStringArray(R.array.zoomFive));
                } else if (measuredHeight >= getIndexBarHeight(finalSize + INDEX_BAR_SEVEN_POSITION)) {
                    mTempType = ZOOM_TYPE_FIVE;
                    tempLetter = Arrays.asList(getContext().getResources().getStringArray(R.array.zoomSix));
                }

                // 真实的 IndexBar 的字母数据集合
                List<String> letters = new ArrayList<>();
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

                    indexBean = new IndexBean(letters.get(i), letters.get(i));
                    if (mTempType == ZOOM_TYPE_ONE) {
                        if (i - headSize == INDEX_BAR_ONE_POSITION) {
                            indexBean.setLists(Arrays.asList("B", "C"));
                        } else if (i - headSize == INDEX_BAR_THREE_POSITION) {
                            indexBean.setLists(Arrays.asList("E", "F"));
                        } else if (i - headSize == INDEX_BAR_FIVE_POSITION) {
                            indexBean.setLists(Arrays.asList("H", "I"));
                        } else if (i - headSize == INDEX_BAR_SEVEN_POSITION) {
                            indexBean.setLists(Arrays.asList("K", "L"));
                        } else if (i - headSize == INDEX_BAR_NINE_POSITION) {
                            indexBean.setLists(Arrays.asList("N", "O"));
                        } else if (i - headSize == INDEX_BAR_ELEVEN) {
                            indexBean.setLists(Arrays.asList("Q", "R"));
                        } else if (i - headSize == INDEX_BAR_THIRTEEN) {
                            indexBean.setLists(Arrays.asList("T", "U"));
                        } else if (i - headSize == INDEX_BAR_FIFTEEN) {
                            indexBean.setLists(Arrays.asList("W", "X", "Y"));
                        }
                    } else if (mTempType == ZOOM_TYPE_TWO) {
                        if (i - headSize == INDEX_BAR_ONE_POSITION) {
                            indexBean.setLists(Arrays.asList("B", "C", "D"));
                        } else if (i - headSize == INDEX_BAR_THREE_POSITION) {
                            indexBean.setLists(Arrays.asList("F", "G", "H"));
                        } else if (i - headSize == INDEX_BAR_FIVE_POSITION) {
                            indexBean.setLists(Arrays.asList("J", "K", "L"));
                        } else if (i - headSize == INDEX_BAR_SEVEN_POSITION) {
                            indexBean.setLists(Arrays.asList("N", "O", "P"));
                        } else if (i - headSize == INDEX_BAR_NINE_POSITION) {
                            indexBean.setLists(Arrays.asList("R", "S", "T"));
                        } else if (i - headSize == INDEX_BAR_ELEVEN) {
                            indexBean.setLists(Arrays.asList("V", "W", "X", "Y"));
                        }
                    } else if (mTempType == ZOOM_TYPE_THREE) {
                        if (i - headSize == INDEX_BAR_ONE_POSITION) {
                            indexBean.setLists(Arrays.asList("B", "C", "D", "E"));
                        } else if (i - headSize == INDEX_BAR_THREE_POSITION) {
                            indexBean.setLists(Arrays.asList("G", "H", "I", "J"));
                        } else if (i - headSize == INDEX_BAR_FIVE_POSITION) {
                            indexBean.setLists(Arrays.asList("L", "M", "N", "O"));
                        } else if (i - headSize == INDEX_BAR_SEVEN_POSITION) {
                            indexBean.setLists(Arrays.asList("Q", "R", "S", "T"));
                        } else if (i - headSize == INDEX_BAR_NINE_POSITION) {
                            indexBean.setLists(Arrays.asList("V", "W", "X", "Y"));
                        }
                    } else if (mTempType == ZOOM_TYPE_FOUR) {
                        if (i - headSize == INDEX_BAR_ONE_POSITION) {
                            indexBean.setLists(Arrays.asList("B", "C", "D", "E", "F"));
                        } else if (i - headSize == INDEX_BAR_THREE_POSITION) {
                            indexBean.setLists(Arrays.asList("H", "I", "J", "K", "L"));
                        } else if (i - headSize == INDEX_BAR_FIVE_POSITION) {
                            indexBean.setLists(Arrays.asList("N", "O", "P", "Q", "R"));
                        } else if (i - headSize == INDEX_BAR_SEVEN_POSITION) {
                            indexBean.setLists(Arrays.asList("T", "U", "V", "W", "X", "Y"));
                        }
                    } else if (mTempType == ZOOM_TYPE_FIVE) {
                        if (i - headSize == INDEX_BAR_ONE_POSITION) {
                            indexBean.setLists(Arrays.asList("B", "C", "D", "E", "F", "G"));
                        } else if (i - headSize == INDEX_BAR_THREE_POSITION) {
                            indexBean.setLists(Arrays.asList("I", "J", "K", "L", "M", "N"));
                        } else if (i - headSize == INDEX_BAR_FIVE_POSITION) {
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
//            setLocation(bean, false);
            Log.e("fei.wang", "position -> " + position);
            mOnTouchListener.onChanged(bean, position, y, false);
        } else {
            setLocation(bean, false);
        }
    }

    @Override
    public void onDispatchTouch(boolean touching) {
        setTipsViewHide(touching);
    }

    /**
     * @param bean
     * @param secondaryIndex 是否是二级点击事件的回掉
     */
    private void setLocation(IndexBean bean, boolean secondaryIndex) {
        if (realData != null && realData.size() > 0 && mLinearLayoutManager != null) {
            for (int i = 0; i < realData.size(); i++) {
                if (secondaryIndex) {
                    if (TextUtils.equals(realData.get(i).getId(), bean.getId())) {
//                        mRecyclerView.smoothScrollToPosition(i);
                        Log.e("fei.wang", "i name -> " + i);
//                        mLinearLayoutManager.scrollToPositionWithOffset(i, -UDisplayUtil.dp2Px(getContext(),36));
                        mLinearLayoutManager.scrollToPositionWithOffset(i, 0);
                        return;
                    }
                } else {
//                    String key = SpellingUtils.getFirstLetter(realData.get(i).substring(0, SUBSTRING_END_INDEX));
                    String key = realData.get(i).getLetter();
                    if (("#".equals(bean.getLetter()) && key.startsWith("#")) || TextUtils.equals(key, bean.getLetter())) {
//                        mRecyclerView.smoothScrollToPosition(i);
                        Log.e("fei.wang", "i -> " + i);
//                        mLinearLayoutManager.scrollToPositionWithOffset(i, -UDisplayUtil.dp2Px(getContext(),36));
                        mLinearLayoutManager.scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        }
    }

    /**
     * 二级引导（IndexBarTipsView） 列表 item 的点击事件
     *
     * @param view     对应item 的 View
     * @param bean     对应item 的实体数据类
     * @param position 点击 IndexBarTipsView item 的 角标
     * @param isLetter true 点击的字母，false 点击的是文字目前是汉字，多语言后会有其他文字
     */
    @Override
    public void onClick(View view, IndexBean bean, int position, boolean isLetter) {
        if (mOnTouchListener != null) {
//            setLocation(bean, isLetter);
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

    @Override
    public void onMoving(IndexBean bean, float y, float currentItemY, int position) {
        int index = position - headSize;
        if (mTempType == ZOOM_TYPE_ONE) {
            if (index == INDEX_BAR_ONE_POSITION) {
                setIndexBarTipsData(bean, y, currentItemY, index, INDEX_BAR_TWO_POSITION);
//                indexBean.setLists(Arrays.asList("B", "C"));
            } else if (index == INDEX_BAR_THREE_POSITION) {
                setIndexBarTipsData(bean, y, currentItemY, index, INDEX_BAR_TWO_POSITION);
//                indexBean.setLists(Arrays.asList("E", "F"));
            } else if (index == INDEX_BAR_FIVE_POSITION) {
                setIndexBarTipsData(bean, y, currentItemY, index, INDEX_BAR_TWO_POSITION);
//                indexBean.setLists(Arrays.asList("H", "I"));
            } else if (index == INDEX_BAR_SEVEN_POSITION) {
                setIndexBarTipsData(bean, y, currentItemY, index, INDEX_BAR_TWO_POSITION);
//                indexBean.setLists(Arrays.asList("K", "L"));
            } else if (index == INDEX_BAR_NINE_POSITION) {
                setIndexBarTipsData(bean, y, currentItemY, index, INDEX_BAR_TWO_POSITION);
//                indexBean.setLists(Arrays.asList("N", "O"));
            } else if (index == INDEX_BAR_ELEVEN) {
                setIndexBarTipsData(bean, y, currentItemY, index, INDEX_BAR_TWO_POSITION);
//                indexBean.setLists(Arrays.asList("Q", "R"));
            } else if (index == INDEX_BAR_THIRTEEN) {
                setIndexBarTipsData(bean, y, currentItemY, index, INDEX_BAR_TWO_POSITION);
//                indexBean.setLists(Arrays.asList("T", "U"));
            } else if (index == INDEX_BAR_FIFTEEN) {
                setIndexBarTipsData(bean, y, currentItemY, index, INDEX_BAR_THREE_POSITION);
//                indexBean.setLists(Arrays.asList("W", "X", "Y"));
            }
        } else if (mTempType == ZOOM_TYPE_TWO) {
            if (index == INDEX_BAR_ONE_POSITION) {
                setIndexBarTipsData(bean, y, currentItemY, index, INDEX_BAR_THREE_POSITION);
//                indexBean.setLists(Arrays.asList("B", "C", "D"));
            } else if (index == INDEX_BAR_THREE_POSITION) {
                setIndexBarTipsData(bean, y, currentItemY, index, INDEX_BAR_THREE_POSITION);
//                indexBean.setLists(Arrays.asList("F", "G", "H"));
            } else if (index == INDEX_BAR_FIVE_POSITION) {
                setIndexBarTipsData(bean, y, currentItemY, index, INDEX_BAR_THREE_POSITION);
//                indexBean.setLists(Arrays.asList("J", "K", "L"));
            } else if (index == INDEX_BAR_SEVEN_POSITION) {
                setIndexBarTipsData(bean, y, currentItemY, index, INDEX_BAR_THREE_POSITION);
//                indexBean.setLists(Arrays.asList("N", "O", "P"));
            } else if (index == INDEX_BAR_NINE_POSITION) {
                setIndexBarTipsData(bean, y, currentItemY, index, INDEX_BAR_THREE_POSITION);
//                indexBean.setLists(Arrays.asList("R", "S", "T"));
            } else if (index == INDEX_BAR_ELEVEN) {
                setIndexBarTipsData(bean, y, currentItemY, index, INDEX_BAR_FOUR_POSITION);
//                indexBean.setLists(Arrays.asList("V", "W", "X", "Y"));
            }
        } else if (mTempType == ZOOM_TYPE_THREE) {
            if (index == INDEX_BAR_ONE_POSITION) {
                setIndexBarTipsData(bean, y, currentItemY, index, INDEX_BAR_FOUR_POSITION);
//                indexBean.setLists(Arrays.asList("B", "C", "D", "E"));
            } else if (index == INDEX_BAR_THREE_POSITION) {
                setIndexBarTipsData(bean, y, currentItemY, index, INDEX_BAR_FOUR_POSITION);
//                indexBean.setLists(Arrays.asList("G", "H", "I", "J"));
            } else if (index == INDEX_BAR_FIVE_POSITION) {
                setIndexBarTipsData(bean, y, currentItemY, index, INDEX_BAR_FOUR_POSITION);
//                indexBean.setLists(Arrays.asList("L", "M", "N", "O"));
            } else if (index == INDEX_BAR_SEVEN_POSITION) {
                setIndexBarTipsData(bean, y, currentItemY, index, INDEX_BAR_FOUR_POSITION);
//                indexBean.setLists(Arrays.asList("Q", "R", "S", "T"));
            } else if (index == INDEX_BAR_NINE_POSITION) {
                setIndexBarTipsData(bean, y, currentItemY, index, INDEX_BAR_FOUR_POSITION);
//                indexBean.setLists(Arrays.asList("V", "W", "X", "Y"));
            }
        } else if (mTempType == ZOOM_TYPE_FOUR) {
            if (index == INDEX_BAR_ONE_POSITION) {
                setIndexBarTipsData(bean, y, currentItemY, index, INDEX_BAR_FIVE_POSITION);
//                indexBean.setLists(Arrays.asList("B", "C", "D", "E", "F"));
            } else if (index == INDEX_BAR_THREE_POSITION) {
                setIndexBarTipsData(bean, y, currentItemY, index, INDEX_BAR_FIVE_POSITION);
//                indexBean.setLists(Arrays.asList("H", "I", "J", "K", "L"));
            } else if (index == INDEX_BAR_FIVE_POSITION) {
                setIndexBarTipsData(bean, y, currentItemY, index, INDEX_BAR_FIVE_POSITION);
//                indexBean.setLists(Arrays.asList("N", "O", "P", "Q", "R"));
            } else if (index == INDEX_BAR_SEVEN_POSITION) {
                setIndexBarTipsData(bean, y, currentItemY, index, INDEX_BAR_SIX_POSITION);
//                indexBean.setLists(Arrays.asList("T", "U", "V", "W", "X", "Y"));
            }
        } else if (mTempType == ZOOM_TYPE_FIVE) {
            if (index == INDEX_BAR_ONE_POSITION) {
                setIndexBarTipsData(bean, y, currentItemY, index, INDEX_BAR_SIX_POSITION);
//                indexBean.setLists(Arrays.asList("B", "C", "D", "E", "F", "G"));
            } else if (index == INDEX_BAR_THREE_POSITION) {
                setIndexBarTipsData(bean, y, currentItemY, index, INDEX_BAR_SIX_POSITION);
//                indexBean.setLists(Arrays.asList("I", "J", "K", "L", "M", "N"));
            } else if (index == INDEX_BAR_FIVE_POSITION) {
                setIndexBarTipsData(bean, y, currentItemY, index, INDEX_BAR_TEN_POSITION);
//                indexBean.setLists(Arrays.asList("P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y"));
            }
        }
    }

    /**
     * 滑动到省略的点二级索引展示的列表
     *
     * @param bean         item 对应的实体
     * @param y            在 item 上的偏移距离
     * @param index        item 对应的下标
     * @param currentItemY 当前 item 的 Y 轴偏移距离
     * @param moiety       当前省略的 item 几等份 最大也就是10等分
     */
    private void setIndexBarTipsData(IndexBean bean, float y, float currentItemY, int index, int moiety) {
        if (mIndexBarTipsView != null) {
            if (TextUtils.equals("·", bean.getLetter())) {
                if (bean.getLists() != null && bean.getLists().size() > 0) {

                    float itemOffsetY = y - currentItemY; // item 内手指偏移的距离

                    int moietyHeight = UDisplayUtil.dp2Px(getContext(), ITEM_HEIGHT) / moiety; // 没等份的高度

                    float itemOffsetMoietyY = itemOffsetY / moietyHeight; // 手指滑动的距离是否到了等分 item 高度的距离

                    if (itemOffsetMoietyY <= INDEX_BAR_ONE_POSITION) {
                        String letter = bean.getLists().get(0);
                        mIndexBarTipsView.setText(letter, index, y);
                        mIndexBarTipsView.setData(mMap.get(letter));
                    } else if (itemOffsetMoietyY <= INDEX_BAR_TWO_POSITION) {
                        if (bean.getLists().size() >= INDEX_BAR_TWO_POSITION) {
                            String letter = bean.getLists().get(INDEX_BAR_ONE_POSITION);
                            mIndexBarTipsView.setText(letter, index, y);
                            mIndexBarTipsView.setData(mMap.get(letter));
                        }
                    } else if (itemOffsetMoietyY <= INDEX_BAR_THREE_POSITION) {
                        if (bean.getLists().size() >= INDEX_BAR_THREE_POSITION) {
                            String letter = bean.getLists().get(INDEX_BAR_TWO_POSITION);
                            mIndexBarTipsView.setText(letter, index, y);
                            mIndexBarTipsView.setData(mMap.get(letter));
                        }
                    } else if (itemOffsetMoietyY <= INDEX_BAR_FOUR_POSITION) {
                        if (bean.getLists().size() >= INDEX_BAR_FOUR_POSITION) {
                            String letter = bean.getLists().get(INDEX_BAR_THREE_POSITION);
                            mIndexBarTipsView.setText(letter, index, y);
                            mIndexBarTipsView.setData(mMap.get(letter));
                        }
                    } else if (itemOffsetMoietyY <= INDEX_BAR_FIVE_POSITION) {
                        if (bean.getLists().size() >= INDEX_BAR_FIVE_POSITION) {
                            String letter = bean.getLists().get(INDEX_BAR_FOUR_POSITION);
                            mIndexBarTipsView.setText(letter, index, y);
                            mIndexBarTipsView.setData(mMap.get(letter));
                        }
                    } else if (itemOffsetMoietyY <= INDEX_BAR_SIX_POSITION) {
                        if (bean.getLists().size() >= INDEX_BAR_SIX_POSITION) {
                            String letter = bean.getLists().get(INDEX_BAR_FIVE_POSITION);
                            mIndexBarTipsView.setText(letter, index, y);
                            mIndexBarTipsView.setData(mMap.get(letter));
                        }
                    } else if (itemOffsetMoietyY <= INDEX_BAR_SEVEN_POSITION) {
                        if (bean.getLists().size() >= INDEX_BAR_SEVEN_POSITION) {
                            String letter = bean.getLists().get(INDEX_BAR_SIX_POSITION);
                            mIndexBarTipsView.setText(letter, index, y);
                            mIndexBarTipsView.setData(mMap.get(letter));
                        }
                    } else if (itemOffsetMoietyY <= INDEX_BAR_EIGHT_POSITION) {
                        if (bean.getLists().size() >= INDEX_BAR_EIGHT_POSITION) {
                            String letter = bean.getLists().get(INDEX_BAR_SEVEN_POSITION);
                            mIndexBarTipsView.setText(letter, index, y);
                            mIndexBarTipsView.setData(mMap.get(letter));
                        }
                    } else if (itemOffsetMoietyY <= INDEX_BAR_NINE_POSITION) {
                        if (bean.getLists().size() >= INDEX_BAR_NINE_POSITION) {
                            String letter = bean.getLists().get(INDEX_BAR_EIGHT_POSITION);
                            mIndexBarTipsView.setText(letter, index, y);
                            mIndexBarTipsView.setData(mMap.get(letter));
                        }
                    } else if (itemOffsetMoietyY <= INDEX_BAR_TEN_POSITION) {
                        if (bean.getLists().size() >= INDEX_BAR_TEN_POSITION) {
                            String letter = bean.getLists().get(INDEX_BAR_NINE_POSITION);
                            mIndexBarTipsView.setText(letter, index, y);
                            mIndexBarTipsView.setData(mMap.get(letter));
                        }
                    }
                }
            } else {
                mIndexBarTipsView.setText(bean.getLetter(), index, y);
                mIndexBarTipsView.setData(mMap.get(bean.getLetter()));
            }
        }
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
        /**
         * 索引item点击和滑动的回掉事件
         *
         * @param bean           item 对应的实体
         * @param position       item 对应的下标
         * @param y              在 item 上的偏移距离
         * @param secondaryIndex true 是二级索引文字点击事件的回掉
         */
        void onChanged(IndexBean bean, int position, float y, boolean secondaryIndex);

        /**
         * 手指按下和抬起的回掉
         *
         * @param touching true 手指在屏幕上，false 手指离开屏幕
         */
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
//                String key = SpellingUtils.getFirstLetter(realData.get(position).substring(0, SUBSTRING_END_INDEX));
                String key = realData.get(position).getLetter();
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