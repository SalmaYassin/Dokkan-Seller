package com.example.dokkanseller.views.Add_Product;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.example.dokkanseller.R;


public class LoadingDialog {
    private static android.app.Dialog mDialog;

    public static void showProgress(Context mContext) {
        mDialog = new android.app.Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.loading_progress);
        mDialog.findViewById(R.id.avi).setVisibility(android.view.View.VISIBLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    public static void hideProgress() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
