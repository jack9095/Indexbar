package com.fei.indexbar.copy;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fei.indexbar.OnRecyclerItemClickListener;
import com.fei.indexbar.R;
import com.fei.indexbar.util.SpellingUtils;
import com.fei.indexbar.adapter.MainIndexBarFocusAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivityCopy extends AppCompatActivity implements MyRecyclerViewCopy.OnTouchListener {
    private List<String> focusList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_copy);
        initData();
        initViews();
    }

    @Override
    public void onChanged(String letter, int position, float y) {

    }

    @Override
    public void onTouching(boolean touching) {

    }

    private void initViews() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final IndexBarCopy focusSideBar = findViewById(R.id.index_bar);
        focusSideBar.setOnTouchListener(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new MainIndexBarFocusAdapter(focusList, R.layout.main_list_item_adapter));

        recyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerView) {

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }

            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                Log.e("fei.wang", "点击事件");
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {

            }
        });

        final List<String> enNameList = new ArrayList<>();
        for (String value : focusList) {
            String key = SpellingUtils.getFirstLetter(value.substring(0, 1));
            key = key.startsWith("#") ? "#" : key;
            if (!enNameList.contains(key)) {
                enNameList.add(key);
            }
        }
//        focusSideBar.setIndexItems(enNameList.toArray(new String[enNameList.size()]));
//        focusSideBar.setOnSelectIndexItemListener(new FocusSideBar.OnSelectIndexItemListener() {
//            @Override
//            public void onSelectIndexItem(String index) {
//                for (int i = 0; i < focusList.size(); i++) {
//                    String key = SpellingUtils.getFirstLetter(focusList.get(i).substring(0, 1));
//                    if (("#".equals(index) && key.startsWith("#")) || TextUtils.equals(key, index)) {
//                        layoutManager.scrollToPositionWithOffset(i, 0);
//                        return;
//                    }
//                }
//            }
//        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                final int position = layoutManager.findFirstCompletelyVisibleItemPosition();
                String key = SpellingUtils.getFirstLetter(focusList.get(position).substring(0, 1));
//                focusSideBar.setCurrentIndex(enNameList.indexOf(key));
            }
        });
    }

    private void initData() {
        focusList = new ArrayList<>();
        focusList.add("北京");
        focusList.add("天津");
        focusList.add("河北");
        focusList.add("石家庄");
        focusList.add("保定");
        focusList.add("廊坊");
        focusList.add("张家口");
        focusList.add("山西");
        focusList.add("太原");
        focusList.add("大同");
        focusList.add("忻州");
        focusList.add("运城");
        focusList.add("吕梁");
        focusList.add("晋中");
        focusList.add("内蒙");
        focusList.add("辽宁");
        focusList.add("沈阳");
        focusList.add("大连");
        focusList.add("葫芦岛");
        focusList.add("吉林");
        focusList.add("长春");
        focusList.add("黑龙江");
        focusList.add("哈尔滨");
        focusList.add("齐齐哈尔");
        focusList.add("牡丹江");
        focusList.add("上海");
        focusList.add("江苏");
        focusList.add("苏州");
        focusList.add("南京");
        focusList.add("常州");
        focusList.add("无锡");
        focusList.add("徐州");
        focusList.add("扬州");
        focusList.add("浙江");
        focusList.add("杭州");
        focusList.add("湖州");
        focusList.add("温州");
        focusList.add("宁波");
        focusList.add("台州");
        focusList.add("丽水");
        focusList.add("桐庐");
        focusList.add("金华");
        focusList.add("绍兴");
        focusList.add("嘉兴");
        focusList.add("安徽");
        focusList.add("合肥");
        focusList.add("芜湖");
        focusList.add("福建");
        focusList.add("福洲");
        focusList.add("江西");
        focusList.add("南昌");
        focusList.add("九江");
        focusList.add("宜春");
        focusList.add("赣州");
        focusList.add("山东");
        focusList.add("济南");
        focusList.add("青岛");
        focusList.add("潍坊");
        focusList.add("高密");
        focusList.add("日照");
        focusList.add("河南");
        focusList.add("郑州");
        focusList.add("湖北");
        focusList.add("湖南");
        focusList.add("长沙");
        focusList.add("郴州");
        focusList.add("株洲");
        focusList.add("湘潭");
        focusList.add("岳阳");
        focusList.add("邵阳");
        focusList.add("益阳");
        focusList.add("永州");
        focusList.add("吉首");
        focusList.add("广东");
        focusList.add("广州");
        focusList.add("东莞");
        focusList.add("深圳");
        focusList.add("珠海");
        focusList.add("汕头");
        focusList.add("汕尾");
        focusList.add("潮汕");
        focusList.add("韶关");
        focusList.add("河源");
        focusList.add("梅州");
        focusList.add("惠州");
        focusList.add("广西");
        focusList.add("河池");
        focusList.add("百色");
        focusList.add("柳州");
        focusList.add("南宁");
        focusList.add("西宁");
        focusList.add("北海");
        focusList.add("海南");
        focusList.add("海口");
        focusList.add("三亚");
        focusList.add("馋涎");
        focusList.add("五指山");
        focusList.add("四川");
        focusList.add("成都");
        focusList.add("绵阳");
        focusList.add("广元");
        focusList.add("凉山");
        focusList.add("贵州");
        focusList.add("贵阳");
        focusList.add("凯里");
        focusList.add("云南");
        focusList.add("西藏");
        focusList.add("陕西");
        focusList.add("甘肃");
        focusList.add("青海");
        focusList.add("宁夏");
        focusList.add("新疆");
        focusList.add("台湾");
        focusList.add("香港");
        focusList.add("澳门");
        focusList.add("123");
        focusList.add("*873");
        focusList.add("$&&^");
        focusList = SpellingUtils.stringSort(focusList);
    }
}