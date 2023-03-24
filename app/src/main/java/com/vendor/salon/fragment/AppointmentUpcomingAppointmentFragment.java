package com.vendor.salon.fragment;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vendor.salon.adapters.TodayAppointmentRecyclerAdapter;
import com.vendor.salon.data_Class.appointmentsfilter.AppointmentsFilterResponse;
import com.vendor.salon.data_Class.appointmentsfilter.AppointmentsItem;
import com.vendor.salon.databinding.FragmentAppointmentUpcomingAppointmentBinding;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.loginResponsePref;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentUpcomingAppointmentFragment extends Fragment {

    private FragmentAppointmentUpcomingAppointmentBinding upcomingAppointmentBinding;
    Context homeContext;
    private int currentPage = 0;
    private boolean isLoading = false ;
    private boolean isLastPage = false ;
    private TodayAppointmentRecyclerAdapter todayAppointmentRecyclerAdapter;

    public AppointmentUpcomingAppointmentFragment(Context homeContext) {
        this.homeContext = homeContext;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public AppointmentUpcomingAppointmentFragment() {
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

        upcomingAppointmentBinding.upcomingAppointmentRecylcer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= PAGE_SIZE) {
                        isLoading = true;
                        currentPage++;

                        getSearchedData();

                    }
                }
            }
        });

    }


    private void setAppointmentData(AppointmentsFilterResponse filterResponse) {
        List<AppointmentsItem> recentAppointmentsList = filterResponse.getAppointments();
        if (recentAppointmentsList != null && recentAppointmentsList.size() > 0) {
            todayAppointmentRecyclerAdapter = null;
            upcomingAppointmentBinding.upcomingAppointmentRecylcer.setVisibility(View.VISIBLE);
            upcomingAppointmentBinding.showNoDataText.setVisibility(View.GONE);
            todayAppointmentRecyclerAdapter = null;
            todayAppointmentRecyclerAdapter = new TodayAppointmentRecyclerAdapter(this.getContext(), recentAppointmentsList);
            upcomingAppointmentBinding.upcomingAppointmentRecylcer.setAdapter(todayAppointmentRecyclerAdapter);
        } else {
            todayAppointmentRecyclerAdapter = new TodayAppointmentRecyclerAdapter(this.getContext(), recentAppointmentsList);
            upcomingAppointmentBinding.upcomingAppointmentRecylcer.setAdapter(todayAppointmentRecyclerAdapter);
            upcomingAppointmentBinding.upcomingAppointmentRecylcer.setVisibility(View.GONE);
            upcomingAppointmentBinding.showNoDataText.setVisibility(View.VISIBLE);
        }
    }


    public void getSearchedData() {

//        FunctionCall.showProgressDialog(homeContext);
        String token = "Bearer " + loginResponsePref.getInstance(AppointmentUpcomingAppointmentFragment.this.getContext()).getToken();
        if(AppointmentFragment.status.equals("3")) {
            AppointmentFragment.status = "";
        }
        AppointmentFragment.filter = "upcoming";
        Call<AppointmentsFilterResponse> call = RetrofitClient.getVendorService().getAppointmentSearchedData(token, AppointmentFragment.status, AppointmentFragment.start_date, AppointmentFragment.end_date, AppointmentFragment.filter, AppointmentFragment.search, "", currentPage);
        call.enqueue(new Callback<AppointmentsFilterResponse>() {
            @Override
            public void onResponse(Call<AppointmentsFilterResponse> call, Response<AppointmentsFilterResponse> response) {
//                FunctionCall.DismissDialog(homeContext);
                AppointmentFragment.isApiCalled = false ;
                if (response.isSuccessful() && (response.body() != null) && response.body().isStatus()) {
                    if ( currentPage  ==0 ) {
                        setAppointmentData(response.body());
                    }
                    else {
                        setNextPageData(response.body().getAppointments());
                    }
                }
                else {
                    Log.d("categoriesfilterhit", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<AppointmentsFilterResponse> call, Throwable t) {
//                FunctionCall.DismissDialog(homeContext);
                AppointmentFragment.isApiCalled = false ;
                Log.d("categoriesfilterhit", "onFailure: s" + t.getMessage());
            }
        });

    }


    private void setNextPageData(List<AppointmentsItem> appointments) {
        if (appointments.size() == 0) {
            isLastPage = true;
        } else {
            todayAppointmentRecyclerAdapter.addItems(appointments);
        }
    }


}