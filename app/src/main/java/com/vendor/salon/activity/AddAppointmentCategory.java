package com.vendor.salon.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationChannelCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.vendor.salon.R;
import com.vendor.salon.adapters.AppointmentCategoryAdapter;
import com.vendor.salon.data_Class.appointment_categories.AppointmentCategoriesResponse;
import com.vendor.salon.data_Class.appointment_categories.DataItem;
import com.vendor.salon.data_Class.appointmentservicesedit.AppointmentServicesEditResponse;
import com.vendor.salon.databinding.ActivityAddAppointmentCategoryBinding;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.OnClickListener;
import com.vendor.salon.utilityMethod.loginResponsePref;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAppointmentCategory extends AppCompatActivity {

    private String gender = "Male";
    private AppointmentCategoryAdapter appointmentCategoryAdapter;
    private ActivityAddAppointmentCategoryBinding addAppointmentCategoryBinding;
    private int appointment_id;
    private String is_doorstep = "1";
    private boolean isSaloonButtonSelected = true;
    private List<DataItem> appointmentCategoryList = new ArrayList<>();
    public static boolean isintialentry = true ;
    HashMap<Integer, List<com.vendor.salon.data_Class.appointment_sub_categories.DataItem>> unconfirmed_category_list = new HashMap<>();
    HashMap<Integer, Boolean > isNotInitialList = new HashMap<>();
    private boolean isApiCalled = false ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addAppointmentCategoryBinding = DataBindingUtil.setContentView(AddAppointmentCategory.this, R.layout.activity_add_appointment_category);
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == AddClientActivity.RESULT_OK) {
                    Intent resultData = result.getData();

                    List<com.vendor.salon.data_Class.appointment_sub_categories.DataItem> unconfirmed_serives_list = (List<com.vendor.salon.data_Class.appointment_sub_categories.DataItem>) resultData.getSerializableExtra("new_services_list");

                    isintialentry = false ;
                    int changed_category_position = resultData.getIntExtra("Category_position", -1);
                    unconfirmed_category_list.put(changed_category_position, unconfirmed_serives_list);

                    int final_category_amount = resultData.getIntExtra("final_amount" ,  -1 );
                    if (final_category_amount != -1 ) {
                        appointmentCategoryList.get(changed_category_position).setSum(final_category_amount);
                    }

                    int   selected_services_count = resultData.getIntExtra("no_of_selected_services" , -1 );
                    if (selected_services_count > 0 ) {
                        appointmentCategoryList.get(changed_category_position).setSelected("1");
                        appointmentCategoryList.get(changed_category_position).setCount(String.valueOf(selected_services_count));
                    }
                     else {
                         appointmentCategoryList.get(changed_category_position).setSelected("0");
                     }

                    boolean isintialentry = resultData.getBooleanExtra("isintialentry", false) ;
                    if (isintialentry) {
                        isNotInitialList.put(changed_category_position, true);
                    }

                    String selected_service_name  = resultData.getStringExtra("selected_service_name");
                    appointmentCategoryList.get(changed_category_position).setServiceName(selected_service_name);

                    int resul_counts = resultData.getIntExtra("no_of_selected_services" , -1 ) ;
                    if (resul_counts  > 0) {
                        appointmentCategoryList.get(changed_category_position).setCount(String.valueOf(resul_counts ));
                    }

                    appointmentCategoryAdapter.refreshList(appointmentCategoryList);
                }
            }
        });
        appointmentCategoryAdapter = new AppointmentCategoryAdapter(AddAppointmentCategory.this, appointmentCategoryList, (context, position) -> {
            DataItem categoriesItem = appointmentCategoryList.get(position);
            Intent intents = new Intent(context.getApplicationContext(), AddAppointmentServices.class);
            intents.putExtra("id", categoriesItem.getId());
            intents.putExtra("names", categoriesItem.getName());
            intents.putExtra("gender", gender);
            intents.putExtra("is_doorstep", is_doorstep);
            intents.putExtra("appointment_id" , appointment_id );
            intents.putExtra("isintialentry", !Boolean.TRUE.equals(isNotInitialList.get(position)));
            if (!isintialentry ) {
                intents.putExtra("service_List", (Serializable) unconfirmed_category_list.get(position));
            }
            else {
                intents.putExtra("service_List" , new ArrayList<>());
            }
            intents.putExtra("changed_category_position" , position );
            launcher.launch(intents);
        });
        addAppointmentCategoryBinding.appointmentCategoryRecycler.setAdapter(appointmentCategoryAdapter);
        appointment_id = getIntent().getIntExtra("appointment_id", -1);
        isintialentry = getIntent().getBooleanExtra("isintialentry", false);
        if (Home.services_gender.equalsIgnoreCase("mens")) {
            gender = "Male";
//            addAppointmentCategoryBinding.switchLays.setVisibility(View.GONE);
        } else if (Home.services_gender.equalsIgnoreCase("unisex")) {
            gender = "Male";
            addAppointmentCategoryBinding.switchLays.setVisibility(View.VISIBLE);
        } else if (Home.services_gender.equalsIgnoreCase("womens")) {
            gender = "Female";
//            addAppointmentCategoryBinding.switchLays.setVisibility(View.GONE);
        }

        getAppointmentCategories(appointment_id);
        addAppointmentCategoryBinding.btnBack.setOnClickListener(View -> finish());

        addAppointmentCategoryBinding.switchOnes.setOnCheckedChangeListener((compoundButton, b) -> {
            if (!b) {
                gender = "Female";
                addAppointmentCategoryBinding.MaleTv.setVisibility(View.GONE);
                addAppointmentCategoryBinding.femaleTv.setVisibility(View.VISIBLE);
                addAppointmentCategoryBinding.switchOnes.setThumbDrawable(getDrawable(R.drawable.manage_services_female_custom_thumb));
            } else {
                gender = "Male";
                addAppointmentCategoryBinding.femaleTv.setVisibility(View.GONE);
                addAppointmentCategoryBinding.MaleTv.setVisibility(View.VISIBLE);
                addAppointmentCategoryBinding.switchOnes.setThumbDrawable(getDrawable(R.drawable.custom_thumb));
            }
            getAppointmentCategories(appointment_id);
        });

        addAppointmentCategoryBinding.btnApply.setOnClickListener(View -> {
            if (!isApiCalled) {
                isApiCalled = true ;
                FunctionCall.showProgressDialog(AddAppointmentCategory.this);
                String token = "Bearer " + loginResponsePref.getInstance(AddAppointmentCategory.this).getToken();
                Call<AppointmentServicesEditResponse> call = RetrofitClient.getVendorService().confirmEditAppointmentServices(token, appointment_id);
                call.enqueue(new Callback<AppointmentServicesEditResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<AppointmentServicesEditResponse> call, @NonNull Response<AppointmentServicesEditResponse> response) {
                        FunctionCall.DismissDialog(AddAppointmentCategory.this);
                       isApiCalled = false ;
                        if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                            Toast.makeText(AddAppointmentCategory.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Log.d("TAG", "onResponse: " + Objects.requireNonNull(response.body()).getMessage());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AppointmentServicesEditResponse> call, @NonNull Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage() );
                        FunctionCall.DismissDialog(AddAppointmentCategory.this);
                        isApiCalled = false ;
                    }
                });
            }
        });
        addAppointmentCategoryBinding.salonButton.setOnClickListener(view -> {
            if (!isSaloonButtonSelected) {
                isSaloonButtonSelected = true;
                addAppointmentCategoryBinding.DoorStep.setBackground(getDrawable(R.drawable.cardcorner_whit_cut));
                addAppointmentCategoryBinding.salonButton.setBackground(getDrawable(R.drawable.bg_saloonbutton));
                addAppointmentCategoryBinding.salonButton.setTextColor(Color.WHITE);
                addAppointmentCategoryBinding.DoorStep.setTextColor(Color.BLACK);
                is_doorstep = "";
                getAppointmentCategories(appointment_id);

            }
        });

        addAppointmentCategoryBinding.DoorStep.setOnClickListener(view -> {
            if (isSaloonButtonSelected) {
                isSaloonButtonSelected = false;
                addAppointmentCategoryBinding.salonButton.setBackground(getDrawable(R.drawable.cardcorner_whit_cut));
                addAppointmentCategoryBinding.DoorStep.setBackground(getDrawable(R.drawable.bg_saloonbutton));
                addAppointmentCategoryBinding.DoorStep.setTextColor(Color.WHITE);
                addAppointmentCategoryBinding.salonButton.setTextColor(Color.BLACK);
                is_doorstep = "";
                getAppointmentCategories(appointment_id);

            }
        });


    }

    private void getAppointmentCategories(int appointment_id) {
        FunctionCall.showProgressDialog(AddAppointmentCategory.this);
        String token = "Bearer " + loginResponsePref.getInstance(AddAppointmentCategory.this).getToken();
        Call<AppointmentCategoriesResponse> call = RetrofitClient.getVendorService().getAppointmentCategories(token, appointment_id, gender, is_doorstep);
        call.enqueue(new Callback<AppointmentCategoriesResponse>() {
            @Override
            public void onResponse(@NonNull Call<AppointmentCategoriesResponse> call, @NonNull Response<AppointmentCategoriesResponse> response) {
                FunctionCall.DismissDialog(AddAppointmentCategory.this);
                if (response.isSuccessful() && response.body() != null && response.body().isStatus() && response.body().getData() != null && response.body().getData().size() > 0) {
                    appointmentCategoryList = response.body().getData();
                    addAppointmentCategoryBinding.showNoDataText.setVisibility(View.GONE);
                    isNotInitialList = new HashMap<>();
                    for (int i =0 ;  i< response.body().getData().size() ; i++ ) {
                        isNotInitialList.put(i, false) ;
                    }
                    appointmentCategoryAdapter.refreshList(appointmentCategoryList);
                } else {
                    addAppointmentCategoryBinding.showNoDataText.setVisibility(View.VISIBLE);
                    if (response.body() != null) {
                        Toast.makeText(AddAppointmentCategory.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Log.d("appointmentcategorieshit", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AppointmentCategoriesResponse> call, @NonNull Throwable t) {
                FunctionCall.DismissDialog(AddAppointmentCategory.this);
                Log.d("appointmentcategorieshit", "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}