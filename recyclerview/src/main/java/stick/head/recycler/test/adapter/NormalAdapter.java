package stick.head.recycler.test.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fei.indexbar.R;

import java.util.List;

import stick.head.recycler.stick.StickHeaderDecoration1;
import stick.head.recycler.test.model.City;

/**
 * Created by sober_philer on 2017/6/9.
 */

public class NormalAdapter extends RecyclerView.Adapter<NormalAdapter.InnerHolder> implements StickHeaderDecoration1.StickHeaderInterface {

    // ***************** 新增开始 start
    public NormalAdapter(Activity activity, List<City> dates) {
        setHasStableIds(true);
        this.activity = activity;
        this.items = dates;
    }

    //必须重写  不然item会错乱
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 绘制在粘贴头部的内容的 position 位置
    @Override
    public boolean isStick(int position) {
        return position % 6 == 0;
//        return return getItem(position).getFirstLetter().charAt(0);;
    }

    public City getItem(int position) {
        return items.get(position);
    }

    // ******************* 新增结束 end

    Activity activity;
//    private List<String> dates;
    private List<City> items;

    @Override
    public InnerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.item_a, parent,
                false);
        return new InnerHolder(inflate);
    }

    @Override
    public void onBindViewHolder(InnerHolder holder, int position) {
        if (isStick(position)) {
            holder.itemView.setBackgroundResource(R.color.white);
            holder.tvText.setText(items.get(position).getFirstLetter()); // TODO 显示粘贴在头部的内容
        } else {
            holder.itemView.setBackgroundResource(R.color.white);
            holder.tvText.setText(items.get(position).getCityName());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class InnerHolder extends RecyclerView.ViewHolder {
        TextView tvText;

        public InnerHolder(View itemView) {
            super(itemView);
            tvText = (TextView) itemView.findViewById(R.id.tvText);
        }
    }
}