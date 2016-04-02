package com.odoo.addons.employees;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.odoo.R;
import com.odoo.addons.employees.fragments.RoleSelectionFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
    }

    private void setupViews() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flContentMain, RoleSelectionFragment.newInstance(), RoleSelectionFragment.TAG)
                .commit();
    }
}
