package com.vendor.salon.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.vendor.salon.R;
import com.vendor.salon.adapters.ViewStaffAdapter;
import com.vendor.salon.data_Class.getStaff.DataItem;
import com.vendor.salon.data_Class.getStaff.GetStaffResponse;
import com.vendor.salon.databinding.ActivityStaffBinding;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.loginResponsePref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Staff extends AppCompatActivity {

    private ActivityStaffBinding staffBinding;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        staffBinding = DataBindingUtil.setContentView(this,R.layout.activity_staff);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        token = "Bearer "+ loginResponsePref.getInstance(Staff.this).getToken();
        getDatas() ;

        staffBinding.swipeRefreshLayout.setOnRefreshListener(()  -> {
                getDatas();
                staffBinding.swipeRefreshLayout.setRefreshing(false);
        });
        staffBinding.staffViewLists.setOnItemClickListener((adapterView, view, i, l) -> {

        });

        staffBinding.back.setOnClickListener(view -> {
            Intent homeInt = new Intent(Staff.this, Home.class);
            startActivity(homeInt);
            finishAffinity();
        });

        staffBinding.svAppointmentsSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getDatas();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()<1) {
                    getDatas();
                }
                return true;
            }
        });
        staffBinding.btnAddStaff.setOnClickListener(view -> {
            Intent addIntents = new Intent(Staff.this, AddStaff.class);
            addIntents.putExtra("staff_detail",  new DataItem());
            addIntents.putExtra("layout_head","add");
            startActivity(addIntents);
        });
    }

    private void getDatas() {
        FunctionCall.showProgressDialog(Staff.this);
        Call<GetStaffResponse> call = RetrofitClient.getVendorService().getAllStaffList(token , staffBinding.svAppointmentsSearch.getQuery().toString());
        call.enqueue(new Callback<GetStaffResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetStaffResponse> call, @NonNull Response<GetStaffResponse> response) {
                FunctionCall.DismissDialog(Staff.this );
                if(response.isSuccessful() && response.body()!= null) {
                    setGridData(response.body());
                }
                else {
                    if (response.body() != null) {
                        Toast.makeText( Staff.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Log.d("getstaffhit", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetStaffResponse> call, @NonNull Throwable t) {
                FunctionCall.DismissDialog(Staff.this );
                Log.d("getstaffhit", "onFailure: "+t.getMessage());
            }
        });


    }

    private void setGridData(GetStaffResponse getStaffResponse) {
        if ( getStaffResponse != null && getStaffResponse.getData() != null &&  getStaffResponse.getData().size() > 0 ) {
            staffBinding.showNoDataText.setVisibility(GONE);
        }
        else {
                staffBinding.showNoDataText.setVisibility(VISIBLE);
        }
        assert getStaffResponse != null;
        staffBinding.staffViewLists.setAdapter(new ViewStaffAdapter(Staff.this, getStaffResponse.getData()));
    }

    @Override
    public void onBackPressed() {
        Intent homeInt = new Intent(Staff.this, Home.class);
        startActivity(homeInt);
        finishAffinity();
    }
}