package com.instinctools.scheduler.service;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

/**
 * Created by Alexandr Golovach on 23.10.2015.
 */
public class GCMSyncService extends GcmTaskService {

    static final String GCM_REPEAT_TASK_TAG = "repeat|task";

    @Override
    public void onInitializeTasks() {
        super.onInitializeTasks();
    }

    @Override
    public int onRunTask(TaskParams taskParams) {
        if (taskParams.getTag().equals(GCM_REPEAT_TASK_TAG)) {
            SyncServiceHelper.startSyncService(this, SyncService.SyncType.GCM);
        }
        return GcmNetworkManager.RESULT_SUCCESS;
    }


}
