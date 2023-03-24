package com.vendor.salon.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.vendor.salon.R;
import com.vendor.salon.adapters.AppointmentServicesAdapter;
import com.vendor.salon.data_Class.appointment_sub_categories.AppointmentSubCategoriesResponse;
import com.vendor.salon.data_Class.appointment_sub_categories.DataItem;
import com.vendor.salon.data_Class.appointmentservicesedit.AppointmentServicesEditResponse;
import com.vendor.salon.databinding.ActivityAddAppointmentServicesBinding;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.NetworkChangeListener;
import com.vendor.salon.utilityMethod.OnAppointmentServiceUpdateButtonClick;
import com.vendor.salon.utilityMethod.loginResponsePref;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAppointmentServices extends AppCompatActivity {

    private ActivityAddAppointmentServicesBinding addAppointmentServicesBinding;
    private String category_id;
    private String gender;
    private String is_doorstep;
    private AppointmentServicesAdapter appointmentServicesAdapter;
    private boolean isintialentry;
    private List<DataItem> service_List = new ArrayList<>();
    private int changed_category_position;
    private int final_amount;
    private String selected_service_name;
    private int appointment_id;
    public static boolean isChangedSuccessfully = false ;
    private final NetworkChangeListener networkChangeListener = new NetworkChangeListener() ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addAppointmentServicesBinding = DataBindingUtil.setContentView(AddAppointmentServices.this, R.layout.activity_add_appointment_services);

        category_id = String.valueOf(getIntent().getIntExtra("id" , -1 ));
        String category_name = getIntent().getStringExtra("names");
        gender = getIntent().getStringExtra("gender");
        isintialentry = getIntent().getBooleanExtra("isintialentry", true);
        is_doorstep = getIntent().getStringExtra("is_doorstep");
        appointment_id = getIntent().getIntExtra("appointment_id" , -1 );
        changed_category_position = getIntent().getIntExtra("changed_category_position", -1);
        service_List = (List<DataItem>) getIntent().getSerializableExtra("service_List");
        appointmentServicesAdapter = new AppointmentServicesAdapter(AddAppointmentServices.this, new ArrayList<>(), new OnAppointmentServiceUpdateButtonClick() {
            @Override
            public void OnPlusButtonClick(Context tv_count_context, Context btn_minus_context, Context service_main_lays_context, int count, int position) {
                int old_amount =  service_List.get(position).getOfferPrice() * count ;
                int new_count_value = count +1 ;
                isChangedSuccessfully = false ;
                int new_price_sum  = service_List.get(position).getOfferPrice() * new_count_value ;
                int service_id  = service_List.get(position).getId();
                changeServicesValue(new_count_value , service_id , old_amount , new_price_sum , position, count, new_count_value , true );
            }

            @Override
            public void OnMinusButtonClick(Context tv_count_context, Context btn_minus_context, Context service_main_lays_context, int count, int position) {
                int old_amount =  service_List.get(position).getOfferPrice() * count ;
                int new_count_value = count -1 ;
                isChangedSuccessfully = false ;
                int new_price_sum  = service_List.get(position).getOfferPrice() * new_count_value ;
                int service_id  = service_List.get(position).getId();
                changeServicesValue(new_count_value , service_id , old_amount , new_price_sum , position , count , new_count_value, false );

            }

        });

        addAppointmentServicesBinding.appointmentServicesRecycler.setAdapter(appointmentServicesAdapter);
        if (isintialentry) {
            getAppointmentServicesData();
        } else {
            setCategoryServiceData(service_List);
        }

        addAppointmentServicesBinding.btnBack.setOnClickListener(View -> returnBackValue());
         addAppointmentServicesBinding.tvCategoryHeaders.setText(category_name);

    }

    private void changeServicesValue(int qty, int service_id, int old_amount, int new_price_sum , int position , int count , int new_count_value , boolean isPlusPressed ) {
        FunctionCall.showProgressDialog(AddAppointmentServices.this);
        String token = "Bearer " + loginResponsePref.getInstance(AddAppointmentServices.this).getToken();
        Call<AppointmentServicesEditResponse> call = RetrofitClient.getVendorService().editAppointmentService(token , appointment_id , service_id , qty ) ;
        call.enqueue(new Callback<AppointmentServicesEditResponse>() {
            @Override
            public void onResponse(@NonNull Call<AppointmentServicesEditResponse> call, @NonNull Response<AppointmentServicesEditResponse> response) {
                FunctionCall.DismissDialog(AddAppointmentServices.this);
                if (response.isSuccessful() && response.body() != null && response.body().isStatus() ) {
                    Toast.makeText(AddAppointmentServices.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    if (final_amount-old_amount > -1) {
                        final_amount -= old_amount;
                    }
                    final_amount += new_price_sum ;
                    isChangedSuccessfully = true ;
                    selected_service_name = service_List.get(position).getServiceName();
                    if (isPlusPressed) {
                        service_List.get(position).setQty(String.valueOf(new_count_value));
                        service_List.get(position).setSeleted(true);
                        if (count == 0) {

                        } else {
                            service_List.get(position).setSeleted(false);
                        }
                    }
                    else {
                        service_List.get(position).setQty(String.valueOf(new_count_value));
                        service_List.get(position).setSeleted(count != 1);
                    }
                    appointmentServicesAdapter.notifyItemChanged(position);
             }
                else {
                    if (response.body() != null ) {
                        isintialentry = false ;
                        Toast.makeText(AddAppointmentServices.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Log.d("appointmentserviceshit", "onResponse: " + response.body() );
                }
            }

            @Override
            public void onFailure(@NonNull Call<AppointmentServicesEditResponse> call, @NonNull Throwable t) {
                FunctionCall.DismissDialog(AddAppointmentServices.this );
                isintialentry = false ;
                Log.d("appointmentserviceshit", "onFailure: " + t.getMessage() );
            }
        });
    }

    private void returnBackValue() {
        int no_of_selected_services = 0 ;
        final_amount = 0 ;
        for (DataItem serviceOne : service_List ) {
            int qtyies = Integer.parseInt(serviceOne.getQty()) ;
            if (qtyies>0 ) {
                final_amount +=  Integer.parseInt(serviceOne.getQty()) * serviceOne.getOfferPrice() ;
                selected_service_name =  serviceOne.getServiceName() ;
                no_of_selected_services ++ ;
            }
        }
        Intent returnResultIntent = new Intent(AddAppointmentServices.this, AddAppointmentCategory.class);
        returnResultIntent.putExtra("Category_position", changed_category_position);
        returnResultIntent.putExtra("final_amount", final_amount);
        returnResultIntent.putExtra("no_of_selected_services", no_of_selected_services);
        returnResultIntent.putExtra("new_services_list", (Serializable) service_List);
        returnResultIntent.putExtra("isintialentry", isintialentry);
        returnResultIntent.putExtra("selected_service_name", selected_service_name);

        setResult(RESULT_OK, returnResultIntent);
        finish();
    }

    private void setCategoryServiceData(List<DataItem> services_list) {
        this.service_List = services_list ;
        appointmentServicesAdapter.refreshLists(services_list);
        addAppointmentServicesBinding.showNoDataText.setVisibility(View.GONE);

    }

    private void getAppointmentServicesData() {
        String token = "Bearer " + loginResponsePref.getInstance(AddAppointmentServices.this).getToken();
        FunctionCall.showProgressDialog(AddAppointmentServices.this);
        Call<AppointmentSubCategoriesResponse> call = RetrofitClient.getVendorService().getAppointmentCategoryServices(token, category_id, gender, is_doorstep);
        call.enqueue(new Callback<AppointmentSubCategoriesResponse>() {
            @Override
            public void onResponse(@NonNull Call<AppointmentSubCategoriesResponse> call, @NonNull Response<AppointmentSubCategoriesResponse> response) {
                FunctionCall.DismissDialog(AddAppointmentServices.this);
                if (response.isSuccessful() && response.body() != null && response.body().isStatus() && response.body().getData() != null && response.body().getData().size() > 0) {
                    setCategoryServiceData(response.body().getData());

                } else {
                    if (response.body() != null)
                        Toast.makeText(AddAppointmentServices.this, " " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("appointmentsubcategorieshit", "onResponse: " + response.body());
                    addAppointmentServicesBinding.showNoDataText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AppointmentSubCategoriesResponse> call, @NonNull Throwable t) {
                Log.d("appointmentsubcategorieshit", "onFailure: " + t.getMessage());
                FunctionCall.DismissDialog(AddAppointmentServices.this);
            }
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
    @Override
    public void onBackPressed() {
        returnBackValue();
    }
}