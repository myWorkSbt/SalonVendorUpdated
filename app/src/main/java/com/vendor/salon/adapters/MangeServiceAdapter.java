package com.vendor.salon.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vendor.salon.R;
import com.vendor.salon.data_Class.home.CategoriesItem;
import com.vendor.salon.databinding.ManageServicesItemsBinding;
import com.vendor.salon.utilityMethod.OnClickListener;

import java.util.List;

public class MangeServiceAdapter extends RecyclerView.Adapter<MangeServiceAdapter.ViewHolder> {

    private Context context;
    private List<CategoriesItem>categoryList;
    public OnClickListener clickListener;
    public MangeServiceAdapter(Context context, List<CategoriesItem> categoryList, OnClickListener clickListener) {
        this.context = context;
        this.categoryList = categoryList ;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ManageServicesItemsBinding itemsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.manage_services_items, parent, false);
        return new ViewHolder(itemsBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CategoriesItem category  = categoryList.get(position);
        Uri icon = Uri.parse(category.getImage());
        String text = (category.getName());
        Glide.with(holder.servicesItemsBinding.kaichi.getContext()).load(icon).circleCrop().placeholder(R.drawable.image).into(holder.servicesItemsBinding.kaichi);
        holder.servicesItemsBinding.kaichiText.setText(text.toString());
        int itempositions = position;
        holder.servicesItemsBinding.categoryItemsLays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClickListener(view.getContext(),itempositions);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ManageServicesItemsBinding servicesItemsBinding;

        public ViewHolder(ManageServicesItemsBinding servicesItemsBinding) {
            super(servicesItemsBinding.getRoot());
            this.servicesItemsBinding = servicesItemsBinding ;
        }
    }

}
