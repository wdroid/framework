package com.odoo.addons.projects.models;

import android.content.Context;
import android.net.Uri;

import com.odoo.App;
import com.odoo.core.orm.OModel;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.orm.fields.types.OText;
import com.odoo.core.orm.fields.types.OVarchar;
import com.odoo.core.support.OUser;

/**
 * Created by wangcl on 16/4/2.
 */
public class ProjectTask extends OModel {
    public static final String TAG = ProjectTask.class.getSimpleName();
    public static final String AUTHORITY = App.APPLICATION_ID + ".provider.content.sync.project_task";
    OColumn name = new OColumn("Name", OVarchar.class).setSize(100);
    OColumn project_id = new OColumn("Project", ProjectProject.class, OColumn.RelationType.ManyToOne);
    OColumn description = new OColumn("Description", OText.class);

    public ProjectTask(Context context, OUser user) {
        super(context, "project.task", user);
    }

    @Override
    public Uri uri() {
        return buildURI(AUTHORITY);
    }
}
