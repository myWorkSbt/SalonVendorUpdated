package com.vendor.salon.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.vendor.salon.R;
import com.vendor.salon.activity.AddServices;
import com.vendor.salon.activity.AppointmentDetail;
import com.vendor.salon.activity.CategoryDetails;
import com.vendor.salon.activity.Home;
import com.vendor.salon.activity.ManagePackages;
import com.vendor.salon.activity.ManageServices;
import com.vendor.salon.activity.Notifications;
import com.vendor.salon.activity.Wallet;
import com.vendor.salon.adapters.AppointmentAdapter;
import com.vendor.salon.adapters.ChartviewpagerAdapter;
import com.vendor.salon.adapters.MangeServiceAdapter;
import com.vendor.salon.adapters.SliderAdapter;
import com.vendor.salon.adapters.TopServiceAdapter;
import com.vendor.salon.data_Class.getProfile.GetProfileResponse;
import com.vendor.salon.data_Class.home.BannerItem;
import com.vendor.salon.data_Class.home.CategoriesItem;
import com.vendor.salon.data_Class.home.HomeResponse;
import com.vendor.salon.data_Class.home.RecentAppointmentItem;
import com.vendor.salon.data_Class.home.TopServicesItem;
import com.vendor.salon.databinding.ActivityHomeBinding;
import com.vendor.salon.databinding.FragmentHomeBinding;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.OnClickListener;
import com.vendor.salon.utilityMethod.loginResponsePref;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding viewDataBinding;


    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.LayoutManager manager;
    List<CategoriesItem> categoriesItemList;
    List<RecentAppointmentItem> recentAppointment;
    Context homeContext;
    ActivityHomeBinding homeBinding;
    private String vendor_services_for = "none";

    public HomeFragment(ActivityHomeBinding homeBinding) {
        // Required empty public constructor
        this.homeBinding = homeBinding;
    }

    public HomeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewDataBinding = FragmentHomeBinding.inflate(inflater, container, false);
        if (homeBinding == null ) {
            homeBinding = Home.homeBinding ;
        }
        homeContext = homeBinding.getRoot().getContext();

        return viewDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getData();


        layoutManager = new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );

        getVendor_Details ();
        viewDataBinding.recyclerManageServices.setLayoutManager(layoutManager);

        manager = new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );

        viewDataBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                viewDataBinding.swipeRefreshLayout.setRefreshing(false);
            }
        });

        viewDataBinding.appointmentRecyclerView.setLayoutManager(manager);

        viewDataBinding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeBinding.drawerLayout.openDrawer(GravityCompat.START);
//            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }
        });

        layoutManager = new LinearLayoutManager(
                HomeFragment.this.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);
        viewDataBinding.topServicesRecycler.setLayoutManager(layoutManager);


        viewDataBinding.notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent notificatinIntent = new Intent(getActivity(), Notifications.class);
                startActivity(notificatinIntent);
            }
        });
        viewDataBinding.seeAll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ManagePackages.class);
                startActivity(intent);
            }
        });

        viewDataBinding.seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ManageServices.class);
                intent.putExtra("services_for" , vendor_services_for);
                startActivity(intent);
            }
        });


        viewDataBinding.wallet.setOnClickListener(View -> {
            Intent intent = new Intent(getActivity(), Wallet.class);
            homeContext.startActivity(intent);
        });


        viewDataBinding.appointmentSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeBinding.bottomNegivation.getMenu().getItem(1).setChecked(true);
                getParentFragmentManager().beginTransaction().replace(R.id.frag_containers, new AppointmentFragment(getParentFragmentManager(), homeBinding)).commit();
//                Intent appointIntent = new Intent(getActivity(), Appointment.class);
//                startActivity(appointIntent);
//                getParentFragmentManager().beginTransaction().replace(R.id.frag_containers, new AppointmentRecentAppointmentFragment(recentAppointment)).commit();
            }
        });

        viewDataBinding.manageAddServicesTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addServicesIntent = new Intent(getActivity(), AddServices.class);
                addServicesIntent.putExtra("services_for" , vendor_services_for);
                startActivity(addServicesIntent);
            }
        });
    }

    private void getVendor_Details() {
        String token = "Bearer "+ loginResponsePref.getInstance(homeContext).getToken();
        Call<GetProfileResponse> call = RetrofitClient.getVendorService().getVendorDetails("Bearer " + token);
        call.enqueue(new Callback<GetProfileResponse>() {
            @Override
            public void onResponse(Call<GetProfileResponse> call, Response<GetProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isStatus()) {
                        if (response.body().getOwnerDetail() != null) {
                            vendor_services_for = String.valueOf(response.body().getOwnerDetail().getService_for()).toLowerCase() ;
                            Home.services_gender = vendor_services_for ;
                        }
                    }
                } else {
                    if (response.body() != null) {
                        Toast.makeText(homeContext, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Log.d("getprofilehit", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<GetProfileResponse> call, Throwable t) {
                Toast.makeText(homeContext , "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("getprofilehit", "onFailure: " + t.getMessage());
            }
        });


    }

    private void getData() {
        FunctionCall.showProgressDialog(homeContext);
        String token = loginResponsePref.getInstance(getContext()).getToken();
        Call<HomeResponse> call = RetrofitClient.getVendorService().home("Bearer " + token);
        call.enqueue(new Callback<HomeResponse>() {
            @Override
            public void onResponse(Call<HomeResponse> call, Response<HomeResponse> response) {
                FunctionCall.DismissDialog(homeContext);
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isStatus()) {
                        if (response.body().getBanners() != null) {
                            SetBanner(response.body().getBanners());
                        }
                        if (response.body().getCategories() != null) {
                            setServices(response.body().getCategories());
                        }
                        if (response.body().getRecentAppointment() != null  && response.body().getRecentAppointment().size()>0 )  {
                            recentAppointment = response.body().getRecentAppointment();
                            viewDataBinding.Appointment.setVisibility(View.VISIBLE);
                            viewDataBinding.appointmentSeeAll.setVisibility(View.VISIBLE);
                            setAppointments(response.body().getRecentAppointment());
                        } else {
                            viewDataBinding.Appointment.setVisibility(View.GONE);
                            viewDataBinding.appointmentSeeAll.setVisibility(View.GONE);
                        }
                        if (response.body().getTopServices() != null && response.body().getTopServices().size()>0 ) {
                            viewDataBinding.topServices.setVisibility(View.VISIBLE);
                            viewDataBinding.seeAll3.setVisibility(View.VISIBLE);
                            setTopServices(response.body().getTopServices());
                        } else {
                            viewDataBinding.topServices.setVisibility(View.GONE);
                            viewDataBinding.seeAll3.setVisibility(View.GONE);
                        }
                        if (response.body().getDailySales() != null) {
                            viewDataBinding.Revenue.setVisibility(View.VISIBLE);
                            viewDataBinding.viewPager.setVisibility(View.VISIBLE);
                            setRevenue(response.body());
                        } else {
                            viewDataBinding.Revenue.setVisibility(View.GONE);
                            viewDataBinding.viewPager.setVisibility(View.GONE);
                        }
                    }
                } else {
                    viewDataBinding.Appointment.setVisibility(View.GONE);
                    viewDataBinding.appointmentSeeAll.setVisibility(View.GONE);
                    viewDataBinding.topServices.setVisibility(View.GONE);
                    viewDataBinding.seeAll3.setVisibility(View.GONE);
                    viewDataBinding.Revenue.setVisibility(View.GONE);
                    viewDataBinding.viewPager.setVisibility(View.GONE);
                    Log.d("homehit", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<HomeResponse> call, Throwable t) {
                FunctionCall.DismissDialog(homeContext);
                Log.d("homehit", "onFailure:  " + t.getMessage());
                viewDataBinding.Appointment.setVisibility(View.GONE);
                viewDataBinding.appointmentSeeAll.setVisibility(View.GONE);
                viewDataBinding.topServices.setVisibility(View.GONE);
                viewDataBinding.seeAll3.setVisibility(View.GONE);
                viewDataBinding.Revenue.setVisibility(View.GONE);
                viewDataBinding.viewPager.setVisibility(View.GONE);
            }
        });
    }

    private void setAppointments(List<RecentAppointmentItem> recentAppointment) {
        AppointmentAdapter appointmentAdapter = new AppointmentAdapter(getActivity(), recentAppointment, new OnClickListener() {
            @Override
            public void onItemClickListener(Context context, int position) {
                Intent intent = new Intent(getActivity(), AppointmentDetail.class);
                intent.putExtra("positions", position + "");
                context.startActivity(intent);
            }
        });
        viewDataBinding.appointmentRecyclerView.setAdapter(appointmentAdapter);

    }

    private void setRevenue(HomeResponse homeResponse) {

        ChartviewpagerAdapter chartViewPagerAdapter = new ChartviewpagerAdapter(getParentFragmentManager(), getLifecycle(), homeResponse);


        viewDataBinding.viewPager.setAdapter(chartViewPagerAdapter);
//        new TabLayoutMediator(viewDataBinding.tabLayout,viewDataBinding.viewPager,
//                new TabLayoutMediator.TabConfigurationStrategy() {
//                    @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
//                         switch (position) {
//                             case 0 :
//                                 tab.setText("Day");
//                                 break;
//                             case 1:
//                                 tab.setText("Month");
//                                 break;
//                             case 2:
//                                 tab.setText("Year");
//                                 break;
//                         }
//                    }
//                }).attach();
//        viewDataBinding.viewPager.setCurrentItem(1,  false );
    }

    private void setTopServices(List<TopServicesItem> topServices) {


        TopServiceAdapter topServiceAdapter = new TopServiceAdapter(HomeFragment.this.getContext(), topServices);
        viewDataBinding.topServicesRecycler.setAdapter(topServiceAdapter);

    }

    private void setServices(List<CategoriesItem> categories) {

        categoriesItemList = categories;

        MangeServiceAdapter mange_service_adapter = new MangeServiceAdapter(getContext(), categories, new OnClickListener() {
            @Override
            public void onItemClickListener(Context context, int position) {
                Intent intent = new Intent(context.getApplicationContext(), CategoryDetails.class);
                intent.putExtra("id", categories.get(position).getId());
                intent.putExtra("names" ,  categories.get(position).getName());
                intent.putExtra("gender",  "male" );
                intent.putExtra("services_for" , vendor_services_for );
                context.startActivity(intent);
            }
        });
        viewDataBinding.recyclerManageServices.setAdapter(mange_service_adapter);


    }

    private void SetBanner(List<BannerItem> banners) {
        Log.d("list==>>", banners.get(0).getName());
        SliderAdapter imageSliderAdapter = new SliderAdapter(this.getContext(), banners);
        viewDataBinding.imageSlider.setSliderAdapter(imageSliderAdapter);
        viewDataBinding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM);
        viewDataBinding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        viewDataBinding.imageSlider.startAutoCycle();
    }

}
