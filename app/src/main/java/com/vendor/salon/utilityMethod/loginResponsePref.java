package com.vendor.salon.utilityMethod;

import android.content.Context;
import android.content.SharedPreferences;

public class loginResponsePref {
    private static final String SHARED_PREF_NAME = "loginResponse";
    private static final String Token = "token";
    private static final String MSG = "msg";
    private static final String PHONE = "phone";
    private static loginResponsePref mInstance;
    private static Context ctx;

    private loginResponsePref(Context context) {
        ctx = context;
    }
    public static synchronized loginResponsePref getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new loginResponsePref(context);
        }
        return mInstance;
    }

    public void setToken(String token, String msg, String phone) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Token, token);
        editor.putString(MSG, msg);
        editor.putString(PHONE,phone);
        editor.apply();
    }

    public String getToken() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Token, null);
    }

    public String getMsg() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(MSG, null);
    }

    public String getPhone( ) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PHONE, null);
    }
    public void removeToken() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public Boolean isLogIN() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return   sharedPreferences.getString(Token,null) != null;
    }

}
