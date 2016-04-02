package com.odoo.addons.employees.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.odoo.R;
import com.odoo.addons.employees.backend.LocationService;

import java.util.Arrays;
import java.util.Calendar;

public class Utils {
//    public static boolean isDeviceOnline(@NonNull Context context) {
//        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//        return (networkInfo != null && networkInfo.isConnected());
//    }

//    /**
//     * <strong>Uses:</strong><br/>
//     * <p>
//     * {@code
//     * List<Pair<String, String>> pairs = new ArrayList<>();}
//     * <br/>
//     * {@code pairs.add(new Pair<>("key1", "value1"));}<br/>
//     * {@code pairs.add(new Pair<>("key2", "value2"));}<br/>
//     * {@code pairs.add(new Pair<>("key3", "value3"));}<br/>
//     * <br/>
//     * {@code Utils.postToServer("http://www.example.com/", pairs);}<br/>
//     * </p>
//     *
//     * @param url   server url
//     * @param pairs List of support.V4 Pair
//     * @return response from server in String format
//     * @throws Exception
//     */
//    public static String postToServer(String url, List<Pair<String, String>> pairs) throws Exception {
//        OkHttpClient client = new OkHttpClient();
//        Request.Builder builder = new Request.Builder().url(url);
//        if (pairs != null) {
//            FormEncodingBuilder postData = new FormEncodingBuilder();
//            for (Pair<String, String> pair : pairs) {
//                postData.add(pair.first, pair.second);
//            }
//            builder.post(postData.build());
//        }
//        Request request = builder.build();
//        Response response = client.newCall(request).execute();
//        if (!response.isSuccessful()) {
//            throw new IOException(response.message() + " " + response.toString());
//        }
//        return response.body().string();
//    }

//    public static void resetForm(ViewGroup group) {
//        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
//            View view = group.getChildAt(i);
//            if (view instanceof EditText) {
//                ((EditText) view).getText().clear();
//            }
//
//            if (view instanceof RadioGroup) {
//                ((RadioButton) ((RadioGroup) view).getChildAt(0)).setChecked(true);
//            }
//
//            if (view instanceof Spinner) {
//                ((Spinner) view).setSelection(0);
//            }
//
//            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
//                resetForm((ViewGroup) view);
//        }
//    }

    public static void setWidgetTint(ViewGroup group, Context context) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                EditText editText = ((EditText) view);
                // Left drawable
                Drawable drawable = editText.getCompoundDrawables()[0];
                drawable = DrawableCompat.wrap(drawable);
                DrawableCompat.setTint(drawable, ContextCompat.getColor(context, R.color.colorAccent));
                DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
                editText.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            }

            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0)) {
                setWidgetTint((ViewGroup) view, context);
            }
        }
    }

//    public static String parseDate(int year, int monthOfYear, int dayOfMonth) {
//        monthOfYear++;
//        return (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "/"
//                + (monthOfYear < 10 ? "0" + monthOfYear : monthOfYear) + "/" + year;
//    }

//    public static String parseTime(int hourOfDay, int minute) {
//        boolean isAM = true;
//        if (hourOfDay > 12) {
//            hourOfDay -= 12;
//            isAM = false;
//        }
//        return (hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + ":"
//                + (minute < 10 ? "0" + minute : minute) + " " + (isAM ? "AM" : "PM");
//    }

    public static String getCurrentDate() {
        Calendar now = Calendar.getInstance();
        int dayOfMonth = now.get(Calendar.DAY_OF_MONTH);
        int month = now.get(Calendar.MONTH) + 1;
        int year = now.get(Calendar.YEAR);

        return ("" + year) + "-"
                + (month < 10 ? ("0" + month) : ("" + month)) + "-"
                + (dayOfMonth < 10 ? ("0" + dayOfMonth) : ("" + dayOfMonth));
    }

    /**
     * @param cls class type
     *            <br/>
     *            <code>YourClassName.class;</code>
     *            <br/><br/>
     */
    public static void getStackTrace(final Class<?> cls) {
        final String className = cls.getName();
        final String classSimpleName = cls.getSimpleName();
        final StackTraceElement[] steArray = Thread.currentThread().getStackTrace();
        int index = 0;
        for (StackTraceElement ste : steArray) {
            if (ste.getClassName().equals(className)) {
                break;
            }
            index++;
        }
        if (index >= steArray.length) {
            // Little Hacky
            Log.w(classSimpleName,
                    Arrays.toString(new String[]{steArray[3].getMethodName(),
                            String.valueOf(steArray[3].getLineNumber())}));
        } else {
            // Legitimate
            Log.w(classSimpleName,
                    Arrays.toString(new String[]{steArray[index].getMethodName(),
                            String.valueOf(steArray[index].getLineNumber())}));
        }
    }

//    public static void hideSoftKeyboard(Activity activity) {
//        View view = activity.getCurrentFocus();
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//    }


    public static AlertDialog showMessage(Context context, String title, String message,
                                          String positiveButton) {
        return new AlertDialog.Builder(context, R.style.AppAlertDialogTheme)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButton, null)
                .show();
    }

    public static AlertDialog showMessage(Context context, String title, String message,
                                          String positiveButton,
                                          DialogInterface.OnClickListener positiveButtonListener,
                                          String negativeButton,
                                          DialogInterface.OnClickListener negativeButtonListener) {
        return new AlertDialog.Builder(context, R.style.AppAlertDialogTheme)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButton, positiveButtonListener)
                .setNegativeButton(negativeButton, negativeButtonListener)
                .setCancelable(false)
                .show();
    }

    public static boolean isGPSLocationEnabled(final Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void showLocationMessage(final Context context) {
        showMessage(context, "Location off!", "Do you want to turn on Location?", "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                }, "No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(1);
                    }
                });

    }

    public static void checkLocationSettings(final Context context) {
        if (isGPSLocationEnabled(context)) {
            LocationService.start(context);
        } else {
            showLocationMessage(context);
        }
    }

    public static void checkLocationSettings(final Context context, final LocationService.LocationListener locationListener) {
        if (isGPSLocationEnabled(context)) {
            LocationService.start(context, locationListener);
        } else {
            showLocationMessage(context);
        }
    }

//    public static void rateApp(Activity activity) {
//        try {
//            activity.startActivity(
//                    new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + activity.getPackageName())));
//        } catch (ActivityNotFoundException e) {
//            try {
//                activity.startActivity(new Intent(Intent.ACTION_VIEW,
//                        Uri.parse("https://play.google.com/store/apps/details?id=" + activity.getPackageName())));
//            } catch (ActivityNotFoundException e2) {
//                e.printStackTrace();
//            }
//        }
//    }

//    public static void shareApp(Activity activity) {
//        Intent share_que = new Intent(Intent.ACTION_SEND);
//        share_que.setType("text/plain");
//        share_que.putExtra(Intent.EXTRA_TEXT,
//                "Hey friends,\nI just discover an amazing app called "
//                        + activity.getResources().getString(R.string.app_name)
//                        + ". Get it from https://play.google.com/store/apps/details?id=" + activity.getPackageName());
//        activity.startActivityForResult(Intent.createChooser(share_que,
//                "Share " + activity.getResources().getString(R.string.app_name) + " Using"), 123);
//    }

//    public static void moreApps(Activity activity) {
//        activity.startActivity(new Intent(Intent.ACTION_VIEW,
//                Uri.parse("https://play.google.com/store/apps/developer?id=Serpent+Consulting+Services+Pvt.+Ltd.")));
//    }
}
