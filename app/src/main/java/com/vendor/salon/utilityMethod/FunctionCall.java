package com.vendor.salon.utilityMethod;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.vendor.salon.R;

public class FunctionCall {

    public static Dialog mdialog;
    public static void showProgressDialog(Context context){
        mdialog = new Dialog(context);
        mdialog.setContentView(R.layout.prograssbar_custom_layouts);
        mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mdialog.setCancelable(false);
        mdialog.show();
    }

    public static void DismissDialog(Context context){
        if(context !=null){
            if(mdialog != null){
                mdialog.dismiss();
            }

        }

    }
  }
