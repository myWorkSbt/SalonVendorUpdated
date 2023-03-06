package com.vendor.salon.fragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.vendor.salon.activity.EditOwner;
import com.vendor.salon.activity.EditSalon;
import com.vendor.salon.adapters.EditSaloonBannerAdapter;
import com.vendor.salon.data_Class.bankedit.BankEditResponse;
import com.vendor.salon.data_Class.editprofile.EditProfileResponse;
import com.vendor.salon.data_Class.getProfile.GetProfileResponse;
import com.vendor.salon.databinding.ActivityHomeBinding;
import com.vendor.salon.databinding.FragmentEditProfileBinding;
import com.vendor.salon.model.Salon_detail_image_model;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.GetFileFromUriUsingBufferReader;
import com.vendor.salon.utilityMethod.loginResponsePref;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends Fragment {

    private FragmentEditProfileBinding editProfileBinding ;
    private List<Salon_detail_image_model> list = new ArrayList<>();
    String phone, ccp;
    String token;
    private int Image_REQ_CODE;
    File CheckFile;
    private boolean isEntriesEnabled = false;
    String vendor_Type;
    String gender;
    String designation;
    ActivityHomeBinding homeBinding;
    Context homeContext;

    public EditProfileFragment(ActivityHomeBinding homeBinding, FragmentManager supportFragmentManager) {
        this.homeBinding = homeBinding ;
        homeContext = homeBinding.getRoot().getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        editProfileBinding = FragmentEditProfileBinding.inflate(inflater, container, false);

        return editProfileBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        editProfileBinding.CancelCheckLays.setClickable(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(editProfileBinding.salonDetailsRecycler.getContext() , LinearLayoutManager.HORIZONTAL, false);
        editProfileBinding.salonDetailsRecycler.setLayoutManager(layoutManager);
        phone = loginResponsePref.getInstance(EditProfileFragment.this.getContext()).getPhone();
        ccp = "+91";
        token = loginResponsePref.getInstance(EditProfileFragment.this.getContext()).getToken();
        getProfileData();
        editProfileBinding.btnMenus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeBinding.drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        editProfileBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProfileData();
                editProfileBinding.swipeRefreshLayout.setRefreshing(false);
            }
        });
        editProfileBinding.editsBankDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEntriesEnabled) {
                    editProfileBinding.progressBar.setVisibility(View.VISIBLE);
                    editProfileBinding.BankName.setEnabled(true);
                    editProfileBinding.BankName.setBackgroundColor(Color.parseColor("#F5F5F5"));
                    isEntriesEnabled = true;
                    editProfileBinding.AccountNumber.setEnabled(true);
                    editProfileBinding.AccountNumber.setBackgroundColor(Color.parseColor("#F5F5F5"));
                    editProfileBinding.AccountHolderName.setEnabled(true);
                    editProfileBinding.BankName.requestFocus();
                    editProfileBinding.AccountHolderName.setBackgroundColor(Color.parseColor("#F5F5F5"));
                    editProfileBinding.TvIfcCode.setEnabled(true);
                    editProfileBinding.TvIfcCode.setBackgroundColor(Color.parseColor("#F5F5F5"));
                    editProfileBinding.TvCancelCheckHeading.setTextColor(Color.RED);
                    editProfileBinding.CancelCheckLays.setClickable(true);
                    editProfileBinding.progressBar.setVisibility(View.GONE);
                } else {
                    editProfileBinding.progressBar.setVisibility(View.VISIBLE);
                    editProfileBinding.BankName.setEnabled(false);
                    editProfileBinding.BankName.setBackgroundColor(Color.parseColor("#ffffff"));
                    isEntriesEnabled = false;
                    editProfileBinding.AccountNumber.setEnabled(false);

                    editProfileBinding.AccountNumber.setBackgroundColor(Color.parseColor("#ffffff"));
                    editProfileBinding.AccountHolderName.setEnabled(false);
                    editProfileBinding.AccountHolderName.setBackgroundColor(Color.parseColor("#ffffff"));
                    editProfileBinding.TvIfcCode.setEnabled(false);
                    editProfileBinding.CancelCheckLays.setClickable(false);
                    editProfileBinding.TvIfcCode.setBackgroundColor(Color.parseColor("#ffffff"));
                    editProfileBinding.progressBar.setVisibility(View.GONE);
                    editProfileBinding.TvCancelCheckHeading.setTextColor(Color.BLACK);
                }
            }
        });
        editProfileBinding.submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                MultipartBody.Part check_Img_part_val = null;
                if (CheckFile != null) {
                    RequestBody license_request = RequestBody.create(MediaType.parse("check_image"), CheckFile);
                    check_Img_part_val = MultipartBody.Part.createFormData("check_image", CheckFile.getName(), license_request);
                }

                if (editProfileBinding.BankName.getText().toString().isEmpty() ) {
                    editProfileBinding.BankName.setError("Mandatory Field! ");
                    editProfileBinding.BankName.requestFocus();
                }
                else  if (editProfileBinding.AccountNumber.getText().toString().isEmpty() ) {
                    editProfileBinding.AccountNumber.setError("Mandatory Field! ");
                    editProfileBinding.AccountNumber.requestFocus();
                }
                else if (editProfileBinding.AccountHolderName.getText().toString().isEmpty() ) {
                    editProfileBinding.AccountHolderName.setError("Mandatory Field! ");
                    editProfileBinding.AccountHolderName.requestFocus();
                }
                else if (editProfileBinding.TvIfcCode.getText().toString().isEmpty() ) {
                    editProfileBinding.TvIfcCode.setError("Mandatory Field! ");
                    editProfileBinding.TvIfcCode.requestFocus();
                }
                else {
                    FunctionCall.showProgressDialog(homeBinding.getRoot().getContext());
                    String token = "Bearer " + loginResponsePref.getInstance(EditProfileFragment.this.getContext()).getToken();
                    Call<BankEditResponse> call = RetrofitClient.getVendorService().EditBankDetails(token,
                            getRequestBody(editProfileBinding.BankName.getText().toString()),
                            getRequestBody(editProfileBinding.AccountNumber.getText().toString()),
                            getRequestBody(editProfileBinding.AccountHolderName.getText().toString()),
                            getRequestBody(editProfileBinding.TvIfcCode.getText().toString()),
                            check_Img_part_val);
                    call.enqueue(new Callback<BankEditResponse>() {
                        @Override
                        public void onResponse(Call<BankEditResponse> call, Response<BankEditResponse> response) {
                            FunctionCall.DismissDialog(homeBinding.getRoot().getContext());
                            if (response.isSuccessful() && response.body() != null && response.body().isResult()) {
                                isEntriesEnabled = false;
                                editProfileBinding.BankName.setEnabled(false);
                                editProfileBinding.BankName.setBackgroundColor(Color.parseColor("#ffffff"));
                                editProfileBinding.AccountNumber.setEnabled(false);
                                editProfileBinding.AccountNumber.setBackgroundColor(Color.parseColor("#ffffff"));
                                editProfileBinding.AccountHolderName.setEnabled(false);
                                editProfileBinding.AccountHolderName.setBackgroundColor(Color.parseColor("#ffffff"));
                                editProfileBinding.TvIfcCode.setEnabled(false);
                                editProfileBinding.TvIfcCode.setBackgroundColor(Color.parseColor("#ffffff"));
                                editProfileBinding.CancelCheckLays.setClickable(false);
                                editProfileBinding.TvCancelCheckHeading.setTextColor(Color.BLACK);
                                editProfileBinding.submit.setVisibility(View.GONE);
                            } else {
                                if (response.body() != null) {
                                    Toast.makeText(homeContext, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                Log.d("bankedithit", "onResponse: " + response.body());
                            }
                        }

                        @Override
                        public void onFailure(Call<BankEditResponse> call, Throwable t) {
                            FunctionCall.DismissDialog(homeBinding.getRoot().getContext());
                            Log.d("bankedithit", "onFailure: " + t.getMessage());
                        }
                    });

                }
            }
        });

        editProfileBinding.editSalonDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToSalonEditPage();
            }
        });

        editProfileBinding.addMoreSalonDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Image_REQ_CODE = 102;
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
            }
        });

        editProfileBinding.addImageLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Image_REQ_CODE = 102;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        editProfileBinding.CancelCheckLays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Image_REQ_CODE = 99;
                ImagePicker.with(EditProfileFragment.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(
                                1080,
                                1080
                        )   //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        editProfileBinding.editOwnerDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfileBinding.progressBar.setVisibility(View.VISIBLE);
                Intent editOwnerIntent = new Intent(getActivity(), EditOwner.class);
                startActivity(editOwnerIntent);
                editProfileBinding.progressBar.setVisibility(View.GONE);
            }
        });


    }




    private void moveToSalonEditPage() {
        editProfileBinding.progressBar.setVisibility(View.VISIBLE);
        Intent editSalonDetails = new Intent(getActivity() , EditSalon.class);
        editSalonDetails.putExtra("mobile_no", editProfileBinding.salonMnumber.getText().toString());
        editProfileBinding.progressBar.setVisibility(View.GONE);
        startActivity(editSalonDetails);
    }

    private void SaveBannerImages(ArrayList<File> imagesEncodedList) {
        if (imagesEncodedList != null && imagesEncodedList.size() > 0) {
            MultipartBody.Part[] banner_image_part = new MultipartBody.Part[imagesEncodedList.size()];
            for (int i = 0; i < imagesEncodedList.size(); i++) {
                if (imagesEncodedList.get(i) != null) {
                    RequestBody thumbBody = RequestBody.create(MediaType.parse("image/jpg"), imagesEncodedList.get(i));
                    banner_image_part[i] = MultipartBody.Part.createFormData("banner_image[]", imagesEncodedList.get(i).getName(), thumbBody);
                    ;
                }
            }
            RequestBody phone_body = getRequestBody(editProfileBinding.salonMnumber.getText().toString());
            RequestBody ccp_bdy = getRequestBody("+91");
            RequestBody type_body;
            type_body = getRequestBody(vendor_Type);
            String token = loginResponsePref.getInstance(homeContext).getToken();
            Call<EditProfileResponse> call = RetrofitClient.getVendorService().AddSalonBannerImagesEditProfile("Bearer " + token, phone_body, ccp_bdy, banner_image_part, type_body);
            call.enqueue(new Callback<EditProfileResponse>() {
                @Override
                public void onResponse(Call<EditProfileResponse> call, Response<EditProfileResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().isResult()) {

                            getProfileData();
                        }
                    } else {
                        if (response.body() != null) {
                            Toast.makeText(homeContext, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        Log.d("editprofilehitsalon", "onResponse: " + response.body());
                    }
                }

                @Override
                public void onFailure(Call<EditProfileResponse> call, Throwable t) {
                    Log.d("editprofilehitsalon", "onFailure: " + t.getMessage());
                }
            });
        }
    }

    private void getProfileData() {
        FunctionCall.showProgressDialog(homeContext);
        String token = loginResponsePref.getInstance(homeContext).getToken();
        Call<GetProfileResponse> call = RetrofitClient.getVendorService().getVendorDetails("Bearer " + token);
        call.enqueue(new Callback<GetProfileResponse>() {
            @Override
            public void onResponse(Call<GetProfileResponse> call, Response<GetProfileResponse> response) {
                FunctionCall.DismissDialog(homeContext);
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isStatus()) {
                        GetProfileResponse profileResponse = response.body();
                        if (response.body().getOwnerDetail() != null) {
                            vendor_Type = response.body().getOwnerDetail().getVendorType() + "";
                            if (vendor_Type.equals("FREELANCER")) {
                                editProfileBinding.salonDetailCard.setVisibility(View.GONE);
                            }
                        }
                        setProfileData(profileResponse);
                    }
                } else {
                    if (response.body() != null) {
                        Toast.makeText(homeContext, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Log.d("getprofilehit", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<GetProfileResponse> call, Throwable t) {
                FunctionCall.DismissDialog(homeContext);
                Toast.makeText(homeContext , "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("getprofilehit", "onFailure: " + t.getMessage());
            }
        });

        editProfileBinding.progressBar.setVisibility(View.GONE);
    }


    private RequestBody getRequestBody(String str) {
        return RequestBody.create(str, MediaType.parse("text/plain"));
    }

    private void setProfileData(GetProfileResponse vendorProfile) {
        if (vendorProfile.getOwnerDetail() != null) {
            vendor_Type = vendorProfile.getOwnerDetail().getVendorType() + "";
            editProfileBinding.ownerEmailId.setText(vendorProfile.getOwnerDetail().getEmail());
            gender = vendorProfile.getOwnerDetail().getGender() + "";
            designation = vendorProfile.getOwnerDetail().getDesignation() + "";
            editProfileBinding.salonMnumber.setText(vendorProfile.getOwnerDetail().getPhone());
            editProfileBinding.ownerMobileNo.setText(vendorProfile.getOwnerDetail().getPhone());
            editProfileBinding.hiddenTvImgUrl.setText(vendorProfile.getOwnerDetail().getUserImage());
            editProfileBinding.ownerName.setText(vendorProfile.getOwnerDetail().getName());
//            owner_gender = vendorProfile.getOwnerDetail()
            editProfileBinding.ownerDateOfBirth.setText(vendorProfile.getOwnerDetail().getDob());
            if (vendor_Type.equals("SALON")) {
                editProfileBinding.salonDetailCard.setVisibility(View.VISIBLE);
                if (vendorProfile.getSalonDetail() != null) {
//            profileBinding.UploadLicense.setText(vendorProfile.getSalonDetail().getLicence_image());
                    editProfileBinding.salonDetailsEmail.setText(vendorProfile.getSalonDetail().getEmail());
                    editProfileBinding.salonName.setText(vendorProfile.getSalonDetail().getSalon_name());
                    editProfileBinding.etAboutSession.setText(vendorProfile.getSalonDetail().getAbout());
                    editProfileBinding.salonMnumber.setText(vendorProfile.getSalonDetail().getPhone());
                    editProfileBinding.ownerMobileNo.setText(vendorProfile.getSalonDetail().getPhone());
                    editProfileBinding.salonDetailAddress.setText(vendorProfile.getSalonDetail().getAddress());
                    if (vendorProfile.getSalonDetail().getBanner() != null && vendorProfile.getSalonDetail().getBanner().size() > 0) {
                        EditSaloonBannerAdapter editSaloonBannerAdapter = new EditSaloonBannerAdapter(homeContext, vendorProfile.getSalonDetail().getBanner());
                        editProfileBinding.salonDetailsRecycler.setAdapter(editSaloonBannerAdapter);
                    }
                }
            }
            if (vendorProfile.getOwnerDetail().getUserImage() != null) {
                Glide.with(editProfileBinding.ownerProfileImage.getContext()).load(vendorProfile.getOwnerDetail().getUserImage()).into(editProfileBinding.ownerProfileImage);
            }
        }
        if (vendorProfile.getBankDetail() != null) {
            editProfileBinding.TvIfcCode.setText(vendorProfile.getBankDetail().getIfscCode());
            editProfileBinding.AccountHolderName.setText(vendorProfile.getBankDetail().getAccountHolderName());
            editProfileBinding.BankName.setText(vendorProfile.getBankDetail().getBankName());
            editProfileBinding.AccountNumber.setText(vendorProfile.getBankDetail().getAccountNo());
//            profileBinding.cancelCheck.setText(vendorProfile.getBankDetail().getCancelCheck());
//            profileBinding.UploadIDProof.setText(vendorProfile.getBankDetail().getImage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Image_REQ_CODE == 99) {
            if (resultCode == Activity.RESULT_OK) {
                Image_REQ_CODE = 7;
                editProfileBinding.submit.setVisibility(View.VISIBLE);
                editProfileBinding.submit.isFocused();
                CheckFile = GetFileFromUriUsingBufferReader.getImageFile(homeContext, data.getData());
                if (data != null) {
                    if (CheckFile != null && CheckFile.getName() != null) {
                        editProfileBinding.TvCancelCheckHeading.setText(CheckFile.getName());
                        editProfileBinding.TvCancelCheckHeading.setTextColor(Color.BLACK);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(homeContext, " Cancelled. ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(homeContext , " Something went wrong .  ", Toast.LENGTH_SHORT).show();
            }
        } else if (Image_REQ_CODE == 102) {
            Image_REQ_CODE = 7;
            if (resultCode == Activity.RESULT_OK) {
//                if (data != null && data.getData() != null) {
//                    BannerImgFile = GetFileFromUriUsingBufferReader.getImageFile(EditProfile.this, data.getData());
//                    if (BannerImgFile != null) {
//                        Toast.makeText(this, " ---> " + BannerImgFile.getName()+" . Added to Banner Lists                 ", Toast.LENGTH_SHORT).show();
//                    }
//                }
                // When an Image is picked
                if (null != data) {
                    // Get the Image from data

                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    ArrayList<File> imagesEncodedList = new ArrayList<File>();
                    String imageEncoded;
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
                        File oneImageFile = GetFileFromUriUsingBufferReader.getImageFile(homeContext, mImageUri);
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
                                File oneImageFile = GetFileFromUriUsingBufferReader.getImageFile(homeContext , uri);
                                if (oneImageFile != null) {
                                    imagesEncodedList.add(oneImageFile);
                                }
                            }
                            Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                        }
                    }
                    SaveBannerImages(imagesEncodedList);
                } else {
                    Toast.makeText(homeContext, "You haven't picked Image",
                            Toast.LENGTH_LONG).show();
                }


            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(homeContext , " Cancelled. ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(homeContext , " Something went wrong.  ", Toast.LENGTH_SHORT).show();
            }
        }
    }


}