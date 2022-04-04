package com.fei.sidebardemo.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fei.sidebardemo.R;
import com.fei.sidebardemo.model.IndexBean;

import java.util.List;


public class IndexBarAdapter extends RecyclerView.Adapter<IndexBarAdapter.IndexBarHolder> {
    private List<IndexBean> lists;

    public void setData(List<IndexBean> lists) {
        this.lists = lists;
        notifyDataSetChanged();
    }

    public List<IndexBean> getData() {
        return lists;
    }

    @NonNull
    @Override
    public IndexBarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IndexBarHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.u_index_bar_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull IndexBarHolder holder, int position) {
        if (lists != null && lists.size() > position && position >= 0) {
            String str = lists.get(position).getLetter();
            if (lists.get(position).isSelect()) {
                holder.mTextView.setBackgroundResource(R.drawable.u_index_bar_text_background);
            } else {
                holder.mTextView.setBackground(null);
            }
            if (!TextUtils.isEmpty(str)) {
                holder.mTextView.setText(str);
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
        public TextView mTextView;
        public LinearLayout mLinearLayout;

        public IndexBarHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.text_view);
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
