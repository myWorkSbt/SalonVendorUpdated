package com.vendor.salon.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.vendor.salon.data_Class.editprofile.EditProfileResponse;
import com.vendor.salon.data_Class.getProfile.GetProfileResponse;
import com.vendor.salon.data_Class.getProfile.OwnerDetail;
import com.vendor.salon.databinding.ActivityEditOwnerBinding;
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

public class EditOwner extends AppCompatActivity {

    private ActivityEditOwnerBinding editOwnerBinding;
    private File OwnerImageFile = null;
    private boolean isApiCalled = false ;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
//    PermissionUtils takePermissionUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editOwnerBinding = ActivityEditOwnerBinding.inflate(getLayoutInflater());
        setContentView(editOwnerBinding.getRoot());
//        takePermissionUtils=new PermissionUtils(this,mPermissionResult);

        GetOwnersData();
        editOwnerBinding.editProfileBtn.setOnClickListener(view -> {
//                 if (takePermissionUtils.isStorageCameraPermissionGranted()) {
            ImagePicker.with(EditOwner.this)
                    .crop()//Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(
                            1080,
                            1080
                    )   //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
//                 }
//                 else
//                 {
//                     takePermissionUtils.showStorageCameraPermissionDailog(getString(R.string.we_need_storage_and_camera_permission_for_upload_profile_pic));
//                 }

        });

        editOwnerBinding.genderCard.setOnClickListener(view -> {
            if (editOwnerBinding.genderItemCard.getVisibility() == View.VISIBLE) {
                editOwnerBinding.genderItemCard.setVisibility(View.GONE);

            } else {
                editOwnerBinding.genderItemCard.setVisibility(View.VISIBLE);

            }
            editOwnerBinding.male.setOnClickListener(view14 -> {
                editOwnerBinding.Gender.setText(editOwnerBinding.male.getText().toString());
                if (editOwnerBinding.genderItemCard.getVisibility() == View.VISIBLE) {
                    editOwnerBinding.genderItemCard.setVisibility(View.GONE);
                }
            });
            editOwnerBinding.female.setOnClickListener(view12 -> {
                editOwnerBinding.Gender.setText(editOwnerBinding.female.getText().toString());
                if (editOwnerBinding.genderItemCard.getVisibility() == View.VISIBLE) {
                    editOwnerBinding.genderItemCard.setVisibility(View.GONE);
                }
            });
        });


        editOwnerBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
            GetOwnersData();
            editOwnerBinding.swipeRefreshLayout.setRefreshing(false);
        });


        editOwnerBinding.dobCard.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog picker = new DatePickerDialog(EditOwner.this, (datePicker, year1, month1, day1) -> editOwnerBinding.dob.setText(day1 + "/" + (month1 + 1) + "/" + year1), year, month, day);
            picker.show();
            picker.getDatePicker().setMaxDate(cal.getTimeInMillis());
//            dataPicDialog.datePicker.maxDate();
        });

        editOwnerBinding.submit.setOnClickListener(view -> {
            editOwnerBinding.progressBar.setVisibility(View.VISIBLE);
            RequestBody ProfImgBody;
            MultipartBody.Part Owner_part_val = null;
            if (OwnerImageFile != null) {
                ProfImgBody = RequestBody.create(MediaType.parse("owner_image"), Compress.images(OwnerImageFile.getPath() , 1024 * 1024) ) ;
                Owner_part_val = MultipartBody.Part.createFormData("owner_image", OwnerImageFile.getName(), ProfImgBody);
            }
            if (Objects.requireNonNull(editOwnerBinding.ownerName.getText()).toString().isEmpty()) {
                editOwnerBinding.ownerName.setError("Mandatory");
                editOwnerBinding.ownerName.requestFocus();
            } else if (editOwnerBinding.dob.getText().toString().isEmpty()) {
                editOwnerBinding.dob.setError("mandatory Field! ");
                editOwnerBinding.dob.requestFocus();
            } else if (Objects.requireNonNull(editOwnerBinding.email.getText()).toString().isEmpty()) {
                editOwnerBinding.email.setError(" Mandatory Field! ");
            } else if (editOwnerBinding.Gender.getText().toString().isEmpty()) {
                editOwnerBinding.dob.setError("mandatory Field! ");
                editOwnerBinding.dob.requestFocus();
            } else {
                String tokens = "Bearer " + loginResponsePref.getInstance(EditOwner.this).getToken();
                Call<EditProfileResponse> call = RetrofitClient.getVendorService().EditOwnerDetails(tokens, getRequestBody(editOwnerBinding.ownerName.getText().toString()), getRequestBody(editOwnerBinding.phoneNo.getText().toString()), getRequestBody(editOwnerBinding.dob.getText().toString()), getRequestBody("" + editOwnerBinding.email.getText().toString()), getRequestBody(editOwnerBinding.Gender.getText().toString()), getRequestBody("+91"), getRequestBody(Objects.requireNonNull(editOwnerBinding.serviceName.getText()).toString()), Owner_part_val, getRequestBody("owner"));
                call.enqueue(new Callback<EditProfileResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<EditProfileResponse> call, @NonNull Response<EditProfileResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().isResult()) {
                                Toast.makeText(EditOwner.this, " " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                                Intent editProfileIntent = new Intent(EditOwner.this, EditProfile.class);
//                                startActivity(editProfileIntent);
                                finish();
                            } else {
                                Log.d("editProfilehit", " " + response.body().getMessage());
                                Toast.makeText(EditOwner.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (response.body() != null) {
                                Toast.makeText(EditOwner.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            Log.d("editprofilehit", "onResponse: " + response.body());
                        }
                        editOwnerBinding.progressBar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(@NonNull Call<EditProfileResponse> call, @NonNull Throwable t) {

                        Log.d("editprofilehit", "onFailure: " + t.getMessage());
                        editOwnerBinding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(EditOwner.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        editOwnerBinding.back.setOnClickListener(view -> {
//                Intent editProfileIntent = new Intent(EditOwner.this, EditProfile.class);
//                startActivity(editProfileIntent);
            finish();
        });
    }

    private void GetOwnersData() {
    if (!isApiCalled) {

        FunctionCall.showProgressDialog(EditOwner.this);
        String token = "Bearer " + loginResponsePref.getInstance(EditOwner.this).getToken();
        Call<GetProfileResponse> call = RetrofitClient.getVendorService().getVendorDetails(token);
        call.enqueue(new Callback<GetProfileResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetProfileResponse> call, @NonNull Response<GetProfileResponse> response) {
                FunctionCall.DismissDialog(EditOwner.this);
                isApiCalled = false;
                if (response.isSuccessful() && response.body() != null) {
                    setData(response.body());
                } else {
                    if (response.body() != null) {
                        Toast.makeText(EditOwner.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Log.d("getprofilehit", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetProfileResponse> call, @NonNull Throwable t) {
                FunctionCall.DismissDialog(EditOwner.this);
                isApiCalled = false ;
                Log.d("getprofilehit", "onFailure: " + t.getMessage());
            }
        });
        editOwnerBinding.progressBar.setVisibility(View.GONE);
    }
    }

    private void setData(GetProfileResponse profileResponse) {
        OwnerDetail ownerDetail = profileResponse.getOwnerDetail();
        if (ownerDetail != null) {
            editOwnerBinding.ownerName.setText(ownerDetail.getName());
            editOwnerBinding.email.setText(ownerDetail.getEmail());
            editOwnerBinding.phoneNo.setText(ownerDetail.getPhone());
            String url_strs = ownerDetail.getUserImage();
            if (ownerDetail.getGender()!= null && ownerDetail.getGender().equalsIgnoreCase("MALE")) {
                editOwnerBinding.Gender.setText("Male");
            }
            else {
                editOwnerBinding.Gender.setText("Female");
            }
            editOwnerBinding.dob.setText(ownerDetail.getDob());
            editOwnerBinding.serviceName.setText(ownerDetail.getDesignation());
            Glide.with(editOwnerBinding.ownerImg.getContext()).load(Uri.parse(url_strs)).into(editOwnerBinding.ownerImg);
        }
    }


    private RequestBody getRequestBody(String str) {
        return RequestBody.create(str, MediaType.parse("text/plain"));
    }
//    private ActivityResultLauncher<String[]> mPermissionResult = registerForActivityResult(
//            new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
//                @RequiresApi(api = Build.VERSION_CODES.M)
//                @Override
//                public void onActivityResult(Map<String, Boolean> result) {
//
//                    boolean allPermissionClear=true;
//                    List<String> blockPermissionCheck=new ArrayList<>();
//                    for (String key : result.keySet())
//                    {
//                        if (!(result.get(key)))
//                        {
//                            allPermissionClear=false;
//                            blockPermissionCheck.add(FunctionsClass.getPermissionStatus(EditOwner.this,key));
//                        }
//                    }
//                    if (blockPermissionCheck.contains("blocked"))
//                    {
//                        FunctionsClass.showPermissionSetting(getApplicationContext(),getString(R.string.we_need_storage_and_camera_permission_for_upload_profile_pic));
//                    }
//                    else
//                    if (allPermissionClear)
//                    {
//                        selectImage();
//                    }
//
//                }
//            });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            OwnerImageFile = GetFileFromUriUsingBufferReader.getImageFile(this, data != null ? data.getData() : null);
            if (Objects.requireNonNull(data).getData() != null) {
                if (OwnerImageFile != null) {
                    editOwnerBinding.ownerImg.setImageURI(Uri.parse(OwnerImageFile.getPath()));
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
//        Intent editProfileIntent = new Intent(EditOwner.this, EditProfile.class);
//        startActivity(editProfileIntent);
        finish();
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