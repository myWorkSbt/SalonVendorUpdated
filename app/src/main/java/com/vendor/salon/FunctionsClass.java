package com.vendor.salon;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.vendor.salon.R;

public class FunctionsClass {

    public static String getPermissionStatus(Activity activity, String androidPermissionName) {
        if (ContextCompat.checkSelfPermission(activity, androidPermissionName) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName)) {
                return "blocked";
            }
            return "denied";
        }
        return "granted";
    }

    //show permission setting screen
    public static void showPermissionSetting(Context context, String message) {
        showDoubleButtonAlert(context, context.getString(R.string.permission_alert), message,
                context.getString(R.string.cancel_), context.getString(R.string.settings), false, new FragmentCallBack() {
                    @Override
                    public void onResponce(Bundle bundle) {
                        if (bundle.getBoolean("isShow", false)) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                            intent.setData(uri);
                            context.startActivity(intent);
                        }
                    }
                });
    }

    public static void showDoubleButtonAlert(Context context, String title, String message, String negTitle, String posTitle, boolean isCancelable, FragmentCallBack callBack) {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(isCancelable);
        dialog.setContentView(R.layout.show_double_button_new_popup_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final TextView tvtitle, tvMessage, tvPositive, tvNegative;
        tvtitle = dialog.findViewById(R.id.tvtitle);
        tvMessage = dialog.findViewById(R.id.tvMessage);
        tvNegative = dialog.findViewById(R.id.tvNegative);
        tvPositive = dialog.findViewById(R.id.tvPositive);


        tvtitle.setText(title);
        tvMessage.setText(message);
        tvNegative.setText(negTitle);
        tvPositive.setText(posTitle);

        tvNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Bundle bundle = new Bundle();
                bundle.putBoolean("isShow", false);
                callBack.onResponce(bundle);
            }
        });

        tvPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Bundle bundle = new Bundle();
                bundle.putBoolean("isShow", true);
                callBack.onResponce(bundle);
            }
        });
        dialog.show();
    }
}

