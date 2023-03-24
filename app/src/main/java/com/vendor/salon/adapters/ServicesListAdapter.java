package com.vendor.salon.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vendor.salon.R;
import com.vendor.salon.activity.CategoryDetails;
import com.vendor.salon.data_Class.update_services.UpdateServiceResponse;
import com.vendor.salon.data_Class.vendor_enable_disable_service.VendorEnableDisableService;
import com.vendor.salon.data_Class.vendor_sub_catgories.DataItem;
import com.vendor.salon.databinding.ServiceItemBinding;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.loginResponsePref;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicesListAdapter extends RecyclerView.Adapter<ServicesListAdapter.ViewHolder> {
    private static final String TAG = "vendor_enable_disable_services_hit";
    private List<DataItem> receivedServicesList;
    CategoryDetails categoryDetails;

    public ServicesListAdapter(CategoryDetails categoryDetails, List<DataItem> serviceList) {
        this.categoryDetails = categoryDetails;
        this.receivedServicesList = serviceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ServiceItemBinding itemsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.service_item, parent, false);

        return new ViewHolder(itemsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataItem service = receivedServicesList.get(position);
        if (service != null) {
            Glide.with(holder.serviceItemBinding.itemImage.getContext()).load(service.getImage()).into(holder.serviceItemBinding.itemImage);
            holder.serviceItemBinding.serviceName.setText(service.getServiceName());
            holder.serviceItemBinding.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(categoryDetails, R.style.BottomSheetDialogStyle);
                    bottomSheetDialog.setContentView(R.layout.edit_service_price_lay);
                    AppCompatEditText et_mrp = bottomSheetDialog.findViewById(R.id.et_mrp_price);
                    AppCompatEditText et_offer_price = bottomSheetDialog.findViewById(R.id.et_offer_price);
                    AppCompatButton btn_applys = bottomSheetDialog.findViewById(R.id.btn_update_prices);
                    bottomSheetDialog.show();
                    et_mrp.requestFocus();

                    btn_applys.setOnClickListener(View -> {
                        String mrp_price = et_mrp.getText().toString();
                        String offer_prices = et_offer_price.getText().toString();
                        if (mrp_price.isEmpty()) {
                            et_mrp.setError("Mandatory!");
                            et_mrp.requestFocus();
                        } else {
                            if (!offer_prices.isEmpty()) {
                                if (Integer.parseInt(offer_prices) > Integer.parseInt(mrp_price)) {
                                    et_offer_price.requestFocus();
                                    et_offer_price.setError(" Please enter correct offer price ");
                                } else {
                                    submitChangess(mrp_price, offer_prices, String.valueOf(service.getId()) , bottomSheetDialog, btn_applys , position );
                                }
                            } else {
                                submitChangess(mrp_price, offer_prices, String.valueOf(service.getId()), bottomSheetDialog, btn_applys , position );
                            }
                        }
                    });
                }
            });
            holder.serviceItemBinding.servicePrice.setText(service.getOfferPrice() + "");

            if (!service.getDisabled().equals("2")) {
                holder.serviceItemBinding.activeBtnLay.setImageResource(R.drawable.in_active);
                holder.serviceItemBinding.serviceImageLays.setAlpha(0.7f);
            } else {
                holder.serviceItemBinding.activeBtnLay.setImageResource(R.drawable.active);
                holder.serviceItemBinding.serviceImageLays.setAlpha(1f);
            }

            holder.serviceItemBinding.switchBtnActive.setOnClickListener(View -> {
                ChangeActiveStatus(service.getId() + "", position);
                notifyItemChanged(position);

            });
        }
    }


    private void ChangeActiveStatus(String id, int position) {

        FunctionCall.showProgressDialog(categoryDetails);
        String token = "Bearer " + loginResponsePref.getInstance(categoryDetails).getToken();
        Call<VendorEnableDisableService> call = RetrofitClient.getVendorService().changeActiveStatus(token, id);
        call.enqueue(new Callback<VendorEnableDisableService>() {
            @Override
            public void onResponse(Call<VendorEnableDisableService> call, Response<VendorEnableDisableService> response) {
                FunctionCall.DismissDialog(categoryDetails);
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    if (receivedServicesList.get(position).getDisabled().equals("2")) {
                        receivedServicesList.get(position).setDisabled("1");
                    } else {
                        receivedServicesList.get(position).setDisabled("2");
                    }
                    notifyItemChanged(position);
                } else {
                    if (response.body() != null) {
                        Toast.makeText(categoryDetails, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Log.d(TAG, "onResponse: " + response);
                }
            }

            @Override
            public void onFailure(Call<VendorEnableDisableService> call, Throwable t) {
                FunctionCall.DismissDialog(categoryDetails);
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return receivedServicesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ServiceItemBinding serviceItemBinding;

        public ViewHolder(@NonNull ServiceItemBinding serviceItemBinding) {
            super(serviceItemBinding.getRoot());
            this.serviceItemBinding = serviceItemBinding;
        }


    }

    private void submitChangess(String mrp_price, String offer_prices, String service_ids, BottomSheetDialog bottomSheetDialog, AppCompatButton btn_applys, int position) {
        FunctionCall.showProgressDialog(categoryDetails);
        String token = "Bearer " + loginResponsePref.getInstance(categoryDetails).getToken();

        HashMap<String, String> myLstVals = new HashMap<>();
        myLstVals.put("mrp", mrp_price);
        myLstVals.put("offer_price", offer_prices);
        myLstVals.put("service_id", service_ids);
        Call<UpdateServiceResponse> call = RetrofitClient.getVendorService().updateService(token, myLstVals);
        call.enqueue(new Callback<UpdateServiceResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<UpdateServiceResponse> call, @NonNull Response<UpdateServiceResponse> response) {
                FunctionCall.DismissDialog(categoryDetails );
                if (response.isSuccessful() || response.body() != null) {
                    Toast.makeText(categoryDetails, "  " + Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.hide();
                    receivedServicesList.get(position).setMrp(Integer.parseInt(mrp_price));
                    receivedServicesList.get(position).setOfferPrice(Integer.parseInt(offer_prices));
                    notifyDataSetChanged();
                } else {
                    Log.d("updateserviceshit", "onResponse:  " + response.body());
                }
            }

            @Override
            public void onFailure(Call<UpdateServiceResponse> call, @NonNull Throwable t) {
                Log.d("updateserviceshit", "onFailure:  " + t.getMessage());
                FunctionCall.DismissDialog(categoryDetails );
            }
        });
    }


}
