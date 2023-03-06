package com.vendor.salon.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.DataBindingUtil;

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
import com.vendor.salon.utilityMethod.loginResponsePref;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewStaff extends AppCompatActivity {

    private ActivityViewStaffBinding viewStaffBinding;
    TodayAppointmentRecyclerAdapter todayAppointmentRecyclerAdapter;
    StaffAppointmentsResponse staff_dta;
    String token;
    String ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewStaffBinding = DataBindingUtil.setContentView(this, R.layout.activity_view_staff);

        token = "Bearer " + loginResponsePref.getInstance(ViewStaff.this).getToken();

        ids = getIntent().getStringExtra("id");
//        String ids ="585";

        getData();

        viewStaffBinding.calender.setOnClickListener( View -> {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog picker = new DatePickerDialog(ViewStaff.this, (datePicker, year1, month1, day1) -> viewStaffBinding.tvDate.setText(String.format("%d/%d/%d", day1, month1 + 1, year1)), year, month, day);
                picker.show();
                picker.getDatePicker().setMaxDate(cal.getTimeInMillis());
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
                    DataItem staff_data_items = new DataItem();
                    staff_data_items.setName(staff_dta.getStaff().getName());
                    staff_data_items.setId(staff_dta.getStaff().getId());
                    staff_data_items.setAddress(staff_dta.getStaff().getAddress());
                    staff_data_items.setEmail(staff_dta.getStaff().getEmail());
                    staff_data_items.setPhone(staff_dta.getStaff().getPhone());
                    staff_data_items.setGender(staff_dta.getStaff().getGender());
                    staff_data_items.setOwnerImage(String.valueOf(staff_dta.getStaff().getOwnerImage()));
                    staff_data_items.setDob(staff_dta.getStaff().getDob());
                    staff_data_items.setDesignation(staff_dta.getStaff().getDesignation());
                    intents.putExtra("layout_head", "edit");
                    intents.putExtra("staff_detail", staff_data_items);
                    startActivity(intents);
                    finish();
            });

            btn_remove_staff.setOnClickListener(view1 -> {
                 String token = "Bearer " + loginResponsePref.getInstance(ViewStaff.this).getToken() ;
                 Call<RemoveStaffResponse> call =  RetrofitClient.getVendorService().removeStaff( token, staff_dta.getStaff().getId() +  "" );
                 call.enqueue(new Callback<RemoveStaffResponse>() {
                     @Override
                     public void onResponse(@NonNull Call<RemoveStaffResponse> call, @NonNull Response<RemoveStaffResponse> response) {
                         if ( response.body() != null ) {
                             Toast.makeText(ViewStaff.this, "" + response.body().getMessaage(), Toast.LENGTH_SHORT).show();
                         }

                         if ( response.isSuccessful() &&  response.body() != null  && response.body().isStatus() ) {
                             Intent intents = new Intent(ViewStaff.this, Staff.class);
                             startActivity(intents);
                             finish();
                          }
                          else {
                             Log.d("removestaffhit" , "onResponse: " + response );
                          }
                     }

                     @Override
                     public void onFailure(@NonNull Call<RemoveStaffResponse> call,@NonNull Throwable t) {
                         Log.d("removestaffhit", "onFailure: " + t.getMessage() );
                     }
                 });

            });

            btn_send_login_credentials.setOnClickListener( View -> {
                    Toast.makeText(ViewStaff.this, " Login Credentials has been emailed to the staff. ", Toast.LENGTH_SHORT).show();
                    alertDialog.hide();
            });
        });


        viewStaffBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
            getData();
            viewStaffBinding.swipeRefreshLayout.setRefreshing(false);
        });
        viewStaffBinding.btnBack.setOnClickListener(view -> finish());
    }

    private void getData() {
        FunctionCall.showProgressDialog(ViewStaff.this);
        Call<StaffAppointmentsResponse> call = RetrofitClient.getVendorService().getStaffDetails(token, ids);
        call.enqueue(new Callback<StaffAppointmentsResponse>() {
            @Override
            public void onResponse(@NonNull Call<StaffAppointmentsResponse> call, @NonNull Response<StaffAppointmentsResponse> response) {
                FunctionCall.DismissDialog(ViewStaff.this);
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    setDataToTheUI(response.body());
                } else {
                    if (response.body() != null) {
                        Toast.makeText(ViewStaff.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Log.d("staffappointmentshit", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure( @NonNull Call<StaffAppointmentsResponse> call , @NonNull Throwable t) {
                FunctionCall.DismissDialog(ViewStaff.this);
                Log.d("staffappointmentshit", "onFailure: " + t.getMessage());
            }
        });

    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void setDataToTheUI(StaffAppointmentsResponse staffAppointmentsResponse) {


        long millis = System.currentTimeMillis();
        staff_dta = staffAppointmentsResponse;
        java.sql.Date date = new java.sql.Date(millis);
        viewStaffBinding.tvDate.setText(String.format("%s", date));
//        viewStaffBinding.tvDayTitle.setText(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) ;
        viewStaffBinding.tvDayTitle.setText("Sunday");
        viewStaffBinding.tvNameStaff.setText(staffAppointmentsResponse.getStaff().getName());
        viewStaffBinding.tvEmailStaff.setText(staffAppointmentsResponse.getStaff().getEmail());
        viewStaffBinding.tvGenderStaff.setText(staffAppointmentsResponse.getStaff().getGender());
        viewStaffBinding.tvMobileStaff.setText(staffAppointmentsResponse.getStaff().getPhone());
        viewStaffBinding.tvIdStaff.setText(String.format("%d", staffAppointmentsResponse.getStaff().getId()));
        viewStaffBinding.specialistHeading.setText(staffAppointmentsResponse.getStaff().getDesignation());
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

//            singleAppointmentss.setAppointment(appointmentsItem);
            appointmentsItems.add(singleAppointmentss);
        }
        if (appointmentsItems == null ) {
            viewStaffBinding.showNoDataText.setVisibility(View.VISIBLE);
        }
        else {
            viewStaffBinding.showNoDataText.setVisibility(View.GONE );
        }
        todayAppointmentRecyclerAdapter = new TodayAppointmentRecyclerAdapter(ViewStaff.this, appointmentsItems);
        viewStaffBinding.appointmentListRecycler.setAdapter(todayAppointmentRecyclerAdapter);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}