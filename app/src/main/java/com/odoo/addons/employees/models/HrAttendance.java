package com.odoo.addons.employees.models;

import android.content.Context;

import com.odoo.App;
import com.odoo.core.orm.OModel;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.orm.fields.types.ODateTime;
import com.odoo.core.orm.fields.types.OSelection;
import com.odoo.core.support.OUser;

public class HrAttendance extends OModel {

    public static final String TAG = HrAttendance.class.getSimpleName();
    public static final String AUTHORITY = App.APPLICATION_ID + ".provider.content.sync.hr_attendance";

    OColumn employee_id = new OColumn("Name", HrEmployee.class, OColumn.RelationType.ManyToOne);
    OColumn name = new OColumn("Date", ODateTime.class);
    OColumn state = new OColumn("State", OSelection.class)
            .addSelection("sign_in", "Sign In")
            .addSelection("sign_out", "Sign Out");
    OColumn attendance_report_id = new OColumn("Name", AttReport.class,
            OColumn.RelationType.ManyToOne);

    public HrAttendance(Context context, OUser user) {
        super(context, "hr.attendance", user);
    }

}
