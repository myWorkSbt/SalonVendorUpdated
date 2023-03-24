package com.vendor.salon.activity;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.vendor.salon.R;
import com.vendor.salon.adapters.ViewStaffAdapter;
import com.vendor.salon.data_Class.getStaff.DataItem;
import com.vendor.salon.data_Class.getStaff.GetStaffResponse;
import com.vendor.salon.databinding.ActivityStaffBinding;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.NetworkChangeListener;
import com.vendor.salon.utilityMethod.loginResponsePref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Staff extends AppCompatActivity {

    public static int selected_staff_position = -1 ;
    private ActivityStaffBinding staffBinding;
    String token;
    private String type;
    private int previousSelectedPosition = -1;
    private List<DataItem> staffList = new ArrayList<>();
    private boolean isApiCalled = false ;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        staffBinding = DataBindingUtil.setContentView(this, R.layout.activity_staff);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        type = getIntent().getStringExtra("use_type");
        token = "Bearer " + loginResponsePref.getInstance(Staff.this).getToken();
        getDatas();

        staffBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
            getDatas();
            staffBinding.swipeRefreshLayout.setRefreshing(false);
        });
        if (type != null && type.equals("add_client")) {
            staffBinding.btnAddStaff.setText("Apply");
        }


        staffBinding.staffViewLists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int selected_item_pos, long l) {
                selected_staff_position = selected_item_pos;
//                if (type != null && type.equals("add_client")) {
//                    CardView previousSelectedView = (CardView) adapterView.getChildAt(previousSelectedPosition);
//                    previousSelectedView.setSelected(false);
//                    if ()
//                    staffLayBinding.itemLayBg.setBackground(Resources.getSystem().getDrawable(R.color.blue_light_popular));
//                }
//                else {
//                    Staff.selected_staff_position = String.valueOf(.getId());
//                    Intent intent = new Intent( staff, ViewStaff.class);
//                    intent.putExtra( "staff_data", staff_item_datas);
//                    staff.startActivity(intent);
//                }
            }
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
                if (newText.length() < 1) {
                    getDatas();
                }
                return true;
            }
        });
        staffBinding.btnAddStaff.setOnClickListener(view -> {
            if (selected_staff_position != -1 ) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selected_staff_position", selected_staff_position);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Intent addIntents = new Intent(Staff.this, AddStaff.class);
                addIntents.putExtra("staff_detail", new DataItem());
                addIntents.putExtra("layout_head", "add");
                startActivity(addIntents);
            }
        });
    }

    private void getDatas() {
        if (!isApiCalled) {
            FunctionCall.showProgressDialog(Staff.this);
            isApiCalled = true;
            Call<GetStaffResponse> call = RetrofitClient.getVendorService().getAllStaffList(token, staffBinding.svAppointmentsSearch.getQuery().toString());
            call.enqueue(new Callback<GetStaffResponse>() {
                @Override
                public void onResponse(@NonNull Call<GetStaffResponse> call, @NonNull Response<GetStaffResponse> response) {
                    FunctionCall.DismissDialog(Staff.this);
                    isApiCalled = false;
                    if (response.isSuccessful() && response.body() != null) {
                        setGridData(response.body());
                    } else {
                        if (response.body() != null) {
                            staffBinding.showNoDataText.setVisibility(GONE);
                            Toast.makeText(Staff.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        Log.d("getstaffhit", "onResponse: " + response.body());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetStaffResponse> call, @NonNull Throwable t) {
                    FunctionCall.DismissDialog(Staff.this);
                    isApiCalled = false;
                    Log.d("getstaffhit", "onFailure: " + t.getMessage());
                }
            });
        }
    }

    private void setGridData(GetStaffResponse getStaffResponse) {
        staffList = getStaffResponse.getData();
        if (getStaffResponse != null && getStaffResponse.getData() != null && getStaffResponse.getData().size() > 0) {
            staffBinding.showNoDataText.setVisibility(GONE);
        } else {
            staffBinding.showNoDataText.setVisibility(VISIBLE);
        }
        assert getStaffResponse != null;
        staffBinding.staffViewLists.setAdapter(new ViewStaffAdapter(Staff.this, getStaffResponse.getData(), type));
    }

    @Override
    public void onBackPressed() {
        Intent homeInt = new Intent(Staff.this, Home.class);
        startActivity(homeInt);
        finishAffinity();
    }


    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener , intentFilter );
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}