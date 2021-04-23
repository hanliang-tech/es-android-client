package com.hili.mp.demo.client;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hili.sdk.mp.client.MiniRemoteDevice;

import java.util.List;

/**
 * Create by WeiPeng on 2020/12/12 16:04
 */
public class RefreshDeviceComponent extends SwipeRefreshLayout {

    private FRecyclerViewWrap<MiniRemoteDevice> mRecyclerViewWrap;
    private OnDeviceItemClickListener mClickListener;

    public RefreshDeviceComponent(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        RecyclerView rv = new RecyclerView(context);
        addView(rv);

        if (mRecyclerViewWrap == null) {
            mRecyclerViewWrap = new FRecyclerViewWrap<MiniRemoteDevice>(rv) {
                @Override
                protected void onItemDecoration(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.top = 30;
                }

                @Override
                protected View createItemView(ViewGroup parent, int viewType) {
                    return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_name, parent, false);
                }

                @Override
                protected void bindItemView(FViewHolder holder, int position, int viewType, MiniRemoteDevice device) {
                    TextView tv = holder.itemView.findViewById(R.id.tv_device_name);
                    tv.setText(device.getDeviceName());
                }

                @Override
                protected void onItemClicked(FViewHolder holder, int position, MiniRemoteDevice device) {
                    mClickListener.onDeviceClick(device);
                }
            };
        }
    }

    public void setOnItemClickListener(OnDeviceItemClickListener listener) {
        mClickListener = listener;
    }

    public void setData(List<MiniRemoteDevice> devices) {
        mRecyclerViewWrap.setData(devices);
    }

    public boolean hasDevice() {
        return mRecyclerViewWrap != null && mRecyclerViewWrap.getItemCount() > 0;
    }

    public void startRefreshing() {
        setRefreshing(true);
    }

    public void endRefreshing() {
        setRefreshing(false);
    }

    public interface OnDeviceItemClickListener {
        void onDeviceClick(MiniRemoteDevice device);
    }
}
