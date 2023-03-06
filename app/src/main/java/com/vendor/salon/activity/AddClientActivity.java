package com.vendor.salon.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.JsonParser;
import com.vendor.salon.R;
import com.vendor.salon.adapters.AddServicesCategorySpinnerAdapter;
import com.vendor.salon.adapters.AddServicesServiceSpinnerAdapter;
import com.vendor.salon.adapters.AssignSpecialistAdapter;
import com.vendor.salon.adapters.ListClientAddMoreServicesAdapter;
import com.vendor.salon.data_Class.addclient.AddClientActivityResponse;
import com.vendor.salon.data_Class.categories.CategoriesItem;
import com.vendor.salon.data_Class.categories.CategoriesResponse;
import com.vendor.salon.data_Class.category_services.CategoryServicesResponse;
import com.vendor.salon.data_Class.getStaff.GetStaffResponse;
import com.vendor.salon.databinding.ActivityAddClientBinding;
import com.vendor.salon.model.ClientSelectedListModel;
import com.vendor.salon.model.SelectedServicesListModel;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.loginResponsePref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddClientActivity extends AppCompatActivity {

    public static String selected_specialists_id = "-1";
    ActivityAddClientBinding addClientBinding;
    boolean isNoClicked = true;
    private MaterialAlertDialogBuilder confirmDataLossDialogBox;
    String selectedGender;
    boolean serviceSelectionHided = true;
    int selected_category_position = 0;
    int selected_services_position = 0;
     public static List<ClientSelectedListModel> selectedServiceslists = new ArrayList<>();

    ArrayList<com.vendor.salon.data_Class.category_services.CategoriesItem> servicesList;
    ArrayList<CategoriesItem> categoriesList = new ArrayList<>() ;
    ListClientAddMoreServicesAdapter listClientAddMoreServicesAdapter ;
    AddServicesCategorySpinnerAdapter category_adapter;
    AddServicesServiceSpinnerAdapter services_adapter;


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addClientBinding = DataBindingUtil.setContentView(AddClientActivity.this, R.layout.activity_add_client);

        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN );
        listClientAddMoreServicesAdapter = new ListClientAddMoreServicesAdapter(AddClientActivity.this, selectedServiceslists , selectedGender,  categoriesList );
        addClientBinding.selectedServicesList.setAdapter(listClientAddMoreServicesAdapter);
        getSpecialistData();

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
                        selectedServiceslists.add(new ClientSelectedListModel(selected_category_position, selected_services_position ,servicesList));
                        listClientAddMoreServicesAdapter.refreshLists(selectedServiceslists );
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

        addClientBinding.btnApply.setOnClickListener(view -> {
//                Toast.makeText(AddClientActivity.this, " APi is not prepared. ", Toast.LENGTH_SHORT).show();
            String name = addClientBinding.clientName.getText().toString();
            String contact = addClientBinding.etcontact.getText().toString();
//                getCurrentSelectedServicesValue();
            if (name.isEmpty()) {
                addClientBinding.clientName.setError(" Mandatory Field! ");
                addClientBinding.clientName.requestFocus();
            } else if (contact.length() != 10) {
                addClientBinding.etcontact.setError(" Mandatory Field! ");
                addClientBinding.etcontact.requestFocus();
            } else if ( selectedServiceslists == null || selectedServiceslists.size()< 1 ) {
                Toast.makeText(this, " Select at least one service ", Toast.LENGTH_SHORT).show();
            }
            else if (selected_specialists_id.equals("-1")) {
                Toast.makeText(this, " Select an specialist for your services ", Toast.LENGTH_SHORT).show();
            }
            else {
                FunctionCall.showProgressDialog(AddClientActivity.this);
                //        JSONArray jsonArraySelectedList = parseIntoJsonArrays(selectedClientServiceslists);
                JSONObject finalObjct = new JSONObject();
                JsonParser jsonParser = new JsonParser();
                List<String> selectedServicesList = new ArrayList<>();
                for (int i = 0; i < selectedServiceslists.size(); i++) {
//                    selectedServicesList[i] = selectedServiceslists.get(i).getSelected_service_id();
                    selectedServicesList.add(String.valueOf(selectedServiceslists.get(i).getServicesList().get(selectedServiceslists.get(i).getSelected_services_position()).getId()));
                }
//            try {
//                finalObjct.put("services", selectedServicesList);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            JsonObject gsonObject = new JsonObject();
//            gsonObject = (JsonObject) jsonParser.parse(finalObjct.toString());
                Log.d("convertedjsonArray", " " + selectedServicesList.toString());
                String token = "Bearer " + loginResponsePref.getInstance(AddClientActivity.this).getToken();
//                HashMap<String, String > myValmaps = new HashMap<>();
//                myValmaps.put("country_code" ,"+91" );
//                myValmaps.put("phone" ,contact );
//                myValmaps.put("booking_time" , "skd kds k sdj" );
//                myValmaps.put("specialist_id" , selected_specialists_id );
//                myValmaps.put("services" , selectedServicesList );
                Call<AddClientActivityResponse> call = RetrofitClient.getVendorService().AddClient(token, "+91", contact , "skjfdj;sdlmdlsd;l", selected_specialists_id, selectedServicesList);
                call.enqueue(new Callback<AddClientActivityResponse>() {
                    @Override
                    public void onResponse(Call<AddClientActivityResponse> call, Response<AddClientActivityResponse> response) {
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
                    public void onFailure(Call<AddClientActivityResponse> call, Throwable t) {
                        FunctionCall.DismissDialog(AddClientActivity.this);
                        Log.d("AddClientActivityhit", "onFailure:  " + t.getMessage());
                    }
                });
            }
        });

    }


    private JSONArray parseIntoJsonArrays(List<SelectedServicesListModel> selectedServiceslists) {
        JSONArray jsonArraySelectedList = new JSONArray();
        for (int i = 0; i < selectedServiceslists.size(); i++) {
            SelectedServicesListModel selectedServiceItem = selectedServiceslists.get(i);
            JSONObject single_serivces_value = new JSONObject();
            try {
                single_serivces_value.put("service_id", servicesList.get(selectedServiceItem.getSelected_service_position()).getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArraySelectedList.put(single_serivces_value);
        }


        return jsonArraySelectedList;
    }

    private void getServicesData() {
        FunctionCall.showProgressDialog(AddClientActivity.this);
        Call<CategoryServicesResponse> call = RetrofitClient.getVendorService().getAllServicesOfCategory("Bearer " + loginResponsePref.getInstance(getApplicationContext()).getToken(), categoriesList.get(selected_category_position).getId() + "", selectedGender);
        call.enqueue(new Callback<CategoryServicesResponse>() {
            @Override
            public void onResponse(Call<CategoryServicesResponse> call, Response<CategoryServicesResponse> response) {
                FunctionCall.DismissDialog(AddClientActivity.this);
                if (response.isSuccessful() && response.body() != null) {
                    servicesList.addAll(response.body().getCategories());
                    listClientAddMoreServicesAdapter = new ListClientAddMoreServicesAdapter(AddClientActivity.this, selectedServiceslists, selectedGender, categoriesList );
                    addClientBinding.selectedServicesList.setAdapter(listClientAddMoreServicesAdapter);

                } else {
                    if (response.body() != null) {
                        Toast.makeText(AddClientActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();


                    }
                    Log.d("categoryserviceshit", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<CategoryServicesResponse> call, Throwable t) {
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
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
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
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {
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
        Call<GetStaffResponse> call = RetrofitClient.getVendorService().getAllStaffList(token , "" );
        call.enqueue(new Callback<GetStaffResponse>() {
            @Override
            public void onResponse(Call<GetStaffResponse> call, Response<GetStaffResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    setSpecialistDatas(response.body() );
                }
            }

            @Override
            public void onFailure(Call<GetStaffResponse> call, Throwable t) {
                Log.d("getstaffhit", "onFailure: " + t.getMessage());
            }
        });
    }


    private void setSpecialistDatas(GetStaffResponse staffData) {
        final AssignSpecialistAdapter assignSpecialistAdapter = new AssignSpecialistAdapter(staffData.getData());
        addClientBinding.staffList.setAdapter(assignSpecialistAdapter);

    }


}
