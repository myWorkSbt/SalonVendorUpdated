package com.vendor.salon.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vendor.salon.R;
import com.vendor.salon.activity.AppointmentDetail;
import com.vendor.salon.data_Class.appointmentsfilter.AppointmentsItem;
import com.vendor.salon.databinding.ItemAppointmentsAllBinding;

import java.util.List;

public class AppointmentAllApointmentDataAdapter extends RecyclerView.Adapter<AppointmentAllApointmentDataAdapter.ViewHolder> {

    private List<AppointmentsItem> appointmentDetail;
    Context  context ;
    String completedColors = "#2FC75C";
    public AppointmentAllApointmentDataAdapter(Context context, List<AppointmentsItem> appointmentDetails) {
        this.appointmentDetail = appointmentDetails;
        this.context = context ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAppointmentsAllBinding appointmentsAllBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_appointments_all, parent, false);
        return new ViewHolder(appointmentsAllBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppointmentsItem appointments = appointmentDetail.get(position);
        if(appointments != null ) {
            holder.itemAppointmentsAllBinding.itemName.setText(appointments.getUserName()+"");
                Glide.with(holder.itemAppointmentsAllBinding.itemAllAppointmnImage.getContext()).load(appointments.getUserImage()).into(holder.itemAppointmentsAllBinding.itemAllAppointmnImage);
            holder.itemAppointmentsAllBinding.itemGender.setText(appointments.getClientGender() +"");
        holder.itemAppointmentsAllBinding.paymentMedium.setText("online");
            if (appointments.getStatus().equals("1") ) {
                holder.itemAppointmentsAllBinding.completionStatus.setText(" Completed ");
                holder.itemAppointmentsAllBinding.competionStatusInnerLays.setBackgroundColor(Color.parseColor(completedColors));
            }
            else if(appointments.getStatus().equals( "2") ) {
                holder.itemAppointmentsAllBinding.completionStatus.setText(" Cancelled ");
                holder.itemAppointmentsAllBinding.competionStatusInnerLays.setBackgroundColor(Color.RED);
            }
            holder.itemAppointmentsAllBinding.distance.setText(appointments.getDistance()+"");
            holder.itemAppointmentsAllBinding.saloonSite.setText(appointments.getServiceSite()+"");
            String services_lists = appointments.getServicesName()+"";
            if(services_lists.length()>11 ) {
                services_lists = services_lists.substring(0, 6);
                services_lists += "..";
            }

            holder.itemAppointmentsAllBinding.services.setText(services_lists);
            holder.itemAppointmentsAllBinding.appointmentMainLays.setOnClickListener( View ->{
                Intent intents = new Intent( context , AppointmentDetail.class  );
                intents.putExtra("positions" , appointments.getId() +"" ) ;
                context.startActivity( intents);
            } );
        }
    }

    @Override
    public int getItemCount() {
        return appointmentDetail.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemAppointmentsAllBinding itemAppointmentsAllBinding;

        public ViewHolder(ItemAppointmentsAllBinding itemAppointmentsAllBinding) {
            super(itemAppointmentsAllBinding.getRoot());
            this.itemAppointmentsAllBinding = itemAppointmentsAllBinding ;
        }
    }
}
