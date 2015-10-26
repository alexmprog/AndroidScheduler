package com.instinctools.scheduler.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.instinctools.scheduler.R;
import com.instinctools.scheduler.adapter.SyncInfoAdapter;
import com.instinctools.scheduler.database.AlarmSyncInfo;
import com.instinctools.scheduler.database.DatabaseModule;
import com.instinctools.scheduler.database.GCMSyncInfo;
import com.instinctools.scheduler.database.SyncInfo;
import com.instinctools.scheduler.service.SyncService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandr Golovach on 23.10.2015.
 */
public class InfoActivity extends AppCompatActivity {

    public static final String ARG_TYPE = "ARG_TYPE";

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        Intent intent = getIntent();
        if (intent != null) {
            SyncService.SyncType syncType = (SyncService.SyncType) intent.getSerializableExtra(ARG_TYPE);
            List<SyncInfo> syncInfoList = new ArrayList<>();
            if (syncType == SyncService.SyncType.ALARM) {
                List<AlarmSyncInfo> infoList = DatabaseModule.getAlarmSyncTableInfo();
                for (AlarmSyncInfo info : infoList) {
                    syncInfoList.add(info);
                }
                setTitle(syncType.getType());
            } else if (syncType == SyncService.SyncType.GCM) {
                List<GCMSyncInfo> infoList = DatabaseModule.getGCMSyncTableInfo();
                for (GCMSyncInfo info : infoList) {
                    syncInfoList.add(info);
                }
                setTitle(syncType.getType());
            }

            SyncInfoAdapter adapter = new SyncInfoAdapter(syncInfoList);
            mRecyclerView.setAdapter(adapter);
        }
    }
}
