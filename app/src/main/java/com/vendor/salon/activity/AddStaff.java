package com.vendor.salon.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.vendor.salon.R;
import com.vendor.salon.data_Class.addstaff.AddStaffResponse;
import com.vendor.salon.data_Class.getStaff.DataItem;
import com.vendor.salon.databinding.ActivityAddStaffBinding;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.Compress;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.GetFileFromUriUsingBufferReader;
import com.vendor.salon.utilityMethod.NetworkChangeListener;
import com.vendor.salon.utilityMethod.loginResponsePref;

import java.io.File;
import java.util.Calendar;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddStaff extends AppCompatActivity {

    private ActivityAddStaffBinding addStaffBinding;
    private File Staff_Img_File;
    DataItem staff_data;
    String uses_type;
    private final NetworkChangeListener networkChangeListener = new NetworkChangeListener() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addStaffBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_staff);

        staff_data = (DataItem) getIntent().getSerializableExtra("staff_detail");
        uses_type = getIntent().getStringExtra("layout_head");
        if (uses_type.equals("edit")) {
            addStaffBinding.staffDob.setText(staff_data.getDob());
            addStaffBinding.GenderCustomer.setText(staff_data.getGender());
            addStaffBinding.tvHeaders.setText(R.string.edit_staff);
            addStaffBinding.staffName.setText(staff_data.getName());
            Glide.with(addStaffBinding.staffImage.getContext()).load(staff_data.getOwnerImage()).error(R.drawable.no_image).into(addStaffBinding.staffImage);
            addStaffBinding.staffEmail.setText(staff_data.getEmail());
            addStaffBinding.staffServices.setText(staff_data.getDesignation());
            addStaffBinding.staffPhoneNo.setText(staff_data.getPhone());
        }
        addStaffBinding.dobCard.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog picker = new DatePickerDialog(AddStaff.this, new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    addStaffBinding.staffDob.setText(String.format("%d/%d/%d", day, month + 1, year));
                }
            }, year, month, day);
            picker.show();
            cal.add(Calendar.YEAR, -18);
            picker.getDatePicker().setMaxDate(cal.getTimeInMillis() );
//            dataPicDialog.datePicker.maxDate();
        });

        addStaffBinding.editProfileBtn.setOnClickListener(view -> ImagePicker.with(AddStaff.this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                        1080,
                        1080
                )   //Final image resolution will be less than 1080 x 1080(Optional)
                .start());

        addStaffBinding.GenderCustomerCard.setOnClickListener(view -> {
            if (addStaffBinding.genderCustomerItemCard.getVisibility() == View.VISIBLE) {
                addStaffBinding.genderCustomerItemCard.setVisibility(View.GONE);
                addStaffBinding.customerMale.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addStaffBinding.GenderCustomer.setText(addStaffBinding.customerMale.getText().toString());
                        if (addStaffBinding.genderCustomerItemCard.getVisibility() == View.VISIBLE) {
                            addStaffBinding.genderCustomerItemCard.setVisibility(View.GONE);
                        }
                    }
                });

                addStaffBinding.customerFemale.setOnClickListener(view1 -> {
                    addStaffBinding.GenderCustomer.setText(addStaffBinding.customerFemale.getText().toString());
                    if (addStaffBinding.genderCustomerItemCard.getVisibility() == View.VISIBLE) {
                        addStaffBinding.genderCustomerItemCard.setVisibility(View.GONE);
                    }
                });
            } else {
                addStaffBinding.genderCustomerItemCard.setVisibility(View.VISIBLE);
                addStaffBinding.customerMale.setOnClickListener(view12 -> {
                    addStaffBinding.GenderCustomer.setText(addStaffBinding.customerMale.getText().toString());
                    if (addStaffBinding.genderCustomerItemCard.getVisibility() == View.VISIBLE) {
                        addStaffBinding.genderCustomerItemCard.setVisibility(View.GONE);
                    }
                });

                addStaffBinding.customerFemale.setOnClickListener(view13 -> {
                    addStaffBinding.GenderCustomer.setText(addStaffBinding.customerFemale.getText().toString());
                    if (addStaffBinding.genderCustomerItemCard.getVisibility() == View.VISIBLE) {
                        addStaffBinding.genderCustomerItemCard.setVisibility(View.GONE);
                    }
                });
            }
        });
        addStaffBinding.back.setOnClickListener(view -> {
            Intent intent = new Intent(AddStaff.this, Staff.class);
            startActivity(intent);
            finish();
        });

        addStaffBinding.submit.setOnClickListener(view -> addstaffToDbs());
    }

    private void addstaffToDbs() {
        FunctionCall.showProgressDialog(AddStaff.this);
        String token = "Bearer " + loginResponsePref.getInstance(AddStaff.this).getToken();


        RequestBody staff_Img_Body ;
        MultipartBody.Part staff_img_part_val = null;
        if (Staff_Img_File != null) {
            staff_Img_Body = RequestBody.create(MediaType.parse("owner_image"), Compress.images(Staff_Img_File.getPath(),  1 * 1024 * 1024 ));
            staff_img_part_val = MultipartBody.Part.createFormData("owner_image", Staff_Img_File.getName(), staff_Img_Body);
        }
        Call<AddStaffResponse> call;
        if (uses_type.equals("edit")) {
            call  = RetrofitClient.getVendorService().editStaffElements(token,
                    getRequestBody(Objects.requireNonNull(addStaffBinding.staffName.getText()).toString()),
                    getRequestBody(Objects.requireNonNull(addStaffBinding.staffPhoneNo.getText()).toString()),
                    getRequestBody(addStaffBinding.staffDob.getText().toString()),
                    getRequestBody(addStaffBinding.GenderCustomer.getText().toString()),
                    getRequestBody(Objects.requireNonNull(addStaffBinding.staffEmail.getText()).toString()),
                    getRequestBody("+91"),
                    staff_img_part_val,
                    getRequestBody(staff_data.getId() + ""));

        } else {
            call = RetrofitClient.getVendorService().addStaffElements(token,
                    getRequestBody(Objects.requireNonNull(addStaffBinding.staffName.getText()).toString()),
                    getRequestBody(Objects.requireNonNull(addStaffBinding.staffPhoneNo.getText()).toString()),
                    getRequestBody(addStaffBinding.staffDob.getText().toString()),
                    getRequestBody(addStaffBinding.GenderCustomer.getText().toString()),
                    getRequestBody(Objects.requireNonNull(addStaffBinding.staffEmail.getText()).toString()),
                    getRequestBody("+91"),
                    staff_img_part_val);
        }
        call.enqueue(new Callback<AddStaffResponse>() {
            @Override
            public void onResponse(@NonNull Call<AddStaffResponse> call, @NonNull Response<AddStaffResponse> response) {
                FunctionCall.DismissDialog(AddStaff.this);
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    Toast.makeText(AddStaff.this, " " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent staffIntent = new Intent(AddStaff.this, Staff.class);
                    startActivity(staffIntent);
                    finishAffinity();
                } else {
                    if (response.body() != null) {
                        Toast.makeText(AddStaff.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Log.d("addstaffhit", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddStaffResponse> call, @NonNull Throwable t) {
                FunctionCall.DismissDialog(AddStaff.this);
                Log.d("addstaffhit", "onFailure: " + t.getMessage());
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Staff_Img_File = GetFileFromUriUsingBufferReader.getImageFile(this, Objects.requireNonNull(data).getData());
            if (data.getData() != null) {
                if (Staff_Img_File != null) {
                    addStaffBinding.staffImage.setImageURI(Uri.parse(Staff_Img_File.getPath()));
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, "Cancelled. ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Something went wrong ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddStaff.this, Staff.class);
        startActivity(intent);
        finish();
    }

    private RequestBody getRequestBody(String str) {
        return RequestBody.create(str, MediaType.parse("text/plain"));
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