package com.vendor.salon.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.vendor.salon.adapters.TodayAppointmentRecyclerAdapter;
import com.vendor.salon.data_Class.appointmentsfilter.AppointmentsFilterResponse;
import com.vendor.salon.data_Class.appointmentsfilter.AppointmentsItem;
import com.vendor.salon.databinding.FragmentAppointmentUpcomingAppointmentBinding;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.loginResponsePref;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentUpcomingAppointmentFragment extends Fragment {

    private FragmentAppointmentUpcomingAppointmentBinding upcomingAppointmentBinding;
    boolean isDataFiltered;

    public AppointmentUpcomingAppointmentFragment(boolean isDataFiltered) {
        this.isDataFiltered = isDataFiltered;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        upcomingAppointmentBinding = FragmentAppointmentUpcomingAppointmentBinding.inflate(getLayoutInflater(), container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        upcomingAppointmentBinding.upcomingAppointmentRecylcer.setLayoutManager(layoutManager);
        return upcomingAppointmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getSearchedData();
    }


    private void setAppointmentData(AppointmentsFilterResponse filterResponse) {
        List<AppointmentsItem> recentAppointmentsList = filterResponse.getAppointments();
        TodayAppointmentRecyclerAdapter todayAppointmentRecyclerAdapter = null;
        if (recentAppointmentsList != null && recentAppointmentsList.size() > 0) {
            upcomingAppointmentBinding.upcomingAppointmentRecylcer.setVisibility(View.VISIBLE);
            upcomingAppointmentBinding.showNoDataText.setVisibility(View.GONE);
            todayAppointmentRecyclerAdapter = null;
            todayAppointmentRecyclerAdapter = new TodayAppointmentRecyclerAdapter(this.getContext(), recentAppointmentsList);
            upcomingAppointmentBinding.upcomingAppointmentRecylcer.setAdapter(todayAppointmentRecyclerAdapter);
        } else {
            upcomingAppointmentBinding.upcomingAppointmentRecylcer.setVisibility(View.GONE);
            upcomingAppointmentBinding.showNoDataText.setVisibility(View.VISIBLE);
        }
    }


    public void getSearchedData() {

//        FunctionCall.showProgressDialog(getContext());
        String token = "Bearer " + loginResponsePref.getInstance(AppointmentUpcomingAppointmentFragment.this.getContext()).getToken();
        if(AppointmentFragment.status.equals("3")) {
            AppointmentFragment.status = "";
        }
        AppointmentFragment.filter = "upcoming";
        Call<AppointmentsFilterResponse> call = RetrofitClient.getVendorService().getAppointmentSearchedData(token, AppointmentFragment.status, AppointmentFragment.start_date, AppointmentFragment.end_date, AppointmentFragment.filter, AppointmentFragment.search, "");
        call.enqueue(new Callback<AppointmentsFilterResponse>() {
            @Override
            public void onResponse(Call<AppointmentsFilterResponse> call, Response<AppointmentsFilterResponse> response) {
//                FunctionCall.DismissDialog(getContext());
                if (response.isSuccessful() && (response.body() != null) && response.body().isStatus()) {
                    setAppointmentData(response.body());
                }
                else {
                    Log.d("categoriesfilterhit", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<AppointmentsFilterResponse> call, Throwable t) {
//                FunctionCall.DismissDialog(getContext());
                Log.d("categoriesfilterhit", "onFailure: s" + t.getMessage());
            }
        });

    }

}