package com.jokerwan.recyclerviewstickheader;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fei.indexbar.R;

public class StickHeaderActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private StickyHeaderLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_header_a);
        layout = findViewById(R.id.sh_layout);
        recycler = findViewById(R.id.recycler);

        layout.setShowStickItemPosition(5);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new MyAdapter());

    }
}
