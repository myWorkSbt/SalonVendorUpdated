package com.vendor.salon.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import com.vendor.salon.data_Class.getProfile.GetProfileResponse;
import com.vendor.salon.databinding.ActivityHomeBinding;
import com.vendor.salon.fragment.AppointmentFragment;
import com.vendor.salon.fragment.EditProfileFragment;
import com.vendor.salon.fragment.HomeFragment;
import com.vendor.salon.fragment.SaleFragment;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.loginResponsePref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity {

    private ActivityHomeBinding homeBinding;
    private Fragment previousFragment;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private String phone_no ;
    private int backpressedCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding   = DataBindingUtil.setContentView(Home.this , R.layout.activity_home );

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, homeBinding.drawerLayout, R.string.open_drawer, R.string.close_drawer);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        homeBinding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        String vendor_types = getIntent().getStringExtra("vendor") + "";

        if (vendor_types.toLowerCase().equals("freelancer")) {
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

        HomeFragment homes = new HomeFragment(homeBinding);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frag_containers, homes).commit();
        previousFragment = homes;

        homeBinding.bottomNegivation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    setCurrentFragment(homes);
                    return true;
                case R.id.Appointment:
                    setCurrentFragment(new AppointmentFragment(getSupportFragmentManager(),homeBinding));
                    return true;
                case R.id.sales:
                    setCurrentFragment(new SaleFragment(homeBinding ,getSupportFragmentManager()));
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
                public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
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
                public void onFailure(Call<LogoutResponse> call, Throwable t) {
                    FunctionCall.DismissDialog(Home.this);
                    Log.d("logouthit", "onFailure: " + t.getMessage());
                }
            });
        });


        homeBinding.negivationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.Edit_profile:
                    setCurrentFragment(new EditProfileFragment(homeBinding, getSupportFragmentManager()));
                    homeBinding.bottomNegivation.getMenu().getItem(3).setChecked(true);
                    break;
                case R.id.home:
                    setCurrentFragment(new HomeFragment(homeBinding));
                    homeBinding.bottomNegivation.getMenu().getItem(0).setChecked(true);
                    break;
                case R.id.Add_Staff:
                    Intent intent = new Intent(Home.this, Staff.class);
                    startActivity(intent);
                    break;
                case R.id.Add_Client:
                    Intent intentClient = new Intent(Home.this, AddClientActivity.class);
                    startActivity(intentClient);
                    break;
                case R.id.Salon_Time:
                    Intent salon_time = new Intent(Home.this, SalonTime.class);
                    startActivity(salon_time);
                    break;
                case R.id.Inventory:
                    Intent inventoryIntent = new Intent(Home.this, InventoryActivity.class);
                    startActivity(inventoryIntent);
                    break;

                case R.id.Cover_Range:
                    Intent cover_rangeIntent = new Intent(Home.this, CoverRangeActivity.class);
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
                        if (response.body().getOwnerDetail() != null ) {
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
            public void onFailure(Call<GetProfileResponse> call, Throwable t) {
                Toast.makeText(Home.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("getprofilehit", "onFailure: " + t.getMessage());
            }
        });
    }

    private void setCurrentFragment(Fragment fragment) {
        if (!previousFragment.getClass().isInstance(fragment)) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_containers, fragment).commit();
            previousFragment = fragment;
        }
    }

    @Override
    public void onBackPressed() {
        backpressedCount++;
        if (backpressedCount < 2) {
            Toast.makeText(this, " Press again to exist from app", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }
}