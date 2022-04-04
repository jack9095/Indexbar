package stick.head.recycler.test;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fei.indexbar.R;
import com.fei.indexbar.model.IndexBean;
import com.fei.indexbar.view.IndexBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import stick.head.recycler.stick.StickHeaderDecoration1;
import stick.head.recycler.test.adapter.NormalAdapter;
import stick.head.recycler.test.model.City;
import stick.head.recycler.test.model.DataConstants;

public class TestActivity extends AppCompatActivity implements IndexBar.OnTouchListener {

    private RecyclerView mRecyclerView;
    private IndexBar mIndexBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_test);
        mRecyclerView = findViewById(R.id.recycle);
        mIndexBar = findViewById(R.id.index_bar_stick);
        mIndexBar.setOnTouchListener(this);

        //GSON解释出来
        Type listType = new TypeToken<LinkedList<City>>() {
        }.getType();
        Gson gson = new Gson();
        LinkedList<City> cities = gson.fromJson(DataConstants.cityDataList, listType);

        ArrayList<String> customLetters = new ArrayList<>();

        for (City city : cities) {
            customLetters.add(city.getCityName());
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        mIndexBar.setData(customLetters, Arrays.asList("☆"));
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(new NormalAdapter(this, cities));
        mRecyclerView.addItemDecoration(new StickHeaderDecoration1(mRecyclerView));

        mIndexBar.setRecyclerView(mRecyclerView, layoutManager);
    }


    @Override
    public void onChanged(IndexBean bean, int position, float y, boolean secondaryIndex) {

    }

    @Override
    public void onTouching(boolean touching) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIndexBar.release();
    }
}
