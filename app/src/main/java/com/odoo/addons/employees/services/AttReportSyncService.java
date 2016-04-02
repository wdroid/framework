package com.odoo.addons.employees.services;

import android.content.Context;
import android.os.Bundle;

import com.odoo.addons.employees.models.AttReport;
import com.odoo.core.service.OSyncAdapter;
import com.odoo.core.service.OSyncService;
import com.odoo.core.support.OUser;

public class AttReportSyncService extends OSyncService {

    public static final String TAG = AttReportSyncService.class.getSimpleName();

    @Override
    public OSyncAdapter getSyncAdapter(OSyncService service, Context context) {
        return new OSyncAdapter(context, AttReport.class, service, true, true);
    }

    @Override
    public void performDataSync(OSyncAdapter adapter, Bundle extras, OUser user) {

    }
}
