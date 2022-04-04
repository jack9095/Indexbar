package stick.head.recycler.test;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fei.indexbar.R;

import stick.head.recycler.stick.StickHeaderDecoration;
import stick.head.recycler.test.adapter.RecyclerAdapter;

public class StickHeaderActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_header_a);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new StickHeaderDecoration(new StickHeaderDecoration.SectionCallback() {
            @Override
            public boolean isFirstItem(int position) {
                return position == 0 || position == 4 || position == 7;
            }

            @Override
            public int getGroupId(int position) {
                if (position < 4) {
                    return 0;
                } else if (position < 7) {
                    return 1;
                } else {
                    return 2;
                }
            }

            @Override
            public String getTitle(int position) {
                return RecyclerAdapter.groups.get(getGroupId(position));
            }
        }));
        recyclerView.setAdapter(new RecyclerAdapter(this));
    }
}
