package com.vendor.salon.activity;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vendor.salon.R;
import com.vendor.salon.adapters.AddServicesCategorySpinnerAdapter;
import com.vendor.salon.adapters.AddServicesServiceLocationAdapter;
import com.vendor.salon.adapters.AddServicesServiceSpinnerAdapter;
import com.vendor.salon.adapters.ListAddMoreServicesAdapter;
import com.vendor.salon.data_Class.AddServicesResponse;
import com.vendor.salon.data_Class.categories.CategoriesItem;
import com.vendor.salon.data_Class.categories.CategoriesResponse;
import com.vendor.salon.data_Class.category_services.CategoryServicesResponse;
import com.vendor.salon.databinding.ActivityAddServicesBinding;
import com.vendor.salon.databinding.RowAddMoreServicesBinding;
import com.vendor.salon.model.SelectedServicesListModel;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.loginResponsePref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddServices extends AppCompatActivity {

    private final String[] serviceLocationLists = new String[]{"Both", "At DoorStep"};
    List<SelectedServicesListModel> selectedServiceslists = new ArrayList<>();
    String selectedGender = "male";
    int selected_services_position = 0;
    int selected_category_position = 0;
    List<com.vendor.salon.data_Class.category_services.CategoriesItem> servicesList;
    List<CategoriesItem> categoriesList = new ArrayList<>();
    AddServicesServiceSpinnerAdapter services_adapter =null ;
    MaterialAlertDialogBuilder confirmDataLossDialogBox;
    boolean isNoClicked = false;
    int selectedServiceLocationPosition = 0;
    private ActivityAddServicesBinding addServicesBinding;
    private boolean serviceSelectionHided = false;
    AddServicesCategorySpinnerAdapter category_adapter = null ;
    ListAddMoreServicesAdapter listAddMoreServicesAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addServicesBinding = ActivityAddServicesBinding.inflate(getLayoutInflater());
        setContentView(addServicesBinding.getRoot());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        addServicesBinding.switchRoomAvailability.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!isNoClicked) {

                    if (selectedServiceslists != null && selectedServiceslists.size() > 0) {
                        confirmDataLossDialogBox = new MaterialAlertDialogBuilder(AddServices.this , R.style.warning_dialog_style);
                        confirmDataLossDialogBox.setTitle(" Are you sure! ");
                        confirmDataLossDialogBox.setMessage(" Your selected services list will be lost since  you have not added your selected services .");
                        confirmDataLossDialogBox.setCancelable(false);
                        confirmDataLossDialogBox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (!b) {
                                    addServicesBinding.MaleTv.setVisibility(View.GONE);
                                    addServicesBinding.switchRoomAvailability.setThumbDrawable(getDrawable(R.drawable.female_thumb));
                                    selectedGender = "female";
                                    addServicesBinding.femaleTv.setVisibility(View.VISIBLE);
                                } else {
                                    addServicesBinding.switchRoomAvailability.setThumbDrawable(getDrawable(R.drawable.custom_thumb));
                                    addServicesBinding.femaleTv.setVisibility(View.GONE);
                                    selectedGender = "male";
                                    addServicesBinding.MaleTv.setVisibility(View.VISIBLE);
                                    addServicesBinding.MaleTv.setTextColor(Color.BLACK);
                                }
                                selectedServiceslists = new ArrayList<>();
                                getCategoryData();
                                listAddMoreServicesAdapter = new ListAddMoreServicesAdapter(AddServices.this, servicesList, serviceLocationLists, selectedServiceslists);
                                addServicesBinding.selectedServicesList.setAdapter(listAddMoreServicesAdapter);
                                listAddMoreServicesAdapter.notifyDataSetChanged();
                                 addServicesBinding.selectServicesLays.setVisibility(View.VISIBLE);
                                setServicesData();
                            }
                        });
                        confirmDataLossDialogBox.setNegativeButton("No ", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(AddServices.this, " Click on \"Apply\" Button to add selected services. ", Toast.LENGTH_SHORT).show();
                                        isNoClicked = true;
                                        if (!addServicesBinding.switchRoomAvailability.isChecked()) {
                                            addServicesBinding.switchRoomAvailability.setChecked(true);
                                        } else {
                                            addServicesBinding.switchRoomAvailability.setChecked(false);
                                        }
                                    }
                                })
                                .show();

                    } else {
                        if (!b) {
                            addServicesBinding.MaleTv.setVisibility(View.GONE);
                            addServicesBinding.switchRoomAvailability.setThumbDrawable(getDrawable(R.drawable.female_thumb));
                            selectedGender = "female";
                            addServicesBinding.femaleTv.setVisibility(View.VISIBLE);
                        } else {
                            addServicesBinding.switchRoomAvailability.setThumbDrawable(getDrawable(R.drawable.custom_thumb));
                            addServicesBinding.femaleTv.setVisibility(View.GONE);
                            selectedGender = "male";
                            addServicesBinding.MaleTv.setVisibility(View.VISIBLE);
                            addServicesBinding.MaleTv.setTextColor(Color.BLACK);
                        }
                    }
                } else {
                    isNoClicked = false;
                }
            }
        });

        addServicesBinding.serviceLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedServiceLocationPosition = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        AddServicesServiceLocationAdapter serviceLocationAdapter = new AddServicesServiceLocationAdapter(AddServices.this, serviceLocationLists);
        getCategoryData();

        addServicesBinding.serviceLocationSpinner.setAdapter(serviceLocationAdapter);
        addServicesBinding.btnAddMoreServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addServicesBinding.serviceSelectionTopsLay.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        final int scrollViewHeight = addServicesBinding.serviceSelectionTopsLay.getHeight();
                        if (scrollViewHeight > 0) {
                            addServicesBinding.serviceSelectionTopsLay.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                            final View lastView = addServicesBinding.serviceSelectionTopsLay.getChildAt(addServicesBinding.serviceSelectionTopsLay.getChildCount() - 1);
                            final int lastViewBottom = lastView.getBottom() + addServicesBinding.serviceSelectionTopsLay.getPaddingBottom();
                            final int deltaScrollY = lastViewBottom - scrollViewHeight - addServicesBinding.serviceSelectionTopsLay.getScrollY();
                            /* If you want to see the scroll animation, call this. */
                            addServicesBinding.serviceSelectionTopsLay.smoothScrollBy(0, deltaScrollY);
                            /* If you don't want, call this. */
                            addServicesBinding.serviceSelectionTopsLay.scrollBy(0, deltaScrollY);
                        }
                    }
                });
                if (serviceSelectionHided) {
                    serviceSelectionHided = false;
                    addServicesBinding.selectServicesLays.setVisibility(View.VISIBLE);
                } else {
                    String mrp_price = addServicesBinding.mrpPrice.getText() + "";
                    String offer_price = addServicesBinding.etOfferPrice.getText() + "";
                    if (selected_category_position < 1) {
                        Toast.makeText(AddServices.this, "Select any Category", Toast.LENGTH_SHORT).show();
                        addServicesBinding.categorySpinner.requestFocus();
                    } else if (selected_services_position < 1) {
                        Toast.makeText(AddServices.this, "Select any services", Toast.LENGTH_SHORT).show();
                        addServicesBinding.servicesSpinner.requestFocus();
                    } else if (mrp_price.isEmpty()) {
                        addServicesBinding.servicesSpinner.setBackgroundColor(Color.WHITE);
                        addServicesBinding.mrpPrice.setError("Mandatory field ");
                        addServicesBinding.mrpPrice.requestFocus();
                    } else {
                        if (!offer_price.isEmpty()) {
                            if (Float.parseFloat(offer_price) > 0f && (Float.parseFloat(offer_price) > Float.parseFloat(mrp_price))) {
                                addServicesBinding.etOfferPrice.setError(" Offer price should be less than Mrp . ");
                                addServicesBinding.etOfferPrice.requestFocus();
                            }
//                    else if (selectedServiceLocationPosition == 0) {
//                        Toast.makeText(AddServices.this, "Please Select service Location . ", Toast.LENGTH_SHORT).show();
//                        addServicesBinding.serviceLocationSpinner.requestFocus();
//                    }
                            else {
                                addServicesBinding.selectServicesLays.setVisibility(View.GONE);
                                addServicesBinding.mrpPrice.setTextColor(Color.BLACK);
                                selectedServiceslists.add(new SelectedServicesListModel(mrp_price, offer_price, categoriesList.get(selected_category_position).getId() + "", selectedServiceLocationPosition == 1, selected_services_position, selectedGender));
                                listAddMoreServicesAdapter = new ListAddMoreServicesAdapter(AddServices.this, servicesList, serviceLocationLists, selectedServiceslists);
                                addServicesBinding.selectedServicesList.setAdapter(listAddMoreServicesAdapter);
                                listAddMoreServicesAdapter.notifyDataSetChanged();
                                addServicesBinding.servicesSpinner.setSelection(0);
                                addServicesBinding.serviceLocationSpinner.setSelection(0);
                                addServicesBinding.mrpPrice.setText(null);
                                addServicesBinding.etOfferPrice.setText(null);
                                serviceSelectionHided = true;
                            }
                        } else {
                            addServicesBinding.selectServicesLays.setVisibility(View.GONE);
                            addServicesBinding.mrpPrice.setTextColor(Color.BLACK);
                            selectedServiceslists.add(new SelectedServicesListModel(mrp_price, offer_price, categoriesList.get(selected_category_position).getId() + "", selectedServiceLocationPosition == 1, selected_services_position, selectedGender));
                            ListAddMoreServicesAdapter listAddMoreServicesAdapter = new ListAddMoreServicesAdapter(AddServices.this, servicesList, serviceLocationLists, selectedServiceslists);
                            addServicesBinding.selectedServicesList.setAdapter(listAddMoreServicesAdapter);
                            listAddMoreServicesAdapter.notifyDataSetChanged();
                            addServicesBinding.servicesSpinner.setSelection(0);
                            addServicesBinding.serviceLocationSpinner.setSelection(0);
                            addServicesBinding.mrpPrice.setText(null);
                            addServicesBinding.etOfferPrice.setText(null);
                            serviceSelectionHided = true;

                        }
                        addServicesBinding.serviceSelectionTopsLay.scrollTo(0 , addServicesBinding.serviceSelectionTopsLay.getBottom() );
                    }
                }
            }
        });


        addServicesBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addServicesBinding.btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(AddServices.this, " APi is not prepared. ", Toast.LENGTH_SHORT).show();
//                getCurrentSelectedServicesValue();
                FunctionCall.showProgressDialog(AddServices.this);
                JSONArray jsonArraySelectedList = parseIntoJsonArrays(selectedServiceslists);
                JSONObject finalObjct = new JSONObject();
                JsonParser jsonParser = new JsonParser();
                try {
                    finalObjct = finalObjct.put("service_data", jsonArraySelectedList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObject gsonObject = new JsonObject();
                gsonObject = (JsonObject) jsonParser .parse(finalObjct.toString());
                Log.d("convertedjsonArray", " " + gsonObject.toString());
                String token = "Bearer " + loginResponsePref.getInstance(AddServices.this).getToken();
                Call<AddServicesResponse> call = RetrofitClient.getVendorService().addServices(token, gsonObject );
                call.enqueue(new Callback<AddServicesResponse>() {
                    @Override
                    public void onResponse(Call<AddServicesResponse> call, Response<AddServicesResponse> response) {
                        FunctionCall.DismissDialog(AddServices.this) ;
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(AddServices.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            if (response.body().isStatus()) {
                                Intent manageServices = new Intent(AddServices.this, ManageServices.class);
                                startActivity(manageServices);
                                finish();
                            }
                        } else {
                            if (response.body() != null) {
                                Toast.makeText(AddServices.this, " " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            Log.d("addserviceshit", "onErors: " + response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<AddServicesResponse> call, Throwable t) {
                        FunctionCall.DismissDialog(AddServices.this) ;
                        Log.d("addserviceshit", "onFailure:  " + t.getMessage());
                    }
                });
            }
        });
    }

//    private void getCurrentSelectedServicesValue() {
//        for (int adapterItemPosition = 0; adapterItemPosition < selectedServiceslists.size(); adapterItemPosition++) {
//            RowAddMoreServicesBinding addMoreServicesBinding = (RowAddMoreServicesBinding) addServicesBinding.selectedServicesList.getItemAtPosition(0);
//            SelectedServicesListModel selectedServicesListmodel = selectedServiceslists.get(adapterItemPosition);
//            String mrp_price = addMoreServicesBinding.rowMrpPrice.getText() + "";
//            String offer_price = addMoreServicesBinding.rowEtOfferPrice.getText() + "";
//            if (selected_services_position < 1) {
//                Toast.makeText(AddServices.this, "Select any services", Toast.LENGTH_SHORT).show();
//                addMoreServicesBinding.rowServicesSpinner.requestFocus();
//            } else if (mrp_price.isEmpty()) {
//                addMoreServicesBinding.rowServicesSpinner.setBackgroundColor(Color.WHITE);
//                addMoreServicesBinding.rowMrpPrice.setError("Mandatory field ");
//                addMoreServicesBinding.rowMrpPrice.requestFocus();
//
//            } else {
//                if (!offer_price.isEmpty()) {
//                    if (Float.parseFloat(offer_price) > 0f && (Float.parseFloat(offer_price) > Float.parseFloat(mrp_price))) {
//                        addMoreServicesBinding.rowEtOfferPrice.setError(" Offer price should be less than Mrp . ");
//                        addMoreServicesBinding.rowEtOfferPrice.requestFocus();
//                    } else {
//                        selectedServicesListmodel.setMrp(addMoreServicesBinding.rowMrpPrice.getText().toString());
//                        selectedServicesListmodel.setSelected_service_position(addMoreServicesBinding.rowServicesSpinner.getSelectedItemPosition());
//
//                        selectedServicesListmodel.setPayable_amount(addMoreServicesBinding.rowEtOfferPrice.getText().toString());
//                        if (addMoreServicesBinding.rowServiceLocationSpinner.getSelectedItemPosition() == 0) {
//                            selectedServicesListmodel.setIs_door_step(false);
//                        } else {
//                            selectedServicesListmodel.setIs_door_step(true);
//                        }
//                    }
//                } else {
//                    selectedServicesListmodel.setMrp(addMoreServicesBinding.rowMrpPrice.getText().toString());
//                    selectedServicesListmodel.setSelected_service_position(addMoreServicesBinding.rowServicesSpinner.getSelectedItemPosition());
//
//                    selectedServicesListmodel.setPayable_amount(addMoreServicesBinding.rowEtOfferPrice.getText().toString());
//                    if (addMoreServicesBinding.rowServiceLocationSpinner.getSelectedItemPosition() == 0) {
//                        selectedServicesListmodel.setIs_door_step(false);
//                    } else {
//                        selectedServicesListmodel.setIs_door_step(true);
//                    }
//                }
//                continue;
//            }
//            break;
//        }
//    }

    private JSONArray parseIntoJsonArrays(List<SelectedServicesListModel> selectedServiceslists) {
        JSONArray jsonArraySelectedList = new JSONArray();
        for (int i = 0; i < selectedServiceslists.size(); i++) {
            SelectedServicesListModel selectedServiceItem = selectedServiceslists.get(i);
            JSONObject single_serivces_value = new JSONObject();
            try {
                single_serivces_value.put("mrp", selectedServiceItem.getMrp());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                single_serivces_value.put("offer_price", selectedServiceItem.getPayable_amount());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                single_serivces_value.put("service_name", servicesList.get(selected_services_position).getName() + "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                single_serivces_value.put("service_id", servicesList.get(selectedServiceItem.getSelected_service_position()).getId() + "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                single_serivces_value.put("category_id", selectedServiceItem.getCategory_id());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (selectedServiceItem.is_door_step()) {
                    single_serivces_value.put("is_doorstep", 2 + "");
                } else {
                    single_serivces_value.put("is_doorstep", "");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                single_serivces_value.put("gender", selectedServiceItem.getGender());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArraySelectedList.put(single_serivces_value);
        }


        return jsonArraySelectedList;
    }

    private void getServicesData() {
        FunctionCall.showProgressDialog(AddServices.this);
        Call<CategoryServicesResponse> call = RetrofitClient.getVendorService().getAllServicesOfCategory("Bearer " + loginResponsePref.getInstance(getApplicationContext()).getToken(), categoriesList.get(selected_category_position).getId() + "", selectedGender);
        call.enqueue(new Callback<CategoryServicesResponse>() {
            @Override
            public void onResponse(Call<CategoryServicesResponse> call, Response<CategoryServicesResponse> response) {
                FunctionCall.DismissDialog( AddServices.this);
                if (response.isSuccessful() && response.body() != null) {
                    servicesList.addAll(response.body().getCategories());
                } else {
                    if (response.body() != null) {
                        Toast.makeText(AddServices.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Log.d("categoryserviceshit", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<CategoryServicesResponse> call, Throwable t) {
                FunctionCall.DismissDialog(AddServices.this) ;
                Log.d("categoryserviceshit", "onFailure: " + t.getMessage());
            }
        });
    }

    private void setServicesData() {

        services_adapter = new AddServicesServiceSpinnerAdapter(this, servicesList);
// Specify the layout to use when the list of choices appears

//        category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        addServicesBinding.servicesSpinner.setAdapter(services_adapter);
        services_adapter.notifyDataSetChanged();
        addServicesBinding.servicesSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {

//                                 Toast.makeText(AddServices.this, " ___  Clicked. ", Toast.LENGTH_SHORT).show();
                        // It returns the clicked item.
                        selected_services_position = position;

//                                 String name = selected_category.getName();
//                        selected_service_id =
//                        Toast.makeText(AddServices.this, name + " selected", Toast.LENGTH_SHORT).show();
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

        FunctionCall.showProgressDialog( AddServices.this);
        Call<CategoriesResponse> call = RetrofitClient.getVendorService().getCategories("Bearer " + loginResponsePref.getInstance(getApplicationContext()).getToken(), selectedGender);
        call.enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                FunctionCall.DismissDialog(getApplicationContext());
                if (response.isSuccessful() && response.body() != null) {
                    categoriesList.addAll(response.body().getCategories());
                } else {
                    if (response.body() != null) {
                        Toast.makeText(AddServices.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Log.d("categorieshit", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                FunctionCall.DismissDialog(AddServices.this) ;
                Log.d("categorieshit", "onFailure: is - " + t.getMessage());
            }
        });
        setCategoryData();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void setCategoryData() {
        // Create an ArrayAdapter using the string array and a default spinner layout

        category_adapter = new AddServicesCategorySpinnerAdapter(this, categoriesList);
// Specify the layout to use when the list of choices appears

//        category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        addServicesBinding.categorySpinner.setAdapter(category_adapter);
        category_adapter.notifyDataSetChanged();
        addServicesBinding.categorySpinner.setOnItemSelectedListener(
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
//                                 addServicesBinding.categorySpinner.setEnabled(false );
//                        Toast.makeText(AddServices.this, name + " selected", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

    }


}