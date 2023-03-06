package com.vendor.salon.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.vendor.salon.R;
import com.vendor.salon.adapters.AppointmentTabLayoutAdapter;
import com.vendor.salon.databinding.ActivityHomeBinding;
import com.vendor.salon.databinding.FragmentAppointmentBinding;
import com.vendor.salon.utilityMethod.FunctionCall;

import java.util.Calendar;

public class AppointmentFragment extends Fragment {

    private FragmentAppointmentBinding appointmentBinding;
    AppointmentTabLayoutAdapter appointmentTabLayoutAdapter;
    BottomSheetDialog bottomFilter;
    public static String start_date = "", status = "", end_date = "", search = "";
    public static String filter = "";
    Context homeContext;
    ActivityHomeBinding homeBinding;

    public AppointmentFragment(FragmentManager supportFragmentManager, ActivityHomeBinding homeBinding) {
        // Required empty public constructor
        this.homeBinding = homeBinding;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        appointmentBinding = FragmentAppointmentBinding.inflate(inflater, container, false);
        homeContext = homeBinding.getRoot().getContext();

        return appointmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        setTabLayoutWithViewPagerWithFragments(view);
        appointmentBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                appointmentBinding.viewPagerAppointments.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        appointmentBinding.viewPagerAppointments.setUserInputEnabled(false);


        appointmentBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setFilteredData();
                appointmentBinding.swipeRefreshLayout.setRefreshing(false);
            }
        });
        appointmentBinding.btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeBinding.drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        createBottomSheetDialogs();

        appointmentBinding.btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomFilter.show();
            }
        });


        appointmentBinding.svAppointmentsSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                AppointmentFragment.search = query;
                setFilteredData();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    appointmentBinding.btnFilter.setVisibility(View.GONE);
                } else {
                    AppointmentFragment.search = "";
                    setFilteredData();
                    appointmentBinding.btnFilter.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

    }


    private void createBottomSheetDialogs() {
        bottomFilter = new BottomSheetDialog(homeBinding.getRoot().getContext());
        bottomFilter.setContentView(R.layout.filter_bottom_sheet);
        AppCompatButton reset = bottomFilter.findViewById(R.id.btn_reset);
        AppCompatButton applyfilters = bottomFilter.findViewById(R.id.btn_apply_filter);
        AppCompatTextView all = bottomFilter.findViewById(R.id.btn_all);
        AppCompatTextView completed = bottomFilter.findViewById(R.id.btn_completed);
        AppCompatTextView cancel = bottomFilter.findViewById(R.id.btn_cancel);
        CardView to_lay = bottomFilter.findViewById(R.id.to_lays);
        CardView from_lay = bottomFilter.findViewById(R.id.from_lays);


//                Toast.makeText(Appointment.this, " Is clickeds. ", Toast.LENGTH_SHORT).show();
        int tabInt = appointmentBinding.tabLayout.getSelectedTabPosition();
//                switch (tabInt) {
//                    case 0:
//                        appointmentTabLayoutAdapter = new AppointmentTabLayoutAdapter( getSupportFragmentManager(), getLifecycle() , appointmentDetails);
//                        break;
//                    case 1:
//                        appointmentTabLayoutAdapter = new AppointmentTabLayoutAdapter(getSupportFragmentManager(), getLifecycle(), appointmentDetails);
//                        break;
//                    case 2:
//                        appointmentTabLayoutAdapter = new AppointmentTabLayoutAdapter(getSupportFragmentManager(), getLifecycle(), appointmentDetails);
//                        break;
//                    case 3:
//                        appointmentTabLayoutAdapter = new AppointmentTabLayoutAdapter(getSupportFragmentManager(), getLifecycle(), appointmentDetails);
//                        break;
//
//                appointmentTabLayoutAdapter.notifyDataSetChanged();
//            }


        all.setOnClickListener(view -> {
            all.setSelected(true);
            all.setBackground(homeContext.getDrawable(R.drawable.filter_selected_box));
            completed.setBackground(homeContext.getDrawable(R.drawable.filtered_unseleced_box));
            cancel.setBackground(homeContext.getDrawable(R.drawable.filtered_unseleced_box));
            if (completed.isSelected()) {
                completed.setSelected(false);
            }
            if (cancel.isSelected()) {
                cancel.setSelected(false);
            }
        });

        completed.setOnClickListener(view -> {
            completed.setSelected(true);
            all.setBackground(homeContext.getDrawable(R.drawable.filtered_unseleced_box));
            cancel.setBackground(homeContext.getDrawable(R.drawable.filtered_unseleced_box));
            if (all.isSelected()) {
                all.setSelected(false);
            }
            if (cancel.isSelected()) {
                cancel.setSelected(false);
            }
            completed.setBackground(homeContext.getDrawable(R.drawable.filter_selected_box));
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                cancel.setSelected(true);
                completed.setBackground(homeContext.getDrawable(R.drawable.filtered_unseleced_box));
                all.setBackground(homeContext.getDrawable(R.drawable.filtered_unseleced_box));
                cancel.setBackground(homeContext.getDrawable(R.drawable.filter_selected_box));
                if (all.isSelected()) {
                    all.setSelected(false);
                }
                if (completed.isSelected()) {
                    completed.setSelected(false);
                }
            }
        });

        from_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatTextView tv_from_dte = view.findViewById(R.id.tv_from_date);
                setCalenderDates(tv_from_dte);
            }
        });

        to_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatTextView tv_to_dt = view.findViewById(R.id.tv_to_date);
                setCalenderDates(tv_to_dt);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FunctionCall.showProgressDialog(homeContext);
                if (completed.isSelected()) {
                    completed.setSelected(false);
                    completed.setBackground(homeContext.getDrawable(R.drawable.filtered_unseleced_box));
                }
                if (all.isSelected()) {
                    all.setSelected(false);
                    all.setBackground(homeContext.getDrawable(R.drawable.filtered_unseleced_box));
                }
                if (cancel.isSelected()) {
                    cancel.setSelected(false);
                    cancel.setBackground(homeContext.getDrawable(R.drawable.filtered_unseleced_box));
                }
                AppCompatTextView from_date = from_lay.findViewById(R.id.tv_from_date);
                if (from_date.getText() != "DD/MM/YYYY") {
                    from_date.setText("DD/MM/YYYY");
                }

                AppointmentFragment.status = "";
                AppointmentFragment.end_date = "";
                AppointmentFragment.start_date = "";
                AppCompatTextView to_date = to_lay.findViewById(R.id.tv_to_date);
                if (to_date.getText() != "DD/MM/YYYY") {
                    to_date.setTextColor(Color.BLACK);
                    to_date.setText("DD/MM/YYYY");
                }

                FunctionCall.DismissDialog(homeContext);
            }
        });

        applyfilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (completed.isSelected()) {
                    AppointmentFragment.status = "" + 1;
                } else if (cancel.isSelected()) {
                    AppointmentFragment.status = 2 + "";
                } else if (all.isSelected()) {
                    AppointmentFragment.status = "";
                } else {
                    AppointmentFragment.status = "";
                }

                AppCompatTextView from_date = from_lay.findViewById(R.id.tv_from_date);
                if (!from_date.getText().toString().equals("DD/MM/YYYY")) {
                    AppointmentFragment.start_date = from_date.getText() + "T09:26:34.000000Z";
                } else {
                    AppointmentFragment.start_date = "";
                }
                AppCompatTextView to_date = to_lay.findViewById(R.id.tv_to_date);
                if (!to_date.getText().toString().equals("DD/MM/YYYY")) {
                    AppointmentFragment.end_date = to_date.getText() + "T09:26:34.000000Z";
                } else {
                }
                if (!from_date.getText().toString().equals("DD/MM/YYYY") && to_date.getText().toString().equals("DD/MM/YYYY")) {
                    to_date.setTextColor(Color.RED);
                    AppointmentFragment.end_date = "";
                    Toast.makeText(homeContext, "end date is required. ", Toast.LENGTH_SHORT).show();
                } else {
                    setFilteredData();
                    bottomFilter.hide();
                }
                /* appointmentBinding.viewPagerAppointments.getAdapter().notifyDataSetChanged();

                 */
            }
        });
    }

    private void setFilteredData() {
        int pos = appointmentBinding.viewPagerAppointments.getCurrentItem();
//        appointmentBinding.tabLayout.getSelectedTabPosition();
        switch (pos) {
            case 0:
//                FunctionCall.showProgressDialog(AppointmentFragment.this.getContext());
//                AppointmentAllAppointmentFragment allFragment = (AppointmentAllAppointmentFragment) this.getSupportFragmentManager().findFragmentById(appointmentBinding.viewPagerAppointments.getCurrentItem());
                AppointmentAllAppointmentFragment allFragment = (AppointmentAllAppointmentFragment) appointmentTabLayoutAdapter.getRegisteredFragment(pos);
                allFragment.getSearchedData();
//                FunctionCall.DismissDialog(AppointmentFragment.this.getContext()) ;
                break;
            case 1:
                AppointmentRecentAppointmentFragment recentFragment = (AppointmentRecentAppointmentFragment) appointmentTabLayoutAdapter.getRegisteredFragment(pos);
//                AppointmentRecentAppointmentFragment recentFragment = (AppointmentRecentAppointmentFragment) this.getSupportFragmentManager().findFragmentById(appointmentBinding.viewPagerAppointments.getId());
                recentFragment.getSearchedData();
                break;
            case 2:
//                AppointmentTodayAppointmentFragment todayAppointmentFragment = (AppointmentTodayAppointmentFragment) this.getSupportFragmentManager().findFragmentById(appointmentBinding.viewPagerAppointments.getId());
                AppointmentTodayAppointmentFragment todayAppointmentFragment = (AppointmentTodayAppointmentFragment) appointmentTabLayoutAdapter.getRegisteredFragment(pos);
                todayAppointmentFragment.getSearchedData();
                break;
            case 3:
//                AppointmentUpcomingAppointmentFragment upcomingFragment = (AppointmentUpcomingAppointmentFragment) this.getSupportFragmentManager().findFragmentById(appointmentBinding.viewPagerAppointments.getId());
                AppointmentUpcomingAppointmentFragment upcomingFragment = (AppointmentUpcomingAppointmentFragment) appointmentTabLayoutAdapter.getRegisteredFragment(pos);
                upcomingFragment.getSearchedData();

        }
//        appointmentTabLayoutAdapter.getRegisteredFragment(pos);
//        appointmentTabLayoutAdapter = null;
//        appointmentTabLayoutAdapter = new AppointmentTabLayoutAdapter(getSupportFragmentManager(), getLifecycle(), true);
    }

    private void setCalenderDates(AppCompatTextView tv_from_dte) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog picker = new DatePickerDialog(homeContext, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                tv_from_dte.setText(year + "-" + (month + 1) + "-" + day);
            }
        }, year, month, day);
        picker.show();

    }

    private void setTabLayoutWithViewPagerWithFragments(View view) {
//        appointmentTabLayoutAdapter = new AppointmentTabLayoutAdapter( Appointment.this.getSupportFragmentManager(), getLifecycle(),  appointments, Appointment.this);

        appointmentTabLayoutAdapter = new AppointmentTabLayoutAdapter(getParentFragmentManager(), getLifecycle(), false);
        appointmentBinding.viewPagerAppointments.setAdapter(appointmentTabLayoutAdapter);
        (new TabLayoutMediator(appointmentBinding.tabLayout, appointmentBinding.viewPagerAppointments,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position) {
                            case 0:
                                tab.setText("All");
                                break;
                            case 1:
                                tab.setText("Recent");
                                break;
                            case 2:
                                tab.setText("Today");
                                break;
                            case 3:
                                tab.setText("Upcoming");
                                break;
                        }
                    }
                })).attach();

    }

}