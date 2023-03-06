package com.vendor.salon.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vendor.salon.adapters.AppointmentAllApointmentDataAdapter;
import com.vendor.salon.data_Class.appointmentsfilter.AppointmentsFilterResponse;
import com.vendor.salon.data_Class.appointmentsfilter.AppointmentsItem;
import com.vendor.salon.databinding.FragmentAppointmentAllAppointmentBinding;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.loginResponsePref;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentAllAppointmentFragment extends Fragment {

    FragmentAppointmentAllAppointmentBinding appointmentBinding;
    AppointmentAllApointmentDataAdapter allApointmentDataAdapter = null;
    boolean isDataFiltered;

    public AppointmentAllAppointmentFragment(boolean isDataFiltered) {
        this.isDataFiltered = isDataFiltered;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        appointmentBinding = FragmentAppointmentAllAppointmentBinding.inflate(getLayoutInflater(), container, false);
        // Inflate the layout for this fragment

        return appointmentBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getSearchedData();
    }


    void setAppointmentData(AppointmentsFilterResponse body) {
        List<AppointmentsItem> appointmentsList = body.getAppointments();
        if (appointmentsList.size() > 0) {
            appointmentBinding.showNoDataText.setVisibility(View.GONE);
//            if(appointmentBinding.allApointmentsRecyclers.getVisibility() == View.GONE) {
            appointmentBinding.allApointmentsRecyclers.setVisibility(View.VISIBLE);
//            }
            allApointmentDataAdapter = null;
            allApointmentDataAdapter = new AppointmentAllApointmentDataAdapter( AppointmentAllAppointmentFragment.this.getContext() ,appointmentsList);
//            allApointmentDataAdapter.notifyDataSetChanged();
            appointmentBinding.allApointmentsRecyclers.setAdapter(allApointmentDataAdapter);
        } else {
//            if(appointmentBinding.allApointmentsRecyclers.getVisibility() == View.VISIBLE) {
            appointmentBinding.allApointmentsRecyclers.setVisibility(View.GONE);
//            }
            appointmentBinding.showNoDataText.setVisibility(View.VISIBLE);
        }

    }

    public void getSearchedData() {


        if(AppointmentFragment.status.equals("3")) {
            AppointmentFragment.status = "";
        }
        AppointmentFragment.filter = "" ;
        String token = "Bearer " + loginResponsePref.getInstance(getContext()).getToken();
        FunctionCall.showProgressDialog( getContext());
        Call<AppointmentsFilterResponse> call = RetrofitClient.getVendorService().getAppointmentSearchedData(token, AppointmentFragment.status, AppointmentFragment.start_date, AppointmentFragment.end_date, AppointmentFragment.filter, AppointmentFragment.search, "");
        call.enqueue(new Callback<AppointmentsFilterResponse>() {
            @Override
            public void onResponse(Call<AppointmentsFilterResponse> call, Response<AppointmentsFilterResponse> response) {
                FunctionCall.DismissDialog(getContext());
                if (response.isSuccessful() && (response.body() != null) && response.body().isStatus()) {
                    setAppointmentData(response.body());
                } else {
                    Log.d("appointmentsfilterhit", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<AppointmentsFilterResponse> call, Throwable t) {
                FunctionCall.DismissDialog( getContext());
                Log.d("appointmentsfilterhit", "onFailure: s" + t.getMessage());
                Toast.makeText(getContext(), " ---- " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}