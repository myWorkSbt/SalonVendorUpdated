package com.vendor.salon.activity;

import static com.vendor.salon.R.id.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.vendor.salon.R;
import com.vendor.salon.data_Class.LogoutResponse;
import com.vendor.salon.data_Class.areacover.CoverRangeResponse;
import com.vendor.salon.data_Class.getProfile.GetProfileResponse;
import com.vendor.salon.databinding.ActivityHomeBinding;
import com.vendor.salon.fragment.AppointmentFragment;
import com.vendor.salon.fragment.EditProfileFragment;
import com.vendor.salon.fragment.HomeFragment;
import com.vendor.salon.fragment.SaleFragment;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.NetworkChangeListener;
import com.vendor.salon.utilityMethod.loginResponsePref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity {

    public  static ActivityHomeBinding homeBinding;
    private Fragment previousFragment;
    private int backpressedCount = 0;
    private Double range_vals;
    private Double range_lat;
    public static String services_gender = "";
    private Double range_lng;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private HomeFragment homes;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = DataBindingUtil.setContentView(Home.this, R.layout.activity_home);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, homeBinding.drawerLayout, R.string.open_drawer, R.string.close_drawer);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        homeBinding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        String vendor_types = getIntent().getStringExtra("vendor") + "";
        getCoverRangeVal();
        if (vendor_types.equalsIgnoreCase("freelancer")) {
            homeBinding.negivationView.inflateMenu(R.menu.drawer_menu_freelancer);
        } else {
            homeBinding.negivationView.inflateMenu(R.menu.drawer_menu_salon);
        }
        homeBinding.negivationView.bringToFront();
//        homeBinding.negivationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                uncheckItemFromBottomNavigation();
//                switch (item.getItemId()) {
//                    case R.id.nav_drawer_services:
//                        break;
//                }

//                homeBinding.negivationView.getMenu().findItem(previous_navDrawer_ItemId).setChecked(false);
//                previous_navDrawer_ItemId = item.getItemId();
//                item.setChecked(true);
//                drawerLayout.closeDrawer(GravityCompat.START);
//                return true;
//            }
//        });
        getProfileData();

        homes = new HomeFragment(homeBinding);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frag_containers, homes).commit();
        previousFragment = homes;

        homeBinding.bottomNegivation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case home:
                    setCurrentFragment(homes);
                    return true;
                case R.id.Appointment:
                    setCurrentFragment(new AppointmentFragment(getSupportFragmentManager(), homeBinding));
                    return true;
                case R.id.sales:
                    setCurrentFragment(new SaleFragment(homeBinding, getSupportFragmentManager()));
                    return true;
                case R.id.video_inactive:
                    setCurrentFragment(new EditProfileFragment(homeBinding, getSupportFragmentManager()));
                    return true;
                default:
                    return false;
            }
        });

        setMenuColors();
        homeBinding.logout.setOnClickListener(view -> {
            FunctionCall.showProgressDialog(Home.this);
            String token = loginResponsePref.getInstance(getApplicationContext()).getToken();
            Call<LogoutResponse> call = RetrofitClient.getVendorService().LogoutVendor("Bearer " + token);
            call.enqueue(new Callback<LogoutResponse>() {
                @Override
                public void onResponse(@NonNull Call<LogoutResponse> call, @NonNull Response<LogoutResponse> response) {
                    FunctionCall.DismissDialog(Home.this);
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().isResult()) {
                            Toast.makeText(Home.this, " " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Home.this, Login.class);
                            startActivity(intent);
                            loginResponsePref.getInstance(Home.this).removeToken();
                            finishAffinity();
                        }

                    } else {
                        if (response.body() != null) {
                            Toast.makeText(Home.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        Log.d("logouthit", "onResponse: " + response.body());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LogoutResponse> call, @NonNull Throwable t) {
                    FunctionCall.DismissDialog(Home.this);
                    Log.d("logouthit", "onFailure: " + t.getMessage());
                }
            });
        });


        homeBinding.negivationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.Edit_profile:
                    backpressedCount = 0;
                    setCurrentFragment(new EditProfileFragment(homeBinding, getSupportFragmentManager()));
                    homeBinding.bottomNegivation.getMenu().getItem(3).setChecked(true);
                    break;
                case R.id.home:
                    backpressedCount = 0;
                    setCurrentFragment(homes);
                    break;
                case R.id.Add_Staff:
                    backpressedCount = 0;
                    Intent intent = new Intent(Home.this, Staff.class);
                    intent.putExtra("use_type" , "home" );
                    startActivity(intent);
                    break;
                case R.id.Add_Client:
                    backpressedCount = 0;
                    Intent intentClient = new Intent(Home.this, AddClientActivity.class);
                    intentClient.putExtra("services_for", services_gender);
                    startActivity(intentClient);
                    break;
                case R.id.Salon_Time:
                    backpressedCount = 0;
                    Intent salon_time = new Intent(Home.this, SalonTime.class);
                    startActivity(salon_time);
                    break;
                case R.id.Inventory:
                    backpressedCount = 0;
                    Intent inventoryIntent = new Intent(Home.this, InventoryActivity.class);
                    startActivity(inventoryIntent);
                    break;

                case R.id.Cover_Range:
                    backpressedCount = 0;
                    Intent cover_rangeIntent = new Intent(Home.this, CoverRangeActivity.class);
                    cover_rangeIntent.putExtra("radius", range_vals);
                    cover_rangeIntent.putExtra("lats", range_lat);
                    cover_rangeIntent.putExtra("lng", range_lng);
                    startActivity(cover_rangeIntent);
                    break;
                default:
                    Toast.makeText(Home.this, "Under Development . ", Toast.LENGTH_SHORT).show();
                    return false;
            }
            homeBinding.drawerLayout.closeDrawers();

            return true;
        });
    }


    private void setMenuColors() {
        ColorStateList ColorStates = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        Color.parseColor("#000000"),
                        Color.parseColor("#F43FFF")
                }
        );
        homeBinding.negivationView.setItemTextColor(ColorStates);
        homeBinding.negivationView.setItemIconTintList(ColorStates);

    }


    private void getProfileData() {
        String token = loginResponsePref.getInstance(this).getToken();
        Call<GetProfileResponse> call = RetrofitClient.getVendorService().getVendorDetails("Bearer " + token);
        call.enqueue(new Callback<GetProfileResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetProfileResponse> call, @NonNull Response<GetProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().isStatus()) {
//                        setProfileData(response.body());
                        AppCompatTextView tv_vendor_name = homeBinding.drawerLayout.findViewById(R.id.vendor_name);
                        if (response.body().getOwnerDetail() != null) {
                            tv_vendor_name.setText(response.body().getOwnerDetail().getName());
                            Glide.with(((ImageView) homeBinding.drawerLayout.findViewById(R.id.profile_vendor_img)).getContext()).load((response.body().getOwnerDetail().getUserImage())).into((ImageView) homeBinding.drawerLayout.findViewById(R.id.profile_vendor_img));
                        }
                    }
                } else {
                    if (response.body() != null) {
                        Toast.makeText(Home.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Log.d("getprofilehit", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetProfileResponse> call, @NonNull Throwable t) {
                Toast.makeText(Home.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("getprofilehit", "onFailure: " + t.getMessage());
            }
        });
    }

    private void setCurrentFragment(Fragment fragment) {
        backpressedCount = 0;
        if (!previousFragment.getClass().isInstance(fragment)) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_containers, fragment).commit();
            previousFragment = fragment;
        }
    }

    @Override
    public void onBackPressed() {
        backpressedCount++;
        if ( !previousFragment.getClass().isInstance(homes)) {
            homeBinding.bottomNegivation.getMenu().getItem(0).setChecked(true);
            setCurrentFragment(homes);
        } else {
            if (backpressedCount < 2) {
                Toast.makeText(this, " Press again to exist from app", Toast.LENGTH_SHORT).show();
            } else {
                super.onBackPressed();
            }
        }

    }

    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver( networkChangeListener,intentFilter);
        super.onStart();
    }

    public void getCoverRangeVal() {
        String token = "Bearer " + loginResponsePref.getInstance(Home.this).getToken();
        Call<CoverRangeResponse> call = RetrofitClient.getVendorService().getRangeValue(token);
        call.enqueue(new Callback<CoverRangeResponse>() {
            @Override
            public void onResponse(@NonNull Call<CoverRangeResponse> call, @NonNull Response<CoverRangeResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus() && response.body().getData() != null) {
                    if (response.body().getData().getAreaCover() == null || response.body().getData().getAreaCover().isEmpty()) {
                        range_vals = 0.0;
                    } else {
                        range_vals = Double.parseDouble(response.body().getData().getAreaCover());
                    }
                    range_lng = (Double) response.body().getData().getLng();
                    range_lat = (Double) response.body().getData().getLat();
                } else {
                    Log.d("coverrangehit", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CoverRangeResponse> call, @NonNull Throwable t) {
                Log.d("coverrangehit", "onFailuress: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener ); ;
        super.onStop();
    }
}