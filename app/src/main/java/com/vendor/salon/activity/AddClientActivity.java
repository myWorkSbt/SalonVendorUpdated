package com.vendor.salon.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.vendor.salon.R;
import com.vendor.salon.adapters.AddServicesCategorySpinnerAdapter;
import com.vendor.salon.adapters.AddServicesServiceSpinnerAdapter;
import com.vendor.salon.adapters.AssignSpecialistAdapter;
import com.vendor.salon.adapters.ListClientAddMoreServicesAdapter;
import com.vendor.salon.data_Class.addclient.AddClientActivityResponse;
import com.vendor.salon.data_Class.categories.CategoriesItem;
import com.vendor.salon.data_Class.categories.CategoriesResponse;
import com.vendor.salon.data_Class.category_services.CategoryServicesResponse;
import com.vendor.salon.data_Class.getStaff.DataItem;
import com.vendor.salon.data_Class.getStaff.GetStaffResponse;
import com.vendor.salon.databinding.ActivityAddClientBinding;
import com.vendor.salon.model.ClientSelectedListModel;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.NetworkChangeListener;
import com.vendor.salon.utilityMethod.loginResponsePref;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddClientActivity extends AppCompatActivity {

    public static String selected_specialists_id = "";
    ActivityAddClientBinding addClientBinding;
    boolean isNoClicked = true;
    private MaterialAlertDialogBuilder confirmDataLossDialogBox;
    String selectedGender;
    boolean serviceSelectionHided = true;
    int selected_category_position = 0;
    int selected_services_position = 0;
    public static List<ClientSelectedListModel> selectedServiceslists = new ArrayList<>();

    ArrayList<com.vendor.salon.data_Class.category_services.CategoriesItem> servicesList;
    ArrayList<CategoriesItem> categoriesList = new ArrayList<>();
    ListClientAddMoreServicesAdapter listClientAddMoreServicesAdapter;
    AddServicesCategorySpinnerAdapter category_adapter;
    AddServicesServiceSpinnerAdapter services_adapter;
    private List<DataItem> staff_List = new ArrayList<>() ;
    private AssignSpecialistAdapter assignSpecialistAdapter;
    private final NetworkChangeListener networkChangeListener = new NetworkChangeListener() ;


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addClientBinding = DataBindingUtil.setContentView(AddClientActivity.this, R.layout.activity_add_client);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        listClientAddMoreServicesAdapter = new ListClientAddMoreServicesAdapter(AddClientActivity.this, selectedServiceslists, selectedGender, categoriesList);
        addClientBinding.selectedServicesList.setAdapter(listClientAddMoreServicesAdapter);

        DataItem anyoneItem = new DataItem();
        anyoneItem.setName("Anyone");
        anyoneItem.setDesignation(null);
        anyoneItem.setId(-1);
        anyoneItem.setSelected(true);
        staff_List.add(anyoneItem);
        assignSpecialistAdapter = new AssignSpecialistAdapter(staff_List);
        addClientBinding.staffList.setAdapter(assignSpecialistAdapter);
        getSpecialistData();
        String services_for = getIntent().getStringExtra("services_for");

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                 if ( result.getResultCode() == Activity.RESULT_OK) {
                     Intent data = result.getData();
                     int selected_staff_position = Objects.requireNonNull(data).getIntExtra("selected_staff_position" , -1);
                     if (selected_staff_position != -1 ) {
                         selected_specialists_id = String.valueOf(staff_List.get(selected_staff_position).getId());
                         setSelectedStaff(selected_staff_position);
                     }
                 }
            }
        });

        if (services_for.equals("mens")) {
            selectedGender = "male";
            addClientBinding.switchLays.setVisibility(View.GONE);
        } else if (services_for.equals("womens")) {
            addClientBinding.switchLays.setVisibility(View.GONE);
            selectedGender = "female";
        } else {
            addClientBinding.switchLays.setVisibility(View.VISIBLE);
        }

        addClientBinding.switchRoomAvailability.setOnCheckedChangeListener((compoundButton, b) -> {
            if (!isNoClicked) {

                if (selectedServiceslists != null && selectedServiceslists.size() > 0) {
                    confirmDataLossDialogBox = new MaterialAlertDialogBuilder(AddClientActivity.this, R.style.warning_dialog_style);
                    confirmDataLossDialogBox.setTitle(" Are you sure! ");
                    confirmDataLossDialogBox.setMessage(" Your selected services list will be lost since  you have not added your selected services .");
                    confirmDataLossDialogBox.setCancelable(false);
//                            addClientBinding.MaleTv.setVisibility(View.VISIBLE);
                    confirmDataLossDialogBox.setPositiveButton("Yes", (dialogInterface, i) -> {
                        if (!b) {
                            addClientBinding.MaleTv.setVisibility(View.GONE);
                            addClientBinding.switchRoomAvailability.setThumbDrawable(getDrawable(R.drawable.female_thumb));
                            selectedGender = "female";
                            addClientBinding.femaleTv.setVisibility(View.VISIBLE);
                        } else {
                            addClientBinding.switchRoomAvailability.setThumbDrawable(getDrawable(R.drawable.custom_thumb));
                            addClientBinding.femaleTv.setVisibility(View.GONE);
                            selectedGender = "male";
                            addClientBinding.MaleTv.setVisibility(View.VISIBLE);
                            addClientBinding.MaleTv.setTextColor(Color.BLACK);
                        }
                        selectedServiceslists = new ArrayList<>();
//                                getCategoryData();
//                                listAddMoreServicesAdapter = new ListAddMoreServicesAdapter(AddClientActivity.this, servicesList, serviceLocationLists, selectedServiceslists);
//                                addClientBinding.selectedServicesList.setAdapter(listAddMoreServicesAdapter);
//                                listAddMoreServicesAdapter.notifyDataSetChanged();
//                                addClientBinding.selectServicesLays.setVisibility(View.VISIBLE);
//                                setServicesData();
                    });
//                        confirmDataLossDialogBox.setNegativeButton("No ", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        Toast.makeText(AddClientActivity.this, " Click on \"Apply\" Button to add selected services. ", Toast.LENGTH_SHORT).show();
//                                        isNoClicked = true;
//                                        if (!addClientBinding.switchRoomAvailability.isChecked()) {
//                                            addClientBinding.switchRoomAvailability.setChecked(true);
//                                        } else {
//                                            addClientBinding.switchRoomAvailability.setChecked(false);
//                                        }
//                                    }
//                                })
//                                .show();
//
//                    } else {
//                        if (!b) {
//                            addClientBinding.MaleTv.setVisibility(View.GONE);
//                            addClientBinding.switchRoomAvailability.setThumbDrawable(getDrawable(R.drawable.female_thumb));
//                            selectedGender = "female";
//                            addClientBinding.femaleTv.setVisibility(View.VISIBLE);
//                        } else {
//                            addClientBinding.switchRoomAvailability.setThumbDrawable(getDrawable(R.drawable.custom_thumb));
//                            addClientBinding.femaleTv.setVisibility(View.GONE);
//                            selectedGender = "male";
//                            addClientBinding.MaleTv.setTextColor(Color.BLACK);
//                        }
                }
            } else {
                isNoClicked = false;
            }
        });

        getCategoryData();

        addClientBinding.btnAddMoreServices.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                addClientBinding.serviceSelectionTopsLay.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        final int scrollViewHeight = addClientBinding.serviceSelectionTopsLay.getHeight();
                        if (scrollViewHeight > 0) {
                            addClientBinding.serviceSelectionTopsLay.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                            final View lastView = addClientBinding.serviceSelectionTopsLay.getChildAt(addClientBinding.serviceSelectionTopsLay.getChildCount() - 1);
                            final int lastViewBottom = lastView.getBottom() + addClientBinding.serviceSelectionTopsLay.getPaddingBottom();
                            final int deltaScrollY = lastViewBottom - scrollViewHeight - addClientBinding.serviceSelectionTopsLay.getScrollY();
                            /* If you want to see the scroll animation, call this. */
                            addClientBinding.serviceSelectionTopsLay.smoothScrollBy(0, deltaScrollY);
                            /* If you don't want, call this. */
                            addClientBinding.serviceSelectionTopsLay.scrollBy(0, deltaScrollY);
                        }
                    }
                });
                if (serviceSelectionHided) {
                    serviceSelectionHided = false;
                    addClientBinding.addClientSelectionLays.setVisibility(View.VISIBLE);
                } else {
                    if (selected_category_position < 1) {
                        Toast.makeText(AddClientActivity.this, "Select any Category", Toast.LENGTH_SHORT).show();
                        addClientBinding.categorySpinner.requestFocus();
                    } else if (selected_services_position < 1) {
                        Toast.makeText(AddClientActivity.this, "Select any services", Toast.LENGTH_SHORT).show();
                        addClientBinding.servicesSpinner.requestFocus();
                    } else {
                        addClientBinding.addClientSelectionLays.setVisibility(View.GONE);
                        selectedServiceslists.add(new ClientSelectedListModel(selected_category_position, selected_services_position, servicesList));
                        listClientAddMoreServicesAdapter.refreshLists(selectedServiceslists);
                        listClientAddMoreServicesAdapter.notifyDataSetChanged();
                        addClientBinding.servicesSpinner.setSelection(0);
                        addClientBinding.categorySpinner.setSelection(0);
                        serviceSelectionHided = true;
                    }
                }
                addClientBinding.serviceSelectionTopsLay.scrollTo(0, addClientBinding.serviceSelectionTopsLay.getBottom());
            }
        });


        addClientBinding.btnBack.setOnClickListener(view -> finish());

        addClientBinding.btnSeeAllSpecialst.setOnClickListener(View -> {
             Intent intent = new Intent(AddClientActivity.this ,  Staff.class);
             intent.putExtra("use_type" , "add_client");
             launcher.launch(intent );

        });

        addClientBinding.btnApply.setOnClickListener(view -> {
//                Toast.makeText(AddClientActivity.this, " APi is not prepared. ", Toast.LENGTH_SHORT).show();
            String name = Objects.requireNonNull(addClientBinding.clientName.getText()).toString();
            String contact = Objects.requireNonNull(addClientBinding.etcontact.getText()).toString();
//                getCurrentSelectedServicesValue();
            if (name.isEmpty()) {
                addClientBinding.clientName.setError(" Mandatory Field! ");
                addClientBinding.clientName.requestFocus();
            } else if (contact.length() != 10) {
                addClientBinding.etcontact.setError(" Mandatory Field! ");
                addClientBinding.etcontact.requestFocus();
            } else if (selectedServiceslists == null || selectedServiceslists.size() < 1) {
                if (selected_category_position < 1) {
                    Toast.makeText(AddClientActivity.this, "Select any Category", Toast.LENGTH_SHORT).show();
                    addClientBinding.categorySpinner.requestFocus();
                } else if (selected_services_position < 1) {
                    Toast.makeText(AddClientActivity.this, "Select any services", Toast.LENGTH_SHORT).show();
                    addClientBinding.servicesSpinner.requestFocus();
                } else if (selected_specialists_id.equals("-1")) {
                    Toast.makeText(this, " Select an specialist for your services ", Toast.LENGTH_SHORT).show();
                } else {
//                    addClientBinding.addClientSelectionLays.setVisibility(View.GONE);
                    Objects.requireNonNull(selectedServiceslists).add(new ClientSelectedListModel(selected_category_position, selected_services_position, servicesList));
                    addClient(contact);
                }
            } else if (selected_specialists_id.equals("-1")) {
                Toast.makeText(this, " Select an specialist for your services ", Toast.LENGTH_SHORT).show();
            } else {
                addClient(contact);

            }
        });

    }

    private void setSelectedStaff(int selected_staff_position) {
        assignSpecialistAdapter.setSelectedItem ( selected_staff_position);
    }

    private void addClient(String contact) {

        FunctionCall.showProgressDialog(AddClientActivity.this);
        //        JSONArray jsonArraySelectedList = parseIntoJsonArrays(selectedClientServiceslists);
        List<String> selectedServicesList = new ArrayList<>();
        for (int i = 0; i < selectedServiceslists.size(); i++) {
//                    selectedServicesList[i] = selectedServiceslists.get(i).getSelected_service_id();
            selectedServicesList.add(String.valueOf(selectedServiceslists.get(i).getServicesList().get(selectedServiceslists.get(i).getSelected_services_position()).getId()));
        }
        Log.d("convertedjsonArray", " " + selectedServicesList);
        String token = "Bearer " + loginResponsePref.getInstance(AddClientActivity.this).getToken();
        Call<AddClientActivityResponse> call = RetrofitClient.getVendorService().AddClient(token, "+91", contact, "skjfdj;sdlmdlsd;l", selected_specialists_id, selectedServicesList);
        call.enqueue(new Callback<AddClientActivityResponse>() {
            @Override
            public void onResponse(@NonNull Call<AddClientActivityResponse> call, @NonNull Response<AddClientActivityResponse> response) {
                FunctionCall.DismissDialog(AddClientActivity.this);
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(AddClientActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    if (response.body().isStatus()) {
                        Intent manageServices = new Intent(AddClientActivity.this, Home.class);
                        startActivity(manageServices);
                        finish();
                    }
                } else {
                    if (response.body() != null) {
                        Toast.makeText(AddClientActivity.this, " " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Log.d("AddClientActivityhit", "onErors: " + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddClientActivityResponse> call, @NonNull Throwable t) {
                FunctionCall.DismissDialog(AddClientActivity.this);
                Log.d("AddClientActivityhit", "onFailure:  " + t.getMessage());
            }
        });
    }


    private void getServicesData() {
        FunctionCall.showProgressDialog(AddClientActivity.this);
        Call<CategoryServicesResponse> call = RetrofitClient.getVendorService().getAllServicesOfCategory("Bearer " + loginResponsePref.getInstance(getApplicationContext()).getToken(), categoriesList.get(selected_category_position).getId() + "", selectedGender);
        call.enqueue(new Callback<CategoryServicesResponse>() {
            @Override
            public void onResponse(@NonNull Call<CategoryServicesResponse> call, @NonNull Response<CategoryServicesResponse> response) {
                FunctionCall.DismissDialog(AddClientActivity.this);
                if (response.isSuccessful() && response.body() != null) {
                    servicesList.addAll(response.body().getCategories());
                    listClientAddMoreServicesAdapter = new ListClientAddMoreServicesAdapter(AddClientActivity.this, selectedServiceslists, selectedGender, categoriesList);
                    addClientBinding.selectedServicesList.setAdapter(listClientAddMoreServicesAdapter);

                } else {
                    if (response.body() != null) {
                        Toast.makeText(AddClientActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();


                    }
                    Log.d("categoryserviceshit", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryServicesResponse> call, @NonNull Throwable t) {
                FunctionCall.DismissDialog(AddClientActivity.this);
                Log.d("categoryserviceshit", "onFailure: " + t.getMessage());
            }
        });
    }

    private void setServicesData() {

        services_adapter = new AddServicesServiceSpinnerAdapter(this, servicesList);
// Specify the layout to use when the list of choices appears

//        category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        addClientBinding.servicesSpinner.setAdapter(services_adapter);
        services_adapter.notifyDataSetChanged();
        addClientBinding.servicesSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {

//                                 Toast.makeText(AddClientActivity.this, " ___  Clicked. ", Toast.LENGTH_SHORT).show();
                        // It returns the clicked item.
                        selected_services_position = position;

//                                 String name = selected_category.getName();
//                        selected_service_id =
//                        Toast.makeText(AddClientActivity.this, name + " selected", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
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
        com.vendor.salon.data_Class.category_services.CategoriesItem serviceDefaultpositionItems = new com.vendor.salon.data_Class.category_services.CategoriesItem();
        serviceDefaultpositionItems.setId(-1);
        serviceDefaultpositionItems.setName("Select Service");
        servicesList.add(serviceDefaultpositionItems);
        setServicesData();

        FunctionCall.showProgressDialog(AddClientActivity.this);
        Call<CategoriesResponse> call = RetrofitClient.getVendorService().getCategories("Bearer " + loginResponsePref.getInstance(getApplicationContext()).getToken(), selectedGender);
        call.enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(@NonNull Call<CategoriesResponse> call, @NonNull Response<CategoriesResponse> response) {
                FunctionCall.DismissDialog(getApplicationContext());
                if (response.isSuccessful() && response.body() != null) {
                    categoriesList.addAll(response.body().getCategories());
                } else {
                    if (response.body() != null) {
                        Toast.makeText(AddClientActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Log.d("categorieshit", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoriesResponse> call, @NonNull Throwable t) {
                FunctionCall.DismissDialog(AddClientActivity.this);
                Log.d("categorieshit", "onFailure: is - " + t.getMessage());
            }
        });
        setCategoryData();
    }


    private void setCategoryData() {

        category_adapter = new AddServicesCategorySpinnerAdapter(this, categoriesList);


        addClientBinding.categorySpinner.setAdapter(category_adapter);
        category_adapter.notifyDataSetChanged();
        addClientBinding.categorySpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {

//                                 Toast.makeText(AddServices.this, " ___  Clicked. ", Toast.LENGTH_SHORT).show();
                        // It returns the clicked item.
                        servicesList = null;
                        servicesList = new ArrayList<>();
                        com.vendor.salon.data_Class.category_services.CategoriesItem serviceDefaultpositionItems = new com.vendor.salon.data_Class.category_services.CategoriesItem();
                        serviceDefaultpositionItems.setId(-1);
                        serviceDefaultpositionItems.setName("Select Service");
                        selected_category_position = position;
                        servicesList.add(serviceDefaultpositionItems);
                        if (selected_category_position > 0) {
                            getServicesData();
                        }
                        if (categoriesList.size() > 1) {
                            setServicesData();
                        }
//                                 String name = selected_category.getName();
//                                 addClientBinding.categorySpinner.setEnabled(false );
//                        Toast.makeText(AddServices.this, name + " selected", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

    }

    private void getSpecialistData() {
        String token = "Bearer " + loginResponsePref.getInstance(AddClientActivity.this).getToken();
        Call<GetStaffResponse> call = RetrofitClient.getVendorService().getAllStaffList(token, "");
        call.enqueue(new Callback<GetStaffResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetStaffResponse> call, @NonNull Response<GetStaffResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus() && response.body().getData() != null && response.body().getData().size() >0 ) {
                    addClientBinding.showNoDataText.setVisibility(View.GONE);
                    setSpecialistDatas(response.body());
                } else {
                     addClientBinding.showNoDataText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetStaffResponse> call, @NonNull Throwable t) {
                Log.d("getstaffhit", "onFailure: " + t.getMessage());
            }
        });
    }


    private void setSpecialistDatas(GetStaffResponse staffData) {

        staff_List.clear();
        DataItem anyoneItem = new DataItem();
        anyoneItem.setName("Anyone");
        anyoneItem.setDesignation(null);
        anyoneItem.setId(-1);
        anyoneItem.setSelected(true);
        staff_List.add(anyoneItem);
        staff_List.addAll(staffData.getData());
        assignSpecialistAdapter.refreshLists(staff_List);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        serviceSelectionHided = false;
        selectedServiceslists.clear();
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
