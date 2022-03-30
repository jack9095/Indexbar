package com.fei.indexbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fei.indexbar.adapter.IndexBarFocusAdapter;
import com.fei.indexbar.util.DataUtil;
import com.fei.indexbar.util.SpellingUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IndexBar.OnTouchListener {

    private List<String> focusList;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        focusList = DataUtil.getData();
        initViews();
    }

    @Override
    public void onChanged(String letter, int position, float y) {
        Log.e("fei.wang", "触摸滑动事件");
        for (int i = 0; i < focusList.size(); i++) {
            String key = SpellingUtils.getFirstLetter(focusList.get(i).substring(0, 1));
            if (("#".equals(letter) && key.startsWith("#")) || TextUtils.equals(key, letter)) {
                layoutManager.scrollToPositionWithOffset(i, 0);
                return;
            }
        }
    }

    @Override
    public void onTouching(boolean touching) {
    }

    private void initViews() {

        final List<String> enNameList = new ArrayList<>(); // 给右侧字母导航栏设置的数据
        for (String value : focusList) {
            String key = SpellingUtils.getFirstLetter(value.substring(0, 1));
            key = key.startsWith("#") ? "#" : key;
            if (!enNameList.contains(key)) {
                enNameList.add(key);
            }
        }

        layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final IndexBar mIndexBar = findViewById(R.id.index_bar);
        mIndexBar.setOnTouchListener(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new IndexBarFocusAdapter(focusList, R.layout.item_focus_side_bar));
        // TODO recyclerView 滚动的时候 联动右侧滚动
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                final int position = layoutManager.findFirstCompletelyVisibleItemPosition();
                String key = SpellingUtils.getFirstLetter(focusList.get(position).substring(0, 1));
                mIndexBar.setCurrentIndex(enNameList.indexOf(key));
            }
        });
    }

}