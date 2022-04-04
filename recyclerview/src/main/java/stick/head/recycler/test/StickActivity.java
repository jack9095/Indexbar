package stick.head.recycler.test;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fei.indexbar.R;
import com.fei.indexbar.model.IndexBean;
import com.fei.indexbar.util.SpellingUtils;
import com.fei.indexbar.util.UDisplayUtil;
import com.fei.indexbar.view.IndexBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import stick.head.recycler.stickyheadrecycler.StickyRecyclerHeadersDecoration;
import stick.head.recycler.test.adapter.CityListWithHeadersAdapter;
import stick.head.recycler.test.model.City;
import stick.head.recycler.test.model.ModuleUtil;

/**
 * "☆"
 * 用法
 * LinearLayoutManager manager = new TopLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
 * 然后item点击的时候调用：
 * mRecyclerView.smoothScrollToPosition(position);
 */
public class StickActivity extends AppCompatActivity implements IndexBar.OnTouchListener {
    RecyclerView mRecyclerView;

    HashMap<String, Integer> letters = new HashMap<>();

    private IndexBar mIndexBar;
    private LinkedList<City> cities;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick);
        cities = ModuleUtil.getData();
        initViews();
        String key = SpellingUtils.getFirstLetter("重庆".substring(0, 1));
        Log.e("fei.wang","重庆 -> " + key);
    }

    @Override
    public void onChanged(IndexBean bean, int position, float y, boolean secondaryIndex) {
        setLocation(bean, position, y, secondaryIndex);
    }

    @Override
    public void onTouching(boolean touching) {

    }

    private void initViews() {
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = findViewById(R.id.recycler_view_stick);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mIndexBar = findViewById(R.id.index_bar_stick);
        mIndexBar.setOnTouchListener(this);

        ArrayList<String> customLetters = new ArrayList<>();

        for (City city : cities) {
            customLetters.add(city.getCityName());
        }

        mIndexBar.setData(customLetters, Arrays.asList("☆"));

        CityListWithHeadersAdapter adapter = new CityListWithHeadersAdapter();
        adapter.addAll(cities);
        mRecyclerView.setAdapter(adapter);
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);
        mRecyclerView.addItemDecoration(headersDecor);

        mIndexBar.setRecyclerView(mRecyclerView, mLinearLayoutManager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIndexBar.release();
    }

    /**
     * 索引item点击和滑动的回掉事件
     * @param bean item 对应的实体
     * @param position item 对应的下标
     * @param y 在 item 上的偏移距离
     * @param secondaryIndex true 是二级索引文字点击事件的回掉
     */
    private void setLocation(IndexBean bean, int position, float y, boolean secondaryIndex) {
        if (cities != null && cities.size() > 0) {
            for (int i = 0; i < cities.size(); i++) {
                if (secondaryIndex) {
                    if (TextUtils.equals(cities.get(i).getCityName(), bean.getName())) {
//                        mRecyclerView.smoothScrollToPosition(i);
                        Log.e("fei.wang", "i name -> " + i);
//                        mLinearLayoutManager.scrollToPositionWithOffset(i, -UDisplayUtil.dp2Px(this,36));
                        mLinearLayoutManager.scrollToPositionWithOffset(i, 0);
                        return;
                    }
                } else {
//                    String key = SpellingUtils.getFirstLetter(cities.get(i).getCityName().substring(0, 1));
                    String key = cities.get(i).getFirstLetter();
                    if (("#".equals(bean.getLetter()) && key.startsWith("#")) || TextUtils.equals(key, bean.getLetter())) {
//                        mRecyclerView.smoothScrollToPosition(i);
                        Log.e("fei.wang", "key -> " + key);
                        Log.e("fei.wang", "bean.getLetter() -> " + bean.getLetter());
                        Log.e("fei.wang", "i -> " + i);
//                        mLinearLayoutManager.scrollToPositionWithOffset(i, -UDisplayUtil.dp2Px(this,36));
                        mLinearLayoutManager.scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        }
    }
}