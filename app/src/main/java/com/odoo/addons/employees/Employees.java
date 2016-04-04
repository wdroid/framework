package com.odoo.addons.employees;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.odoo.R;
import com.odoo.addons.employees.backend.LocationService;
import com.odoo.addons.employees.models.AttReport;
import com.odoo.addons.employees.models.HrEmployee;
import com.odoo.addons.employees.models.SchoolSchool;
import com.odoo.addons.employees.utils.Utils;
import com.odoo.base.addons.res.ResUsers;
import com.odoo.core.orm.ODataRow;
import com.odoo.core.orm.OValues;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.support.addons.fragment.BaseFragment;
import com.odoo.core.support.addons.fragment.ISyncStatusObserverListener;
import com.odoo.core.support.drawer.ODrawerItem;
import com.odoo.core.support.list.OCursorListAdapter;
import com.odoo.core.utils.BitmapUtils;
import com.odoo.core.utils.OControls;
import com.odoo.core.utils.OCursorUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import me.sudar.zxingorient.ZxingOrient;
import me.sudar.zxingorient.ZxingOrientResult;
import odoo.Odoo;
import odoo.handler.OdooVersionException;
import odoo.helper.OUser;
import odoo.listeners.IOdooConnectionListener;
import odoo.listeners.IOdooLoginCallback;
import odoo.listeners.OdooError;

public class Employees extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>,
        ISyncStatusObserverListener, SwipeRefreshLayout.OnRefreshListener,
        OCursorListAdapter.OnViewBindListener {

    private static final String TAG = Employees.class.getSimpleName();
    AppCompatTextView counter;
    private View mView;
    private ListView listView;
    private OCursorListAdapter listAdapter;
    private Odoo mOdoo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        createOdooInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_employees, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mView = view;
        listView = (ListView) mView.findViewById(R.id.listview);
        listAdapter = new OCursorListAdapter(getActivity(), null, R.layout.item_row_employee);
        listView.setAdapter(listAdapter);
        listAdapter.setOnViewBindListener(this);
        setHasSyncStatusObserver(TAG, this, db());
        getLoaderManager().initLoader(0, null, this);

        hideFab();
        counter = (AppCompatTextView) view.findViewById(R.id.list_count);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                counter.setText(listView.getCheckedItemCount() + "/" + listAdapter.getCount());
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_employees, menu);

        //  for Allen's _id (USER)
        ResUsers resUser = new ResUsers(getActivity(), user());
        List<ODataRow> userIds = resUser.select(new String[]{"_id"},
                "id = ?",
                new String[]{user().getUserId().toString()});

        for (ODataRow userId : userIds) {
            // for Allen's School's _id ()
            List<ODataRow> institutionIds = db().select(new String[]{"institution_id", "_id"},
                    "user_id = ?",
                    new String[]{userId.getString("_id")});

            for (ODataRow institutionId : institutionIds) {
                if (!institutionId.getString("institution_id").equals("false")) {
                    ODataRow usersSchoolRow = institutionId
                            .getM2ORecord("institution_id")
                            .browse();

                    Log.e(TAG, "usersSchoolRow: " + usersSchoolRow.toString());
                    if (usersSchoolRow.getFloat("lat") != 0f && usersSchoolRow.getFloat("lng") != 0f) {
                        MenuItem updateLocation = menu.findItem(R.id.menu_update_location);
                        updateLocation.setVisible(false);
                    }
                }
            }
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_update_location) {
            Utils.getStackTrace(Employees.class);
            Utils.checkLocationSettings(getActivity(), new LocationService.LocationListener() {
                @Override
                public void onLocation(LatLng latLng) {
                    if (latLng != null) {
                        Utils.getStackTrace(Employees.class);
                        //  for Allen's _id (USER)
                        ResUsers resUser = new ResUsers(getActivity(), null);
                        List<ODataRow> userIds = resUser.select(new String[]{"_id"},
                                "id = ?",
                                new String[]{user().getUserId().toString()});

                        for (ODataRow userId : userIds) {
                            // for Allen's School's _id ()
                            List<ODataRow> institutionIds = db().select(new String[]{"institution_id", "_id"},
                                    "user_id = ?",
                                    new String[]{userId.getString("_id")});

                            for (ODataRow institutionId : institutionIds) {
                                if (!institutionId.getString("institution_id").equals("false")) {
                                    ProgressDialog dialog = new ProgressDialog(getContext());
                                    dialog.setMessage("Updating location...");
                                    dialog.setCancelable(false);
                                    dialog.show();

                                    ODataRow usersSchoolRow = institutionId
                                            .getM2ORecord("institution_id")
                                            .browse();

                                    SchoolSchool school = new SchoolSchool(getContext(), user());

                                    OValues values = new OValues();
                                    values.put("id", usersSchoolRow.getInt("id"));
                                    values.put("lat", latLng.latitude);
                                    values.put("lng", latLng.longitude);

                                    school.update("id = ?", new String[]{usersSchoolRow.getInt("id").toString()}, values);
                                    if (inNetwork()) {
                                        parent().sync().requestSync(SchoolSchool.AUTHORITY);
                                    }
                                    item.setVisible(false);
                                    dialog.dismiss();
                                    Snackbar.make(mView, "location updated", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        }
                    } else {
                        Utils.getStackTrace(Employees.class);
                        Log.e(TAG, "NULL Location");
                    }
                }
            });
            return true;
        } else if (id == R.id.menu_report_send) {
            if (listView.getCheckedItemCount() > 0) {
                item.setEnabled(false);
                JSONArray employeeData = new JSONArray();
                final SparseBooleanArray items = listView.getCheckedItemPositions();
                if (items != null) {
                    for (int i = 0; i < items.size(); i++) {
                        final int key = items.keyAt(i);
                        if (items.get(key)) {
                            ODataRow selectedRow = OCursorUtils.toDatarow((Cursor) listAdapter.getItem(key));
                            Bundle data = selectedRow.getPrimaryBundleData();
                            ODataRow selectesRecord = db().browse(data.getInt(OColumn.ROW_ID));
                            employeeData.put(selectesRecord.getInt("_id"));
                        }
                    }
                }

                //  for Allen's or Admin's _id (USER)
                ResUsers resUser = new ResUsers(getActivity(), user());
                List<ODataRow> userIds = resUser.select(new String[]{"_id"},
                        "id = ?",
                        new String[]{user().getUserId().toString()});

                for (ODataRow userId : userIds) {
                    // for Allen's or Admin's School's _id ()
                    List<ODataRow> institutionIds = db().select(new String[]{"_id"},
                            "user_id = ?",
                            new String[]{userId.getString("_id")});

                    for (ODataRow institutionId : institutionIds) {
                        LatLng latLng = LocationService.getLatLng();
                        AttReport report = new AttReport(getContext(), user());
                        OValues values = new OValues();
                        values.put("lat", latLng != null ? String.valueOf(latLng.latitude) : "0");
                        values.put("lng", latLng != null ? String.valueOf(latLng.longitude) : "0");
                        values.put("employee_id", String.valueOf(institutionId.getString("_id")));
                        values.put("employee_absent_ids", employeeData);

                        Log.e(TAG, "Values: " + values.toString());
                        Log.e(TAG, "newID" + report.insert(values));
                        if (inNetwork()) {
                            parent().sync().requestSync(AttReport.AUTHORITY);
                        }

                        listView.clearChoices();
                        listAdapter.notifyDataSetChanged();
                        counter.setText(listView.getCheckedItemCount() + "/" + listAdapter.getCount());
                        Utils.showMessage(getContext(), "Success", "Data Saved Successfully", "OK");
                    }
                }
                item.setEnabled(true);
            } else {
                new QRCodeDialog(getActivity(), R.style.AppDialogTheme);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ZxingOrientResult scanResult = ZxingOrient.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null && scanResult.getContents() != null) {
            Utils.showMessage(getContext(), "Barcode", scanResult.getContents(), "OK");
        }
    }

    @Override
    public List<ODrawerItem> drawerMenus(Context context) {
        List<ODrawerItem> menu = new ArrayList<>();
//        menu.add(new ODrawerItem(TAG).setTitle(context.getString(R.string.teacher)).setGroupTitle());
        menu.add(new ODrawerItem(TAG).setTitle(context.getString(R.string.teacher))
                .setIcon(R.drawable.ic_action_customers)
                .setInstance(new Employees()));
        return menu;
    }

    @Override
    public Class<HrEmployee> database() {
        return HrEmployee.class;
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
            counter.setText(listView.getCheckedItemCount() + "/" + listAdapter.getCount());
            OControls.setVisible(mView, R.id.ll_swipe_container);
            OControls.setGone(mView, R.id.customer_no_items);
            setHasSwipeRefreshView(mView, R.id.swipe_container, this);
        } else {
            OControls.setGone(mView, R.id.loadingProgress);
            OControls.setVisible(mView, R.id.ll_swipe_container);
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
            parent().sync().requestSync(HrEmployee.AUTHORITY);
        }
    }

    @Override
    public void onViewBind(View view, Cursor cursor, ODataRow row) {
        // OControls.setText(view, android.R.id.text1, row.getString("name"));

        Bitmap img;
        if (row.getString("image_small").equals("false")) {
            img = BitmapUtils.getAlphabetImage(getContext(), row.getString("name"));
        } else {
            img = BitmapUtils.getBitmapImage(getContext(), row.getString("image_small"));
        }
        OControls.setImage(view, R.id.image_small, img);
        OControls.setText(view, R.id.name, row.getString("name"));
        OControls.setText(view, R.id.company_name, (row.getString("company_name").equals("false"))
                ? "" : row.getString("company_name"));
        OControls.setText(view, R.id.email, (row.getString("email").equals("false") ? " "
                : row.getString("email")));
    }

    @Override
    public void onStart() {
        super.onStart();
        Utils.checkLocationSettings(getContext());
    }

    private void createOdooInstance() {
        try {
            Odoo.createInstance(getContext(), user().getHost()).setOnConnect(new IOdooConnectionListener() {
                @Override
                public void onConnect(Odoo odoo) {
                    odoo.authenticate(user().getUsername(), user().getPassword(), user().getDatabase(), new IOdooLoginCallback() {
                        @Override
                        public void onLoginSuccess(Odoo odoo, OUser oUser) {
                            mOdoo = odoo;
                        }

                        @Override
                        public void onLoginFail(OdooError odooError) {
                            Utils.showMessage(getContext(), "Login Failed!", odooError.getMessage(),
                                    "Retry", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            getActivity().recreate();
                                        }
                                    },
                                    "Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            System.exit(1);
                                        }
                                    });
                        }
                    });
                }

                @Override
                public void onError(OdooError odooError) {
                    Utils.showMessage(getContext(), "Server Error", odooError.getMessage(),
                            "Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getActivity().recreate();
                                }
                            },
                            "Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    System.exit(1);
                                }
                            });
                }
            });
        } catch (OdooVersionException e) {
            e.printStackTrace();
        }
    }
}
