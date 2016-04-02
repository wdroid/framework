package com.odoo.addons.employees.services;

import android.content.Context;
import android.os.Bundle;

import com.odoo.addons.employees.models.SchoolSchool;
import com.odoo.core.service.OSyncAdapter;
import com.odoo.core.service.OSyncService;
import com.odoo.core.support.OUser;

public class SchoolSchoolSyncService extends OSyncService {
    @Override
    public OSyncAdapter getSyncAdapter(OSyncService service, Context context) {
        return new OSyncAdapter(context, SchoolSchool.class, service, true, true);
    }

    @Override
    public void performDataSync(OSyncAdapter adapter, Bundle extras, OUser user) {

    }
}
