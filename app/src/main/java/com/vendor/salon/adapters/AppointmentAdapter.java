package com.vendor.salon.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vendor.salon.R;
import com.vendor.salon.data_Class.home.RecentAppointmentItem;
import com.vendor.salon.databinding.AppointmentRecyclerviewItemBinding;
import com.vendor.salon.utilityMethod.OnClickListener;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

    private Context context;
    private List<RecentAppointmentItem> appointmentList;
    public OnClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AppointmentRecyclerviewItemBinding appointmentRecyclerviewItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.appointment_recyclerview_item, parent, false);
        return new ViewHolder(appointmentRecyclerviewItemBinding);
    }

    public AppointmentAdapter(Context context, List<RecentAppointmentItem> appointmentList, OnClickListener onClickListener) {
        this.context = context;
        this.appointmentList = appointmentList;
        this.listener = onClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecentAppointmentItem apiList = appointmentList.get(position);
        if (apiList != null) {
            Glide.with(holder.appointmentRecyclerviewItemBinding.customerProfile.getContext()).load(Uri.parse(apiList.getUserImage())).into(holder.appointmentRecyclerviewItemBinding.customerProfile);
//        Glide.with(context).load(Uri.parse())
            holder.appointmentRecyclerviewItemBinding.name.setText(apiList.getUserName());
            holder.appointmentRecyclerviewItemBinding.atHome.setText(apiList.getServiceSite());
            holder.appointmentRecyclerviewItemBinding.online.setText("online");
            String servicesLists = apiList.getServicesName() + "";
            if (servicesLists.length() > 10) {
                servicesLists = servicesLists.substring(0, 6);
                servicesLists += "..";
            }

            holder.appointmentRecyclerviewItemBinding.HaircutHairSpa1.setText(servicesLists);

            holder.appointmentRecyclerviewItemBinding.Male.setText(apiList.getClientGender());
            holder.appointmentRecyclerviewItemBinding.distance.setText(apiList.getDistance());
            if (apiList.getStartTime() != null && !apiList.getStartTime().isEmpty()) {
                holder.appointmentRecyclerviewItemBinding.today.setText(apiList.getStartTime().toString().split(" ")[0]);
            }
            int item_id = Integer.parseInt(apiList.getUserId());

            holder.appointmentRecyclerviewItemBinding.itemsCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClickListener(view.getContext(), item_id );
                }
            });
            if (apiList.getStartTime() != null && !apiList.getStartTime().isEmpty()) {
                holder.appointmentRecyclerviewItemBinding.time.setText(apiList.getStartTime().split(" ")[apiList.getStartTime().split(" ").length - 1]);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (appointmentList.size() < 4) {
            return appointmentList.size();
        }
        return 4;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AppointmentRecyclerviewItemBinding appointmentRecyclerviewItemBinding;

        public ViewHolder(AppointmentRecyclerviewItemBinding appointmentRecyclerviewItemBinding) {
            super(appointmentRecyclerviewItemBinding.getRoot());
            this.appointmentRecyclerviewItemBinding = appointmentRecyclerviewItemBinding;
        }

    }
}
