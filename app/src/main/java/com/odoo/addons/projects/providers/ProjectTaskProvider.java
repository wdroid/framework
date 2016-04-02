package com.odoo.addons.projects.providers;

import com.odoo.addons.projects.models.ProjectTask;
import com.odoo.core.orm.provider.BaseModelProvider;

/**
 * Created by wangcl on 16/4/2.
 */
public class ProjectTaskProvider extends BaseModelProvider {
    public static final String TAG = ProjectTaskProvider.class.getSimpleName();

    @Override
    public String authority() {
        return ProjectTask.AUTHORITY;
    }
}
