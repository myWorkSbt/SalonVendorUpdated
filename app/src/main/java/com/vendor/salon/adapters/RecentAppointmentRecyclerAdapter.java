package com.vendor.salon.adapters;

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

import java.util.List;

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
            holder.recentAppointmentsBinding.itemName.setText(recentAppointments.getUserName());
            holder.recentAppointmentsBinding.itemGender.setText(recentAppointments.getClientGender());
            Glide.with(holder.recentAppointmentsBinding.itemAllAppointmnImage.getContext()).load(recentAppointments.getUserImage()).into(holder.recentAppointmentsBinding.itemAllAppointmnImage);

            holder.recentAppointmentsBinding.stylishVal.setText(recentAppointments.getSpecialist() + "");
            holder.recentAppointmentsBinding.distance.setText(recentAppointments.getDistance());
            if (recentAppointments.getStartTime() != null ) {
                holder.recentAppointmentsBinding.appointmentDay.setText(recentAppointments.getStartTime().split(" ")[0]);
                holder.recentAppointmentsBinding.appointmentTime.setText(recentAppointments.getStartTime().split(" ")[1]);
                holder.recentAppointmentsBinding.appointmentTime.setText(recentAppointments.getStartTime().split(" ")[1]);
            }


        }


    holder.recentAppointmentsBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_Staff_name = "";
                selected_staff_id = "" ;
                callAssignStaffApis("2", recentAppointments.getId() + "");
                AppointmentFragment.status = "3" ;
                AppointmentFragment.filter = "" ;
                recentAppointmentFragment.getSearchedData( );
            }
        });

        holder.recentAppointmentsBinding.btnAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAssignStaffApis("4", recentAppointments.getId() + "");
                AppointmentFragment.filter = "";
                AppointmentFragment.status = "3" ;
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

                }
                else {
                    callAssignStaffApis("4", recentAppointments.getId() + "");
                    AppointmentFragment.status = "3";
                    AppointmentFragment.filter = "" ;
                    recentAppointmentFragment.getSearchedData();
                }
            }
        });
        holder.recentAppointmentsBinding.btnRecentReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_staff_id = "";
                selected_Staff_name = "" ;
                callAssignStaffApis("2", recentAppointments.getId() + "");
                AppointmentFragment.status = "3";
                AppointmentFragment.filter = "" ;
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
        Call<GetStaffResponse> call = RetrofitClient.getVendorService().getAllStaffList(token , "" );
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemRecentAppointmentsBinding recentAppointmentsBinding;

        public ViewHolder(ItemRecentAppointmentsBinding recentAppointmentsBinding) {
            super(recentAppointmentsBinding.getRoot());
            this.recentAppointmentsBinding =  recentAppointmentsBinding ;
//            onRecentBindingItemsClickListener.OnItemClick(recentAppointmentsBinding1, getBindingAdapterPosition() );
        }
    }
}
