package com.vendor.salon.fragment;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;

import android.annotation.SuppressLint;
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

import com.vendor.salon.adapters.AssignStaffAdapter;
import com.vendor.salon.adapters.RecentAppointmentRecyclerAdapter;
import com.vendor.salon.data_Class.appointmentsfilter.AppointmentsFilterResponse;
import com.vendor.salon.data_Class.appointmentsfilter.AppointmentsItem;
import com.vendor.salon.data_Class.getStaff.GetStaffResponse;
import com.vendor.salon.databinding.FragmentAppointmentRecentAppointmentBinding;
import com.vendor.salon.databinding.ItemRecentAppointmentsBinding;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.loginResponsePref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentRecentAppointmentFragment extends Fragment {

    private FragmentAppointmentRecentAppointmentBinding recentAppointmentBinding;
    Context homeContext;
    RecentAppointmentRecyclerAdapter recentAppointmentRecyclerAdapter = null;
    private int currentPage = 0;
    private boolean isLoading = false ;
    private boolean isLastPage = false ;

    public AppointmentRecentAppointmentFragment(Context homeContext) {
        this.homeContext = homeContext;
    }

    public AppointmentRecentAppointmentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        recentAppointmentBinding = FragmentAppointmentRecentAppointmentBinding.inflate(getLayoutInflater(), container, false);

        return recentAppointmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppointmentFragment.status = "3";
        getSearchedData();


        recentAppointmentBinding.appointmentRecentAppointmentsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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


    @SuppressLint("NotifyDataSetChanged")
    private void setAppointmentData(AppointmentsFilterResponse appointmentsFilterResponse) {

        List<AppointmentsItem> recentAppointmentsList = appointmentsFilterResponse.getAppointments();
        if (recentAppointmentsList != null && recentAppointmentsList.size() > 0) {
            recentAppointmentBinding.showNoDataText.setVisibility(View.GONE);
            recentAppointmentBinding.appointmentRecentAppointmentsRecyclerView.setVisibility(View.VISIBLE);
            recentAppointmentRecyclerAdapter = null;
            recentAppointmentRecyclerAdapter = new RecentAppointmentRecyclerAdapter(AppointmentRecentAppointmentFragment.this, recentAppointmentsList);
            recentAppointmentBinding.appointmentRecentAppointmentsRecyclerView.setAdapter(recentAppointmentRecyclerAdapter);
            recentAppointmentRecyclerAdapter.notifyDataSetChanged();
        } else {
            recentAppointmentRecyclerAdapter = new RecentAppointmentRecyclerAdapter(AppointmentRecentAppointmentFragment.this, new ArrayList<>());
            recentAppointmentBinding.appointmentRecentAppointmentsRecyclerView.setAdapter(recentAppointmentRecyclerAdapter);
            recentAppointmentBinding.appointmentRecentAppointmentsRecyclerView.setVisibility(View.GONE);
            recentAppointmentBinding.showNoDataText.setVisibility(View.VISIBLE);
        }
    }

    public void getSearchedData() {

        AppointmentFragment.status = "3" ;
        AppointmentFragment.filter = "";

        String token = "Bearer " + loginResponsePref.getInstance(AppointmentRecentAppointmentFragment.this.getContext()).getToken();
//        FunctionCall.showProgressDialog(homeContext);
        Call<AppointmentsFilterResponse> call = RetrofitClient.getVendorService().getAppointmentSearchedData(token, AppointmentFragment.status, AppointmentFragment.start_date, AppointmentFragment.end_date, AppointmentFragment.filter, AppointmentFragment.search, "", currentPage);
        call.enqueue(new Callback<AppointmentsFilterResponse>() {
            @Override
            public void onResponse(Call<AppointmentsFilterResponse> call, Response<AppointmentsFilterResponse> response) {
//                FunctionCall.DismissDialog(homeContext);
                AppointmentFragment.isApiCalled = false ;
                if (response.isSuccessful() && (response.body() != null) && response.body().isStatus()) {
                    if ( currentPage == 0) {
                        setAppointmentData(response.body());
                    }
                    else {
                        setNextPageData(response.body().getAppointments());
                    }
                } else {
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
                recentAppointmentRecyclerAdapter.addItems(appointments);
            }
    }


    private void getStaffData(ItemRecentAppointmentsBinding recentAppointmentsBinding) {
        String token = "Bearer " + loginResponsePref.getInstance(AppointmentRecentAppointmentFragment.this.getContext()).getToken();
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AppointmentRecentAppointmentFragment.this.getContext(), RecyclerView.HORIZONTAL, false);
        recentAppointmentsBinding.assignStaffRecyclerLay.setLayoutManager(linearLayoutManager);
        recentAppointmentsBinding.assignStaffRecyclerLay.setAdapter(assignStaffAdapter);
        if (staffData.getData() != null && staffData.getData().size() > 0) {
            recentAppointmentsBinding.assignStaffLays.setVisibility(View.VISIBLE);
        }
    }

}