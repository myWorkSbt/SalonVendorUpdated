package com.vendor.salon.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.vendor.salon.R;
import com.vendor.salon.data_Class.appointmentsfilter.AppointmentsFilterResponse;
import com.vendor.salon.data_Class.appointmentsfilter.AppointmentsItem;
import com.vendor.salon.databinding.ActivityAppointmentDetailBinding;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.loginResponsePref;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentDetail extends AppCompatActivity implements OnMapReadyCallback {

    private String recentAppointmentsPosition;
    private ActivityAppointmentDetailBinding appointmentDetailBinding;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appointmentDetailBinding = ActivityAppointmentDetailBinding.inflate(getLayoutInflater());
        setContentView(appointmentDetailBinding.getRoot());
        appointmentDetailBinding.detailHeads.setPaintFlags(appointmentDetailBinding.detailHeads.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        recentAppointmentsPosition = getIntent().getStringExtra("positions");

        setData(recentAppointmentsPosition);
//        getSupportFragmentManager().beginTransaction().replace(R.id.frag_containers, new MapFragment()).commit();

        appointmentDetailBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setData(recentAppointmentsPosition);
                appointmentDetailBinding.swipeRefreshLayout.setRefreshing(false);
            }
        });



        appointmentDetailBinding.btnAddRemoveServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addServicesIntents = new Intent(AppointmentDetail.this, AddServices.class );
                startActivity(addServicesIntents);
                finish();
            }
        });
    }

    private void setData(String user_id) {
        String token = "Bearer " + loginResponsePref.getInstance(AppointmentDetail.this).getToken();

        FunctionCall.showProgressDialog(AppointmentDetail.this);
        Call<AppointmentsFilterResponse> call = RetrofitClient.getVendorService().getAppointmentSearchedData( token, "","","","","",user_id+"");
        call.enqueue(new Callback<AppointmentsFilterResponse>() {
            @Override
            public void onResponse(Call<AppointmentsFilterResponse> call, Response<AppointmentsFilterResponse> response) {
                FunctionCall.DismissDialog(AppointmentDetail.this);
                if(response.isSuccessful() && response.body() != null && response.body().isStatus() ) {
                    List<AppointmentsItem> appointmentLists = response.body().getAppointments();
                    if(appointmentLists != null && appointmentLists.size() >0 ) {
                        AppointmentsItem appointments = appointmentLists.get(0);
                        if (appointments != null) {
                            appointmentDetailBinding.personName.setText(appointments.getUserName() + "");
                            appointmentDetailBinding.mobileNo.setText(appointments.getUserPhone() + "");
                            appointmentDetailBinding.address.setText(appointments.getUserLocation() + "");
                            if (appointments.getUserImage() != null) {
                                Glide.with(appointmentDetailBinding.personImage.getContext()).load(appointments.getUserImage()).into(appointmentDetailBinding.personImage);
                            }

                            String services_val = ""+ appointments.getServicesName();

                            appointmentDetailBinding.service.setText(services_val);
                            appointmentDetailBinding.location.setText(appointments.getUserLocation() + "");
                            latitude = Double.parseDouble(appointments.getUser_lat());
                            longitude = Double.parseDouble(appointments.getUser_lng());
                            setUpMapsWithLocations();
                        }
                    }

                }
                else {
                    Log.d( "appointmentsfilterhit", "onResponse: "+ response.body() );
                }
            }

            @Override
            public void onFailure(Call<AppointmentsFilterResponse> call, Throwable t) {
                FunctionCall.DismissDialog(AppointmentDetail.this);
                Log.d("appointmentsfilterhit", "onFailure:  "+ t.getMessage() );
            }
        });
        appointmentDetailBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void setUpMapsWithLocations() {
        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.my_map);
        mapFragment.getMapAsync(this );
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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng sydney ;
        if( !(latitude >1.000) || !(longitude > 1.0000) ) {
             sydney = new LatLng(latitude, longitude);
        }
        else {
            sydney = new LatLng(28.6076, 77.3683);
        }
         googleMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title(latitude+" , "+longitude+" ")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {

                MarkerOptions markerOptions =  new MarkerOptions();
                markerOptions.position(latLng);
                googleMap.clear();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                googleMap.addMarker(markerOptions);
            }
        });
    }
}