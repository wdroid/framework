package com.odoo.addons.employees.models;

import android.content.Context;
import android.net.Uri;

import com.odoo.App;
import com.odoo.base.addons.res.ResUsers;
import com.odoo.core.orm.OModel;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.orm.fields.types.OVarchar;
import com.odoo.core.support.OUser;

public class HrEmployee extends OModel {

    public static final String TAG = HrEmployee.class.getSimpleName();
    public static final String AUTHORITY = App.APPLICATION_ID + ".provider.content.sync.hr_employee";

    OColumn name = new OColumn("Name", OVarchar.class).setSize(100);
    OColumn institution_id = new OColumn("Instutation", SchoolSchool.class, OColumn.RelationType.ManyToOne);
    OColumn user_id = new OColumn("Related User", ResUsers.class, OColumn.RelationType.ManyToOne);

    public HrEmployee(Context context, OUser user) {
        super(context, "hr.employee", user);
    }

    @Override
    public Uri uri() {
        return buildURI(AUTHORITY);
    }
}
