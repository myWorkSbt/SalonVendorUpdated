package com.vendor.salon.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vendor.salon.R;
import com.vendor.salon.activity.AppointmentDetail;
import com.vendor.salon.data_Class.appointmentsfilter.AppointmentsItem;
import com.vendor.salon.databinding.ItemTodayAppointmentsBinding;

import java.util.List;

public class TodayAppointmentRecyclerAdapter extends RecyclerView.Adapter<TodayAppointmentRecyclerAdapter.ViewHolder> {

    Context contexts ;
    private List<AppointmentsItem> recentAppointmentsList;
    public TodayAppointmentRecyclerAdapter(Context context, List<AppointmentsItem> recentAppointmentsList) {
        this.recentAppointmentsList = recentAppointmentsList;
        this.contexts = context ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         ItemTodayAppointmentsBinding todayAppointmentsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_today_appointments, parent, false);

         return new ViewHolder(todayAppointmentsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppointmentsItem appointmentsItemLists = recentAppointmentsList.get(position);
        if(appointmentsItemLists != null ) {
            holder.todayAppointmentsBinding.itemName.setText(appointmentsItemLists.getUserName());
            holder.todayAppointmentsBinding.itemGender.setText(appointmentsItemLists.getClientGender());
            Glide.with(holder.todayAppointmentsBinding.itemAllAppointmnImage.getContext()).load(appointmentsItemLists.getUserImage()).into(holder.todayAppointmentsBinding.itemAllAppointmnImage);


            holder.todayAppointmentsBinding.saloonSite.setText(appointmentsItemLists.getServiceSite());
            holder.todayAppointmentsBinding.distance.setText(appointmentsItemLists.getDistance());

            String services_name = appointmentsItemLists.getServicesName() + "";
            if(services_name.length()>8 ) {
                services_name = services_name.substring(0, 3);
                services_name += "..";
            }

            holder.todayAppointmentsBinding.services.setText(services_name);
        }
        holder.todayAppointmentsBinding.appointmentItemMainsLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intents = new Intent( contexts , AppointmentDetail.class );
                intents.putExtra( "positions" ,appointmentsItemLists.getId() +"");
                contexts.startActivity(intents);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recentAppointmentsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTodayAppointmentsBinding todayAppointmentsBinding;

        public ViewHolder(ItemTodayAppointmentsBinding todayAppointmentsBinding) {
            super(todayAppointmentsBinding.getRoot());
            this.todayAppointmentsBinding = todayAppointmentsBinding ;
        }
    }
}
