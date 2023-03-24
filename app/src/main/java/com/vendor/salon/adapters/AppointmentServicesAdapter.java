package com.vendor.salon.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.vendor.salon.R;
import com.vendor.salon.activity.AddAppointmentServices;
import com.vendor.salon.data_Class.appointment_sub_categories.DataItem;
import com.vendor.salon.databinding.ItemAppointmentServicesLaysBinding;
import com.vendor.salon.utilityMethod.OnAppointmentServiceUpdateButtonClick;
import com.vendor.salon.utilityMethod.OnClickListener;

import java.util.List;

public class AppointmentServicesAdapter extends RecyclerView.Adapter<AppointmentServicesAdapter.ViewHolder> {

    Context context;
    OnAppointmentServiceUpdateButtonClick onAppointmentServiceUpdateButtonClick;
    List<DataItem> appointmentServiceList;

    public AppointmentServicesAdapter(Context context, List<DataItem> servceList, OnAppointmentServiceUpdateButtonClick onAppointmentServiceUpdateButtonClick) {
        this.appointmentServiceList = servceList;
        this.onAppointmentServiceUpdateButtonClick = onAppointmentServiceUpdateButtonClick;
        this.context = context;
    }

    @NonNull
    @Override
    public AppointmentServicesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemAppointmentServicesLaysBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentServicesAdapter.ViewHolder holder, int position) {
        DataItem appointments = appointmentServiceList.get(position);
        if (appointments != null) {
            holder.appointmentServicesLaysBinding.tvSubcategoryName.setText(appointments.getServiceName());
            holder.appointmentServicesLaysBinding.tvPrice.setText(String.valueOf(appointments.getOfferPrice()));
            if (appointments.getQty() != null && !appointments.getQty().contains("-")) {
                holder.appointmentServicesLaysBinding.tvQuantity.setText(String.valueOf(appointments.getQty()));
            } else {
                holder.appointmentServicesLaysBinding.tvQuantity.setText("0");
            }
        }
        holder.appointmentServicesLaysBinding.buttonPlus.setOnClickListener(View -> {
            int service_count = Integer.parseInt(holder.appointmentServicesLaysBinding.tvQuantity.getText().toString());
            onAppointmentServiceUpdateButtonClick.OnPlusButtonClick(holder.appointmentServicesLaysBinding.tvQuantity.getContext(), holder.appointmentServicesLaysBinding.buttonMinus.getContext(), holder.appointmentServicesLaysBinding.container.getContext(), service_count, position);
            if (AddAppointmentServices.isChangedSuccessfully) {
                 appointmentServiceList.get(position).setQty(String.valueOf(service_count+1));
                if (service_count == 0) {
                    appointmentServiceList.get(position).setSeleted(true);
                     holder.appointmentServicesLaysBinding.container.setAlpha(1f);
                }
                else {
                    appointmentServiceList.get(position).setSeleted(false);
                }
                notifyItemChanged(position);
            }
            notifyItemChanged(position);
        });
        holder.appointmentServicesLaysBinding.buttonMinus.setOnClickListener(View -> {
            int service_count = Integer.parseInt(holder.appointmentServicesLaysBinding.tvQuantity.getText().toString());
        if (service_count > 0 ) {
            onAppointmentServiceUpdateButtonClick.OnMinusButtonClick(holder.appointmentServicesLaysBinding.tvQuantity.getContext(), holder.appointmentServicesLaysBinding.buttonMinus.getContext(), holder.appointmentServicesLaysBinding.container.getContext(), service_count, position);
            if (AddAppointmentServices.isChangedSuccessfully) {
                holder.appointmentServicesLaysBinding.tvQuantity.setText(String.valueOf(service_count -1));
                if ( service_count == 1) {
                    appointmentServiceList.get(position).setSeleted(false);
                    holder.appointmentServicesLaysBinding.container.setAlpha(0.7f);
                }
                else {
                    appointmentServiceList.get(position).setSeleted(true);
                }
            }
        }
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return appointmentServiceList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshLists(List<DataItem> dataLists) {
        this.appointmentServiceList = dataLists;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemAppointmentServicesLaysBinding appointmentServicesLaysBinding;

        public ViewHolder(@NonNull ItemAppointmentServicesLaysBinding appointmentServicesLaysBinding) {
            super(appointmentServicesLaysBinding.getRoot());
            this.appointmentServicesLaysBinding = appointmentServicesLaysBinding;
        }
    }


}
