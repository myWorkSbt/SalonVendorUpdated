package com.vendor.salon.adapters;

import static okhttp3.RequestBody.create;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vendor.salon.R;
import com.vendor.salon.activity.ManagePackages;
import com.vendor.salon.data_Class.categories.CategoriesItem;
import com.vendor.salon.data_Class.enable_disable_packages.DisablePackageItemsResponse;
import com.vendor.salon.data_Class.get_ManagePackageData.DataItem;
import com.vendor.salon.data_Class.get_ManagePackageData.ServicesListingItem;
import com.vendor.salon.data_Class.manage_package.ManagePackageResponse;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.loginResponsePref;
import com.vendor.salon.utilityMethod.onSumitButtonClicked;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagePackagesAdapter extends RecyclerView.Adapter<ManagePackagesAdapter.viewHolder> {


    private static final String TAG = "manage_package_hit";
    List<DataItem> packagesLists;
    ManagePackages managePackages;
    onSumitButtonClicked onSumitButtonClicked;
    List<com.vendor.salon.data_Class.vendor_sub_catgories.DataItem> servicesList;
    OnPackageSelectClick onPackageSelectClick;
    ArrayList<CategoriesItem> categoriesList;

    public ManagePackagesAdapter(ManagePackages managePackages, List<DataItem> dataLists, ArrayList<CategoriesItem> categoriesList, ArrayList<com.vendor.salon.data_Class.vendor_sub_catgories.DataItem> servicesList, onSumitButtonClicked onSumitButtonClicked, OnPackageSelectClick onPackageSelectClick) {
        this.managePackages = managePackages;
        this.categoriesList = categoriesList;
        this.onPackageSelectClick = onPackageSelectClick;
        this.servicesList = servicesList;
        this.onSumitButtonClicked = onSumitButtonClicked;
        packagesLists = dataLists;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      /*  packageLaysBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_package_lays, parent, false);
        return new viewHolder(packageLaysBinding);*/
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_package_lays, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        final DataItem packageOne = packagesLists.get(position);

        if (packageOne != null) {


            holder.serviceName.setText(packageOne.getTitle());
            holder.service_price.setText(packageOne.getMrp());
            Glide.with(holder.item_image.getContext()).load(Uri.parse(packageOne.getImage() + "")).error(R.drawable.no_image_rectangle).into(holder.item_image);
            holder.et_mrp_price.setText(packageOne.getMrp());
            holder.et_package_name.setText(packageOne.getTitle());
//            holder.et_about_package.setText(packageOne.getAbout());
            holder.et_offer_price.setText(packageOne.getOfferPrice());
            if (packageOne.getImage() != null) {
                Glide.with(holder.edit_package_image.getContext()).load(Uri.parse(packageOne.getImage().toString())).into(holder.item_image);
            }
            categoriesList = new ArrayList<>();
            CategoriesItem categoryOne = new CategoriesItem();

            if (packageOne.getCategoryId() != null ) {
                categoryOne.setId(Integer.parseInt(String.valueOf(packageOne.getCategoryId())));
            }
            else {
                categoryOne.setId(-1);
            }
            categoryOne.setName(packageOne.getCategoryName());
            categoriesList.add(categoryOne);
            holder.categorySpinner.setAdapter(new AddServicesCategorySpinnerAdapter(holder.categorySpinner.getContext(), categoriesList));
            holder.categorySpinner.setSelection(0);
            if (packageOne.getGender().toLowerCase().equals("male")) {
                holder.male_btn_lay.setChecked(true);
            }
            else {
                holder.female_btn.setChecked(true);
            }
            if (packageOne.getIsDoorstep().equals("2")) {
                holder.doorstep_btn.setChecked(true);
            }
            else {
                holder.salon_btn.setChecked(true);
            }

            holder.gender_lay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    onPackageSelectClick.onGenderSwitchClicked(position, i);
                }
            });
            holder.service_at_lay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    onPackageSelectClick.onServicesAtClicked(position, i);
                }
            });
            for (ServicesListingItem servicesListingItem : packageOne.getServicesListing()) {
                com.vendor.salon.data_Class.vendor_sub_catgories.DataItem dataItm = new com.vendor.salon.data_Class.vendor_sub_catgories.DataItem();
                dataItm.setServiceName(servicesListingItem.getName());
                dataItm.setServiceId(String.valueOf(servicesListingItem.getId()));
                if (servicesListingItem.getSelected().toString().equals("0")) {
                    dataItm.setSelected(false);

                } else {
                    dataItm.setSelected(true);
                }

                servicesList.add(dataItm);
            }
            holder.servicesSpinner.setAdapter(new AddPackageServicesAdapter(holder.servicesSpinner.getContext(), servicesList));
            holder.servicesSpinner.setSelection(0);

            if (packageOne.getDisabled().equals("2")) {
                holder.item_package_full_lay.setAlpha(0.7f);
                holder.active_btn_lay.setImageResource(R.drawable.in_active);
            } else {
                holder.item_package_full_lay.setAlpha(1f);
                holder.active_btn_lay.setImageResource(R.drawable.active);
            }

        }

        holder.btn_edit.setOnClickListener(View -> {
            holder.edit_package_image.setVisibility(android.view.View.VISIBLE);
            holder.serviceName.setVisibility(android.view.View.GONE);

            holder.service_price.setVisibility(android.view.View.GONE);
            holder.package_ExtendedHiddenlays.setVisibility(android.view.View.VISIBLE);
            holder.btn_edit.setVisibility(android.view.View.GONE);
            holder.edit_package_name_lays.setVisibility(android.view.View.VISIBLE);
        });


        holder.edit_package_image.setOnClickListener(View -> onSumitButtonClicked.onImageViewClick(position));

//
//        holder.btn_edit_name.setOnClickListener( View  -> {
//                holder.et_package_name.setEnabled(true);
//                holder.et_package_name.requestFocus();
//        });


        holder.btn_itm_apply.setOnClickListener(View -> {
            String packageName = Objects.requireNonNull(holder.et_package_name.getText()).toString();
            String mrp_vals = Objects.requireNonNull(holder.et_mrp_price.getText()).toString();
            String about_package_val = Objects.requireNonNull(holder.et_about_package.getText()).toString();
            onSumitButtonClicked.onBtnClicked(holder.btn_itm_apply, position);

            if (packageName.isEmpty()) {
                holder.et_package_name.requestFocus();
                holder.et_package_name.setError("Mandatory Field.");
            } else if (mrp_vals.isEmpty()) {
                holder.et_mrp_price.setError("Mandatory Field. ");
                holder.et_mrp_price.requestFocus();
            } else if (about_package_val.isEmpty()) {
                holder.et_about_package.setError("Mandatory Field. ");
                holder.et_about_package.requestFocus();
            } else {
                String token = "Bearer " + loginResponsePref.getInstance(managePackages).getToken();
                assert packageOne != null;
                File ItemImgFile = new File(String.valueOf(packageOne.getImage()));
                updatePackageItemDatas(token, packageName, about_package_val, mrp_vals, Objects.requireNonNull(holder.et_offer_price.getText()).toString(), ItemImgFile, packageOne.getId() + "", position);

            }
        });


//
        holder.active_btn_lay.setOnClickListener(View -> {
                    Log.d("kjshj", "click");
                    assert packageOne != null;
                    updateActiveStatus(position, packageOne.getId() + "");
                    if (packageOne.getDisabled().equals("2")) {
                        packageOne.setDisabled("1");
                    } else {
                        packageOne.setDisabled("2");
                    }


              /*  if(packageOne.isActive()) {
                    packageOne.setActive(false);
                     holder.active_btn_lay.setBackgroundDrawable(managePackages.getDrawable(R.drawable.active));
                    holder.btn_edit_name.setEnabled(true);
                    holder.item_package_full_lay.setAlpha(1f);
                } else {
                    packageOne.setActive(true);
                    holder.active_btn_lay.setEnabled(false);
                    holder.item_package_full_lay.setAlpha(0.7f);
                    holder.active_btn_lay.setBackgroundDrawable(managePackages.getDrawable(R.drawable.in_active));
                }
                notifyDataSetChanged();*/
                }
        );
//
//        packageLaysBinding.btnEditPackages.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                packageLaysBinding.etAboutPackage.setEnabled(true);
//            }
//        });

        holder.btn_edit_packages.setOnClickListener(View -> {
            if (Objects.requireNonNull(holder.et_about_package.getText()).toString().isEmpty()) {
                holder.et_about_package.setHint(" About Package ...");
            }
            holder.et_about_package.setEnabled(true);
            holder.et_about_package.requestFocus();

        });

    }

    private void updateActiveStatus(int positions, String package_id) {
        FunctionCall.showProgressDialog(managePackages);
        String token = "Bearer " + loginResponsePref.getInstance(managePackages).getToken();
        Call<DisablePackageItemsResponse> call = RetrofitClient.getVendorService().changePackageActiveStatus(token, package_id);
        call.enqueue(new Callback<DisablePackageItemsResponse>() {
            @Override
            public void onResponse(@NonNull Call<DisablePackageItemsResponse> call, @NonNull Response<DisablePackageItemsResponse> response) {
                FunctionCall.DismissDialog(managePackages);
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    notifyItemChanged(positions);

                } else {
                    if (response.body() != null) {
                        Toast.makeText(managePackages, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Log.d(TAG, "onResponse: " + response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<DisablePackageItemsResponse> call, @NonNull Throwable t) {
                FunctionCall.DismissDialog(managePackages);
                Log.d(TAG, "onResponse: " + t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return packagesLists.size();
    }

    private void updatePackageItemDatas(String token, String name, String about, String mrp, String offer_price, File packageImage, String package_id, int position) {
//        FunctionCall.showProgressDialog(managePackages);
        RequestBody package_image_body;
        MultipartBody.Part package_image_parts = null;
        if (packageImage != null) {
            package_image_body = RequestBody.create(MediaType.parse("image"), packageImage);
            package_image_parts = MultipartBody.Part.createFormData("image", packageImage.getName(), package_image_body);
        }
        Call<ManagePackageResponse> call = RetrofitClient.getVendorService().updatePackage(token,
                create(name, MediaType.parse("text/plain")),
                create(about, MediaType.parse("text/plain")),
                create(mrp, MediaType.parse("text/plain")),
                create(offer_price, MediaType.parse("text/plain")),
                package_image_parts,
                create(package_id, MediaType.parse("text/plain"))
        );
        call.enqueue(new Callback<ManagePackageResponse>() {
            @Override
            public void onResponse(@NonNull Call<ManagePackageResponse> call, @NonNull Response<ManagePackageResponse> response) {

                Log.d(TAG, "onResponse: " + response.body());

                if (response.body() != null) {
                    Toast.makeText(managePackages, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    if (response.isSuccessful() && response.body().isStatus() && response.body().getData() != null) {

                        notifyItemChanged(position);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ManagePackageResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void setImageTotheViews(File update_item_image_file, int item_positions) {

        packagesLists.get(item_positions).setImage(update_item_image_file.getAbsolutePath());
//        notifyItemChanged(item_positions);
    }


    public static class viewHolder extends RecyclerView.ViewHolder {
        private final AppCompatSpinner categorySpinner, servicesSpinner;
        AppCompatTextView service_price, serviceName;
        AppCompatEditText et_package_name, et_mrp_price, et_offer_price, et_about_package;
        AppCompatImageView item_image, btn_edit, btn_edit_packages, active_btn_lay;
        CardView edit_package_image;
        RadioButton male_btn_lay, female_btn, salon_btn, doorstep_btn;
        RadioGroup gender_lay, service_at_lay;
        ConstraintLayout edit_package_name_lays, package_ExtendedHiddenlays, item_package_full_lay;
        AppCompatButton btn_itm_apply;

        public viewHolder(@NonNull View view) {
            super(view);

            service_price = view.findViewById(R.id.service_price);
            serviceName = view.findViewById(R.id.service_name);
            et_package_name = view.findViewById(R.id.et_package_name);
            et_mrp_price = view.findViewById(R.id.et_mrp_price);
            et_offer_price = view.findViewById(R.id.et_offer_price);
            et_about_package = view.findViewById(R.id.et_about_package);
            item_image = view.findViewById(R.id.item_image);
            btn_edit = view.findViewById(R.id.btn_edit);
            btn_edit_packages = view.findViewById(R.id.btn_edit_packages);
            edit_package_image = view.findViewById(R.id.edit_package_image);
            edit_package_name_lays = view.findViewById(R.id.edit_package_name_lays);
            package_ExtendedHiddenlays = view.findViewById(R.id.package_extended_hidden_lays);
            btn_itm_apply = view.findViewById(R.id.btn_item_apply);
            item_package_full_lay = view.findViewById(R.id.item_package_full_lays);
            service_at_lay = view.findViewById(R.id.service_at_vals);
            male_btn_lay = view.findViewById(R.id.male);
            salon_btn = view.findViewById(R.id.salon_val);
            female_btn = view.findViewById(R.id.female);
            doorstep_btn = view.findViewById(R.id.doorstep_val);
            gender_lay = view.findViewById(R.id.gender_val);
            categorySpinner = view.findViewById(R.id.row_category_spinner);
            servicesSpinner = view.findViewById(R.id.row_services_spinner);
            active_btn_lay = view.findViewById(R.id.active_btn_lay);
        }


    }

    public void refreshData(List<DataItem> dataLists) {
        this.packagesLists = dataLists;
    }


    public interface OnPackageSelectClick {
        void onGenderSwitchClicked(int position, int genderPosition);

        void onServicesAtClicked(int position, int serviceAt);
    }


}
