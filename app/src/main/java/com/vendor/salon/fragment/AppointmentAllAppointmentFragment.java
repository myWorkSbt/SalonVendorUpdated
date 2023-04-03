package com.vendor.salon.fragment;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    Context homeContext;
    private int currentPage = 0 ;
    private boolean isLoading = false ;
    private boolean isLastPage = false ;

    public AppointmentAllAppointmentFragment(Context homeContext) {
        this.homeContext = homeContext;
    }

    public AppointmentAllAppointmentFragment() {
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
        appointmentBinding.allApointmentsRecyclers.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
            allApointmentDataAdapter = new AppointmentAllApointmentDataAdapter( AppointmentAllAppointmentFragment.this.getContext() ,appointmentsList);
            appointmentBinding.allApointmentsRecyclers.setAdapter(allApointmentDataAdapter);
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
        if (homeContext == null ) {
            homeContext = AppointmentAllAppointmentFragment.this.getContext();
        }
        String token = "Bearer " + loginResponsePref.getInstance(getContext()).getToken();
        FunctionCall.showProgressDialog( homeContext);
        Call<AppointmentsFilterResponse> call = RetrofitClient.getVendorService().getAppointmentSearchedData(token, AppointmentFragment.status, AppointmentFragment.start_date, AppointmentFragment.end_date, AppointmentFragment.filter, AppointmentFragment.search, "", currentPage);
        call.enqueue(new Callback<AppointmentsFilterResponse>() {
            @Override
            public void onResponse(Call<AppointmentsFilterResponse> call, Response<AppointmentsFilterResponse> response) {
                FunctionCall.DismissDialog(homeContext);
                AppointmentFragment.isApiCalled = false ;
                isLoading = false ;
                if (response.isSuccessful() && (response.body() != null) && response.body().isStatus()) {
                    if (currentPage == 0) {
                        setAppointmentData(response.body());
                } else {
                    setNextPageData(response.body().getAppointments());
                }
            } else {
                    Log.d("appointmentsfilterhit", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<AppointmentsFilterResponse> call, Throwable t) {
                FunctionCall.DismissDialog( homeContext);
                AppointmentFragment.isApiCalled = false ;
                Log.d("appointmentsfilterhit", "onFailure: s" + t.getMessage());
                Toast.makeText(getContext(), " ---- " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setNextPageData(List<AppointmentsItem> data) {
        if (data.size() == 0) {
            isLastPage = true;
        } else {
            allApointmentDataAdapter.addItems(data);
        }
    }
}