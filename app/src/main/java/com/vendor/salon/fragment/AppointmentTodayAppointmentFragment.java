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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.vendor.salon.adapters.TodayAppointmentRecyclerAdapter;
import com.vendor.salon.data_Class.appointmentsfilter.AppointmentsFilterResponse;
import com.vendor.salon.data_Class.appointmentsfilter.AppointmentsItem;
import com.vendor.salon.databinding.FragmentAppointmentTodayAppointmentBinding;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.loginResponsePref;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentTodayAppointmentFragment extends Fragment {

    private FragmentAppointmentTodayAppointmentBinding todayAppointmentBinding;
    boolean isDataFiltered;

    public AppointmentTodayAppointmentFragment(boolean isDataFiltered) {
        this.isDataFiltered = isDataFiltered;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        todayAppointmentBinding = FragmentAppointmentTodayAppointmentBinding.inflate(getLayoutInflater(), container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        todayAppointmentBinding.appointmentTodayAptnmentRecycler.setLayoutManager(layoutManager);
        return todayAppointmentBinding.getRoot();
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
            todayAppointmentBinding.showNoDataText.setVisibility(View.GONE);
            todayAppointmentBinding.appointmentTodayAptnmentRecycler.setVisibility(View.VISIBLE);
            todayAppointmentRecyclerAdapter = null ;
            todayAppointmentRecyclerAdapter = new TodayAppointmentRecyclerAdapter(this.getContext(),recentAppointmentsList);
            todayAppointmentBinding.appointmentTodayAptnmentRecycler.setAdapter(todayAppointmentRecyclerAdapter);
        } else {
            todayAppointmentBinding.appointmentTodayAptnmentRecycler.setVisibility(View.GONE);
            todayAppointmentBinding.showNoDataText.setVisibility(View.VISIBLE);
        }
    }


    public void getSearchedData() {

        String token = "Bearer " + loginResponsePref.getInstance(AppointmentTodayAppointmentFragment.this.getContext()).getToken();
        if( AppointmentFragment.status.equals("3")) {
            AppointmentFragment.status = "" ;
        }
        AppointmentFragment.filter = "today" ;
        FunctionCall.showProgressDialog(getActivity());
        Call<AppointmentsFilterResponse> call = RetrofitClient.getVendorService().getAppointmentSearchedData(token, AppointmentFragment.status, AppointmentFragment.start_date, AppointmentFragment.end_date, AppointmentFragment.filter, AppointmentFragment.search, "");
        call.enqueue(new Callback<AppointmentsFilterResponse>() {
            @Override
            public void onResponse(Call<AppointmentsFilterResponse> call, Response<AppointmentsFilterResponse> response) {
                FunctionCall.DismissDialog(getActivity());
                Log.d("categoriesfilterhit", "onResponse: " + response.body().getAppointments());
                if (response.isSuccessful() && (response.body() != null) && response.body().isStatus()) {
                    setAppointmentData(response.body());
                }
                else {
                }
            }

            @Override
            public void onFailure(Call<AppointmentsFilterResponse> call, Throwable t) {
                FunctionCall.DismissDialog(getActivity());
                Log.d("categoriesfilterhit", "onFailure: s" + t.getMessage());
            }
        });
        FunctionCall.DismissDialog(getContext());
    }

}