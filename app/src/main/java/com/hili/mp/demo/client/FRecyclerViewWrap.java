package com.hili.mp.demo.client;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by WeiPeng on 2020/08/11 18:22
 */
public abstract class FRecyclerViewWrap<DATA> extends RecyclerView.Adapter<FRecyclerViewWrap.FViewHolder> implements View.OnClickListener, View.OnFocusChangeListener {

    public static final int TYPE_EMPTY = 1;
    public static final int TYPE_NORMAL = 2;

    private final ArrayList<DATA> mData;

    public FRecyclerViewWrap(RecyclerView rv) {
        if (rv.getLayoutManager() == null) {
            rv.setLayoutManager(new LinearLayoutManager(rv.getContext(), LinearLayoutManager.VERTICAL, false));
        }
        rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                onItemDecoration(outRect, view, parent, state);
            }
        });
        mData = new ArrayList<>(0);
        rv.setAdapter(this);
    }

    public void setData(List<DATA> data) {
        mData.clear();
        addData(data);
    }

    public void addData(List<DATA> data) {
        if (data != null) mData.addAll(data);
        notifyDataSetChanged();
    }

    private boolean isEmpty() {
        return mData.size() == 0;
    }

    @Override
    public int getItemCount() {
        if (useEmptyView() && isEmpty()) return 1;
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isEmpty() && useEmptyView()) return TYPE_EMPTY;
        return TYPE_NORMAL;
    }

    @Override
    public FViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FViewHolder(createItemView(parent, viewType));
    }

    @Override
    public void onBindViewHolder(FViewHolder holder, int position) {
        if(isEmpty()) return;
        setListeners(holder);
        bindItemView(holder, position, getItemViewType(position), mData.get(position));
    }

    private void setListeners(FViewHolder holder) {
        holder.itemView.setTag(R.id.id_view_tag, holder);
        if (holder.itemView.isClickable()) {
            holder.itemView.setOnClickListener(this);
        }
        if (holder.itemView.isFocusable()) {
            holder.itemView.setOnFocusChangeListener(this);
        }
    }

    protected abstract View createItemView(ViewGroup parent, int viewType);

    protected abstract void bindItemView(FViewHolder holder, int position, int viewType, DATA data);

    protected boolean useEmptyView() {
        return false;
    }

    protected void onItemDecoration(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){}

    protected void onItemClicked(FViewHolder holder, int position, DATA data) {
    }

    protected void onItemFocusChanged(FViewHolder holder, int position, boolean hasFocus, DATA data) {
    }

    @Override
    public void onClick(View v) {
        FViewHolder holder = (FViewHolder) v.getTag(R.id.id_view_tag);
        int position = holder.getLayoutPosition();
        onItemClicked(holder, position, mData.get(position));
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        FViewHolder holder = (FViewHolder) v.getTag(R.id.id_view_tag);
        int position = holder.getLayoutPosition();
        onItemFocusChanged(holder, position, hasFocus, mData.get(position));
    }

  public static final class FViewHolder extends RecyclerView.ViewHolder {
        public FViewHolder(View itemView) {
            super(itemView);
        }

        public <V_TYPE extends View> V_TYPE getView(int viewId) {
            return itemView.findViewById(viewId);
        }

        public void setText(CharSequence text) {
            ((TextView) itemView).setText(text);
        }

        public void setText(int viewId, CharSequence text) {
            ((TextView) getView(viewId)).setText(text);
        }
    }
}
