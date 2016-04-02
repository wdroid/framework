package com.odoo.addons.employees;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.odoo.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.sudar.zxingorient.Barcode;
import me.sudar.zxingorient.ZxingOrient;

public class QRCodeDialog extends Dialog {

    public static final String TAG = QRCodeDialog.class.getSimpleName();

    Activity mActivity;

    @Bind(R.id.tbQRCode)
    Toolbar mToolbar;

    public QRCodeDialog(Activity context, int themeResId) {
        super(context, themeResId);
        mActivity = context;
        setContentView(R.layout.dialog_qrcode);
        ButterKnife.bind(this);
        setUpViews();
        show();
    }

    public void setUpViews() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @OnClick(R.id.tvQRCodeGenerate)
    public void onGenerate() {
        dismiss();
    }

    @OnClick(R.id.tvQRCodeSelect)
    public void onScan() {
        dismiss();
        new ZxingOrient(mActivity)
                .setInfo("Scan QR Code")
                .setToolbarColor("#c099cc00")
                .setInfoBoxColor("#c099cc00")
                .setBeep(false)
                .initiateScan(Barcode.QR_CODE);
    }
}
