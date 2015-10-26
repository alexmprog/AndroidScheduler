package com.instinctools.scheduler.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.instinctools.scheduler.R;
import com.instinctools.scheduler.database.SyncInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Alexandr Golovach on 23.10.2015.
 */
public class SyncInfoAdapter extends RecyclerView.Adapter<SyncInfoAdapter.ViewHolder> {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
    private static final long ONE_MINUTE = 1000 * 60;

    private List<SyncInfo> mSyncInfoList;

    public SyncInfoAdapter(List<SyncInfo> syncInfoList) {
        this.mSyncInfoList = syncInfoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sync_info_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SyncInfo info = mSyncInfoList.get(position);
        if (info != null) {
            holder.mSyncTime.setText(DATE_FORMAT.format(info.getCurrentTime()));
            holder.mPrevSyncTime.setText(getTimeString(mSyncInfoList.get(position).getTimeSincePreviousSync()));
            holder.mDeviceInfo.setText(mSyncInfoList.get(position).getDeviceInfo());
        }
    }

    @Override
    public int getItemCount() {
        return mSyncInfoList.size();
    }

    private String getTimeString(long time) {
        StringBuilder builder = new StringBuilder();
        int minutes = (int) (time / ONE_MINUTE);
        builder.append(minutes).append(" minutes");
        return builder.toString();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mSyncTime;
        public TextView mPrevSyncTime;
        public TextView mDeviceInfo;

        public ViewHolder(View itemView) {
            super(itemView);
            mSyncTime = (TextView) itemView.findViewById(R.id.sync_start_time);
            mPrevSyncTime = (TextView) itemView.findViewById(R.id.sync_time_info);
            mDeviceInfo = (TextView) itemView.findViewById(R.id.sync_status_info);
        }
    }
}


