package com.vendor.salon.adapters;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vendor.salon.R;
import com.vendor.salon.data_Class.appointmentsfilter.AppointmentsItem;
import com.vendor.salon.data_Class.assign_staff.AssignStaffResponse;
import com.vendor.salon.data_Class.getStaff.GetStaffResponse;
import com.vendor.salon.databinding.ItemRecentAppointmentsBinding;
import com.vendor.salon.fragment.AppointmentFragment;
import com.vendor.salon.fragment.AppointmentRecentAppointmentFragment;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.OnRecentBindingItemsClickListener;
import com.vendor.salon.utilityMethod.loginResponsePref;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecentAppointmentRecyclerAdapter extends RecyclerView.Adapter<RecentAppointmentRecyclerAdapter.ViewHolder> {

    private List<AppointmentsItem> recentAppointmentsList;
    AppointmentRecentAppointmentFragment recentAppointmentFragment;
    OnRecentBindingItemsClickListener onRecentBindingItemsClickListener;
    int selecteditem = -1;
    static String selected_staff_id = "";
    static String selected_Staff_name = "";


    public RecentAppointmentRecyclerAdapter(AppointmentRecentAppointmentFragment appointmentRecentAppointmentFragment, List<AppointmentsItem> recentAppointmentsList) {
        this.recentAppointmentsList = recentAppointmentsList;
        recentAppointmentFragment = appointmentRecentAppointmentFragment;
//        this.onRecentBindingItemsClickListener = onRecentBindingItemsClickListener ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemRecentAppointmentsBinding recentAppointmentsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_recent_appointments, parent, false);
        return new ViewHolder(recentAppointmentsBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        holder.setIsRecyclable(false);
        final AppointmentsItem recentAppointments = recentAppointmentsList.get(holder.getBindingAdapterPosition());
        if (recentAppointments != null) {
            if (recentAppointments.getUserName() != null) {
                if (recentAppointments.getUserName().length() < 13) {
                    String[] name_arr = recentAppointments.getUserName().split(" ");
                    String name = String.format("%s%s", name_arr[0].substring(0, 1).toUpperCase(), name_arr[0].substring(1).toLowerCase()) + "";
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
                    holder.recentAppointmentsBinding.itemName.setText(name);
                } else {
                    String new_name = recentAppointments.getUserName().substring(0, 11);
                    String[] name_arr = new_name.split(" ");
                    String name = name_arr[0].substring(0, 1).toUpperCase() + name_arr[0].substring(1).toLowerCase();
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
                    name += "..";
                    holder.recentAppointmentsBinding.itemName.setText(name);
                }
//
//                    if (name_arr[1] != null) {
//                        name += String.format("%s", name_arr[1].substring(0, 1).toUpperCase());
//                        if (name_arr[1].length() > 1) {
//                            name += name_arr[1].substring(1);
//                        }
//                    }
            } else {
                holder.recentAppointmentsBinding.itemName.setText(recentAppointments.getUserName());
            }
            if (recentAppointments.getClientGender() == null) {
                holder.recentAppointmentsBinding.itemGender.setVisibility(View.GONE);
            } else {
                String gender = null;
                gender = String.format("%s%s", recentAppointments.getClientGender().substring(0, 1).toUpperCase(), recentAppointments.getClientGender().substring(1));
                holder.recentAppointmentsBinding.itemGender.setText(gender);
            }
            Glide.with(holder.recentAppointmentsBinding.itemAllAppointmnImage.getContext()).load(recentAppointments.getUserImage()).into(holder.recentAppointmentsBinding.itemAllAppointmnImage);

            String stylist_val = null;
            if (recentAppointments.getSpecialist() != null) {
                stylist_val = String.format("%s%s", recentAppointments.getSpecialist().substring(0, 1).toUpperCase(), recentAppointments.getSpecialist().substring(1).toLowerCase());
            }
            holder.recentAppointmentsBinding.stylishVal.setText(stylist_val);
            if (recentAppointments.getDistance() == null) {
                holder.recentAppointmentsBinding.distance.setVisibility(View.GONE);
            }
            holder.recentAppointmentsBinding.distance.setText(recentAppointments.getDistance());
            holder.recentAppointmentsBinding.appointmentTime.setText(recentAppointments.getBookingTime());

            String today_date = "YYYY-MM-dd";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                today_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            }
            String booking_dates = recentAppointments.getAppointmentDate();
            if (today_date.equals(booking_dates)) {
                holder.recentAppointmentsBinding.appointmentDay.setText("Today");
            }
            else {
                holder.recentAppointmentsBinding.appointmentDay.setText(booking_dates);
            }


        }


        holder.recentAppointmentsBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_Staff_name = "";
                selected_staff_id = "";
                callAssignStaffApis("2", recentAppointments.getId() + "");
                AppointmentFragment.status = "3";
                AppointmentFragment.filter = "";
                recentAppointmentFragment.getSearchedData();
            }
        });

        holder.recentAppointmentsBinding.btnAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAssignStaffApis("4", recentAppointments.getId() + "");
                AppointmentFragment.filter = "";
                AppointmentFragment.status = "3";
                recentAppointmentFragment.getSearchedData();
            }
        });

        holder.recentAppointmentsBinding.btnRecentAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.recentAppointmentsBinding.stylishVal.getText().toString().toLowerCase().equals("anyone")) {
                    Toast.makeText(recentAppointmentFragment.getContext(), " Please assign staff First . ", Toast.LENGTH_SHORT).show();

                    selecteditem = holder.getBindingAdapterPosition();
                    holder.recentAppointmentsBinding.assignStaffLays.setVisibility(View.VISIBLE);
                    holder.recentAppointmentsBinding.btnRecentAccept.setVisibility(View.GONE);
                    holder.recentAppointmentsBinding.btnRecentReject.setVisibility(View.GONE);
                    getStaffData(holder.recentAppointmentsBinding);

                } else {
                    callAssignStaffApis("4", recentAppointments.getId() + "");
                    AppointmentFragment.status = "3";
                    AppointmentFragment.filter = "";
                    recentAppointmentFragment.getSearchedData();
                }
            }
        });
        holder.recentAppointmentsBinding.btnRecentReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_staff_id = "";
                selected_Staff_name = "";
                callAssignStaffApis("2", recentAppointments.getId() + "");
                AppointmentFragment.status = "3";
                AppointmentFragment.filter = "";
                recentAppointmentFragment.getSearchedData();
            }
        });
    }

    private void callAssignStaffApis(String status, String appointment_id) {
        FunctionCall.showProgressDialog(recentAppointmentFragment.getContext());
        String token = "Bearer " + loginResponsePref.getInstance(recentAppointmentFragment.getContext()).getToken();
        Call<AssignStaffResponse> call = RetrofitClient.getVendorService().assignStaffIns(token, appointment_id, selected_Staff_name, status, selected_staff_id);
        call.enqueue(new Callback<AssignStaffResponse>() {
            @Override
            public void onResponse(Call<AssignStaffResponse> call, Response<AssignStaffResponse> response) {
                FunctionCall.DismissDialog(recentAppointmentFragment.getContext());
                if (response.isSuccessful() && response.body() != null && response.body().isStatus() && response.body().getAppointment() != null) {

                } else {
                    if (response.body() != null) {
                        Toast.makeText(recentAppointmentFragment.getContext(), "" + response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                    Log.d("assignstaffhit", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<AssignStaffResponse> call, Throwable t) {
                FunctionCall.DismissDialog(recentAppointmentFragment.getContext());
                Log.d("assignstaffhit", "onFailure: " + t.getMessage());
            }
        });
    }

    private void getStaffData(ItemRecentAppointmentsBinding recentAppointmentsBinding) {
        String token = "Bearer " + loginResponsePref.getInstance(recentAppointmentFragment.getContext()).getToken();
        Call<GetStaffResponse> call = RetrofitClient.getVendorService().getAllStaffList(token, "");
        call.enqueue(new Callback<GetStaffResponse>() {
            @Override
            public void onResponse(Call<GetStaffResponse> call, Response<GetStaffResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    setStaffData(response.body(), recentAppointmentsBinding);
                }
            }

            @Override
            public void onFailure(Call<GetStaffResponse> call, Throwable t) {
                Log.d("getstaffhit", "onFailure: " + t.getMessage());
            }
        });
    }

    private void setStaffData(GetStaffResponse staffData, ItemRecentAppointmentsBinding recentAppointmentsBinding) {
        final AssignStaffAdapter assignStaffAdapter = new AssignStaffAdapter(staffData.getData());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recentAppointmentFragment.getContext(), RecyclerView.HORIZONTAL, false);
        recentAppointmentsBinding.assignStaffRecyclerLay.setLayoutManager(linearLayoutManager);
        recentAppointmentsBinding.assignStaffRecyclerLay.setAdapter(assignStaffAdapter);
        if (staffData.getData() != null && staffData.getData().size() > 0) {
            recentAppointmentsBinding.assignStaffLays.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return recentAppointmentsList.size();
    }

    public void addItems(List<AppointmentsItem> appointments) {
        recentAppointmentsList.addAll(appointments);
        notifyDataSetChanged();
    }

    public void refreshListss(List<AppointmentsItem> recentAppointmentsList) {
        if (recentAppointmentsList != null && recentAppointmentsList.size() > 0) {
            this.recentAppointmentsList = recentAppointmentsList;
        } else {
            this.recentAppointmentsList = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemRecentAppointmentsBinding recentAppointmentsBinding;

        public ViewHolder(ItemRecentAppointmentsBinding recentAppointmentsBinding) {
            super(recentAppointmentsBinding.getRoot());
            this.recentAppointmentsBinding = recentAppointmentsBinding;
//            onRecentBindingItemsClickListener.OnItemClick(recentAppointmentsBinding1, getBindingAdapterPosition() );
        }
    }
}
