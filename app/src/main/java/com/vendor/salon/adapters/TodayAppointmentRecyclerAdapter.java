package com.vendor.salon.adapters;

import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
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

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TodayAppointmentRecyclerAdapter extends RecyclerView.Adapter<TodayAppointmentRecyclerAdapter.ViewHolder> {

    Context contexts;
    private List<AppointmentsItem> recentAppointmentsList;

    public TodayAppointmentRecyclerAdapter(Context context, List<AppointmentsItem> recentAppointmentsList) {
        this.recentAppointmentsList = recentAppointmentsList;
        this.contexts = context;
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
        if (appointmentsItemLists != null) {
            String name = null;

            if (appointmentsItemLists.getUserName() != null) {
                if (appointmentsItemLists.getUserName().length() < 15) {
                    String[] name_arr = appointmentsItemLists.getUserName().split(" ");
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
                    String new_name = appointmentsItemLists.getUserName().substring(0, 15);
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
//
//                    if (name_arr[1] != null) {
//                        name += String.format("%s", name_arr[1].substring(0, 1).toUpperCase());
//                        if (name_arr[1].length() > 1) {
//                            name += name_arr[1].substring(1);
//                        }
//                    }
                    name += "..";
                }
            }
            holder.todayAppointmentsBinding.itemName.setText(name);

//            if (appointmentsItemLists.getUserName().length()<15) {
//                holder.todayAppointmentsBinding.itemName.setText(String.format("%s%s", appointmentsItemLists.getUserName().substring(0, 1).toUpperCase(), appointmentsItemLists.getUserName().substring(1)));
//            }
//            else {
//                holder.todayAppointmentsBinding.itemName.setText(String.format("%s%s..", appointmentsItemLists.getUserName().substring(0, 1).toUpperCase(), appointmentsItemLists.getUserName().substring(1, 13)));
//            }
            String genders = null;
            if (appointmentsItemLists.getClientGender() == null) {
                holder.todayAppointmentsBinding.itemGender.setVisibility(View.GONE);
            } else {
                genders = String.format("%s%s", appointmentsItemLists.getClientGender().substring(0, 1).toUpperCase(), appointmentsItemLists.getClientGender().substring(1));
            }
            holder.todayAppointmentsBinding.itemGender.setText(genders);
            Glide.with(holder.todayAppointmentsBinding.itemAllAppointmnImage.getContext()).load(appointmentsItemLists.getUserImage()).into(holder.todayAppointmentsBinding.itemAllAppointmnImage);


            holder.todayAppointmentsBinding.saloonSite.setText(appointmentsItemLists.getServiceSite());
            if (appointmentsItemLists.getDistance() == null) {
                holder.todayAppointmentsBinding.distance.setVisibility(View.GONE);
            }
            holder.todayAppointmentsBinding.distance.setText(appointmentsItemLists.getDistance());

            String services_name = appointmentsItemLists.getServicesName() + "";
            if (services_name.length() > 8 && appointmentsItemLists.getServicesName() != null) {
                services_name = String.format("%s%s", services_name.substring(0, 1).toUpperCase(), services_name.substring(1, 3));
                services_name += "..";
            }

            holder.todayAppointmentsBinding.services.setText(services_name);
            String today_date = "YYYY-MM-dd";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                today_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            }
            String booking_dates = appointmentsItemLists.getAppointmentDate();
            if (today_date.equals(booking_dates)) {
                holder.todayAppointmentsBinding.appointmentDay.setText("Today");
            }
            else {
                holder.todayAppointmentsBinding.appointmentDay.setText(booking_dates);
            }
            holder.todayAppointmentsBinding.appointmentTime.setText(appointmentsItemLists.getBookingTime());
        }
        holder.todayAppointmentsBinding.appointmentItemMainsLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intents = new Intent(contexts, AppointmentDetail.class);
                intents.putExtra("positions", appointmentsItemLists.getId() + "");
                contexts.startActivity(intents);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recentAppointmentsList.size();
    }

    public void addItems(List<AppointmentsItem> appointments) {
        recentAppointmentsList.addAll(appointments);
        notifyDataSetChanged();
    }

    public void refreshList(List<AppointmentsItem> appointmentsItems) {
        this.recentAppointmentsList = appointmentsItems;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTodayAppointmentsBinding todayAppointmentsBinding;

        public ViewHolder(ItemTodayAppointmentsBinding todayAppointmentsBinding) {
            super(todayAppointmentsBinding.getRoot());
            this.todayAppointmentsBinding = todayAppointmentsBinding;
        }
    }

}
