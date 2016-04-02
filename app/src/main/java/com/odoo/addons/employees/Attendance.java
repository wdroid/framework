package com.odoo.addons.employees;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.odoo.R;
import com.odoo.addons.employees.models.AttReport;
import com.odoo.addons.employees.models.HrAttendance;
import com.odoo.core.orm.ODataRow;
import com.odoo.core.support.addons.fragment.BaseFragment;
import com.odoo.core.support.addons.fragment.ISyncStatusObserverListener;
import com.odoo.core.support.drawer.ODrawerItem;
import com.odoo.core.support.list.OCursorListAdapter;
import com.odoo.core.utils.BitmapUtils;
import com.odoo.core.utils.OControls;

import java.util.ArrayList;
import java.util.List;

public class Attendance extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>,
        ISyncStatusObserverListener, SwipeRefreshLayout.OnRefreshListener,
        OCursorListAdapter.OnViewBindListener {

    private static final String TAG = Attendance.class.getSimpleName();

    private View mView;
    private ListView listView;
    private OCursorListAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.common_listview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mView = view;
        listView = (ListView) mView.findViewById(R.id.listview);
        listAdapter = new OCursorListAdapter(getActivity(), null, R.layout.customer_row_item);
        listView.setAdapter(listAdapter);
        listAdapter.setOnViewBindListener(this);
        setHasSyncStatusObserver(TAG, this, db());
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public List<ODrawerItem> drawerMenus(Context context) {
        List<ODrawerItem> menu = new ArrayList<>();
        menu.add(new ODrawerItem(TAG)
                .setTitle(context.getString(R.string.sync_label_hr_attendance))
                .setIcon(R.drawable.ic_action_customers)
                .setInstance(new Attendance()));
        return menu;
    }

    @Override
    public Class<HrAttendance> database() {
        return HrAttendance.class;
    }

    @Override
    public void onStatusChange(Boolean refreshing) {
        if (refreshing) {
            getLoaderManager().restartLoader(0, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), db().uri(), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        listAdapter.changeCursor(data);
        if (data.getCount() > 0) {
            OControls.setGone(mView, R.id.loadingProgress);
            OControls.setVisible(mView, R.id.swipe_container);
            OControls.setGone(mView, R.id.customer_no_items);
            setHasSwipeRefreshView(mView, R.id.swipe_container, this);
        } else {
            OControls.setGone(mView, R.id.loadingProgress);
            OControls.setVisible(mView, R.id.swipe_container);
            OControls.setVisible(mView, R.id.customer_no_items);
            setHasSwipeRefreshView(mView, R.id.customer_no_items, this);
            OControls.setImage(mView, R.id.icon, R.drawable.ic_action_customers);
            OControls.setText(mView, R.id.title, "No Tasks found");
            OControls.setText(mView, R.id.subTitle, "Swipe to check new tasks");
        }

        if (db().isEmptyTable()) {
            // Request for sync
            onRefresh();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        listAdapter.changeCursor(null);
    }

    @Override
    public void onRefresh() {
        if (inNetwork()) {
            parent().sync().requestSync(AttReport.AUTHORITY);
        }
    }

    @Override
    public void onViewBind(View view, Cursor cursor, ODataRow row) {
        Bitmap img;
        if (row.getString("image_small").equals("false")) {
            img = BitmapUtils.getAlphabetImage(getContext(), row.getString("name"));
        } else {
            img = BitmapUtils.getBitmapImage(getContext(), row.getString("image_small"));
        }
        Log.e(TAG, "onViewBind" + row.toString());
        OControls.setImage(view, R.id.image_small, img);
        OControls.setText(view, R.id.name, row.getString("lat") + ":" + row.getString("lng"));
        OControls.setText(view, R.id.company_name, row.getString("employee_id"));
        OControls.setText(view, R.id.email, row.getString("employee_ids"));
    }
}
