package com.odoo.addons.employees.providers;

import com.odoo.addons.employees.models.SchoolSchool;
import com.odoo.core.orm.provider.BaseModelProvider;

public class SchoolSchoolProvider extends BaseModelProvider {

    public static final String TAG = SchoolSchoolProvider.class.getSimpleName();

    @Override
    public String authority() {
        return SchoolSchool.AUTHORITY;
    }
}
