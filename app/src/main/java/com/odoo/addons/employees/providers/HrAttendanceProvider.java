package com.odoo.addons.employees.providers;

import com.odoo.addons.employees.models.HrAttendance;
import com.odoo.core.orm.provider.BaseModelProvider;

public class HrAttendanceProvider extends BaseModelProvider {

    public static final String TAG = HrAttendanceProvider.class.getSimpleName();

    @Override
    public String authority() {
        return HrAttendance.AUTHORITY;
    }
}
