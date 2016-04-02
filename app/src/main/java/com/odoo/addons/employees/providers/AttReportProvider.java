package com.odoo.addons.employees.providers;

import com.odoo.addons.employees.models.AttReport;
import com.odoo.core.orm.provider.BaseModelProvider;

public class AttReportProvider extends BaseModelProvider {

    public static final String TAG = AttReportProvider.class.getSimpleName();

    @Override
    public String authority() {
        return AttReport.AUTHORITY;
    }
}
