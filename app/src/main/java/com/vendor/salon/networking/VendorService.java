package com.vendor.salon.networking;

import com.google.gson.JsonObject;
import com.vendor.salon.data_Class.AddServicesResponse;

import com.vendor.salon.data_Class.addclient.AddClientActivityResponse;
import com.vendor.salon.data_Class.enable_disable_packages.DisablePackageItemsResponse;
import com.vendor.salon.data_Class.LogoutResponse;
import com.vendor.salon.data_Class.StaffAppointments.StaffAppointmentsResponse;
import com.vendor.salon.data_Class.addstaff.AddStaffResponse;
import com.vendor.salon.data_Class.appointmentsfilter.AppointmentsFilterResponse;
import com.vendor.salon.data_Class.assign_staff.AssignStaffResponse;
import com.vendor.salon.data_Class.bankedit.BankEditResponse;
import com.vendor.salon.data_Class.categories.CategoriesResponse;
import com.vendor.salon.data_Class.category_services.CategoryServicesResponse;
import com.vendor.salon.data_Class.editprofile.EditProfileResponse;
import com.vendor.salon.data_Class.getProfile.GetProfileResponse;
import com.vendor.salon.data_Class.getStaff.GetStaffResponse;
import com.vendor.salon.data_Class.get_ManagePackageData.getManagePackageResponse;
import com.vendor.salon.data_Class.home.HomeResponse;
import com.vendor.salon.data_Class.inventoryCategory.InventoryCategoriessResponse;
import com.vendor.salon.data_Class.login.LoginResponse;
import com.vendor.salon.data_Class.manage_inventory.ManageInventoryResponse;
import com.vendor.salon.data_Class.manage_package.ManagePackageResponse;
import com.vendor.salon.data_Class.manage_service.ManageServiceResponse;
import com.vendor.salon.data_Class.profile.UpdateProfileResponse;
import com.vendor.salon.data_Class.removestaff.RemoveStaffResponse;
import com.vendor.salon.data_Class.sales.SalesResponse;
import com.vendor.salon.data_Class.salon_timings.SalonDisableTimeData;
import com.vendor.salon.data_Class.salon_timings.SalonTimeData;

import com.vendor.salon.data_Class.sendotp;
import com.vendor.salon.data_Class.vendor_enable_disable_service.VendorEnableDisableService;
import com.vendor.salon.data_Class.vendor_sub_catgories.VendorSubCategoryResponse;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface VendorService {

//    @Headers({ "Content-Type: application/json;charset=UTF-8"})
//    @GET("get_profile_data")
//    Call<VendorProfile> getUserProfile(@Header("Authorization") String auth);
//

    //        @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("saloon/api/send-otp")
    Call<sendotp> sendOtp(@Body HashMap<String, String> phone);

    @POST("saloon/api/login")
    Call<LoginResponse> verifyOtp(@Body HashMap<String, String> data);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("saloon/api/home")
    Call<HomeResponse> home(@Header("Authorization") String auth);


    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("/saloon/api/staff_appointments")
    Call<StaffAppointmentsResponse> getStaffDetails(@Header("Authorization") String auth,
                                                    @Query("staff_id") String staff_id);

    @Multipart
//    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("saloon/api/update-profile")
    Call<UpdateProfileResponse> updateMyProfile(
            @Header("Authorization") String auth,
            @Part("email") RequestBody email,
            @Part("name") RequestBody name,
            @Part("phone") RequestBody phone,
            @Part("country_code") RequestBody ccp,
            @Part("gender") RequestBody gender,
            @Part("dob") RequestBody dob,
            @Part("vendor_type") RequestBody vendor_type,
//            @Part("bank_name") RequestBody bank_name,
//          @Part MultipartBody.Part check_image,
            @Part("location") RequestBody location,
            @Part MultipartBody.Part user_image,
            @Part MultipartBody.Part id_proof_image,
//            @Part("account_holder_name") RequestBody account_holder_name,
            @Part MultipartBody.Part licence_image,
//            @Part("account_no") RequestBody account_no,
            @Part("service_for") RequestBody service_for
//            @Part("ifsc_code") RequestBody ifsc_code
    );


    @Multipart
    @POST("saloon/api/bank_edit")
    Call<BankEditResponse> EditBankDetails(@Header("Authorization") String auth,
                                           @Part("bank_name") RequestBody bank_name,
                                           @Part("account_no") RequestBody account_no,
                                           @Part("account_holder_name") RequestBody account_holder_name,
                                           @Part("ifsc_code") RequestBody ifsc_code,
                                           @Part MultipartBody.Part check_image
    );

    @POST("saloon/api/add_services")
    Call<AddServicesResponse> addServices(@Header("Authorization") String Auth,
                                          @Body JsonObject service_data
    );


    @Multipart
    @POST("saloon/api/edit_profile")
    Call<EditProfileResponse> AddSalonBannerImagesEditProfile(@Header("Authorization") String auth,
                                                              @Part("phone") RequestBody phone,
                                                              @Part("country_code") RequestBody country_code,
                                                              @Part MultipartBody.Part[] banner_image,
                                                              @Part("type") RequestBody type
    );

    @Multipart
    @POST("saloon/api/edit_profile")
    Call<EditProfileResponse> EditSalonDetail(@Header("Authorization") String auth,
                                              @Part("shop_name") RequestBody name,
                                              @Part("phone") RequestBody phone,
                                              @Part("address") RequestBody address,
                                              @Part("email") RequestBody email,
                                              @Part("country_code") RequestBody country_code,
                                              @Part("about") RequestBody about,
//                                                 @Part MultipartBody owner_image,
                                              @Part MultipartBody.Part id_proof_image,
                                              @Part MultipartBody.Part licence_image,
                                              @Part("type") RequestBody type
    );


    @Multipart
    @POST("saloon/api/manage_package")
    Call<ManagePackageResponse> addNewPackage(@Header("Authorization") String auths,
                                              @Part("title") RequestBody title,
                                              @Part("mrp") RequestBody mrp,
                                              @Part("offer_price") RequestBody offer_price,
                                              @Part("about") RequestBody about,
                                              @Part MultipartBody.Part image,
                                              @Part("disabled") RequestBody disabled,
                                              @Part("gender") RequestBody gender,
                                              @Part("category_id") RequestBody category_id,
                                              @Query("service_id[]") ArrayList<String> service_ids
    );


    @Multipart
    @POST("saloon/api/manage_package")
    Call<ManagePackageResponse> updatePackage(@Header("Authorization") String auths,
                                              @Part("title") RequestBody title,
                                              @Part("about") RequestBody about,
                                              @Part("mrp") RequestBody mrp,
                                              @Part("offer_price") RequestBody offer_price,
                                              @Part MultipartBody.Part image,
                                              @Part("id") RequestBody id
    );

    @Multipart
    @POST("saloon/api/edit_profile")
    Call<EditProfileResponse> EditSalonDetailwithGalleryImages(@Header("Authorization") String auth,
                                                               @Part("shop_name") RequestBody name,
                                                               @Part("phone") RequestBody phone,
                                                               @Part("address") RequestBody address,
                                                               @Part("email") RequestBody email,
                                                               @Part("country_code") RequestBody country_code,
                                                               @Part("about") RequestBody about,
//                                                 @Part MultipartBody owner_image,
                                                               @Part MultipartBody.Part id_proof_image,
                                                               @Part MultipartBody.Part licence_image,
                                                               @Part("type") RequestBody type,
                                                               @Part MultipartBody.Part[] gallery_any
    );

    @Multipart
    @POST("saloon/api/edit_profile")
    Call<EditProfileResponse> EditOwnerDetails(@Header("Authorization") String auth,
                                               @Part("name") RequestBody name,
                                               @Part("phone") RequestBody phone,
                                               @Part("dob") RequestBody dob,
                                               @Part("email") RequestBody email,
                                               @Part("gender") RequestBody gender,
                                               @Part("country_code") RequestBody country_code,
                                               @Part("designation") RequestBody designation,
                                               @Part MultipartBody.Part owner_image,
                                               @Part("type") RequestBody type
    );

    @Multipart
    @POST("saloon/api/add_staff")
    Call<AddStaffResponse> addStaffElements(@Header("Authorization") String auth,
                                            @Part("name") RequestBody name,
                                            @Part("phone") RequestBody phone,
                                            @Part("dob") RequestBody dob,
                                            @Part("gender") RequestBody gender,
                                            @Part("email") RequestBody email,
                                            @Part("country_code") RequestBody country_code,
                                            @Part MultipartBody.Part image
    );

    @POST("saloon/api/assign_staff")
    Call<AssignStaffResponse> assignStaffIns(@Header("Authorization") String auth,
                                             @Query("appointment_id") String appointment_id,
                                             @Query("staff_name") String staff_name,
                                             @Query("status") String status,
                                             @Query("staff_id") String staff_id
    );


    @GET("saloon/api/manage_timing")
    Call<SalonTimeData> getTimeSalon(@Header("Authorization") String auth,
                                     @QueryMap HashMap<String, String> hashMap);

    @POST("saloon/api/manage_timing")
    Call<SalonDisableTimeData> postSalonTime(@Header("Authorization") String auth,
                                             @Body HashMap<String, String> hashMap
    );

    @POST("saloon/api/enable_disable_package")
    Call<DisablePackageItemsResponse> changePackageActiveStatus(@Header("Authorization") String token,
                                                                @Query("id") String package_id);

    @POST("saloon/api/manage_inventory")
    Call<ManageInventoryResponse> editInventoryItems(@Header("Authorization") String auths,
                                                     @Query("inventory_category_id") String inventoryCategoryId,
                                                     @Query("inventory_id") String inventoryId,
                                                     @Query("status") String status,
                                                     @Query("item") String item,
                                                     @Query("qty") String qty);


    @POST("saloon/api/enable_disable_service")
    Call<VendorEnableDisableService> changeActiveStatus(@Header("Authorization") String auth,
                                                        @Query("id") String id
    );

    @POST("saloon/api/remove_staff")
    Call<RemoveStaffResponse> removeStaff(@Header("Authorization") String auth,
                                          @Query("staff_id") String staff_id
    );


    @Multipart
    @POST("saloon/api/add_staff")
    Call<AddStaffResponse> editStaffElements(@Header("Authorization") String auth,
                                             @Part("name") RequestBody name,
                                             @Part("phone") RequestBody phone,
                                             @Part("dob") RequestBody dob,
                                             @Part("gender") RequestBody gender,
                                             @Part("email") RequestBody email,
                                             @Part("country_code") RequestBody country_code,
                                             @Part MultipartBody.Part image,
                                             @Part("id") RequestBody staff_id
    );

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("saloon/api/category_services")
    Call<CategoryServicesResponse> getAllServicesOfCategory(@Header("Authorization") String auth,
                                                            @Query("category_id") String id,
                                                            @Query("gender") String gender
    );

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("saloon/api/get-profile")
    Call<GetProfileResponse> getVendorDetails(@Header(("Authorization")) String Auth);

    @GET("saloon/api/appointments_filter")
    Call<AppointmentsFilterResponse> getAppointmentSearchedData(@Header(("Authorization")) String Auth,
                                                                @Query("status") String status,
                                                                @Query("start_date") String start_date,
                                                                @Query("end_date") String end_date,
                                                                @Query("filter") String filter,
                                                                @Query("search") String search,
                                                                @Query("user_id") String user_id
    );


    @GET("saloon/api/logout")
    Call<LogoutResponse> LogoutVendor(@Header("Authorization") String auths);

    @GET("saloon/api/get_staff")
    Call<GetStaffResponse> getAllStaffList(@Header("Authorization") String auths,
                                           @Query("search") String search);


    @GET("/saloon/api/manage_service")
    Call<ManageServiceResponse> getSavedCategoryList(@Header("Authorization") String auth,
                                                     @Query("gender") String gender,
                                                     @Query("is_doorstep") String is_doorstep,
                                                     @Query("search") String search);


    @POST("saloon/api/manage_timing")
    Call<SalonTimeData> getSalonTime(@Header("Authorization") String auths,
                                     @Body HashMap<String, String> myDataMaps);

    @GET("/saloon/api/vendor_sub_categories")
    Call<VendorSubCategoryResponse> getServiceFilteredData(@Header("Authorization") String auth,
                                                           @Query("category_id") String category_id,
                                                           @Query("gender") String gender,
                                                           @Query("is_doorstep") String is_doorstep);

    @GET("saloon/api/manage_package")
    Call<getManagePackageResponse> getPackagesDetail(@Header("Authorization") String auths);

    @GET("saloon/api/inventory_category")
    Call<InventoryCategoriessResponse> getInventoryCategories(@Header("Authorization") String auth
    );

    @GET("saloon/api/sales")
    Call<SalesResponse> salesData(@Header("Authorization") String auths,
                                  @Query("start_date") String start_date,
                                  @Query("end_date") String end_date);

    @GET("saloon/api/categories")
    Call<CategoriesResponse> getCategories(@Header("Authorization") String auth,
                                           @Query("gender") String gender);


    @GET("saloon/api/manage_inventory")
    Call<ManageInventoryResponse> getInventories(@Header("Authorization") String auth,
                                                 @Query("inventory_category_id") String inventory_category_id);


    @POST("saloon/api/add_client")
    Call<AddClientActivityResponse> AddClient(@Header("Authorization") String auths,
                                              @Query("country_code") String country_code,
                                              @Query("phone") String phone  ,
                                              @Query("booking_time") String booking_time,
                                              @Query("specialist_id") String specialist_id,
                                              @Query("services[]") List<String> selectedServices ) ;
}
