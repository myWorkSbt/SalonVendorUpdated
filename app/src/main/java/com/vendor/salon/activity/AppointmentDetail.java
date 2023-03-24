package com.vendor.salon.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vendor.salon.R;
import com.vendor.salon.data_Class.appointmentsfilter.AppointmentsFilterResponse;
import com.vendor.salon.data_Class.appointmentsfilter.AppointmentsItem;
import com.vendor.salon.data_Class.appointmentstartend.ServiceStartEndResponse;
import com.vendor.salon.data_Class.doorstep_status_update.DoorstepStatusUpdateResponse;
import com.vendor.salon.databinding.ActivityAppointmentDetailBinding;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.NetworkChangeListener;
import com.vendor.salon.utilityMethod.loginResponsePref;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentDetail extends AppCompatActivity implements OnMapReadyCallback {

    private String recentAppointmentsPosition;
    private ActivityAppointmentDetailBinding appointmentDetailBinding;
    double latitude;
    private final String[] appointment_not_started_list = new String[]{" Not Started    ", " On the way     ", " Reached        ", " Service Started ", " Service Ended  "};
    private final String[] on_the_way_list = new String[]{" On the way     ", " Reached        ", " Service Started ", " Service Ended  "};
    private final String[] reached_list = new String[]{" Reached        ", " Service Started ", " Service Ended  "};
    private final String[] service_start_list = new String[]{" Service Started ", " Service Ended  "};
    private final String[] service_end_list = new String[]{" Service Ended  "};
    double longitude;
    private AppointmentsItem appointments;
    private double st_latitude;
    private double st_longitude;
    String appointment_status = "not_started";
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appointmentDetailBinding = ActivityAppointmentDetailBinding.inflate(getLayoutInflater());
        setContentView(appointmentDetailBinding.getRoot());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        appointmentDetailBinding.detailHeads.setPaintFlags(appointmentDetailBinding.detailHeads.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        recentAppointmentsPosition = getIntent().getStringExtra("positions");

        setData(recentAppointmentsPosition);
//        getSupportFragmentManager().beginTransaction().replace(R.id.frag_containers, new MapFragment()).commit();

        appointmentDetailBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
            setData(recentAppointmentsPosition);
            appointmentDetailBinding.swipeRefreshLayout.setRefreshing(false);
        });

        getCurrentLatLong();
        appointmentDetailBinding.btnAddRemoveServices.setOnClickListener(view -> {
            Intent addServicesIntents = new Intent(AppointmentDetail.this, AddAppointmentCategory.class);
            addServicesIntents.putExtra("appointment_id", appointments.getId());
            addServicesIntents.putExtra("isintialentry", true);
            startActivity(addServicesIntents);
        });

        appointmentDetailBinding.btnDirection.setOnClickListener(View -> {

            if (latitude == 0.0 || longitude == 0.0) {
                Toast.makeText(this, " This Appointment does not have any Latitude and Longitude .", Toast.LENGTH_SHORT).show();
            } else if (st_latitude == 0.0 || st_longitude == 0.0) {
                Toast.makeText(this, " Current Location is not found. ", Toast.LENGTH_SHORT).show();
            } else {
                Intent mapIntentts = new Intent(AppointmentDetail.this, ShowPathActivity.class);
                mapIntentts.putExtra("s_p_lng", st_longitude);
                mapIntentts.putExtra("s_p_lat", st_latitude);
                mapIntentts.putExtra("e_lat", latitude);
                mapIntentts.putExtra("e_lng", longitude);
                startActivity(mapIntentts);

            }
        });
        appointmentDetailBinding.statusUpdateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                setAppointmentStatus(appointment_status, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void showEnterOtpDialogBOx(int position) {
        if (!appointment_status.equalsIgnoreCase("service_started") && !appointment_status.equalsIgnoreCase("service_ended")) {
            appointment_status = "reached" ;
            appointmentDetailBinding.statusUpdateSpinner.setAdapter(new ArrayAdapter<>(appointmentDetailBinding.statusUpdateSpinner.getContext(), com.hbb20.R.layout.support_simple_spinner_dropdown_item, reached_list));
            appointmentDetailBinding.statusUpdateSpinner.setSelection(0);
        }
//
//        if ((appointment_status.equalsIgnoreCase("not_started") && 4 == position) ||
//                (appointment_status.equalsIgnoreCase("reached") && 2 == position) ||
//                (appointment_status.equalsIgnoreCase("on_the_way") && 3 == position) ||
//                (appointment_status.equalsIgnoreCase("service_started") && 1 == position)) {
//            if (appointment_status.equalsIgnoreCase("service_ended")) {
//                Toast.makeText(this, "Service Completed Successfully. ", Toast.LENGTH_SHORT).show();
//                appointmentDetailBinding.statusUpdateSpinner.setAdapter(new ArrayAdapter<>(appointmentDetailBinding.statusUpdateSpinner.getContext(), com.hbb20.R.layout.support_simple_spinner_dropdown_item, service_end_list));
//                appointmentDetailBinding.statusUpdateSpinner.setSelection(0);
//            } else {
//                Toast.makeText(AppointmentDetail.this, " Service is not started. ", Toast.LENGTH_SHORT).show();
//
//            }
//        }
//        else if (!appointment_status.equalsIgnoreCase("service_started") &&appointment_status.equalsIgnoreCase("service_started") ) {
//            appointment_status = "service_started";
//            appointmentDetailBinding.statusUpdateSpinner.setAdapter(new ArrayAdapter<>(appointmentDetailBinding.statusUpdateSpinner.getContext(), com.hbb20.R.layout.support_simple_spinner_dropdown_item, service_start_list));
//            appointmentDetailBinding.statusUpdateSpinner.setSelection(0);
//        }
//        else {
//            if (appointment_status.equalsIgnoreCase("service_started")) {
//                appointmentDetailBinding.statusUpdateSpinner.setAdapter(new ArrayAdapter<>(appointmentDetailBinding.statusUpdateSpinner.getContext(), com.hbb20.R.layout.support_simple_spinner_dropdown_item, service_start_list));
//                appointmentDetailBinding.statusUpdateSpinner.setSelection(0);
//            } else {
//                if (!appointment_status.equalsIgnoreCase("service_ended")) {
//                    appointment_status = "reached";
//                    appointmentDetailBinding.statusUpdateSpinner.setAdapter(new ArrayAdapter<>(appointmentDetailBinding.statusUpdateSpinner.getContext(), com.hbb20.R.layout.support_simple_spinner_dropdown_item, reached_list));
//                    appointmentDetailBinding.statusUpdateSpinner.setSelection(0);
//                }
//                else {
//                    appointment_status = "service_started";
//                    appointmentDetailBinding.statusUpdateSpinner.setAdapter(new ArrayAdapter<>(appointmentDetailBinding.statusUpdateSpinner.getContext(), com.hbb20.R.layout.support_simple_spinner_dropdown_item, service_start_list));
//                    appointmentDetailBinding.statusUpdateSpinner.setSelection(0);
//                }
//            }
//        }



        BottomSheetDialog enter_otp_dialogs = new BottomSheetDialog(AppointmentDetail.this, R.style.BottomSheetDialogStyle);
        View view = LayoutInflater.from(AppointmentDetail.this).inflate(R.layout.enter_otp_bottom_sheet_lays, null);
        enter_otp_dialogs.setContentView(view);
//        AppCompatTextView btn_resend_otp = enter_otp_dialogs.findViewById(R.id.btn_resend_otp);
        AppCompatButton btn_verify_otp = enter_otp_dialogs.findViewById(R.id.btn_verify_otp);
        AppCompatTextView tv_headings = enter_otp_dialogs.findViewById(R.id.tv_enter_heads);
        AppCompatEditText et_otp = enter_otp_dialogs.findViewById(R.id.et_otps);

        enter_otp_dialogs.show();
        enter_otp_dialogs.setCancelable(false);
        Objects.requireNonNull(btn_verify_otp).setOnClickListener(View -> {
            String otp_txt = Objects.requireNonNull(Objects.requireNonNull(et_otp).getText()).toString();
            if (otp_txt.isEmpty()) {
                et_otp.setError(" Mandatory Field! ");
                et_otp.requestFocus();
            } else {
                verifyOtp(otp_txt, enter_otp_dialogs , position );

            }
        });
        if (appointment_status.equalsIgnoreCase("service_started")) {
            tv_headings.setText("End Your Service");
        } else {
            assert tv_headings != null;
            tv_headings.setText("Start Your Service ");
        }
    }

    private void verifyOtp(String otp_txt, BottomSheetDialog enter_otp_dialogs, int position) {

        String token = "Bearer " + loginResponsePref.getInstance(AppointmentDetail.this).getToken();
        Call<ServiceStartEndResponse> call;
        HashMap<String, String> appointmentStatusInput = new HashMap<>();
        appointmentStatusInput.put("appointment_id", String.valueOf(appointments.getId()));
        FunctionCall.showProgressDialog(AppointmentDetail.this);
        if (appointment_status.equalsIgnoreCase("service_started") ) {
            appointmentStatusInput.put("end_code", otp_txt);
            call = RetrofitClient.getVendorService().verifyAppointmentEndOtp(token, appointmentStatusInput);
        } else {
            appointmentStatusInput.put("start_code", otp_txt);
            call = RetrofitClient.getVendorService().verifyAppointmentStartOtp(token, appointmentStatusInput);
        }
        call.enqueue(new Callback<ServiceStartEndResponse>() {
            @Override
            public void onResponse(@NonNull Call<ServiceStartEndResponse> call, @NonNull Response<ServiceStartEndResponse> response) {
                FunctionCall.DismissDialog(AppointmentDetail.this);
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    enter_otp_dialogs.hide();

                    if (appointment_status.equalsIgnoreCase("service_started")) {
                        appointment_status = "service_ended";
                        appointmentDetailBinding.statusUpdateSpinner.setAdapter(new ArrayAdapter<>(appointmentDetailBinding.statusUpdateSpinner.getContext(), com.hbb20.R.layout.support_simple_spinner_dropdown_item, service_end_list ));
                        appointmentDetailBinding.statusUpdateSpinner.setSelection(0);
                    }
                    else {
                        appointment_status = "service_started";
                        appointmentDetailBinding.statusUpdateSpinner.setAdapter(new ArrayAdapter<>(appointmentDetailBinding.statusUpdateSpinner.getContext(), com.hbb20.R.layout.support_simple_spinner_dropdown_item, service_start_list));
                        appointmentDetailBinding.statusUpdateSpinner.setSelection(0);
                    }


                } else {
                    if (response.body() != null) {
                        Toast.makeText(AppointmentDetail.this, ". " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Log.d("appointmentservicestartedendhits", "onResponse: " + response.body());
                }

            }

            @Override
            public void onFailure(@NonNull Call<ServiceStartEndResponse> call, @NonNull Throwable t) {
                appointment_status = "reached";
                Log.d("appointmentservicestartedendhits", "onFailure: " + t.getMessage());
                FunctionCall.DismissDialog(AppointmentDetail.this);
            }
        });
    }

    private void setData(String user_id) {
        String token = "Bearer " + loginResponsePref.getInstance(AppointmentDetail.this).getToken();

        FunctionCall.showProgressDialog(AppointmentDetail.this);
        Call<AppointmentsFilterResponse> call = RetrofitClient.getVendorService().getAppointmentSearchedData(token, "", "", "", "", "", user_id + "", 0);
        call.enqueue(new Callback<AppointmentsFilterResponse>() {
            @Override
            public void onResponse(@NonNull Call<AppointmentsFilterResponse> call, @NonNull Response<AppointmentsFilterResponse> response) {
                FunctionCall.DismissDialog(AppointmentDetail.this);
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    List<AppointmentsItem> appointmentLists = response.body().getAppointments();
                    if (appointmentLists != null && appointmentLists.size() > 0) {
                        appointments = appointmentLists.get(0);
                        if (appointments != null) {
                            appointmentDetailBinding.personName.setText(String.format("%s", appointments.getUserName()));
                            appointmentDetailBinding.mobileNo.setText(String.format("%s", appointments.getUserPhone()));
                            appointmentDetailBinding.address.setText(String.format("%s", appointments.getUserLocation()));
                            if (appointments.getUserImage() != null) {
                                Glide.with(appointmentDetailBinding.personImage.getContext()).load(appointments.getUserImage()).into(appointmentDetailBinding.personImage);
                            }

                            String services_val = "" + appointments.getServicesName();

                            appointmentDetailBinding.service.setText(services_val);
                            latitude = Double.parseDouble(appointments.getUser_lat());
                            longitude = Double.parseDouble(appointments.getUser_lng());
                            if (appointments.getUser_lat() != null && appointments.getUser_lng() != null && !appointments.getUser_lat().isEmpty() && !appointments.getUser_lat().isEmpty() && !appointments.getUser_lng().isEmpty()) {
                                FindLocationName(latitude, longitude);
                            }
                            setUpMapsWithLocations();
                            setAppointmentStatus(appointments.getDoorstep_status(), -1);
                        }
                    }

                } else {
                    Log.d("appointmentsfilterhit", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AppointmentsFilterResponse> call, @NonNull Throwable t) {
                FunctionCall.DismissDialog(AppointmentDetail.this);
                Log.d("appointmentsfilterhit", "onFailure:  " + t.getMessage());
            }
        });
        appointmentDetailBinding.btnBack.setOnClickListener(view -> finish());

    }

    private void setAppointmentStatus(String doorstep_status, int position) {
        if (doorstep_status == null || doorstep_status.equalsIgnoreCase("") || doorstep_status.equalsIgnoreCase("not_started")) {
            this.appointment_status = "not_started";
        } else {
            this.appointment_status = doorstep_status;
        }
        if (appointment_status.equalsIgnoreCase("on_the_way")) {

            switch (position) {
                case 1:
                    String appointment_status_old = appointment_status ;
                    appointment_status = "reached";

                    updateDoorstepStatus (appointment_status_old);
                    break;
                case 2:
                    showEnterOtpDialogBOx(position);
                    break;
                case 3:
                    if (!appointment_status.equalsIgnoreCase("service_started")) {
                        Toast.makeText(this, "Service is not started. ", Toast.LENGTH_SHORT).show();
                    } else {
                        showEnterOtpDialogBOx(position);
                    }
                    break;
                default:
                    appointmentDetailBinding.statusUpdateSpinner.setAdapter(new ArrayAdapter<>(appointmentDetailBinding.statusUpdateSpinner.getContext(), com.hbb20.R.layout.support_simple_spinner_dropdown_item, on_the_way_list));
                    break;
            }
        }
        else if (appointment_status.equalsIgnoreCase("reached")) {
            switch (position) {
                case 1:
                    showEnterOtpDialogBOx(position);
                    break;
                case 2:
                    Toast.makeText(this, "Service is not started. ", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    appointment_status = "reached";
                    appointmentDetailBinding.statusUpdateSpinner.setAdapter(new ArrayAdapter<>(appointmentDetailBinding.statusUpdateSpinner.getContext(), com.hbb20.R.layout.support_simple_spinner_dropdown_item, reached_list));
                    break;
            }
        }
        else if (appointment_status.equalsIgnoreCase("service_started")) {
            if (position == 1) {
                showEnterOtpDialogBOx(position);
            } else {
                appointmentDetailBinding.statusUpdateSpinner.setAdapter(new ArrayAdapter<>(appointmentDetailBinding.statusUpdateSpinner.getContext(), com.hbb20.R.layout.support_simple_spinner_dropdown_item, service_start_list));
            }
        }
        else if (appointment_status.equalsIgnoreCase("service_ended")) {
            appointmentDetailBinding.statusUpdateSpinner.setAdapter(new ArrayAdapter<>(appointmentDetailBinding.statusUpdateSpinner.getContext(), com.hbb20.R.layout.support_simple_spinner_dropdown_item, service_end_list));
            appointmentDetailBinding.statusUpdateSpinner.setEnabled(false);
        } else {
            switch (position) {
                case 1:
                    String appointment_status_old = appointment_status ;
                    appointment_status = "on_the_way";

                    updateDoorstepStatus (appointment_status_old);
                    break;
                case 2:

                    String appointment_status_old7 = appointment_status ;
                    appointment_status = "reached";
                        updateDoorstepStatus (appointment_status_old7);
                        break;
                case 3:
                    showEnterOtpDialogBOx(position);
                    break;
                case 4:
                    if (!appointment_status.equalsIgnoreCase("service_started")) {
                        Toast.makeText(this, "Service is not started. ", Toast.LENGTH_SHORT).show();
                    } else {
                        showEnterOtpDialogBOx(position);
                    }
                    break;
                default:
                    appointment_status = "not_started";
                    appointmentDetailBinding.statusUpdateSpinner.setAdapter(new ArrayAdapter<>(appointmentDetailBinding.statusUpdateSpinner.getContext(), com.hbb20.R.layout.support_simple_spinner_dropdown_item, appointment_not_started_list));
                    break;

            }
        }

    }

    private void updateDoorstepStatus(String appointment_status_old) {
        FunctionCall.showProgressDialog(AppointmentDetail.this);
        String token = "Bearer " +  loginResponsePref.getInstance(AppointmentDetail.this).getToken();
        HashMap<String,String  >   updateDoorstepvals = new HashMap<>() ;
        updateDoorstepvals.put("appointment_id" , String.valueOf( appointments.getId()));
        if (!appointment_status.equalsIgnoreCase( "reached")) {
            appointment_status = "on_the_way";
        }
        else {
            appointment_status = "reached" ;
        }
        updateDoorstepvals.put("doorstep_status" , appointment_status);
        Call<DoorstepStatusUpdateResponse> call = RetrofitClient.getVendorService().updateAppointmentDoorstepStatus(token , updateDoorstepvals);
        call.enqueue(new Callback<DoorstepStatusUpdateResponse>() {
            @Override
            public void onResponse(@NonNull Call<DoorstepStatusUpdateResponse> call, @NonNull Response<DoorstepStatusUpdateResponse> response) {
                FunctionCall.DismissDialog(AppointmentDetail.this);
                if (response.isSuccessful()  && response.body() != null ) {
                    if (appointment_status.equalsIgnoreCase("reached")) {
                        appointmentDetailBinding.statusUpdateSpinner.setAdapter(new ArrayAdapter<>(appointmentDetailBinding.statusUpdateSpinner.getContext(), com.hbb20.R.layout.support_simple_spinner_dropdown_item, reached_list));
                    }
                    else {
                        appointmentDetailBinding.statusUpdateSpinner.setAdapter(new ArrayAdapter<>(appointmentDetailBinding.statusUpdateSpinner.getContext(), com.hbb20.R.layout.support_simple_spinner_dropdown_item, on_the_way_list));
                    }
                }
                else {
                    Log.d("updatedoorstephits", "onResponse: " + response.body());
                    appointment_status = appointment_status_old ;
                    if (appointment_status.equalsIgnoreCase("on_the_way")) {
                        appointment_status = "not_started" ;
                        appointmentDetailBinding.statusUpdateSpinner.setAdapter(new ArrayAdapter<>(appointmentDetailBinding.statusUpdateSpinner.getContext(), com.hbb20.R.layout.support_simple_spinner_dropdown_item, appointment_not_started_list));
                    }
                    else if (appointment_status.equalsIgnoreCase("reached")) {
                        appointment_status = "on_the_way";
                        appointmentDetailBinding.statusUpdateSpinner.setAdapter(new ArrayAdapter<>(appointmentDetailBinding.statusUpdateSpinner.getContext(), com.hbb20.R.layout.support_simple_spinner_dropdown_item, on_the_way_list));

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<DoorstepStatusUpdateResponse> call, @NonNull Throwable t) {
                Log.d("updatedoorstephits", "onFailure: " + t.getMessage() );
                FunctionCall.DismissDialog(AppointmentDetail.this);
                appointment_status = appointment_status_old ;
                if (appointment_status.equalsIgnoreCase("on_the_way")) {
                    appointment_status = "not_started" ;
                    appointmentDetailBinding.statusUpdateSpinner.setAdapter(new ArrayAdapter<>(appointmentDetailBinding.statusUpdateSpinner.getContext(), com.hbb20.R.layout.support_simple_spinner_dropdown_item, appointment_not_started_list));
                }
                else if (appointment_status.equalsIgnoreCase("reached")) {
                    appointment_status = "on_the_way";
                    appointmentDetailBinding.statusUpdateSpinner.setAdapter(new ArrayAdapter<>(appointmentDetailBinding.statusUpdateSpinner.getContext(), com.hbb20.R.layout.support_simple_spinner_dropdown_item, on_the_way_list));

                }
            }
        });
    }

    private void FindLocationName(double latitude, double longitude) {
        if (Geocoder.isPresent()) {
            Geocoder geocoder = new Geocoder(AppointmentDetail.this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String locationName = address.getAddressLine(0);
                appointmentDetailBinding.location.setText(locationName);
            }
        }

    }


    private void getCurrentLatLong() {
//        FunctionCall.showProgressDialog(AppointmentDetail.this);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);// getting GPS status
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
//        FunctionCall.DismissDialog(AppointmentDetail.this);
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            int REQUEST_LOCATION = 1000;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    st_latitude = location.getLatitude();
                    st_longitude = location.getLongitude();
                    Log.d("Location", "Latitude: " + st_latitude + ", Longitude: " + st_longitude);
                } else {
                    Log.d("Location", "Location is null");

                }
            });

        }
//        else {
//                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
//            }
//        }
//        FunctionCall.DismissDialog(AppointmentDetail.this);
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    private void setUpMapsWithLocations() {
        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.my_map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
//         mapFragment.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap googleMap) {
//                LatLng location = new LatLng(28.6076, 77.3683);
//                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoom));
//                Marker marker = googleMap.addMarker(new MarkerOptions().position(location));
//            }
//        });
//
    }

    private BitmapDescriptor bitmapFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        assert vectorDrawable != null;
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng sydney;
        if (!(latitude > 1.000) || !(longitude > 1.0000)) {
            sydney = new LatLng(latitude, longitude);
        } else {
            sydney = new LatLng(28.6076, 77.3683);
        }
        googleMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title(latitude + " , " + longitude + " ")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        googleMap.setOnMapClickListener(latLng -> {

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            googleMap.clear();
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
            googleMap.addMarker(markerOptions);
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