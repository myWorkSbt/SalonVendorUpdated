package com.vendor.salon.activity;

import android.app.Activity;
import android.content.ClipData;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.vendor.salon.adapters.EditSalonGalleryAdapter;
import com.vendor.salon.adapters.EditSaloonBannerAdapter;
import com.vendor.salon.data_Class.editprofile.EditProfileResponse;
import com.vendor.salon.data_Class.getProfile.GetProfileResponse;
import com.vendor.salon.data_Class.getProfile.SalonDetail;
import com.vendor.salon.databinding.ActivityEditSalonBinding;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.Compress;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.GetFileFromUriUsingBufferReader;
import com.vendor.salon.utilityMethod.NetworkChangeListener;
import com.vendor.salon.utilityMethod.loginResponsePref;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditSalon extends AppCompatActivity {

    private ActivityEditSalonBinding editSalonBinding;
    private int REQ_CODE;
    String getted_mobileNos ,vendor_Type ;
    private File LicenseFile;
    private File UploadIDProof;
    MultipartBody.Part[] gallery_image_parts ;
    boolean gallerimageSelected = false ;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private int Image_REQ_CODE = -1 ;
    private boolean isApiCalled = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editSalonBinding = ActivityEditSalonBinding.inflate(getLayoutInflater());
        setContentView(editSalonBinding.getRoot());

        getted_mobileNos = getIntent().getStringExtra("mobile_no") ;
        vendor_Type = getIntent().getStringExtra("vendor_Tp") ;
        editSalonBinding.salonPhoneNo.setText(getted_mobileNos);

        getSalonData();
        editSalonBinding.submit.setOnClickListener(view -> {

            RequestBody id_proof_request;
            editSalonBinding.progressBar.setVisibility(View.VISIBLE);
            MultipartBody.Part id_part_val = null;
            if (UploadIDProof != null) {
                id_proof_request = RequestBody.create(MediaType.parse("id_proof_image"), Compress.images(UploadIDProof.getPath() , 1024 * 1024));
                id_part_val = MultipartBody.Part.createFormData("id_proof_image", UploadIDProof.getName(), id_proof_request);
            }

            RequestBody license_request;
            MultipartBody.Part license_part_val = null;
            if (LicenseFile != null) {
                license_request = RequestBody.create(MediaType.parse("licence_image"), Compress.images(LicenseFile.getPath() , 1* 1024 *1024));
                license_part_val = MultipartBody.Part.createFormData("licence_image", LicenseFile.getName(), license_request);
            }
//
//                RequestBody gallery_imae_req = null;
//                MultipartBody.Part gallery_imag_val = null;
//                if (GalleryImgFile != null) {
//                    gallery_imae_req = RequestBody.create(MediaType.parse("gallery_image"), GalleryImgFile);
//                    gallery_imag_val = MultipartBody.Part.createFormData("gallery_image", GalleryImgFile.getName(), gallery_imae_req);
//                }

            String token = String.format("Bearer %s", loginResponsePref.getInstance(EditSalon.this).getToken());
            String address_val = Objects.requireNonNull(editSalonBinding.salonAddress.getText()).toString();
            Call<EditProfileResponse> call ;
            if(!gallerimageSelected) {
                call = RetrofitClient.getVendorService().EditSalonDetail(token, getRequestBody(Objects.requireNonNull(editSalonBinding.salonName.getText()).toString()), getRequestBody(editSalonBinding.salonPhoneNo.getText().toString()), getRequestBody(address_val), getRequestBody(Objects.requireNonNull(editSalonBinding.salonEmail.getText()).toString()), getRequestBody("+91"), getRequestBody(Objects.requireNonNull(editSalonBinding.etAbout.getText()).toString()), id_part_val, license_part_val, getRequestBody("salon"));
            }
            else {
                call = RetrofitClient.getVendorService().EditSalonDetailwithGalleryImages(token, getRequestBody(Objects.requireNonNull(editSalonBinding.salonName.getText()).toString()), getRequestBody(editSalonBinding.salonPhoneNo.getText().toString()), getRequestBody(address_val), getRequestBody(Objects.requireNonNull(editSalonBinding.salonEmail.getText()).toString()), getRequestBody("+91"), getRequestBody(Objects.requireNonNull(editSalonBinding.etAbout.getText()).toString()), id_part_val, license_part_val, getRequestBody("salon"), gallery_image_parts );
            }
            call.enqueue(new Callback<EditProfileResponse>() {
                @Override
                public void onResponse(@NonNull Call<EditProfileResponse> call, @NonNull Response<EditProfileResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        Toast.makeText(EditSalon.this, " " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                            Intent editProfileIntent = new Intent(EditSalon.this, Home.class);
//                            startActivity(editProfileIntent);
                        finish();
                    } else {
                        if (response.body() != null) {
                            Toast.makeText(EditSalon.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        Log.d("editProfilehit", "onResponse: " + response.body());
                    }
                    editSalonBinding.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(@NonNull Call<EditProfileResponse> call, @NonNull Throwable t) {
                    Log.d("editProfilehit", "onFailure: " + t.getMessage());
                    editSalonBinding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(EditSalon.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        });


        editSalonBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
            getSalonData();
            editSalonBinding.swipeRefreshLayout.setRefreshing(false);
        });


        editSalonBinding.btnAddImages.setOnClickListener(view -> {
            REQ_CODE = 107;
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        });

        editSalonBinding.back.setOnClickListener(view -> {
            editSalonBinding.progressBar.setVisibility(View.VISIBLE);
//                Intent editProfileIntent = new Intent(EditSalon.this, EditProfile.class);
//                startActivity(editProfileIntent);
            editSalonBinding.progressBar.setVisibility(View.GONE);
            finish();
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        editSalonBinding.salonBannerRecycler.setLayoutManager(layoutManager);

        editSalonBinding.UploadIdProofBtn.setOnClickListener(view -> {
            REQ_CODE = 99;
            ImagePicker.with(EditSalon.this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(
                            1080,
                            1080
                    )   //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });


        editSalonBinding.addMoreSalonDetails.setOnClickListener(view -> {
            Image_REQ_CODE = 102;
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        });

         editSalonBinding.addMoreSalonDetails.setOnClickListener(view -> {
             Image_REQ_CODE = 1013;
//                ImagePicker.with(EditProfile.this)
//                        .crop()                    //Crop image(Optional), Check Customization for more option
//                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
//                        .maxResultSize(
//                                1080,
//                                1080
//                        )   //Final image resolution will be less than 1080 x 1080(Optional)
//                        .start();
             Intent intent = new Intent();
             intent.setType("image/*");
             intent.setAction(Intent.ACTION_GET_CONTENT);
             intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
             startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
         });


        editSalonBinding.uploadLicenseBtn.setOnClickListener(view -> {
            REQ_CODE = 102;

            ImagePicker.with(EditSalon.this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(
                            1080,
                            1080
                    )   //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });
    }

    private void getSalonData() {
        if (!isApiCalled) {
            FunctionCall.showProgressDialog(EditSalon.this);
            String token = "Bearer " + loginResponsePref.getInstance(EditSalon.this).getToken();
            Call<GetProfileResponse> call = RetrofitClient.getVendorService().getVendorDetails(token);
            call.enqueue(new Callback<GetProfileResponse>() {
                @Override
                public void onResponse(@NonNull Call<GetProfileResponse> call, @NonNull Response<GetProfileResponse> response) {
                    FunctionCall.DismissDialog(EditSalon.this);
                    isApiCalled = false ;
                    if (response.isSuccessful() && response.body() != null) {
                        SalonDetail salonDetails = response.body().getSalonDetail();
//                    String vendor_types = response.body().getOwnerDetail().
                        if (salonDetails != null) {
                            setData(salonDetails);
                        }
                    } else {
                        if (response.body() != null) {
                            Toast.makeText(EditSalon.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        Log.d("editProfilehit", "onResponse: " + response.body());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetProfileResponse> call, @NonNull Throwable t) {
                    FunctionCall.DismissDialog(EditSalon.this);
                    isApiCalled = false ;
                    Log.d("editprofilehit", "onFailure: " + t.getMessage());
                }
            });
        }
    }

    private void setData(SalonDetail salonDetails) {
        editSalonBinding.salonName.setText(salonDetails.getSalon_name());
        editSalonBinding.salonAddress.setText(salonDetails.getAddress());
        editSalonBinding.salonEmail.setText(salonDetails.getEmail());
        editSalonBinding.etAbout.setText(salonDetails.getAbout());
        EditSaloonBannerAdapter editSaloonBannerAdapter = new EditSaloonBannerAdapter(EditSalon.this, salonDetails.getBanner());
        editSalonBinding.salonBannerRecycler.setAdapter(editSaloonBannerAdapter);
    
        EditSalonGalleryAdapter  galleryAdapter = new EditSalonGalleryAdapter(EditSalon.this, salonDetails.getGalleries());
        editSalonBinding.gallerRecyclerView.setAdapter(galleryAdapter);
        editSalonBinding.progressBar.setVisibility(View.GONE);
    }

    private RequestBody getRequestBody(String str) {
        return RequestBody.create(str, MediaType.parse("text/plain"));
    }

    @Override
    public void onBackPressed() {
//        Intent editProfileIntent = new Intent(EditSalon.this, EditProfile.class);
//        startActivity(editProfileIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (REQ_CODE == 99) {
                if (data != null && data.getData() != null) {
                    UploadIDProof = GetFileFromUriUsingBufferReader.getImageFile(this, data.getData());
                    if (UploadIDProof != null) {
                        editSalonBinding.UploadIdProofBtn.setText(UploadIDProof.getName());
                    }
                }
            } else if (REQ_CODE == 102) {
                if (data != null && data.getData() != null) {
                    LicenseFile = GetFileFromUriUsingBufferReader.getImageFile(this, data.getData());
                    if (LicenseFile != null) {
                        editSalonBinding.uploadLicenseBtn.setText(LicenseFile.getName());
                    }
                }
            }
            else if (REQ_CODE == 107) {

                if (null != data) {
                    // Get the Image from data

                    ArrayList<File> imagesEncodedList = new ArrayList<File>();
                    if (data.getData() != null) {

                        Uri mImageUri = data.getData();

                        //// Get the cursor
//                        Cursor cursor = getContentResolver().query(mImageUri,
//                                filePathColumn, null, null, null);
//                        // Move to first row
//                        cursor.moveToFirst();
//
//                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                        imageEncoded  = cursor.getString(columnIndex);
//                        cursor.close();
                        File oneImageFile = GetFileFromUriUsingBufferReader.getImageFile(EditSalon.this , mImageUri);
                        if (oneImageFile != null) {
                            imagesEncodedList.add(oneImageFile);
                        }
                    } else {
                        if (data.getClipData() != null) {
                            ClipData mClipData = data.getClipData();
                            ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                            for (int i = 0; i < mClipData.getItemCount(); i++) {

                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri uri = item.getUri();
//                                mArrayUri.add(uri);
//                                // Get the cursor
//                                Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
//                                // Move to first row
//                                cursor.moveToFirst();
//
//                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                                imageEncoded  = cursor.getString(columnIndex);
//                                imagesEncodedList.add(imageEncoded);
//                                cursor.close();
                                File oneImageFile = GetFileFromUriUsingBufferReader.getImageFile(EditSalon.this  , uri);
                                if (oneImageFile != null) {
                                    imagesEncodedList.add(oneImageFile);
                                }
                            }
                            Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                        }
                    }
                    SaveGalleryImages(imagesEncodedList);
                } else {
                    Toast.makeText(EditSalon.this, "You haven't picked Image",
                            Toast.LENGTH_LONG).show();
                }


            }

            else if (Image_REQ_CODE == 1013) {
                Image_REQ_CODE = -1;

                if (null != data) {
                    // Get the Image from data

                    ArrayList<File> imagesEncodedBannerList = new ArrayList<>();
                    if (data.getData() != null) {

                        Uri mImageUri = data.getData();

                        //// Get the cursor
//                        Cursor cursor = getContentResolver().query(mImageUri,
//                                filePathColumn, null, null, null);
//                        // Move to first row
//                        cursor.moveToFirst();
//
//                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                        imageEncoded  = cursor.getString(columnIndex);
//                        cursor.close();
                        File oneImageFile = GetFileFromUriUsingBufferReader.getImageFile(EditSalon.this , mImageUri);
                        if (oneImageFile != null) {
                            imagesEncodedBannerList.add(oneImageFile);
                        }
                    } else {
                        if (data.getClipData() != null) {
                            ClipData mClipData = data.getClipData();
                            ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                            for (int i = 0; i < mClipData.getItemCount(); i++) {

                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri uri = item.getUri();
                                  File oneImageFile = GetFileFromUriUsingBufferReader.getImageFile(EditSalon.this  , uri);
                                if (oneImageFile != null) {
                                    imagesEncodedBannerList.add(oneImageFile);
                                }
                            }
                            Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                        }
                    }
                    SaveBannerImages(imagesEncodedBannerList);
                } else {
                    Toast.makeText(EditSalon.this, "You haven't picked Image",
                            Toast.LENGTH_LONG).show();
                }

            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, "Cancelled. ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Something went wrong ", Toast.LENGTH_SHORT).show();
        }
    }

    private void SaveBannerImages(ArrayList<File> imagesEncodedBannerList) {
        FunctionCall.showProgressDialog(EditSalon.this);
        MultipartBody.Part[] banner_image_parts = new MultipartBody.Part[0];
        if (imagesEncodedBannerList != null && imagesEncodedBannerList.size() > 0) {
            banner_image_parts = new MultipartBody.Part[imagesEncodedBannerList.size()];
            for (int i = 0; i < imagesEncodedBannerList.size(); i++) {
                if (imagesEncodedBannerList.get(i) != null) {
                    RequestBody thumbBody = RequestBody.create(MediaType.parse("image/jpg"), Compress.images(imagesEncodedBannerList.get(i).getPath(), 1024 * 1024));
                    banner_image_parts[i] = MultipartBody.Part.createFormData("banner_image[]", imagesEncodedBannerList.get(i).getName(), thumbBody);
                }
            }
        }

//        if (imagesEncodedBannerList != null && imagesEncodedBannerList.size() > 0) {
//            MultipartBody.Part[] banner_image_part = new MultipartBody.Part[imagesEncodedBannerList.size()];
//            for (int i = 0; i < imagesEncodedBannerList.size(); i++) {
//                if (imagesEncodedBannerList.get(i) != null) {
//                    RequestBody thumbBody = RequestBody.create(MediaType.parse("image/jpg"), Compress.images(imagesEncodedBannerList.get(i).getAbsolutePath() , 1024 * 1024)) ;
//                    banner_image_part[i] = MultipartBody.Part.createFormData("banner_image[]", imagesEncodedBannerList.get(i).getName(), thumbBody);
//                }
//            }
        RequestBody phone_body = getRequestBody(getted_mobileNos);
        RequestBody ccp_bdy = getRequestBody("+91");
        RequestBody type_body;
        type_body = getRequestBody(vendor_Type);
        String token = loginResponsePref.getInstance(EditSalon.this).getToken();
        Call<EditProfileResponse> call = RetrofitClient.getVendorService().AddSalonBannerImagesEditProfile("Bearer " + token, phone_body, ccp_bdy, banner_image_parts, type_body);
        call.enqueue(new Callback<EditProfileResponse>() {
            @Override
            public void onResponse(@NonNull Call<EditProfileResponse> call, @NonNull Response<EditProfileResponse> response) {
                FunctionCall.DismissDialog(EditSalon.this);
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isResult()) {

                        Toast.makeText(EditSalon.this, " " + Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (response.body() != null) {
                        Toast.makeText(EditSalon.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Log.d("editprofilehitsalon", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<EditProfileResponse> call, @NonNull Throwable t) {
                FunctionCall.DismissDialog(EditSalon.this);
                Log.d("editprofilehitsalon", "onFailure: " + t.getMessage());
            }
        });
    }


    private void SaveGalleryImages(ArrayList<File> imagesEncodedList) {

        if (imagesEncodedList != null && imagesEncodedList.size() > 0) {
            gallery_image_parts = new MultipartBody.Part[imagesEncodedList.size()];
            for (int i = 0; i < imagesEncodedList.size(); i++) {
                if (imagesEncodedList.get(i) != null) {
                    RequestBody thumbBody = RequestBody.create(MediaType.parse("image/jpg"), Compress.images(imagesEncodedList.get(i).getPath() , 1024 * 1024) );
                    gallery_image_parts[i] = MultipartBody.Part.createFormData("gallery_any[]", imagesEncodedList.get(i).getName(), thumbBody);
                }
            }
            gallerimageSelected = true;
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