package com.odoo.addons.employees.models;

import android.content.Context;
import android.net.Uri;

import com.odoo.App;
import com.odoo.core.orm.OModel;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.orm.fields.types.OFloat;
import com.odoo.core.orm.fields.types.OVarchar;
import com.odoo.core.support.OUser;

public class SchoolSchool extends OModel {

    public static final String TAG = SchoolSchool.class.getSimpleName();
    public static final String AUTHORITY = App.APPLICATION_ID + ".provider.content.sync.school_school";

    OColumn name = new OColumn("Name", OVarchar.class).setSize(100);
    OColumn lat = new OColumn("Latitude", OFloat.class);
    OColumn lng = new OColumn("Longitude", OFloat.class);

    public SchoolSchool(Context context, OUser user) {
        super(context, "school.school", user);
    }

    @Override
    public Uri uri() {
        return buildURI(AUTHORITY);
    }
}
