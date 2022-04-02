package com.fei.indexbar;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fei.indexbar.adapter.MainIndexBarFocusAdapter;
import com.fei.indexbar.model.IndexBean;
import com.fei.indexbar.util.DataUtil;
import com.fei.indexbar.view.IndexBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IndexBar.OnTouchListener {

    private List<String> focusList;
    private IndexBar mIndexBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        focusList = DataUtil.getData();
        initViews();
    }

    @Override
    public void onChanged(IndexBean bean, int position, float y, boolean secondaryIndex) {

    }

    @Override
    public void onTouching(boolean touching) {
    }

    private void initViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        mIndexBar = findViewById(R.id.index_bar);
        mIndexBar.setRecyclerView(recyclerView, layoutManager);

        List<String> lists = new ArrayList<>();
        lists.add("☆");

        List<String> ends = new ArrayList<>();
        ends.add("#");
//        lists.add("\uD83D\uDD0D");

        mIndexBar.setData(focusList, lists, ends);
//        mIndexBar.setOnTouchListener(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new MainIndexBarFocusAdapter(focusList, R.layout.main_list_item_adapter));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIndexBar.release();
    }
}