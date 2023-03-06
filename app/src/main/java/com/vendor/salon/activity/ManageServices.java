package com.vendor.salon.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.vendor.salon.R;
import com.vendor.salon.adapters.Manage_service_recyclerAdapter;
import com.vendor.salon.data_Class.home.CategoriesItem;
import com.vendor.salon.data_Class.home.HomeResponse;
import com.vendor.salon.data_Class.manage_service.DataItem;
import com.vendor.salon.data_Class.manage_service.ManageServiceResponse;
import com.vendor.salon.databinding.ActivityManageServicesBinding;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.loginResponsePref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageServices extends AppCompatActivity {

    private ActivityManageServicesBinding manageServicesBinding;
    List<CategoriesItem> list = new ArrayList<>();
    List<CategoriesItem> salonLists = new ArrayList<>();
    List<CategoriesItem> freeLancerList = new ArrayList<>();
    String gender = "Male";
    private LinearLayoutManager manager;
    private boolean isSaloonButtonSelected = true;
    private String vendor_type_selected = "";
    String is_DoorStep = "";
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manageServicesBinding = ActivityManageServicesBinding.inflate(getLayoutInflater());
        setContentView(manageServicesBinding.getRoot());

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//
        token = "Bearer " + loginResponsePref.getInstance(ManageServices.this).getToken();

        getData();

//        for (int i=0 ;i< list.size(); i++ ) {
//            if(list.get(i))
//        }


        manageServicesBinding.Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    gender = "Female";
                    manageServicesBinding.MaleTv.setVisibility(View.GONE);
                    manageServicesBinding.femaleTv.setVisibility(View.VISIBLE);
                    manageServicesBinding.Switch.setThumbDrawable(getDrawable(R.drawable.manage_services_female_custom_thumb));
                } else {
                    gender = "Male";
                    manageServicesBinding.femaleTv.setVisibility(View.GONE);
                    manageServicesBinding.MaleTv.setVisibility(View.VISIBLE);
                    manageServicesBinding.Switch.setThumbDrawable(getDrawable(R.drawable.custom_thumb));
                }
                getData();
            }
        });

        manager = new LinearLayoutManager(
                ManageServices.this,
                LinearLayoutManager.VERTICAL,
                false
        );

        manageServicesBinding.recyclerView.setLayoutManager(manager);


        manageServicesBinding.salonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSaloonButtonSelected) {
                    isSaloonButtonSelected = true;
                    manageServicesBinding.DoorStep.setBackground(getDrawable(R.drawable.cardcorner_whit_cut));
                    manageServicesBinding.salonButton.setBackground(getDrawable(R.drawable.bg_saloonbutton));
                    manageServicesBinding.salonButton.setTextColor(Color.WHITE);
                    manageServicesBinding.DoorStep.setTextColor(Color.BLACK);
                    is_DoorStep = "";
                    getData();

                }
            }
        });


        manageServicesBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                manageServicesBinding.swipeRefreshLayout.setRefreshing(false);
            }
        });


        manageServicesBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intents = new Intent(ManageServices.this, Home.class);
                startActivity(intents);
                finishAffinity();
            }
        });

        manageServicesBinding.btnSearch.setOnClickListener(View -> {
            getData();

        });

        manageServicesBinding.DoorStep.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (isSaloonButtonSelected) {
                    isSaloonButtonSelected = false;
                    manageServicesBinding.salonButton.setBackground(getDrawable(R.drawable.cardcorner_whit_cut));
                    manageServicesBinding.salonButton.setTextColor(Color.BLACK);
                    manageServicesBinding.DoorStep.setBackground(getDrawable(R.drawable.bg_saloonbutton));
                    manageServicesBinding.DoorStep.setTextColor(Color.WHITE);
                    is_DoorStep = "2";
                    getData();
                }
            }
        });

    }

    private void getData() {
        FunctionCall.showProgressDialog(ManageServices.this);

        Call<ManageServiceResponse> call = RetrofitClient.getVendorService().getSavedCategoryList(token, gender, is_DoorStep, manageServicesBinding.searchView.getText().toString());
        call.enqueue(new Callback<ManageServiceResponse>() {
            @Override
            public void onResponse(Call<ManageServiceResponse> call, Response<ManageServiceResponse> response) {
                FunctionCall.DismissDialog(ManageServices.this);
                if (response.isSuccessful() && response.body() != null && response.body().getData().size() > 0) {
                    manageServicesBinding.showNoDataText.setVisibility(View.GONE);
                    savelist(response.body().getData());
                } else {
                    manageServicesBinding.showNoDataText.setVisibility(View.VISIBLE);
                    if (response.body() != null) {
                        savelist(null);
                        Toast.makeText(ManageServices.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("manageservicehit", "onFailure: " + response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<ManageServiceResponse> call, Throwable t) {
                Log.d("manageservicehit", "onFailure: " + t.getMessage());
                FunctionCall.DismissDialog(ManageServices.this);
            }
        });
    }

    private void savelist(List<DataItem> categories) {
        Manage_service_recyclerAdapter adapter;
//        if(vendor_type_selected.equals("salon")){
//            for (CategoriesItem categoriesItem : categories) {
//                if()
//            }
//        }
//        else {
//
//        }
        if (categories == null) {
            adapter = null;
        } else {
            adapter = new Manage_service_recyclerAdapter(ManageServices.this, categories, gender);
        }
        manageServicesBinding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent intents = new Intent(ManageServices.this, Home.class);
        startActivity(intents);
        finishAffinity();
    }
}