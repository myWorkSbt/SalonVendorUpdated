package com.vendor.salon.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.os.Bundle;

import com.vendor.salon.R;
import com.vendor.salon.databinding.ActivityWalletBinding;

public class Wallet extends AppCompatActivity {

     ActivityWalletBinding walletBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        walletBinding = DataBindingUtil.setContentView( Wallet.this ,R.layout.activity_wallet);




    }
}