package com.vendor.salon.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.vendor.salon.R;
import com.vendor.salon.activity.AddClientActivity;
import com.vendor.salon.data_Class.categories.CategoriesItem;
import com.vendor.salon.data_Class.category_services.CategoryServicesResponse;
import com.vendor.salon.databinding.ItemSelectedClientServiceLayBinding;
import com.vendor.salon.model.ClientSelectedListModel;
import com.vendor.salon.model.SelectedServicesListModel;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.loginResponsePref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListClientAddMoreServicesAdapter extends RecyclerView.Adapter<ListClientAddMoreServicesAdapter.ViewHolders> {
    String  selectedGender ;
    List<ClientSelectedListModel> selectedServiceslists ;
    ArrayList<CategoriesItem> categoriesList;
    AddClientActivity addClientActivity ;

    public ListClientAddMoreServicesAdapter(AddClientActivity addClientActivity, List<ClientSelectedListModel> selectedServiceslists, String selectedGender, ArrayList<CategoriesItem> categoriesList ) {
        this.selectedServiceslists = selectedServiceslists ;
        this.addClientActivity = addClientActivity;
        this.categoriesList = categoriesList;
        this.selectedGender  = selectedGender ;
    }


    @NonNull
    @Override
    public ListClientAddMoreServicesAdapter.ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType ) {
         ItemSelectedClientServiceLayBinding itemSelectedClientServiceLayBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()) , R.layout.item_selected_client_service_lay, parent , false);

         return new ViewHolders(itemSelectedClientServiceLayBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ListClientAddMoreServicesAdapter.ViewHolders holder, int position) {
        ClientSelectedListModel servicesListModel = selectedServiceslists.get(position) ;
        holder.itemSelectedClientServiceLayBinding.categorySpinner.setAdapter(new AddServicesCategorySpinnerAdapter(holder.itemSelectedClientServiceLayBinding.categorySpinner.getContext(), categoriesList ));
//        getServicesData( servicesListModel.getSelected_category_position() );
        holder.itemSelectedClientServiceLayBinding.servicesSpinner.setAdapter(new AddServicesServiceSpinnerAdapter (holder.itemSelectedClientServiceLayBinding.servicesSpinner.getContext(), selectedServiceslists.get(position).getServicesList()));

        holder.itemSelectedClientServiceLayBinding.categorySpinner.setSelection( servicesListModel.getSelected_category_position());
        holder.itemSelectedClientServiceLayBinding.servicesSpinner.setSelection( servicesListModel.getSelected_services_position());

//        holder.itemSelectedClientServiceLayBinding.categorySpinner.setOnItemSelectedListener(
//                new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent,
//                                               View view, int categories_positio, long id) {
//
//                        if (position > 0) {
//                            getServicesData(categories_positio , position);
//                        }
//                        else {
//                            ArrayList<com.vendor.salon.data_Class.category_services.CategoriesItem> servicesList = new ArrayList<>();
//                            com.vendor.salon.data_Class.category_services.CategoriesItem serviceDefaultpositionItems = new com.vendor.salon.data_Class.category_services.CategoriesItem();
//                            serviceDefaultpositionItems.setId(-1);
//                            serviceDefaultpositionItems.setName("Select Service");
////                        selected_category_position = position;
//                            servicesList.add(serviceDefaultpositionItems);
//
//                            AddClientActivity.selectedServiceslists.get(position).setServicesList(servicesList);
//                            AddClientActivity.selectedServiceslists.get(position).setSelected_category_position(categories_positio);
//                            AddClientActivity.selectedServiceslists.get(position).setSelected_services_position(0);
//                        }
//                        notifyItemChanged(position );
//                         }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//                    }
//
//                });


    }



    public void refreshLists(List<ClientSelectedListModel> selectedServiceslists ) {
        this.selectedServiceslists = selectedServiceslists;
    }



    @Override
    public int getItemCount() {
        return selectedServiceslists.size();
    }


    public static class ViewHolders extends RecyclerView.ViewHolder {
        ItemSelectedClientServiceLayBinding itemSelectedClientServiceLayBinding ;
        public ViewHolders(@NonNull ItemSelectedClientServiceLayBinding itemSelectedClientServiceLayBinding ) {
            super(itemSelectedClientServiceLayBinding.getRoot() );
            this.itemSelectedClientServiceLayBinding = itemSelectedClientServiceLayBinding ;
        }
    }

    private void getServicesData(int selected_category_position, int position) {
        FunctionCall.showProgressDialog(addClientActivity);
        ArrayList<com.vendor.salon.data_Class.category_services.CategoriesItem> servicesList = new ArrayList<>(); com.vendor.salon.data_Class.category_services.CategoriesItem serviceDefaultpositionItems = new com.vendor.salon.data_Class.category_services.CategoriesItem();
        serviceDefaultpositionItems.setId(-1);
        serviceDefaultpositionItems.setName("Select Service");
//                        selected_category_position = position;
        servicesList.add(serviceDefaultpositionItems);



        Call<CategoryServicesResponse> call = RetrofitClient.getVendorService().getAllServicesOfCategory("Bearer " + loginResponsePref.getInstance(addClientActivity).getToken(), categoriesList.get(selected_category_position).getId() + "", selectedGender);
        call.enqueue(new Callback<CategoryServicesResponse>() {
            @Override
            public void onResponse(Call<CategoryServicesResponse> call, Response<CategoryServicesResponse> response) {
                FunctionCall.DismissDialog(addClientActivity);
                if (response.isSuccessful() && response.body() != null) {
                    servicesList.addAll(response.body().getCategories());

                    AddClientActivity.selectedServiceslists.get(position).setServicesList(servicesList);
                    AddClientActivity.selectedServiceslists.get(position).setSelected_category_position( selected_category_position);
                    AddClientActivity.selectedServiceslists.get(position).setSelected_services_position(0);
                } else {
                    if (response.body() != null) {
                        Toast.makeText( addClientActivity, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();


                    }
                    Log.d("categoryserviceshit", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<CategoryServicesResponse> call, Throwable t) {
                FunctionCall.DismissDialog(addClientActivity);
                Log.d("categoryserviceshit", "onFailure: " + t.getMessage());
            }
        });
    }


}
