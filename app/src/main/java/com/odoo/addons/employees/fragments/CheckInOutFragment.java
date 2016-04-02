package com.odoo.addons.employees.fragments;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.odoo.OdooActivity;
import com.odoo.R;
import com.odoo.addons.employees.MainActivity;
import com.odoo.addons.employees.utils.Prefs;
import com.odoo.addons.employees.utils.Utils;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckInOutFragment extends Fragment {

    public static final String TAG = CheckInOutFragment.class.getSimpleName();

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.etSelectDate)
    AppCompatEditText mSelectDate;

    MainActivity mActivity;
    Prefs mPrefs;

    public CheckInOutFragment() {
        // Required empty public constructor
    }

    public static CheckInOutFragment newInstance() {
        return new CheckInOutFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
        mPrefs = new Prefs(mActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_in_out, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setupViews();
    }

    private void setupViews() {
        mActivity.setSupportActionBar(mToolbar);
        mActivity.setTitle("Select Method");
        Utils.setWidgetTint((ViewGroup) getView(), getContext());
        mSelectDate.setText(Utils.getCurrentDate());
    }

    @OnClick(R.id.bnSignIn)
    public void onSignIn() {
        mPrefs.setSignIn(true);
        startOdooActivity();
    }

    @OnClick(R.id.bnSignOut)
    public void onSignOut() {
        mPrefs.setSignIn(false);
        startOdooActivity();
    }

    @OnClick(R.id.etSelectDate)
    public void onSelectDate() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = year + "-"
                        + (monthOfYear < 10 ? ("0" + monthOfYear) : ("" + monthOfYear)) + "-"
                        + (dayOfMonth < 10 ? ("0" + dayOfMonth) : ("" + dayOfMonth));
                mSelectDate.setText(date);
            }
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void startOdooActivity() {
        mActivity.startActivity(new Intent(mActivity, OdooActivity.class));
    }

}
