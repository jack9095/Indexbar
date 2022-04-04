package recyclerview.test;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fei.sidebardemo.R;
import com.fei.sidebardemo.model.IndexBean;
import com.fei.sidebardemo.util.UDisplayUtil;
import com.fei.sidebardemo.view.IndexBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import recyclerview.stickyheadrecycler.StickyRecyclerHeadersDecoration;
import recyclerview.test.adapter.CityListWithHeadersAdapter;
import recyclerview.test.model.City;
import recyclerview.test.model.ModuleUtil;


/**
 * "☆"
 *  头部粘贴 RecyclerView 的使用
 */
public class StickActivity extends AppCompatActivity implements IndexBar.OnTouchListener {
    RecyclerView mRecyclerView;

    HashMap<String, Integer> letters = new HashMap<>();

    private IndexBar mIndexBar;
    private LinkedList<City> cities;
    private LinearLayoutManager mLinearLayoutManager;
    private CityListWithHeadersAdapter mCityListWithHeadersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.u_activity_stick);
        cities = ModuleUtil.getData();
        initViews();
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

        ArrayList<IndexBean> customLetters = new ArrayList<>();

        for (City city : cities) {
            customLetters.add(new IndexBean(city.getFirstLetter(), city.getCityName(),city.getCityName()));
        }

//        mIndexBar.setData(customLetters, Arrays.asList("☆"));
        mIndexBar.setData(customLetters);

        mCityListWithHeadersAdapter = new CityListWithHeadersAdapter();
        mCityListWithHeadersAdapter.addAll(cities);
        mRecyclerView.setAdapter(mCityListWithHeadersAdapter);
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mCityListWithHeadersAdapter);
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
     * @param position 索引 item 对应的下标
     * @param y 在 item 上的偏移距离
     * @param secondaryIndex true 是二级索引文字点击事件的回掉
     */
    private void setLocation(IndexBean bean, int position, float y, boolean secondaryIndex) {
        if (cities != null && cities.size() > 0) {
            for (int i = 0; i < cities.size(); i++) {
                if (secondaryIndex) {
//                    if (TextUtils.equals(cities.get(i).getCityName(), bean.getId())) {
                    if (TextUtils.equals(cities.get(i).getCityName(), bean.getName())) {
//                        mRecyclerView.smoothScrollToPosition(i);
                        List<Integer> headPosition = mCityListWithHeadersAdapter.getHeadPosition();

                        Log.e("fei.wang", "i name -> " + i);
                        if (headPosition.contains(i)) {
                            mLinearLayoutManager.scrollToPositionWithOffset(i, 0);
                        } else {
                            // TODO 这里偏移的 36 dp 是粘贴头部的高度
                            mLinearLayoutManager.scrollToPositionWithOffset(i, UDisplayUtil.dp2Px(this, 36));
                        }
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
                        mLinearLayoutManager.scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        }
    }
}