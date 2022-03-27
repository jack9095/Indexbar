package com.fei.indexbar.wave.commonadapter;

import android.util.SparseArray;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * author: imilk
 * https://github.com/Solartisan/TurboRecyclerViewHelper
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    /**
     * Views indexed with their IDs
     */
    private final SparseArray<View> mViews;


    public BaseViewHolder(View view) {
        super(view);
        this.mViews = new SparseArray<>();
    }

    @SuppressWarnings("unchecked")
    public  <V extends View> V findViewById(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (V) view;
    }
}
