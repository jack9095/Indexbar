package stick.head.recycler.test.copy;

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
import java.util.List;

import stick.head.recycler.stickyheadrecycler.StickyRecyclerHeadersDecoration;
import stick.head.recycler.test.TopLayoutManager;
import stick.head.recycler.test.adapter.CityListWithHeadersAdapter;
import stick.head.recycler.test.model.City;
import stick.head.recycler.test.model.DataConstants;

/**
 * "☆"
 * 用法
 * LinearLayoutManager manager = new TopLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
 * 然后item点击的时候调用：
 * mRecyclerView.smoothScrollToPosition(position);
 */
public class StickActivityCopy extends AppCompatActivity implements IndexBar.OnTouchListener {
    RecyclerView mRecyclerView;

    HashMap<String, Integer> letters = new HashMap<>();

    private IndexBar mIndexBar;
    private LinkedList<City> cities;
    private TopLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick);
        initViews();
    }

    @Override
    public void onChanged(IndexBean bean, int position, float y, boolean secondaryIndex) {
//        nextShotPosition = position;
//        Log.e("fei.wang", "position -> " + position);
//        mRecyclerView.smoothScrollToPosition(position);
//        MoveToPosition(position);
    }

    @Override
    public void onTouching(boolean touching) {
    }

    private void initViews() {
        layoutManager = new TopLayoutManager(this);
        mRecyclerView = findViewById(R.id.recycler_view_stick);
        mIndexBar = findViewById(R.id.index_bar_stick);
//        mIndexBar.setOnTouchListener(this);
        mRecyclerView.setLayoutManager(layoutManager);


        // Add the sticky headers decoration
        CityListWithHeadersAdapter adapter = new CityListWithHeadersAdapter();

        //GSON解释出来
        Type listType = new TypeToken<LinkedList<City>>() {
        }.getType();
        Gson gson = new Gson();
        cities = gson.fromJson(DataConstants.cityDataList, listType);

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
//        scrollToNextShotDate(cities);
        mRecyclerView.setAdapter(adapter);

        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);
        mRecyclerView.addItemDecoration(headersDecor);
//        recyclerView.addItemDecoration(new DividerDecoration(this));

        mIndexBar.setRecyclerView(mRecyclerView, layoutManager);

//        mRecyclerView.addOnScrollListener(new RcvScrollListener());
    }



    /**
     * RecyclerView 移动到当前位置，
     *
//     * @param manager   设置RecyclerView对应的manager
//     * @param mRecyclerView  当前的RecyclerView
     * @param n  要跳转的位置
     */
    public void MoveToPosition(int n) {


        int firstItem = layoutManager.findFirstVisibleItemPosition();
        int lastItem = layoutManager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }

    }


    private boolean mShouldScroll;
    private int nextPosition; // 要置顶的 position 的下一个position
    private int nextShotPosition; // 要置顶的 position
    // https://www.jianshu.com/p/6d5ecfdbb615
    private void scrollToNextShotDate(List<City> vaccineGroupList) {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        //获取当前RecycleView屏幕可见的第一项和最后一项的Position
        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
        int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
        if (nextPosition < firstVisibleItemPosition) {
            //要置顶的项在当前显示的第一项之前
            mRecyclerView.smoothScrollToPosition(nextShotPosition);
        } else if (nextPosition < lastVisibleItemPosition) {
            //要置顶的项已经在屏幕上显示，计算它离屏幕原点的距离
            int top = mRecyclerView.getChildAt(nextShotPosition - firstVisibleItemPosition).getTop();
            mRecyclerView.smoothScrollBy(0, top);
        } else {
            //要置顶的项在当前显示的最后一项之后
            mShouldScroll = true;
            mRecyclerView.smoothScrollToPosition(nextShotPosition);
        }
    }

    class RcvScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (mShouldScroll && newState == RecyclerView.SCROLL_STATE_IDLE) {
                mShouldScroll = false;
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                int n = nextPosition - linearLayoutManager.findFirstVisibleItemPosition();
                if (n >= 0 && n < mRecyclerView.getChildCount()) {
                    //获取要置顶的项顶部距离RecyclerView顶部的距离
                    int top = mRecyclerView.getChildAt(n).getTop();
                    //进行第二次滚动（最后的距离）
                    mRecyclerView.smoothScrollBy(0, top);
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIndexBar.release();
    }
}