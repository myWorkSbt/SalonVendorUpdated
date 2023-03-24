package com.vendor.salon.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.vendor.salon.R;
import com.vendor.salon.data_Class.sendotp;
import com.vendor.salon.databinding.ActivityLoginBinding;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.NetworkChangeListener;

import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    ActivityLoginBinding loginBinding;
    private int backpressedCount = 0;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());

        //  getWindow().setStatusBarColor();
        loginBinding.btnLoginNext.setOnClickListener(view -> {
            if (Objects.requireNonNull(loginBinding.enter.getText()).toString().length() != 10) {
                loginBinding.enter.setError("Enter a valid mobile number");
                loginBinding.progressBar.setVisibility(View.GONE);
                loginBinding.enter.requestFocus();
            } else {
                FunctionCall.showProgressDialog(Login.this);
                HashMap<String, String> hash= new HashMap<>();
//                    String ccp_values = loginBinding.countryPicker.getSelectedCountryCodeWithPlus();
                String ccp_values = loginBinding.countryPicker.getText().toString();
                String phone_number  =  loginBinding.enter.getText().toString();
                hash.put("phone",phone_number);
                hash.put("country_code", ccp_values);
                Call<sendotp> call = RetrofitClient.getVendorService().sendOtp(hash);
                call.enqueue(new Callback<sendotp>() {
                    @Override
                    public void onResponse(@NonNull Call<sendotp> call, @NonNull Response<sendotp> response) {
                        FunctionCall.DismissDialog(Login.this);
                        if (response.isSuccessful() && response.body() != null ) {
                            sendotp sendOtpResponse = response.body();
                            if (sendOtpResponse.getResult().equals("success")) {
                                Toast.makeText(Login.this, "" + sendOtpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                startActivity((new Intent(Login.this, otp.class)).putExtra("phone", phone_number).putExtra("ccp", ccp_values));
                                loginBinding.progressBar.setVisibility(View.GONE);
                                finish();
                            } else {
                                loginBinding.progressBar.setVisibility(View.GONE);
                                Toast.makeText(Login.this, "Result : " + sendOtpResponse.getResult(), Toast.LENGTH_SHORT).show();

                            }
                        }
                        else {
                            if (response.body() != null) {
                                Toast.makeText(Login.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            Log.d("sendotphit", "onResponse: " + response.body());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<sendotp> call, @NonNull Throwable t) {
                        FunctionCall.DismissDialog(Login.this);
                        Log.d("sendotphit", "onResponse: " + t.getMessage());
                    }
                });
            }

        });
    }

    @Override
    public void onBackPressed() {
        backpressedCount++;
        if(backpressedCount <2) {
            Toast.makeText(this, " Press again to exist from app", Toast.LENGTH_SHORT).show();
        }
        else{
            super.onBackPressed();
        }
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