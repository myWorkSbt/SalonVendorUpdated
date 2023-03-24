package com.vendor.salon.utilityMethod;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import com.vendor.salon.R;

public class NetworkChangeListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!GetNetwork.isConnectedToInternet(context)) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
            View layout_views = LayoutInflater.from(context).inflate(R.layout.nointernetconnectionlays, null);
            alertBuilder.setView(layout_views);

            AppCompatButton btn_retry = layout_views.findViewById(R.id.btn_apply);
            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.show();
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setGravity(Gravity.CENTER);

            btn_retry.setOnClickListener(View -> {
                alertDialog.dismiss();
                onReceive(context, intent);
            });

        }
    }


}
