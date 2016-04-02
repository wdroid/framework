package com.odoo.addons.employees.models;

import android.content.Context;
import android.net.Uri;

import com.odoo.App;
import com.odoo.core.orm.OModel;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.orm.fields.types.ODate;
import com.odoo.core.orm.fields.types.OFloat;
import com.odoo.core.support.OUser;

public class AttReport extends OModel {

    public static final String TAG = AttReport.class.getSimpleName();
    public static final String AUTHORITY = App.APPLICATION_ID + ".provider.content.sync.att_report";

    OColumn lat = new OColumn("Latitude", OFloat.class);
    OColumn lng = new OColumn("Longitude", OFloat.class);
    OColumn employee_id = new OColumn("Name", HrEmployee.class,
            OColumn.RelationType.ManyToOne);

    OColumn date_reported = new OColumn("Date Reported", ODate.class);
    OColumn date_reporting = new OColumn("Date Reporting", ODate.class);
    OColumn hr_attendance_ids = new OColumn("Order Lines", HrAttendance.class,
            OColumn.RelationType.OneToMany)
            .setRelatedColumn("attendance_report_id");

    public AttReport(Context context, OUser user) {
        super(context, "attendance.report", user);
    }

    @Override
    public Uri uri() {
        return buildURI(AUTHORITY);
    }
}
