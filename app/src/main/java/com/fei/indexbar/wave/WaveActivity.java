package com.fei.indexbar.wave;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fei.indexbar.R;
import com.fei.indexbar.wave.adapter.CityAdapter;
import com.fei.indexbar.wave.bean.City;
import com.fei.wavesidebar.WaveSideBarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class WaveActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    WaveSideBarView mSideBarView;
    CityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mSideBarView = (WaveSideBarView) findViewById(R.id.side_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        final PinnedHeaderDecoration decoration = new PinnedHeaderDecoration();
//        decoration.registerTypePinnedHeader(1, new PinnedHeaderDecoration.PinnedHeaderCreator() {
//            @Override
//            public boolean create(RecyclerView parent, int adapterPosition) {
//                return true;
//            }
//        });
//        mRecyclerView.addItemDecoration(decoration);


        new Thread(new Runnable() {
            @Override
            public void run() {
                Type listType = new TypeToken<ArrayList<City>>() {
                }.getType();
                Gson gson = new Gson();
                final List<City> list = gson.fromJson(City.DATA, listType);
                Collections.sort(list, new LetterComparator());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new CityAdapter(WaveActivity.this, list);
                        mRecyclerView.setAdapter(adapter);
                    }
                });
            }
        }).start();

        mSideBarView.setOnTouchLetterChangeListener(new WaveSideBarView.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int pos = adapter.getLetterPosition(letter);

                if (pos != -1) {
                    mRecyclerView.scrollToPosition(pos);
                    LinearLayoutManager mLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    mLayoutManager.scrollToPositionWithOffset(pos, 0);
                }
            }
        });

    }




}
