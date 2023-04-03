package com.vendor.salon.adapters;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
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

import java.util.Date;
import java.util.List;
import java.util.Locale;

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
            String name = null;
            if (apiList.getUserName() != null) {
                if (apiList.getUserName().length() < 13) {
                    String[] name_arr = apiList.getUserName().split(" ");
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
                    String new_name = apiList.getUserName().substring(0, 11);
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
            holder.appointmentRecyclerviewItemBinding.name.setText(name);
            String service_site = null;
            if (apiList.getServiceSite() != null) {
                service_site = String.format("%s%s", apiList.getServiceSite().substring(0, 1).toUpperCase(), apiList.getServiceSite().substring(1).toLowerCase());
            }
            holder.appointmentRecyclerviewItemBinding.atHome.setText(service_site);
            holder.appointmentRecyclerviewItemBinding.online.setText(R.string.online);
            String servicesLists = apiList.getServicesName() + "";
            if (servicesLists.length() > 10 && apiList.getServicesName() != null) {
                servicesLists = servicesLists.substring(0, 1).toUpperCase() + servicesLists.substring(1, 6).toLowerCase();
                servicesLists += "..";
            }

            holder.appointmentRecyclerviewItemBinding.HaircutHairSpa1.setText(servicesLists);
            if (apiList.getClientGender() == null) {
                holder.appointmentRecyclerviewItemBinding.Male.setVisibility(View.GONE);
            } else {
                holder.appointmentRecyclerviewItemBinding.Male.setText(String.format("%s%s", apiList.getClientGender().substring(0, 1).toUpperCase(), apiList.getClientGender().substring(1).toLowerCase()));

            }
            if (apiList.getDistance() == null) {
                holder.appointmentRecyclerviewItemBinding.distance.setVisibility(View.GONE);
            } else {
                holder.appointmentRecyclerviewItemBinding.distance.setText(apiList.getDistance());
            }

            String today_date = "YYYY-MM-dd";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                today_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            }
            String booking_dates = apiList.getAppointmentDate();
            if (today_date.equals(booking_dates)) {
                holder.appointmentRecyclerviewItemBinding.today.setText("Today");
            }
            else {
                holder.appointmentRecyclerviewItemBinding.today.setText(booking_dates);
            }
            int item_id = apiList.getId();

            holder.appointmentRecyclerviewItemBinding.itemsCard.setOnClickListener(view -> listener.onItemClickListener(view.getContext(), item_id));
            holder.appointmentRecyclerviewItemBinding.time.setText(apiList.getBookingTime());
        }
    }

    @Override
    public int getItemCount() {
        if (appointmentList.size() < 4) {
            return appointmentList.size();
        }
        return 4;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final AppointmentRecyclerviewItemBinding appointmentRecyclerviewItemBinding;

        public ViewHolder(AppointmentRecyclerviewItemBinding appointmentRecyclerviewItemBinding) {
            super(appointmentRecyclerviewItemBinding.getRoot());
            this.appointmentRecyclerviewItemBinding = appointmentRecyclerviewItemBinding;
        }

    }
}
