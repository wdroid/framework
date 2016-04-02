package com.odoo.addons.employees.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.odoo.OdooActivity;
import com.odoo.R;
import com.odoo.addons.employees.MainActivity;
import com.odoo.addons.employees.utils.Prefs;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RoleSelectionFragment extends Fragment {

    public static final String TAG = RoleSelectionFragment.class.getSimpleName();

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.bnPrincipal)
    AppCompatButton mPrincipal;

    @Bind(R.id.bnTeacher)
    AppCompatButton mTeacher;

    @Bind(R.id.bnSchool)
    AppCompatButton mSchool;

    MainActivity mActivity;
    Prefs mPrefs;

    public RoleSelectionFragment() {
        // Required empty public constructor
    }

    public static RoleSelectionFragment newInstance() {
        return new RoleSelectionFragment();
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
        return inflater.inflate(R.layout.fragment_role_selection, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setupViews();
    }

    private void setupViews() {
        mActivity.setSupportActionBar(mToolbar);
        mActivity.setTitle("Select Category");
    }


    @OnClick(R.id.bnTeacher)
    public void onTeacher() {
        mPrefs.setRole(Prefs.Role_Teacher);
        startCheckInOutFragment();
    }

    @OnClick(R.id.bnPrincipal)
    public void onPrincipal() {
        mPrefs.setRole(Prefs.Role_Principal);
        startOdooActivity();

    }

    private void startCheckInOutFragment() {
        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flContentMain, CheckInOutFragment.newInstance(), CheckInOutFragment.TAG)
                .addToBackStack(null)
                .commit();
    }

    private void startOdooActivity() {
        mActivity.startActivity(new Intent(mActivity, OdooActivity.class));
    }

}
