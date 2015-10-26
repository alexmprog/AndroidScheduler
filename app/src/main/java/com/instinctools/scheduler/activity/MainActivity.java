package com.instinctools.scheduler.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.instinctools.scheduler.R;
import com.instinctools.scheduler.prefs.Prefs;
import com.instinctools.scheduler.service.AutoStart;
import com.instinctools.scheduler.service.GCMHelper;
import com.instinctools.scheduler.service.SyncService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mStartAlarmButton;
    private Button mAlarmInfoButton;
    private Button mStartGCMButton;
    private Button mGCMInfoButton;
    private Button mStopButton;
    private TextView mInfoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInfoView = (TextView) findViewById(R.id.sync_info);

        mStartAlarmButton = (Button) findViewById(R.id.start_alarm_button);
        mStartAlarmButton.setOnClickListener(this);

        mAlarmInfoButton = (Button) findViewById(R.id.info_alarm_button);
        mAlarmInfoButton.setOnClickListener(this);

        mStartGCMButton = (Button) findViewById(R.id.start_gcm_button);
        mStartGCMButton.setOnClickListener(this);

        mGCMInfoButton = (Button) findViewById(R.id.info_gcm_button);
        mGCMInfoButton.setOnClickListener(this);

        mStopButton = (Button) findViewById(R.id.stop_button);
        mStopButton.setOnClickListener(this);

        updateSyncInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_alarm_button:
                validateSync(SyncService.SyncType.ALARM);
                break;
            case R.id.start_gcm_button:
                validateSync(SyncService.SyncType.GCM);
                break;
            case R.id.stop_button:
                stopSync();
                break;
            case R.id.info_alarm_button:
                Intent alarmIntent = new Intent(MainActivity.this, InfoActivity.class);
                alarmIntent.putExtra(InfoActivity.ARG_TYPE, SyncService.SyncType.ALARM);
                startActivity(alarmIntent);
                break;
            case R.id.info_gcm_button:
                Intent gcmIntent = new Intent(MainActivity.this, InfoActivity.class);
                gcmIntent.putExtra(InfoActivity.ARG_TYPE, SyncService.SyncType.GCM);
                startActivity(gcmIntent);
                break;
        }
    }

    private void validateSync(SyncService.SyncType syncType) {
        final String value = Prefs.getStringValue(this, Prefs.ACTIVE_SYNC_APPROACH);
        if (!TextUtils.isEmpty(value)) {
            SyncService.SyncType type = SyncService.SyncType.getSyncType(value);
            if (type == null) {
                startSync(type);
            } else {
                Toast.makeText(this, getString(R.string.started_text), Toast.LENGTH_SHORT).show();
            }
        } else {
            startSync(syncType);
        }
        updateSyncInfo();
    }

    private void startSync(SyncService.SyncType syncType) {
        switch (syncType) {
            case ALARM:
                AutoStart.startSyncSchedule(this);
                Toast.makeText(this, getString(R.string.alarm_started_text), Toast.LENGTH_SHORT).show();
                break;
            case GCM:
                GCMHelper.startSyncSchedule(this);
                Toast.makeText(this, getString(R.string.gcm_started_text), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void stopSync() {
        final String value = Prefs.getStringValue(this, Prefs.ACTIVE_SYNC_APPROACH);
        if (!TextUtils.isEmpty(value)) {
            SyncService.SyncType type = SyncService.SyncType.getSyncType(value);
            switch (type) {
                case ALARM:
                    AutoStart.stopSyncSchedule(this);
                    Toast.makeText(this, getString(R.string.alarm_stopped_text), Toast.LENGTH_SHORT).show();
                    break;
                case GCM:
                    GCMHelper.stopSyncSchedule(this);
                    Toast.makeText(this, getString(R.string.gcm_stopped_text), Toast.LENGTH_SHORT).show();
                    break;
            }
        } else {
            Toast.makeText(this, getString(R.string.not_started_text), Toast.LENGTH_SHORT).show();
        }
        updateSyncInfo();
    }

    private void updateSyncInfo() {
        final String value = Prefs.getStringValue(this, Prefs.ACTIVE_SYNC_APPROACH);
        if (!TextUtils.isEmpty(value)) {
            SyncService.SyncType type = SyncService.SyncType.getSyncType(value);
            if (type != null) {
                mInfoView.setText(type.getType());
            } else {
                mInfoView.setText("Not selected");
            }
        } else {
            mInfoView.setText("Not selected");
        }
    }
}
