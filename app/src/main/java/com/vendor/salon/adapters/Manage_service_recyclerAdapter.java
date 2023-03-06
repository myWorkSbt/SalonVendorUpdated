package com.vendor.salon.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.vendor.salon.R;
import com.vendor.salon.activity.CategoryDetails;
import com.vendor.salon.data_Class.manage_service.DataItem;
import com.vendor.salon.databinding.ItemManageServicesRecyclerViewBinding;

import java.util.List;

public class Manage_service_recyclerAdapter extends RecyclerView.Adapter<Manage_service_recyclerAdapter.ViewHolder> {

    Context context;
    String selectedGender;
    List<DataItem> categoriesItemList;
    public Manage_service_recyclerAdapter(Context context, List<DataItem> list, String gender) {
        this.context = context;
        this.categoriesItemList = list;
        this.selectedGender = gender ;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemManageServicesRecyclerViewBinding servicesRecyclerViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_manage_services_recycler_view, parent, false);

        return new ViewHolder(servicesRecyclerViewBinding);
//        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataItem categoriesItem = categoriesItemList.get(position);
        if(categoriesItem != null ) {
            holder.itemManageServicesRecyclerViewBinding.HairCut.setText(categoriesItem.getName());
            holder.itemManageServicesRecyclerViewBinding.type.setText(categoriesItem.getCount() + " types ");
        }

        holder.itemManageServicesRecyclerViewBinding.serviceOuterLays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intents = new Intent(context.getApplicationContext(), CategoryDetails.class);
                intents.putExtra("id", categoriesItem.getId());
                intents.putExtra( "names" , categoriesItem.getName() ) ;
                intents.putExtra("gender", selectedGender );
                      context.startActivity(intents);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoriesItemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemManageServicesRecyclerViewBinding itemManageServicesRecyclerViewBinding;

        public ViewHolder(@NonNull ItemManageServicesRecyclerViewBinding itemManageServicesRecyclerViewBinding) {
            super(itemManageServicesRecyclerViewBinding.getRoot());
            this.itemManageServicesRecyclerViewBinding = itemManageServicesRecyclerViewBinding;
        }
    }
}
