package com.fei.indexbar;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fei.indexbar.model.IndexBean;

import java.util.List;

/**
 * 安卓字母导航view 左边显示对应的悬浮窗口
 */
public class IndexBarTipsView extends RelativeLayout {
    private TextView mTextView;
    private RecyclerView mRecyclerView;
    private TipsViewAdapter mTipsViewAdapter;
    private OnTouchListener mOnTouchListener;

    public IndexBarTipsView(Context context) {
        this(context, null);
    }

    public IndexBarTipsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexBarTipsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.u_floating_window, this, true);
        mRecyclerView = findViewById(R.id.rv);
        mTextView = findViewById(R.id.textview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mTipsViewAdapter = new TipsViewAdapter();
        mTipsViewAdapter.setOnItemClickListener(new TipsViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, IndexBean bean, int position) {
                if (mOnTouchListener != null) {
                    mOnTouchListener.onClick(view, bean, position, true);
                }
            }
        });
        mTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnTouchListener != null && !TextUtils.isEmpty(mTextView.getText())) {
                    mOnTouchListener.onClick(view, new IndexBean(mTextView.getText().toString()), 0, false);
                }
            }
        });
        mRecyclerView.setAdapter(mTipsViewAdapter);
    }

    public void setData(List<IndexBean> lists) {
        mTipsViewAdapter.setData(lists);
    }

    /**
     * 关键代码，可以让悬浮窗随着滑动的字母移动
     *
     * @param text
     */
    public void setText(String text, int poistion, float y) {
        mTextView.setText(text);
    }

    static class TipsViewAdapter extends RecyclerView.Adapter<TipsViewAdapter.TipsViewAdapterHolder> {
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
        public TipsViewAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TipsViewAdapterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.u_tips_item_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull TipsViewAdapterHolder holder, int position) {
            if (lists != null && lists.size() > position && position >= 0) {
                String str = lists.get(position).getNameFirst();
                if (lists.get(position).isSelect()) {
                    holder.mTextView.setBackgroundResource(R.drawable.background_shape);
                } else {
                    holder.mTextView.setBackground(null);
                }
                if (!TextUtils.isEmpty(str)) {
                    holder.mTextView.setText(str);
                    holder.itemView.setOnClickListener(v -> {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onClick(v, lists.get(position), position);
                        }
                    });
                }
            }
        }

        @Override
        public int getItemCount() {
            return lists != null ? lists.size() : 0;
        }

        static class TipsViewAdapterHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;

            public TipsViewAdapterHolder(@NonNull View itemView) {
                super(itemView);
                mTextView = itemView.findViewById(R.id.tv);
            }
        }

        public interface OnItemClickListener {
            void onClick(View view, IndexBean bean, int position);
        }

        private OnItemClickListener mOnItemClickListener;

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.mOnItemClickListener = listener;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {
            if (mOnTouchListener != null) {
                mOnTouchListener.onDispatchTouch(false);
            }
        } else {
            //如果是cancel也要调用onLetterUpListener 通知
            if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                if (mOnTouchListener != null) {
                    mOnTouchListener.onDispatchTouch(false);
                }
            } else if (event.getAction() == MotionEvent.ACTION_DOWN) { //按下调用 OnTouchListener
                if (mOnTouchListener != null) {
                    mOnTouchListener.onDispatchTouch(true);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    public void setOnTouchListener(OnTouchListener listener) {
        this.mOnTouchListener = listener;
    }

    public interface OnTouchListener {
        void onDispatchTouch(boolean touching);

        void onClick(View view, IndexBean bean, int position, boolean isLetter);
    }

}
