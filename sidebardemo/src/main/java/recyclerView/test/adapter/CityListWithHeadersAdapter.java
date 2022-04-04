package recyclerView.test.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fei.sidebardemo.R;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import recyclerView.stickyheadrecycler.StickyRecyclerHeadersAdapter;


/**
 * 头部粘贴的适配器，这里是 26 个英文字母
 */
public class CityListWithHeadersAdapter extends CityListAdapter<RecyclerView.ViewHolder>
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    public List<Integer> getHeadPosition() {
        return headPosition;
    }

    private List<Integer> headPosition = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.u_view_item, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        textView.setText(getItem(position).getCityName());
    }

    @Override
    public long getHeaderId(int position) {
        return getItem(position).getFirstLetter().charAt(0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.u_view_header, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        headPosition.add(position);
        Log.e("fei.wang", "adapter -> " + position);
        TextView textView = (TextView) holder.itemView;
        textView.setText(String.valueOf(getItem(position).getFirstLetter()));
        holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
//        holder.itemView.setBackgroundColor(getRandomColor());
    }

    private int getRandomColor() {
        SecureRandom rgen = new SecureRandom();
        return Color.HSVToColor(150, new float[]{
                rgen.nextInt(359), 1, 1
        });
    }

}
