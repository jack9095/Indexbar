package com.fei.indexbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fei.indexbar.adapter.MainIndexBarFocusAdapter;
import com.fei.indexbar.model.IndexBean;
import com.fei.indexbar.util.DataUtil;
import com.fei.indexbar.util.PinyinUtils;
import com.fei.indexbar.util.SpellingUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IndexBar.OnTouchListener {

    private List<String> focusList;
    private LinearLayoutManager layoutManager;
    private List<IndexBean> mLetters = new ArrayList<>();
    private IndexBar mIndexBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        focusList = DataUtil.getData();


        // 测试代码 start
//        IndexBean indexBean;
//        for (String string : focusList) {
//            indexBean = new IndexBean(PinyinUtils.getFirstSpell(string.substring(0,1)));
//            indexBean.name = string;
//            mLetters.add(indexBean);
//        }
//        // 根据拼音为数组进行排序
//        Collections.sort(mLetters, new IndexBean.ComparatorLetter());
//        focusList.clear();
//        for (int i = 0; i < mLetters.size(); i++) {
//            focusList.add(mLetters.get(i).name);
//        }
        // 测试代码 end


        initViews();
    }

    @Override
    public void onChanged(IndexBean bean, int position, float y, boolean secondaryIndex) {
//        for (int i = 0; i < focusList.size(); i++) {
//            if (secondaryIndex) {
//                if (TextUtils.equals(focusList.get(i), bean.getName())) {
//                    layoutManager.scrollToPositionWithOffset(i, 0);
//                    return;
//                }
//            } else {
//                String key = SpellingUtils.getFirstLetter(focusList.get(i).substring(0, 1));
//                if (("#".equals(bean.getLetter()) && key.startsWith("#")) || TextUtils.equals(key, bean.getLetter())) {
//                    layoutManager.scrollToPositionWithOffset(i, 0);
//                    return;
//                }
//            }
//        }
    }

    @Override
    public void onTouching(boolean touching) {
    }

    private void initViews() {

//        final List<String> enNameList = new ArrayList<>(); // 给右侧字母导航栏设置的数据
//        for (String value : focusList) {
//            String key = SpellingUtils.getFirstLetter(value.substring(0, 1));
//            key = key.startsWith("#") ? "#" : key;
//            if (!enNameList.contains(key)) {
//                enNameList.add(key);
//            }
//        }

        layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        mIndexBar = findViewById(R.id.index_bar);
        mIndexBar.setRecyclerView(recyclerView, layoutManager);
        mIndexBar.setData(focusList);
//        mIndexBar.setOnTouchListener(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new MainIndexBarFocusAdapter(focusList, R.layout.main_list_item_adapter));
        // TODO recyclerView 滚动的时候 联动右侧滚动
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                final int position = layoutManager.findFirstCompletelyVisibleItemPosition();
//                String key = SpellingUtils.getFirstLetter(focusList.get(position).substring(0, 1));
//                mIndexBar.setCurrentIndex(enNameList.indexOf(key));
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIndexBar.release();
    }
}