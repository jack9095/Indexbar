package com.fei.indexbar.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fei.indexbar.R;
import com.fei.indexbar.model.IndexBean;

import java.util.List;

public class IndexBarAdapter extends RecyclerView.Adapter<IndexBarAdapter.IndexBarHolder> {
    private static final int DEFAULT_TEXT_SPACE = 8;

    private static final int DEFAULT_ICON_SPACE = 16;

    private static final float SPINNER_PERCENT_TRANSPARENT = 0.3f;

    private static final float SPINNER_OPAQUE = 1.0f;

    private List<IndexBean> lists;

//        public IndexBarAdapter(List<String> lists) {
//            this.lists = lists;
//        }

    public void setData(List<IndexBean> lists) {
        this.lists = lists;
        notifyDataSetChanged();
    }

    public List<IndexBean> getData() {
        return lists;
    }

    @NonNull
    @Override
    public IndexBarAdapter.IndexBarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IndexBarAdapter.IndexBarHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.u_index_bar_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull IndexBarAdapter.IndexBarHolder holder, int position) {
        if (lists != null && lists.size() > position && position >= 0) {
            String str = lists.get(position).getLetter();
            if (lists.get(position).isSelect()) {
                holder.mLinearLayout.setBackgroundResource(R.drawable.background_shape);
            } else {
                holder.mLinearLayout.setBackground(null);
            }
            if (!TextUtils.isEmpty(str)) {
                holder.mTitleTextView.setText(str);
                holder.itemView.setOnClickListener(v -> {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onClick(v, str, position);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return lists != null ? lists.size() : 0;
    }

    static class IndexBarHolder extends RecyclerView.ViewHolder {
        public TextView mTitleTextView;
        public LinearLayout mLinearLayout;

        public IndexBarHolder(@NonNull View itemView) {
            super(itemView);
            mTitleTextView = itemView.findViewById(R.id.text_view);
            mLinearLayout = itemView.findViewById(R.id.root_view);
        }
    }

    public interface OnItemClickListener {
        void onClick(View view, String str, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
