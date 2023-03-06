package com.vendor.salon.fragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vendor.salon.R;
import com.vendor.salon.adapters.SalesDataAdapter;
import com.vendor.salon.data_Class.sales.Data;
import com.vendor.salon.data_Class.sales.SalesResponse;
import com.vendor.salon.databinding.ActivityHomeBinding;
import com.vendor.salon.databinding.FragmentSaleBinding;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.loginResponsePref;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaleFragment extends Fragment {

    CardView btn_to_date;

    AppCompatButton resetFilter = null;
    AppCompatTextView end_date_lays;
    AppCompatTextView start_date_lays;
    SalesDataAdapter dataAdapter;
    BottomSheetDialog salesFiltersSheet;
    FragmentManager supportFragmentManager;
    FragmentSaleBinding binding;
    ActivityHomeBinding homeBinding;
    private String start_date = "";
    private String end_date = "";

    public SaleFragment(ActivityHomeBinding homeBinding, FragmentManager supportFragmentManager) {
        this.homeBinding = homeBinding;
        this.supportFragmentManager = supportFragmentManager;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSaleBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        salesFiltersSheet = new BottomSheetDialog(binding.getRoot().getContext() , R.style.BottomSheetDialogStyle );
        salesFiltersSheet.setContentView(R.layout.sales_bottom_sheet);

        resetFilter = salesFiltersSheet.findViewById(R.id.btn_reset_sales);
        start_date_lays = salesFiltersSheet.findViewById(R.id.tv_from_date);
        end_date_lays = salesFiltersSheet.findViewById(R.id.tv_to_date);
        btn_to_date = salesFiltersSheet.findViewById(R.id.to_date_lay);

//            view.findViewById<CardView>(R.id.from_lays)!!.setOnClickListener { View ->
//                run {
//                    setCalenderDates(start_date_lays)
//                }
//            };
        salesFiltersSheet.findViewById(R.id.from_date_lay).setOnClickListener(View -> {
            setCalenderDates(start_date_lays);
        });


        btn_to_date.setOnClickListener(View -> {
            this.setCalenderDates(end_date_lays);
        });


        salesFiltersSheet.findViewById(R.id.btn_apply_filter_sales)
                .setOnClickListener(View -> {
                    if (start_date_lays.getText().toString().equals("DD/MM/YYYY")) {
                        start_date_lays.setTextColor(Color.RED);
                        Toast.makeText(getActivity() , " Start date is Mandatory! ", Toast.LENGTH_SHORT).show();
                    } else {
                        start_date_lays.setTextColor(Color.BLACK);
                        if (end_date_lays.getText().toString().equals("DD/MM/YYYY")) {
                            if (start_date_lays.getText().toString().equals("DD/MM/YYYY")) {
                                start_date_lays.setTextColor(Color.RED);
                                Toast.makeText(getActivity(), " Start date is Mandatory! ", Toast.LENGTH_SHORT).show();
                            } else {
                                end_date_lays.setTextColor(Color.RED);
                                Toast.makeText(getActivity(), " End date is Mandatory! ", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else {
                            start_date = start_date_lays.getText().toString() + "";
                            end_date = end_date_lays.getText().toString();
                            getSaleData();
                            salesFiltersSheet.hide();
                    }
                    }
                });
        binding.btnFilters.setOnClickListener(View -> {
            salesFiltersSheet.show();
        });

        resetFilter.setOnClickListener(View -> {
            start_date_lays.setText(R.string.ddmmyyyy);
            end_date_lays.setText(R.string.ddmmyyyy);
        });

        binding.btnBack.setOnClickListener(View -> {
            homeBinding.bottomNegivation.getMenu().getItem(0).setChecked(true);
            this.supportFragmentManager.beginTransaction().replace(R.id.frag_containers, new HomeFragment(homeBinding )).commit();
        });

        dataAdapter = new SalesDataAdapter(SaleFragment.this.getContext(), new ArrayList<Data>());
        binding.salesListsRecycler.setAdapter(dataAdapter);
        getSaleData();

    }

    private void setCalenderDates(AppCompatTextView sets_date_lays) {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog picker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                sets_date_lays.setText(day + "/" + (month + 1) + "/" + year);
            }
        }, year, month, day);
        picker.show();
        picker.getDatePicker().setMaxDate(cal.getTimeInMillis());


    }

    private void getSaleData() {
        FunctionCall.showProgressDialog(getActivity());
        String token = loginResponsePref.getInstance(requireContext()).getToken();

        Call<SalesResponse> call = RetrofitClient.getVendorService().salesData(
                "Bearer " + token, start_date, end_date
        );
        call.enqueue(new Callback<SalesResponse>() {
            @Override
            public void onResponse(Call<SalesResponse> call, Response<SalesResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    FunctionCall.DismissDialog(getActivity());
                    dataAdapter.refreshsaleList(response.body().getData());
                    dataAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "" + Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SalesResponse> call, Throwable t) {
                FunctionCall.DismissDialog(getActivity());
//
                Log.d("saleshit", "onFailure: " + t.getMessage());
            }
        });

    }


}