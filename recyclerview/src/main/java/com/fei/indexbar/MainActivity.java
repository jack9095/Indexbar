package com.fei.indexbar;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fei.indexbar.adapter.IndexBarFocusAdapter;
import com.fei.indexbar.util.DataUtil;
import com.fei.indexbar.util.SpellingUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyRecyclerView.OnTouchListener {

    private List<String> focusList;
    HashMap<String,Integer> letters = new HashMap<>();
    private IndexBarTipsView indexBarTipsView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        focusList = DataUtil.getData();
        int position = 0;
        for(String letter: focusList){
            //如果没有这个key则加入并把位置也加入
            if(!letters.containsKey(letter)){
                letters.put(letter,position);
            }
            position++;
        }
        initViews();
    }

    @Override
    public void onChanged(String letter, int position, float y) {
        indexBarTipsView.setText(letter, position, y);
        //有此key则获取位置并滚动到该位置
        if(letters.containsKey(letter)) {
            Integer index = letters.get(letter);
            recyclerView.scrollToPosition(index);
        }
    }

    @Override
    public void onTouching(boolean touching) {
        //可以自己加入动画效果渐显渐隐
        indexBarTipsView.setVisibility(touching? View.VISIBLE:View.INVISIBLE);
    }

    private void initViews() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        indexBarTipsView = findViewById(R.id.indexBarTipsView);
        recyclerView = findViewById(R.id.recyclerView);
        final IndexBar focusSideBar = findViewById(R.id.index_bar);
        focusSideBar.setOnTouchListener(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new IndexBarFocusAdapter(focusList, R.layout.item_focus_side_bar));

        final List<String> enNameList = new ArrayList<>();
        for (String value : focusList) {
            String key = SpellingUtils.getFirstLetter(value.substring(0, 1));
            key = key.startsWith("#") ? "#" : key;
            if (!enNameList.contains(key)) {
                enNameList.add(key);
            }
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                final int position = layoutManager.findFirstCompletelyVisibleItemPosition();
                String key = SpellingUtils.getFirstLetter(focusList.get(position).substring(0, 1));
//                focusSideBar.setCurrentIndex(enNameList.indexOf(key));
            }
        });
    }

}