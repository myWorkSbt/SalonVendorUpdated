  package com.vendor.salon.activity;

import static okhttp3.RequestBody.create;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vendor.salon.R;
import com.vendor.salon.adapters.AddPackageServicesAdapter;
import com.vendor.salon.adapters.AddServicesCategorySpinnerAdapter;
import com.vendor.salon.adapters.ManagePackagesAdapter;
import com.vendor.salon.data_Class.categories.CategoriesItem;
import com.vendor.salon.data_Class.manage_service.DataItem;
import com.vendor.salon.data_Class.get_ManagePackageData.getManagePackageResponse;
import com.vendor.salon.data_Class.manage_package.ManagePackageResponse;
import com.vendor.salon.data_Class.manage_service.ManageServiceResponse;
import com.vendor.salon.data_Class.vendor_sub_catgories.VendorSubCategoryResponse;
import com.vendor.salon.databinding.ActivityManagePackagesBinding;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.GetFileFromUriUsingBufferReader;
import com.vendor.salon.utilityMethod.loginResponsePref;
import com.vendor.salon.utilityMethod.onSumitButtonClicked;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagePackages extends AppCompatActivity {

    private static final String TAG = "manage-package-hit";
    static int REQ_CODE = -1;
    private ActivityManagePackagesBinding managePackagesBinding;
    BottomSheetDialog bottomSheetDialog;
    File Item_image_File;
    String token;
    private boolean isNewPackageActive = true;
    ManagePackagesAdapter managePackagedAdapter = null;
    ImageView package_images;
    File Update_Item_image_File;
    int item_positions;
    private ArrayList<CategoriesItem> categoriesList;
    private ArrayList<com.vendor.salon.data_Class.vendor_sub_catgories.DataItem> servicesList;
    AddServicesCategorySpinnerAdapter category_adapter;
    int selected_category_position = -1;
    AddPackageServicesAdapter services_adapter;
    String is_DoorStep = "1";
    AppCompatEditText add_et_name;
    AppCompatImageView add_package_images;
    ConstraintLayout add_new_package_mainlay;
    ImageView add_package_switch_active_btn;
    AppCompatEditText add_et_mrp;
    AppCompatEditText add_et_offer_price;
    RadioButton add_btn_gender_male;
    RadioButton add_btn_salon;
    AppCompatSpinner add_new_row_category;
    AppCompatSpinner add_new_row_services;
    String selected_category_id;
    String selected_new_gender = "male";
    ArrayList<String> selected_services_list = new ArrayList<>();
    RadioGroup add_btn_gender;
    RadioGroup add_btn_service_ats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        managePackagesBinding = DataBindingUtil.setContentView(this, R.layout.activity_manage_packages);

        token = "Bearer " + loginResponsePref.getInstance(ManagePackages.this).getToken();


        createBottomSheetLays();
        Call<getManagePackageResponse> call = RetrofitClient.getVendorService().getPackagesDetail(token);

        FunctionCall.showProgressDialog(ManagePackages.this);
        call.enqueue(new Callback<getManagePackageResponse>() {
            @Override
            public void onResponse(@NonNull Call<getManagePackageResponse> call, @NonNull Response<getManagePackageResponse> response) {
                FunctionCall.DismissDialog(ManagePackages.this);
                if (response.isSuccessful() && response.body() != null && response.body().isStatus() && response.body().getData() != null) {
                    managePackagedAdapter = new ManagePackagesAdapter(ManagePackages.this, response.body().getData(), categoriesList, servicesList, new onSumitButtonClicked() {
                        @Override
                        public void onBtnClicked(AppCompatButton context, int position) {
                            getData();
                        }

                        @Override
                        public void onImageViewClick(int position) {
                            item_positions = position;

                            REQ_CODE = 97;
                            ImagePicker.with(ManagePackages.this)
                                    .crop()                    //Crop image(Optional), Check Customization for more option
                                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                                    .maxResultSize(
                                            1080,
                                            1080
                                    )   //Final image resolution will be less than 1080 x 1080(Optional)
                                    .start();

                        }
                    } , new ManagePackagesAdapter.OnPackageSelectClick() {
                        @Override
                        public void onGenderSwitchClicked(int position, int genderPosition) {
                            switch (genderPosition ) {
                                case 0 :
                                     selected_new_gender = "male" ;
                                     break;
                                     case 1 :
                                     selected_new_gender = "female" ;
                                     break;
                            }
                            getCategoryData();
                        }

                        @Override
                        public void onServicesAtClicked(int position, int serviceAt) {
                            switch (serviceAt ) {
                                case 0 :
                                    is_DoorStep = "1" ;
                                    break;
                                case 1 :
                                    is_DoorStep = "2" ;
                                    break;
                            }
                            getCategoryData();
                        }
                    });
                    managePackagesBinding.packagesList.setAdapter(managePackagedAdapter);
                } else {
                    Log.d(TAG, "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<getManagePackageResponse> call, @NonNull Throwable t) {
                FunctionCall.DismissDialog(ManagePackages.this);
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        managePackagesBinding.btnBack.setOnClickListener(view -> {
            Intent intents = new Intent(ManagePackages.this, Home.class);
            startActivity(intents);
            finishAffinity();
        });

        managePackagesBinding.btnAdd.setOnClickListener(view -> {
            bottomSheetDialog.show();
            add_et_name.requestFocus();

        });

        managePackagesBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
            getData();
            managePackagesBinding.swipeRefreshLayout.setRefreshing(false);
        });


    }

    private void createBottomSheetLays() {
        bottomSheetDialog = new BottomSheetDialog(ManagePackages.this, R.style.BottomSheetDialogStyleFixedHeigt);
        bottomSheetDialog.setContentView(R.layout.add_new_package_bottom_sheet_lays);
        add_et_name = bottomSheetDialog.findViewById(R.id.et_package_name);
        add_package_images = bottomSheetDialog.findViewById(R.id.new_item_image);
        add_new_package_mainlay = bottomSheetDialog.findViewById(R.id.package_new_main_lays);
        add_package_switch_active_btn = bottomSheetDialog.findViewById(R.id.switch_btn_active);
        add_et_mrp = bottomSheetDialog.findViewById(R.id.et_mrp_price);
        add_et_offer_price = bottomSheetDialog.findViewById(R.id.et_offer_price);
        add_btn_gender_male = bottomSheetDialog.findViewById(R.id.male);
        add_btn_gender = bottomSheetDialog.findViewById(R.id.gender_val);
        add_btn_service_ats = bottomSheetDialog.findViewById(R.id.service_at_vals);
        add_btn_salon = bottomSheetDialog.findViewById(R.id.salon_val);
        add_new_row_category = bottomSheetDialog.findViewById(R.id.row_category_spinner);
        add_new_row_services = bottomSheetDialog.findViewById(R.id.row_services_spinner);


        getCategoryData();
        bottomSheetDialog.findViewById(R.id.btn_edit_image).setOnClickListener(View -> {

            REQ_CODE = 99;
            ImagePicker.with(ManagePackages.this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(
                            1080,
                            1080
                    )   //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });

//        btn_package_names.setOnClickListener(View -> {
//            add_et_name.setEnabled(true);
//            add_et_name.requestFocus();
//        });
//
//            bottomSheetDialog.findViewById( R.id.btn_edit_packages).setOnClickListener( View -> {
//                about_packages_vls.setEnabled(true);
//                about_packages_vls.setHint("About Package.... ");
//                about_packages_vls.requestFocus();
//            });

        bottomSheetDialog.findViewById(R.id.active_btn_lay).setOnClickListener(View -> {
            if (isNewPackageActive) {
                isNewPackageActive = false;
                add_package_switch_active_btn.setImageResource(R.drawable.in_active);
                add_new_package_mainlay.setAlpha(0.7f);
            } else {
                isNewPackageActive = true;
                add_package_switch_active_btn.setImageResource(R.drawable.active);
                add_new_package_mainlay.setAlpha(1f);

            }

        });

        add_btn_gender.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            if (((RadioButton) radioGroup.findViewById(checkedId)).getText().toString().equals("Male")) {
                selected_new_gender = "male";
            } else {
                selected_new_gender = "female";
            }
            getCategoryData();
        });


        bottomSheetDialog.findViewById(R.id.btn_add_package).setOnClickListener(View -> {
            String title = add_et_name.getText().toString();
            String mrp = add_et_mrp.getText().toString();
            String offer_price = add_et_offer_price.getText().toString();


            if (title.isEmpty()) {
                add_et_name.requestFocus();
                add_et_name.setError("Mandatory Field. ");
            } else if (mrp.isEmpty()) {
                add_et_mrp.setError("Mandatory Field. ");
                add_et_mrp.requestFocus();
            } else if (add_new_row_category.getSelectedItemPosition() == 0) {
                Toast.makeText(this, " Please select a category of Package", Toast.LENGTH_SHORT).show();

            } else if (add_new_row_category.getSelectedItemPosition() == 0) {
                Toast.makeText(this, " Please select at least one service of Package", Toast.LENGTH_SHORT).show();
            } else {
                FunctionCall.showProgressDialog(ManagePackages.this);
                RequestBody new_package_img_body = null;
                MultipartBody.Part new_package_image_parts = null;
                if (Item_image_File != null) {
                    new_package_img_body = RequestBody.create(MediaType.parse("image"), Item_image_File);
                    new_package_image_parts = MultipartBody.Part.createFormData("image", Item_image_File.getName(), new_package_img_body);

                }

                String disable;
                if (!isNewPackageActive) {
                    disable = "2";
                } else {
                    disable = "1";
                }


                Call<ManagePackageResponse> new_item_call = RetrofitClient.getVendorService().addNewPackage(token,
                        getRequestBody(title),
                        getRequestBody(mrp),
                        getRequestBody(offer_price),
                        getRequestBody("about_package"),
                        new_package_image_parts,
                        getRequestBody(disable),
                        getRequestBody(selected_new_gender),
                        getRequestBody(selected_category_id),
                        selected_services_list
                );

                new_item_call.enqueue(new Callback<ManagePackageResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ManagePackageResponse> call, @NonNull Response<ManagePackageResponse> response) {
                        FunctionCall.DismissDialog(ManagePackages.this);
                        if (response.body() != null) {
                            bottomSheetDialog.hide();
                            getData();
                            Toast.makeText(ManagePackages.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }


                        Log.d(TAG, "onResponse: " + response);
                    }

                    @Override
                    public void onFailure(Call<ManagePackageResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        Toast.makeText(ManagePackages.this, " Something went wrong! ", Toast.LENGTH_SHORT).show();
                        FunctionCall.DismissDialog(ManagePackages.this);
                    }
                });
            }
        });


        add_btn_service_ats.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            if (((RadioButton) radioGroup.findViewById(checkedId)).getText().toString().equals("Salon")) {
                is_DoorStep = "1";
            } else {
                is_DoorStep = "2";
            }
            getCategoryData();
        });


    }

    private void getCategoryData() {
        categoriesList = new ArrayList<>();
        CategoriesItem categoryItems = new CategoriesItem();
        categoryItems.setId(-1);
        categoryItems.setName("Select Category ");
        categoriesList.add(categoryItems);
        servicesList = null;
        servicesList = new ArrayList<>();
         com.vendor.salon.data_Class.vendor_sub_catgories.DataItem serviceDefaultpositionItems = new com.vendor.salon.data_Class.vendor_sub_catgories.DataItem();
        serviceDefaultpositionItems.setId(-1);
        serviceDefaultpositionItems.setServiceName("Select Service");
        servicesList.add(serviceDefaultpositionItems);
        setServicesData();

        Call<ManageServiceResponse> call = RetrofitClient.getVendorService().getSavedCategoryList(token, selected_new_gender, is_DoorStep, "");
        call.enqueue(new Callback<ManageServiceResponse>() {
            @Override
            public void onResponse(Call<ManageServiceResponse> call, Response<ManageServiceResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null && response.body().getData().size() > 0) {
                    for (int i = 0; i < response.body().getData().size(); i++) {
                        DataItem dataItems = response.body().getData().get(i);
                        if (dataItems != null) {
                            CategoriesItem category_items = new CategoriesItem();
                            category_items.setName(dataItems.getName().toString());
                            category_items.setId(dataItems.getId());
                            categoriesList.add(category_items);
                        }
                    }
                } else {
                    if (response.body() != null) {
                        Toast.makeText(ManagePackages.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Log.d("categorieshit", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<ManageServiceResponse> call, Throwable t) {
                Log.d("categorieshit", "onFailure: is - " + t.getMessage());
            }
        });
        setCategoryData();

    }

    private void setCategoryData() {

        category_adapter = new AddServicesCategorySpinnerAdapter(this, categoriesList);
// Specify the layout to use when the list of choices appears

//        category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        add_new_row_category.setAdapter(category_adapter);
        category_adapter.notifyDataSetChanged();
        add_new_row_category.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {

//                                 Toast.makeText(AddServices.this, " ___  Clicked. ", Toast.LENGTH_SHORT).show();
                        // It returns the clicked item.
                        if (selected_category_position == 0 && position == 0) {
                        } else {
                            servicesList = null;
                            servicesList = new ArrayList<>();
                            com.vendor.salon.data_Class.vendor_sub_catgories.DataItem serviceDefaultpositionItems = new com.vendor.salon.data_Class.vendor_sub_catgories.DataItem();
                            serviceDefaultpositionItems.setId(-1);
                            serviceDefaultpositionItems.setServiceName("Select Service");
                            selected_category_position = position;
                            servicesList.add(serviceDefaultpositionItems);
                            if (selected_category_position > 0) {
                                getServicesData();
                                if (categoriesList.size() > 1) {
                                    setServicesData();
                                }
                            }
//                                 String name = selected_category.getName();
//                                 addServicesBinding.categorySpinner.setEnabled(false );
//                        Toast.makeText(AddServices.this, name + " selected", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


    }

    private void setServicesData() {

        services_adapter = new AddPackageServicesAdapter(this, servicesList);
// Specify the layout to use when the list of choices appears

//        category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        add_new_row_services.setAdapter(services_adapter);
        services_adapter.notifyDataSetChanged();
        add_new_row_services.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {

//                                 Toast.makeText(AddServices.this, " ___  Clicked. ", Toast.LENGTH_SHORT).show();
                        // It returns the clicked item.
//                        if (position != 0) {
                            if (servicesList.get(position).isSelected()) {
                                if (position != 0  ) {
                                    if (selected_services_list.indexOf(String.valueOf(servicesList.get(position).getId())) >0 ) {
                                        selected_services_list.remove(selected_services_list.indexOf(String.valueOf(servicesList.get(position).getId())) -1);
                                    }
                                    servicesList.get(position).setSelected(false);
                                    }
                            } else {
                                if (position != 0 ) {
                                    servicesList.get(position).setSelected(true);
                                    selected_services_list.add(String.valueOf(servicesList.get(position ).getId()));
                                    Log.d(TAG, "onItemSelected: " + selected_services_list);
                                }
                            }
//                                 String name = selected_category.getName();
//                        selected_service_id =
//                        Toast.makeText(AddServices.this, name + " selected", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onItemSelected: " + selected_services_list);
//                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


    }

    private void getServicesData() {
        FunctionCall.showProgressDialog(ManagePackages.this);
        selected_category_id = String.valueOf(categoriesList.get(selected_category_position).getId());
        selected_services_list = null;
        selected_services_list = new ArrayList<>();
        Call<VendorSubCategoryResponse> call = RetrofitClient.getVendorService().getServiceFilteredData("Bearer " + loginResponsePref.getInstance(getApplicationContext()).getToken(), categoriesList.get(selected_category_position).getId() + "", selected_new_gender , is_DoorStep );
        call.enqueue(new Callback<VendorSubCategoryResponse>() {
            @Override
            public void onResponse(Call<VendorSubCategoryResponse> call, Response<VendorSubCategoryResponse> response) {
                FunctionCall.DismissDialog(ManagePackages.this);
                if (response.isSuccessful() && response.body() != null) {
                    servicesList.addAll(response.body().getData());
                } else {
                    if (response.body() != null) {
                        Toast.makeText(ManagePackages.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Log.d("manage-services-hit", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<VendorSubCategoryResponse> call, Throwable t) {
                FunctionCall.DismissDialog(ManagePackages.this);
                Log.d("manage-services-hit", "onFailure: " + t.getMessage());
            }
        });


    }

//    private com.vendor.salon.data_Class.get_ManagePackageData.DataItem convertTOAdpaterObjects(Response<ManagePackageResponse> response) {
//
//        com.vendor.salon.data_Class.get_ManagePackageData.DataItem singleData = new com.vendor.salon.data_Class.get_ManagePackageData.DataItem();
//        singleData.setCreatedAt(Objects.requireNonNull(response.body().getData()).getCreatedAt());
//        singleData.setDisabled(response.body().getData().getDisabled());
//        singleData.setImage(Objects.requireNonNull(response.body().getData()).getImage());
//        singleData.setAbout(Objects.requireNonNull(response.body().getData()).getAbout());
//        singleData.setTitle(Objects.requireNonNull(response.body().getData()).getTitle());
//        singleData.setId(Objects.requireNonNull(response.body().getData()).getId());
//        singleData.setMrp(Objects.requireNonNull(response.body().getData()).getMrp());
//        singleData.setOfferPrice(Objects.requireNonNull(response.body().getData()).getOfferPrice());
//        singleData.setUpdatedAt(Objects.requireNonNull(response.body().getData()).getUpdatedAt());
//        singleData.setVendorId(Objects.requireNonNull(response.body().getData()).getVendorId());
//        return singleData;
//    }

    public void getData() {
        FunctionCall.showProgressDialog(ManagePackages.this);
        Call<getManagePackageResponse> call = RetrofitClient.getVendorService().getPackagesDetail(token);

        call.enqueue(new Callback<getManagePackageResponse>() {
            @Override
            public void onResponse(@NonNull Call<getManagePackageResponse> call, @NonNull Response<getManagePackageResponse> response) {
                FunctionCall.DismissDialog(ManagePackages.this);
                if (response.isSuccessful() && response.body() != null && response.body().isStatus() && response.body().getData() != null) {
                    managePackagesBinding.showNoDataText.setVisibility(View.GONE);
                    managePackagedAdapter.refreshData(response.body().getData());
                    managePackagedAdapter.notifyDataSetChanged();
                } else {
                     managePackagesBinding.showNoDataText.setVisibility(View.VISIBLE);
                    Log.d(TAG, "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<getManagePackageResponse> call, @NonNull Throwable t) {
                FunctionCall.DismissDialog(ManagePackages.this);
                managePackagesBinding.showNoDataText.setVisibility(View.VISIBLE);
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (REQ_CODE == 99) {
                Item_image_File = GetFileFromUriUsingBufferReader.getImageFile(ManagePackages.this, Objects.requireNonNull(data).getData());
                if (Item_image_File != null && !Item_image_File.getName().isEmpty()) {
                    add_package_images
                            .setImageURI(Uri.parse(Item_image_File.getAbsolutePath()));
                }
            } else if (REQ_CODE == 97) {
                Update_Item_image_File = GetFileFromUriUsingBufferReader.getImageFile(ManagePackages.this, Objects.requireNonNull(data).getData());
                if (Update_Item_image_File != null && !Update_Item_image_File.getName().isEmpty()) {
                    managePackagedAdapter.setImageTotheViews(Update_Item_image_File, item_positions);
                }
            }

        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, " Activity Cancelled. ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, " Something went wrong. ", Toast.LENGTH_SHORT).show();
        }
    }

    private RequestBody getRequestBody(String str) {
        return create(str, MediaType.parse("text/plain"));
    }


}