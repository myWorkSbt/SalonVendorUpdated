package com.vendor.salon.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.vendor.salon.data_Class.login.LoginResponse;
import com.vendor.salon.databinding.ActivityOtpBinding;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.NetworkChangeListener;
import com.vendor.salon.utilityMethod.loginResponsePref;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class otp extends AppCompatActivity {

    private ActivityOtpBinding otpBinding;
    private final boolean isGPSEnabled = false;
    private final boolean isNetworkEnabled = false;
    private Location location;
    private String latitude;
    private String longitude;
    private LocationManager locationManager;
     NetworkChangeListener networkChangeListener = new NetworkChangeListener() ;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        otpBinding = ActivityOtpBinding.inflate(getLayoutInflater());
        setContentView(otpBinding.getRoot());
        String entered_phoneno = getIntent().getStringExtra("phone");
        String ccp_val = getIntent().getStringExtra("ccp");
        otpBinding.mobileNumber.setText(String.format("%s%s", ccp_val, entered_phoneno));

        getCurrentLatLong();
        showTimerOnResend();
        otpBinding.Resend.setOnClickListener(view -> {
            if(! otpBinding.Resend.getText().equals("Resend")) {

            }
            else {
                Toast.makeText(otp.this, "Otp Resend Successfully! ", Toast.LENGTH_SHORT).show();
                showTimerOnResend();
            }
        });
//        sendCurrentLatLangToTheDatabase();
        otpBinding.back.setOnClickListener(view -> {
            otpBinding.progressBar.setVisibility(View.VISIBLE);
            startActivity(new Intent(otp.this, Login.class));
            otpBinding.progressBar.setVisibility(View.GONE);
            finish();
        });
        otpBinding.nextBtn.setOnClickListener(view -> {
            otpBinding.progressBar.setVisibility(View.VISIBLE);
            String entered_otp = Objects.requireNonNull(otpBinding.otpPin.getText()).toString();
            if (entered_otp.length() != 4) {
                otpBinding.progressBar.setVisibility(View.GONE);
                otpBinding.otpPin.setError("Mandatory");
            } else {
                FunctionCall.showProgressDialog(otp.this);
                HashMap<String, String> hashmap = new HashMap<>();
                hashmap.put("phone", entered_phoneno);
                hashmap.put("otp", entered_otp);

                hashmap.put("device_id", "dchgsjhkjsb");
                hashmap.put("device_token", "hchwjkcjkwk");
                hashmap.put("country_code", ccp_val);
                hashmap.put("lat", latitude);
                hashmap.put("lng", longitude);
                Call<LoginResponse> call = RetrofitClient.getVendorService().verifyOtp(hashmap);
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<LoginResponse> call,@NonNull Response<LoginResponse> response) {
                        FunctionCall.DismissDialog(otp.this );
                        if (response.isSuccessful() && response.body() != null) {
                            LoginResponse loginResponse = response.body();
                                if (loginResponse.getAgent() != null) {
                                    if (loginResponse.getAgent().getIsRegistered() == 0) {
                                        Toast.makeText(otp.this, " Please Fill Your Details here " + "", Toast.LENGTH_SHORT).show();
                                        Intent profileIntent = new Intent(otp.this, Profile.class);
                                        profileIntent.putExtra("phone", entered_phoneno);
                                        profileIntent.putExtra("token", loginResponse.getToken());
                                        profileIntent.putExtra("ccp", ccp_val);
                                        startActivity(profileIntent);
                                        otpBinding.progressBar.setVisibility(View.GONE);
                                        finish();
                                    } else if (loginResponse.getAgent().getIsRegistered() == 1) {
                                        if (loginResponse.getAgent().getIsApproved() == 0) {
                                            Intent loginIntent = new Intent(otp.this, Login.class);
                                            Toast.makeText(otp.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            startActivity(loginIntent);
                                        } else {
                                            Intent intent;
//                                                if(loginResponse.getAgent().getVendorType().toString()!= null ) {
//                                                    if (loginResponse.getAgent().getVendorType().toString().toUpperCase().equals("STAFF")) {
//                                                        intent = new Intent(otp.this, Appointment.class);
//                                                    } else {
                                                    intent = new Intent(otp.this, Home.class);
                                                    intent.putExtra("vendor", loginResponse.getAgent().getVendorType() + "");
//                                                    }
                                                loginResponsePref.getInstance(getApplicationContext()).setToken(response.body().getToken(), response.body().getMessage(), entered_phoneno);
                                                Toast.makeText(otp.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                                startActivity(intent);
//                                                }
                                        }
                                        otpBinding.progressBar.setVisibility(View.GONE);
                                        finishAffinity();
                                    }
                                }
                                else {
                                otpBinding.progressBar.setVisibility(View.GONE);
                                otpBinding.otpPin.requestFocus();

                                Toast.makeText(otp.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (response.body() != null) {
                                Toast.makeText(otp.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            otpBinding.progressBar.setVisibility(View.GONE);
                            Log.d("loginhit", "onResponse: " + response.body());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginResponse> call,@NonNull Throwable t) {
                        Log.d("loginhit", "onResponse: " + t.getMessage());
                        FunctionCall.DismissDialog(otp.this );
                        Toast.makeText(otp.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showTimerOnResend() {
        new CountDownTimer(30000, 1000) {

            @Override
            public void onTick(long l) {
                otpBinding.Resend.setText(String.format("%ss : remaining ", new SimpleDateFormat("ss").format(new Date(l))));
            }

            @Override
            public void onFinish() {
                otpBinding.Resend.setText("Resend");
            }
            }.start();
    }

    private void getCurrentLatLong() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);// getting GPS status
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isGPSEnabled) {
            OnGPS();
        } else {
            getLocation();
        }

    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", (dialog, which) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                otp.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                otp.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            int REQUEST_LOCATION = 1000;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
//                Toast.makeText(this, "Lat: "+latitude+ " ---  . Long "+ longitude +" .", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        otpBinding.progressBar.setVisibility(View.VISIBLE);
        startActivity(new Intent(otp.this, Login.class));
        otpBinding.progressBar.setVisibility(View.GONE);
        finish();
    }


    //
//    private void sendCurrentLatLangToTheDatabase() {
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext())
//        CallBack callback = OnBackPressedCallback(true).handleOnBackPressed();
//
//
//        getLocations();
//
//    }
//
//    private void getLocations() {
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION], this);
//            return;
//        }
//        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY,
//                new CancellationToken() {
//                    @NonNull
//                    @Override
//                    public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
//                        CancellationTokenSource cancellationToken = new CancellationTokenSource();
//                        return cancellationToken.getToken();
//                    }
//
//                    @Override
//                    public boolean isCancellationRequested() {
//                        return false;
//                    }
//                }).addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                String lat = java.lang.String.valueOf(location.getLatitude());
//                String lang = java.lang.String.valueOf(location.getLongitude());
//            }
//        });
//    }


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