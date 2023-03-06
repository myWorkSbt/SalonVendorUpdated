package com.vendor.salon.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vendor.salon.R;
import com.vendor.salon.adapters.ServicesListAdapter;
import com.vendor.salon.data_Class.category_services.CategoriesItem;
import com.vendor.salon.data_Class.category_services.CategoryServicesResponse;
import com.vendor.salon.data_Class.vendor_sub_catgories.DataItem;
import com.vendor.salon.data_Class.vendor_sub_catgories.VendorSubCategoryResponse;
import com.vendor.salon.databinding.ActivityServiceBinding;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.loginResponsePref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryDetails extends AppCompatActivity {

    private static final String TAG = "vendor-sub-catgory-hit";
    private ActivityServiceBinding serviceBinding;
    String gender = "Male";
    List<DataItem>  serviceList;
    ServicesListAdapter servicesAdapter;
    private int received_category_id;
    private String category_gender;
    String token;
    boolean isSaloonButtonSelected = true ;
    String is_DoorStep = "" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceBinding = ActivityServiceBinding.inflate(getLayoutInflater());
        setContentView(serviceBinding.getRoot());
        received_category_id = getIntent().getIntExtra("id", -1);
        String category_name = getIntent().getStringExtra("names") ;
        category_gender = getIntent().getStringExtra("gender");
        token = "Bearer " + loginResponsePref.getInstance(CategoryDetails.this).getToken();
        serviceBinding.serviceItemsList.setLayoutManager(new LinearLayoutManager(CategoryDetails.this));
        getServicesValue();
         serviceBinding.tvHeader.setText( category_name);

        serviceBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentss = new Intent(CategoryDetails.this, Home.class);
                startActivity(intentss);
                finishAffinity();
            }
        });



        serviceBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getServicesValue();
                serviceBinding.swipeRefreshLayout.setRefreshing(false);
            }
        });


        serviceBinding.switchRoomAvailability.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    gender = "Female";
                    serviceBinding.MaleTv.setVisibility(View.GONE);
                    serviceBinding.femaleTv.setVisibility(View.VISIBLE);
                    serviceBinding.switchRoomAvailability.setThumbDrawable(getDrawable(R.drawable.female_thumb));
                     getServicesValue();
                } else {
                    gender = "Male";
                    serviceBinding.femaleTv.setVisibility(View.GONE);
                    serviceBinding.switchRoomAvailability.setThumbDrawable(getDrawable(R.drawable.custom_thumb));
                    serviceBinding.MaleTv.setVisibility(View.VISIBLE);
                    getServicesValue();
                }
            }
        });


        serviceBinding.salonButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSaloonButtonSelected) {
                    isSaloonButtonSelected = true;
                    serviceBinding.DoorStep.setBackground(getDrawable(R.drawable.cardcorner_whit_cut));
                    serviceBinding.salonButton3.setBackground(getDrawable(R.drawable.bg_saloonbutton));
                    serviceBinding.salonButton3.setTextColor(Color.WHITE);
                    serviceBinding.DoorStep.setTextColor(Color.BLACK);
                    is_DoorStep = "";
                    getServicesValue();

                }
            }
        });

        serviceBinding.DoorStep.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (isSaloonButtonSelected) {
                    isSaloonButtonSelected = false;
                    serviceBinding.salonButton3.setBackground(getDrawable(R.drawable.cardcorner_whit_cut));
                    serviceBinding.salonButton3.setTextColor(Color.BLACK);
                    serviceBinding.DoorStep.setBackground(getDrawable(R.drawable.bg_saloonbutton));
                    serviceBinding.DoorStep.setTextColor(Color.WHITE);
                    is_DoorStep = "2";
                    getServicesValue();
                }
            }
        });




    }

    private void getServicesValue() {
        FunctionCall.showProgressDialog(CategoryDetails.this);
        String token =  "Bearer "  + loginResponsePref.getInstance(CategoryDetails.this).getToken();
        Call<VendorSubCategoryResponse> call =  RetrofitClient.getVendorService().getServiceFilteredData( token , received_category_id +"" , gender , is_DoorStep ) ;
        call.enqueue(new Callback<VendorSubCategoryResponse>() {
            @Override
            public void onResponse(Call<VendorSubCategoryResponse> call, Response<VendorSubCategoryResponse> response) {
                FunctionCall.DismissDialog(CategoryDetails.this);
                if (response.isSuccessful() && response.body() != null && response.body().isStatus() ) {
                     setServicesItems( response.body().getData());
                }
                else {
                    if (response.body() != null ) {
                        Toast.makeText(CategoryDetails.this, "" + response.body().getMessage() , Toast.LENGTH_SHORT).show();
                    }
                    Log.d(TAG, "onResponse: " + response );
                }
            }

            @Override
            public void onFailure(Call<VendorSubCategoryResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage() );
                FunctionCall.DismissDialog(CategoryDetails.this);
            }
        });
    }


    private void setServicesItems(List<DataItem> categoryServicesResponse) {
        serviceList = categoryServicesResponse;
            servicesAdapter = new ServicesListAdapter( CategoryDetails.this,serviceList);
            if (serviceList == null || serviceList.size() == 0 ) {
                    serviceBinding.showNoDataText.setVisibility(View.VISIBLE);
            }
            else {
                 serviceBinding.showNoDataText.setVisibility(View.GONE );
            }
        serviceBinding.serviceItemsList.setAdapter(servicesAdapter );

    }

    @Override
    public void onBackPressed() {
        Intent intents = new Intent(CategoryDetails.this, Home.class);
        startActivity(intents);
        finishAffinity();
    }
}
