package com.vendor.salon.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vendor.salon.R;
import com.vendor.salon.activity.AppointmentDetail;
import com.vendor.salon.activity.Home;
import com.vendor.salon.data_Class.appointmentsfilter.AppointmentsItem;
import com.vendor.salon.databinding.ItemAppointmentsAllBinding;

import java.util.List;
import java.util.Locale;

public class AppointmentAllApointmentDataAdapter extends RecyclerView.Adapter<AppointmentAllApointmentDataAdapter.ViewHolder> {

    private List<AppointmentsItem> appointmentDetail;
    Context context;
    String completedColors = "#2FC75C";

    public AppointmentAllApointmentDataAdapter(Context context, List<AppointmentsItem> appointmentDetails) {
        this.appointmentDetail = appointmentDetails;
        this.context = context;
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
        if (appointments != null) {
            String name = null;
            if (appointments.getUserName() != null) {
                if (appointments.getUserName().length() < 15) {
                    String[] name_arr = appointments.getUserName().split(" ");
                    name = String.format("%s%s", name_arr[0].substring(0, 1).toUpperCase(), name_arr[0].substring(1).toLowerCase()) + "";
                    if (name_arr.length > 1) {
                        for (int i = 1; i < name_arr.length; i++) {
                            if (name_arr[i] != null) {
                                name += String.format(" %s", name_arr[i].substring(0, 1).toUpperCase());
                                if (name_arr[i].length() > 1) {
                                    name += name_arr[i].substring(1).toLowerCase();
                                }
                            }
                        }
                    }
                } else {
                    String new_name = appointments.getUserName().substring(0, 15);
                    String[] name_arr = new_name.split(" ");
                    name = name_arr[0].substring(0, 1).toUpperCase() + name_arr[0].substring(1).toLowerCase();
                    if (name_arr.length > 1) {
                        for (int i = 1; i < name_arr.length; i++) {
                            if (name_arr[i] != null) {
                                name += String.format(" %s", name_arr[i].substring(0, 1).toUpperCase());
                                if (name_arr[i].length() > 1) {
                                    name += name_arr[i].substring(1).toLowerCase();
                                }
                            }
                        }
                    }
//                    if (name_arr[1] != null) {
//                        name += String.format("%s", name_arr[1].substring(0, 1).toUpperCase());
//                        if (name_arr[1].length() > 1) {
//                            name += name_arr[1].substring(1);
//                        }
//                    }
                    name += "..";
                }
            }
//            holder.itemAppointmentsAllBinding.itemName.setText(String.format("%s%s..", appointments.getUserName().substring(0, 1).toUpperCase(), appointments.getUserName().substring(1)));
            holder.itemAppointmentsAllBinding.itemName.setText(name);
            Glide.with(holder.itemAppointmentsAllBinding.itemAllAppointmnImage.getContext()).load(appointments.getUserImage()).into(holder.itemAppointmentsAllBinding.itemAllAppointmnImage);
            if (appointments.getClientGender() == null) {
                holder.itemAppointmentsAllBinding.itemGender.setVisibility(View.GONE);
            }
            String gender = null;
            if (appointments.getClientGender() != null) {
                gender = String.format("%s%s", appointments.getClientGender().substring(0, 1).toUpperCase(), appointments.getClientGender().substring(1));
            }
            holder.itemAppointmentsAllBinding.itemGender.setText(gender);
            holder.itemAppointmentsAllBinding.paymentMedium.setText(R.string.online);
            if (appointments.getStatus().equals("1")) {
                holder.itemAppointmentsAllBinding.completionStatus.setText(R.string.completed);
                holder.itemAppointmentsAllBinding.competionStatusInnerLays.setBackgroundColor(Color.parseColor(completedColors));
            } else if (appointments.getStatus().equals("2")) {
                holder.itemAppointmentsAllBinding.completionStatus.setText(R.string.cancelled);
                holder.itemAppointmentsAllBinding.competionStatusInnerLays.setBackgroundColor(Color.RED);
            }
            if (appointments.getDistance() == null) {
                holder.itemAppointmentsAllBinding.distance.setVisibility(View.GONE);
            }
            holder.itemAppointmentsAllBinding.distance.setText(appointments.getDistance());
            String service_site = null;
            if (appointments.getServiceSite() != null) {
                service_site = String.format("%s%s", appointments.getServiceSite().substring(0, 1).toUpperCase(), appointments.getServiceSite().substring(1));
            }
            holder.itemAppointmentsAllBinding.saloonSite.setText(service_site);
            String services_lists = appointments.getServicesName() + "";
            if (services_lists.length() > 11 && appointments.getServicesName() != null) {
                services_lists = String.format("%s%s", services_lists.substring(0, 1).toUpperCase(), services_lists.substring(1, 6));
                services_lists += "..";
            }
            if (appointments.getBookingTime()!= null ) {
                holder.itemAppointmentsAllBinding.appointmentTime.setText(appointments.getBookingTime());
            }
            else {
                holder.itemAppointmentsAllBinding.appointmentTime.setText("st - et ");
            }
            holder.itemAppointmentsAllBinding.services.setText(services_lists);
            holder.itemAppointmentsAllBinding.appointmentMainLays.setOnClickListener(View -> {
                Intent intents = new Intent(context, AppointmentDetail.class);
                intents.putExtra("positions", appointments.getId() + "");
                context.startActivity(intents);
            });
        }
    }

    @Override
    public int getItemCount() {
        return appointmentDetail.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addItems(List<AppointmentsItem> data) {
        appointmentDetail.addAll(data);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemAppointmentsAllBinding itemAppointmentsAllBinding;

        public ViewHolder(ItemAppointmentsAllBinding itemAppointmentsAllBinding) {
            super(itemAppointmentsAllBinding.getRoot());
            this.itemAppointmentsAllBinding = itemAppointmentsAllBinding;
        }
    }
}
