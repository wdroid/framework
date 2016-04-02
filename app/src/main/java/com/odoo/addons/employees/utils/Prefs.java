package com.odoo.addons.employees.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    public static final String Role_Teacher = "Teachers";
    public static final String Role_Principal = "Principals";

    private static final String PrefsName = Prefs.class.getSimpleName();
    private static final String Role = "role";
    private static final String SignIn = "SignIn";
    private static final String SchoolLocationUpdated = "SchoolLocationUpdated";

    SharedPreferences mPrefs;

    public Prefs(Context context) {
        super();
        mPrefs = context.getSharedPreferences(PrefsName, Context.MODE_PRIVATE);
    }

    public String getRole() {
        return mPrefs.getString(Role, Role_Teacher);
    }

    public Prefs setRole(String role) {
        mPrefs.edit().putString(Role, role).apply();
        return this;
    }

    public boolean isSignIn() {
        return mPrefs.getBoolean(SignIn, true);
    }

    public Prefs setSignIn(boolean signIn) {
        mPrefs.edit().putBoolean(SignIn, signIn).apply();
        return this;
    }

    public boolean isSchoolLocationUpdated() {
        return mPrefs.getBoolean(SchoolLocationUpdated, false);
    }

    public Prefs setSchoolLocationUpdated(boolean schoolLocationUpdated) {
        mPrefs.edit().putBoolean(SchoolLocationUpdated, schoolLocationUpdated).apply();
        return this;
    }
}
