package com.vendor.salon.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vendor.salon.R;
import com.vendor.salon.activity.CategoryDetails;
import com.vendor.salon.data_Class.vendor_enable_disable_service.VendorEnableDisableService;
import com.vendor.salon.data_Class.vendor_sub_catgories.DataItem;
import com.vendor.salon.databinding.ServiceItemBinding;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.loginResponsePref;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicesListAdapter extends RecyclerView.Adapter<ServicesListAdapter.ViewHolder> {
    private static final String TAG = "vendor_enable_disable_services_hit";
    private List<DataItem> receivedServicesList;
    CategoryDetails categoryDetails ;

    public ServicesListAdapter( CategoryDetails categoryDetails ,List<DataItem> serviceList) {
        this.categoryDetails = categoryDetails ;
        this.receivedServicesList = serviceList ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ServiceItemBinding itemsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.service_item, parent, false);

        return new ViewHolder(itemsBinding) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataItem service = receivedServicesList.get(position);
        if(service != null) {
            Glide.with(holder.serviceItemBinding.itemImage.getContext()).load(service.getImage()).into(holder.serviceItemBinding.itemImage);
            holder.serviceItemBinding.serviceName.setText(service.getServiceName());
            holder.serviceItemBinding.servicePrice.setText(service.getOfferPrice()+"");

            if ( !service.getDisabled().equals("2") ) {
                  holder.serviceItemBinding.activeBtnLay.setImageResource( R.drawable.in_active);
            } else {
                holder.serviceItemBinding.activeBtnLay.setImageResource(R.drawable.active);
            }

            holder.serviceItemBinding.switchBtnActive.setOnClickListener(View -> {
                    ChangeActiveStatus( service.getId() + "" , position);
                notifyItemChanged(position);

            });
        }
    }

    private void ChangeActiveStatus(String id, int position) {

        FunctionCall.showProgressDialog(categoryDetails);
        String token = "Bearer " + loginResponsePref.getInstance( categoryDetails).getToken();
        Call<VendorEnableDisableService> call = RetrofitClient.getVendorService().changeActiveStatus(token , id) ;
        call.enqueue(new Callback<VendorEnableDisableService>() {
            @Override
            public void onResponse(Call<VendorEnableDisableService> call, Response<VendorEnableDisableService> response) {
                FunctionCall.DismissDialog(categoryDetails);
                if ( response.isSuccessful() && response.body() != null && response.body().isStatus() ) {
                    if ( receivedServicesList.get(position).getDisabled().equals("2")) {
                         receivedServicesList.get(position).setDisabled("1");
                    }
                    else {
                        receivedServicesList.get(position).setDisabled("2");
                    }
                    notifyItemChanged(position);
                }
                else {
                     if ( response.body() != null ) {
                         Toast.makeText(categoryDetails, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                     }
                    Log.d(TAG, "onResponse: " + response);
                }
            }

            @Override
            public void onFailure(Call<VendorEnableDisableService> call, Throwable t) {
                FunctionCall.DismissDialog(categoryDetails);
                Log.d(TAG, "onFailure: " + t.getMessage() );
            }
        });
    }
    @Override
    public int getItemCount() {
        return receivedServicesList.size() ;
    }

    public class    ViewHolder extends RecyclerView.ViewHolder {
        private final ServiceItemBinding serviceItemBinding;

        public ViewHolder(@NonNull ServiceItemBinding serviceItemBinding) {
            super(serviceItemBinding.getRoot());
            this.serviceItemBinding = serviceItemBinding ;
        }


    }
}
