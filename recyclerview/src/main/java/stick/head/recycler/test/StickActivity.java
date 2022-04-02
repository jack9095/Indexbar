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
import java.util.HashMap;
import java.util.LinkedList;

import stick.head.recycler.stickyheadrecycler.StickyRecyclerHeadersDecoration;
import stick.head.recycler.test.adapter.CityListWithHeadersAdapter;
import stick.head.recycler.test.model.City;
import stick.head.recycler.test.model.DataConstants;

/**
 * "☆"
 */
public class StickActivity extends AppCompatActivity implements IndexBar.OnTouchListener {
    RecyclerView recyclerView;

    HashMap<String, Integer> letters = new HashMap<>();

    private IndexBar mIndexBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick);
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
        recyclerView = findViewById(R.id.recycler_view_stick);
        mIndexBar = findViewById(R.id.index_bar_stick);
        mIndexBar.setOnTouchListener(this);
        recyclerView.setLayoutManager(layoutManager);


        // Add the sticky headers decoration
        CityListWithHeadersAdapter adapter = new CityListWithHeadersAdapter();

        //GSON解释出来
        Type listType = new TypeToken<LinkedList<City>>() {
        }.getType();
        Gson gson = new Gson();
        LinkedList<City> cities = gson.fromJson(DataConstants.cityDataList, listType);

        ArrayList<String> customLetters = new ArrayList<>();

        int position = 0;
        for (City city : cities) {
            customLetters.add(city.getCityName());
//            String letter = city.getFirstLetter();
//            //如果没有这个key则加入并把位置也加入
//            if(!letters.containsKey(letter)){
//                letters.put(letter,position);
//                customLetters.add(letter);
//            }
//            position++;
        }

        mIndexBar.setData(customLetters, Arrays.asList("☆"));
        adapter.addAll(cities);
        recyclerView.setAdapter(adapter);

        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);
        recyclerView.addItemDecoration(headersDecor);
//        recyclerView.addItemDecoration(new DividerDecoration(this));

        mIndexBar.setRecyclerView(recyclerView, layoutManager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIndexBar.release();
    }
}