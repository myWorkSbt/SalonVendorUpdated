package com.vendor.salon.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.vendor.salon.R;
import com.vendor.salon.adapters.TodayAppointmentRecyclerAdapter;
import com.vendor.salon.data_Class.StaffAppointments.StaffAppointmentsResponse;
import com.vendor.salon.data_Class.appointmentsfilter.AppointmentsItem;
import com.vendor.salon.data_Class.assign_staff.Appointment;
import com.vendor.salon.data_Class.getStaff.DataItem;
import com.vendor.salon.data_Class.removestaff.RemoveStaffResponse;
import com.vendor.salon.databinding.ActivityViewStaffBinding;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.NetworkChangeListener;
import com.vendor.salon.utilityMethod.loginResponsePref;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewStaff extends AppCompatActivity {

    private ActivityViewStaffBinding viewStaffBinding;
    TodayAppointmentRecyclerAdapter todayAppointmentRecyclerAdapter;
    String token;
    DataItem getted_staff_data;
    private DatePickerDialog picker;
    private boolean isApiCalled = false ;
    NetworkChangeListener networkChangeListener  = new NetworkChangeListener() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewStaffBinding = DataBindingUtil.setContentView(this, R.layout.activity_view_staff);

        token = "Bearer " + loginResponsePref.getInstance(ViewStaff.this).getToken();

        getted_staff_data = (DataItem) getIntent().getSerializableExtra("staff_data");
//        String ids ="585";
        createDateDialogs();

        todayAppointmentRecyclerAdapter = new TodayAppointmentRecyclerAdapter(ViewStaff.this, new ArrayList<>());
        viewStaffBinding.appointmentListRecycler.setAdapter(todayAppointmentRecyclerAdapter);

        setStaffData();
        getData("");

        viewStaffBinding.calender.setOnClickListener(View -> {
            picker.show();
//            dataPicDialog.datePicker.maxDate()
        });

        viewStaffBinding.btnMenu.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ViewStaff.this);
            View dialogView = getLayoutInflater().inflate(R.layout.staff_menu_lays, null);
            dialogView.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            builder.setView(dialogView);
            AppCompatTextView btn_editstaff = dialogView.findViewById(R.id.btn_edit_staff);
            AppCompatTextView btn_remove_staff = dialogView.findViewById(R.id.btn_remove_staff);
            AppCompatTextView btn_send_login_credentials = dialogView.findViewById(R.id.btn_send_credentials);

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            btn_editstaff.setOnClickListener(View -> {
                Intent intents = new Intent(ViewStaff.this, AddStaff.class);
                intents.putExtra("layout_head", "edit");
                intents.putExtra("staff_detail", getted_staff_data);
                startActivity(intents);
                finish();
            });

            btn_remove_staff.setOnClickListener(view1 -> {
                String token = "Bearer " + loginResponsePref.getInstance(ViewStaff.this).getToken();
                Call<RemoveStaffResponse> call = RetrofitClient.getVendorService().removeStaff(token, getted_staff_data.getId() + "");
                call.enqueue(new Callback<RemoveStaffResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<RemoveStaffResponse> call, @NonNull Response<RemoveStaffResponse> response) {
                        if (response.body() != null) {
                            Toast.makeText(ViewStaff.this, "" + response.body().getMessaage(), Toast.LENGTH_SHORT).show();
                        }

                        if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                            Intent intents = new Intent(ViewStaff.this, Staff.class);
                            intents.putExtra("use_type","view_staff");
                            startActivity(intents);
                            finish();
                        } else {
                            Log.d("removestaffhit", "onResponse: " + response);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<RemoveStaffResponse> call, @NonNull Throwable t) {
                        Log.d("removestaffhit", "onFailure: " + t.getMessage());
                    }
                });

            });

            btn_send_login_credentials.setOnClickListener(View -> {
                Toast.makeText(ViewStaff.this, " Login Credentials has been emailed to the staff. ", Toast.LENGTH_SHORT).show();
                alertDialog.hide();
            });
        });


        viewStaffBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
            viewStaffBinding.tvDate.setText("YYYY-MM-DD");
            getData("");
            viewStaffBinding.swipeRefreshLayout.setRefreshing(false);
        });
        viewStaffBinding.btnBack.setOnClickListener(view -> finish());
    }

    private void createDateDialogs() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        picker = new DatePickerDialog(ViewStaff.this, (datePicker, day1, month1, year1) -> {
            String new_date = String.format("%d-%d-%d", day1, month1 + 1, year1);
            String [] dateArrs = new_date.split("-");
            new_date = dateArrs[0] + "-" ;
            if ( dateArrs[1].length()  == 1) {
                new_date += "0";
            }
            new_date += dateArrs[1]  + "-" ;
            if (dateArrs[2].length() ==1) {
                new_date += "0" ;
            }
            new_date += dateArrs[2]  ;
            viewStaffBinding.tvDate.setText(new_date);
            getData(new_date);
        }, year, month, day);
        picker.getDatePicker().setMaxDate(cal.getTimeInMillis());
    }

    private void getData(String date) {
        if (!isApiCalled) {
            isApiCalled = true;
            FunctionCall.showProgressDialog(ViewStaff.this);
            Call<StaffAppointmentsResponse> call = RetrofitClient.getVendorService().getStaffAppointments(token, String.valueOf(getted_staff_data.getId()), date);
            call.enqueue(new Callback<StaffAppointmentsResponse>() {
                @Override
                public void onResponse(@NonNull Call<StaffAppointmentsResponse> call, @NonNull Response<StaffAppointmentsResponse> response) {
                    FunctionCall.DismissDialog(ViewStaff.this);
                    isApiCalled = false;
                    if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                        setDataToTheUI(response.body(), date);
                        if (response.body().getAppointments() != null && response.body().getAppointments().size() > 0) {
                            viewStaffBinding.showNoDataText.setVisibility(View.GONE);
                        } else {
                            viewStaffBinding.showNoDataText.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (response.body() != null) {
                            Toast.makeText(ViewStaff.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        viewStaffBinding.showNoDataText.setVisibility(View.VISIBLE);
                        Log.d("staffappointmentshit", "onResponse: " + response.body());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<StaffAppointmentsResponse> call, @NonNull Throwable t) {
                    FunctionCall.DismissDialog(ViewStaff.this);
                    Log.d("staffappointmentshit", "onFailure: " + t.getMessage());
                    isApiCalled = false ;
                }
            });
        }
    }

    private void setStaffData() {
        viewStaffBinding.tvIdStaff.setText(String.valueOf(getted_staff_data.getId()));
        viewStaffBinding.tvNameStaff.setText(getted_staff_data.getName());
        viewStaffBinding.tvGenderStaff.setText(getted_staff_data.getGender());
        viewStaffBinding.tvMobileStaff.setText(getted_staff_data.getPhone());
        Glide.with(viewStaffBinding.staffImage.getContext()).load(Uri.parse(getted_staff_data.getOwnerImage())).error(R.drawable.no_image_rectangle).into(viewStaffBinding.staffImage);
        viewStaffBinding.tvEmailStaff.setText(getted_staff_data.getEmail());
        viewStaffBinding.tvSpecialistStaff.setText(getted_staff_data.getDesignation());
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void setDataToTheUI(@NonNull StaffAppointmentsResponse staffAppointmentsResponse, String date) {


        long millis = System.currentTimeMillis();
        String day = "Sunday";
        if (date.equals("") || date.isEmpty() || date == null) {
            java.sql.Date date7 = new java.sql.Date(millis);
            date = String.format("%s", date7);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter dateFormatters = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date2 = LocalDate.parse(date, dateFormatters);
            day = date2.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        }
        viewStaffBinding.tvDate.setText(date);
//        viewStaffBinding.tvDayTitle.setText(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) ;
        viewStaffBinding.tvDayTitle.setText(day);
        //        todayAppointmentRecyclerAdapter = new TodayAppointmentRecyclerAdapter(staff_dta.)
        List<AppointmentsItem> appointmentsItems = new ArrayList<>();
        for (int i = 0; i < staffAppointmentsResponse.getAppointments().size(); i++) {
            AppointmentsItem singleAppointmentss = new AppointmentsItem();
            Appointment appointmentsItem = new Appointment();
            com.vendor.salon.data_Class.StaffAppointments.AppointmentsItem newaAppointmentsItem = staffAppointmentsResponse.getAppointments().get(i);
            if (newaAppointmentsItem != null) {
                appointmentsItem.setAmount(newaAppointmentsItem.getAmount());
                appointmentsItem.setClientGender(newaAppointmentsItem.getClientGender());
                appointmentsItem.setCreatedAt(newaAppointmentsItem.getCreatedAt());
                String distancestrs = String.valueOf(newaAppointmentsItem.getDistance());
                if (distancestrs.length() > 4) {
                    distancestrs = distancestrs.substring(0, 4);
                }
//                appointmentsItem.set(distancestrs);
                appointmentsItem.setDeletedAt(newaAppointmentsItem.getDeletedAt());
                appointmentsItem.setId(newaAppointmentsItem.getId());
                appointmentsItem.setEndTime(newaAppointmentsItem.getEndTime());
                appointmentsItem.setNoOfPeople(newaAppointmentsItem.getNoOfPeople());
                appointmentsItem.setOrderId(newaAppointmentsItem.getOrderId());
                appointmentsItem.setServiceSite(newaAppointmentsItem.getServiceSite());
            }

            appointmentsItems.add(singleAppointmentss);
        }
        todayAppointmentRecyclerAdapter.refreshList(appointmentsItems);

    }

    @Override
    public void onBackPressed() {
        finish();
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