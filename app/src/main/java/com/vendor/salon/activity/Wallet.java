package com.vendor.salon.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.vendor.salon.R;
import com.vendor.salon.databinding.ActivityWalletBinding;
import com.vendor.salon.utilityMethod.NetworkChangeListener;

public class Wallet extends AppCompatActivity {

     ActivityWalletBinding walletBinding;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        walletBinding = DataBindingUtil.setContentView( Wallet.this ,R.layout.activity_wallet);

         walletBinding.btnBack.setOnClickListener(View ->{
             finish();
         });


    }


    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener , intentFilter );
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }


}