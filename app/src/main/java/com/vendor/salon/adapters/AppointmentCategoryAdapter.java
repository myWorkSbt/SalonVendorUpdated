package com.vendor.salon.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.vendor.salon.R;
import com.vendor.salon.data_Class.appointment_categories.DataItem;
import com.vendor.salon.databinding.ItemAppointmentCategoryLaysBinding;
import com.vendor.salon.databinding.ItemAppointmentCategoryLaysBinding;
import com.vendor.salon.databinding.ItemAppointmentCategoryLaysBinding;
import com.vendor.salon.utilityMethod.OnClickListener;

import java.util.List;

public class AppointmentCategoryAdapter extends RecyclerView.Adapter<AppointmentCategoryAdapter.ViewHolders> {

    Context context;
    String selectedGender;
    OnClickListener onClickListener ;
    List<DataItem> categoriesItemList;
    public AppointmentCategoryAdapter(Context context, List<DataItem> list, OnClickListener onClickListener) {
        this.context = context;
        this.categoriesItemList = list;
        this.onClickListener = onClickListener ;
    }


    @NonNull
    @Override
    public AppointmentCategoryAdapter.ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAppointmentCategoryLaysBinding itemAppointmentCategoryLaysBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_appointment_category_lays, parent, false);

        return new ViewHolders(itemAppointmentCategoryLaysBinding);
//        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentCategoryAdapter.ViewHolders holder, int position) {
        DataItem categoriesItem = categoriesItemList.get(position);
        if (categoriesItem != null) {
            holder.ItemAppointmentCategoryLaysBinding.name.setText(categoriesItem.getName());
                 if (categoriesItem.getSelected().equals("1") ) {
                        holder.ItemAppointmentCategoryLaysBinding.types.setText(categoriesItem.getServiceName());
                     holder.ItemAppointmentCategoryLaysBinding.tvTotalPrices.setText(String.valueOf(categoriesItem.getSum()));
                     holder.ItemAppointmentCategoryLaysBinding.tvTotalPrices.setVisibility(View.VISIBLE);
                 }
                 else {
                     holder.ItemAppointmentCategoryLaysBinding.tvTotalPrices.setVisibility(View.GONE);
                     holder.ItemAppointmentCategoryLaysBinding.types.setText(String.format("%s  types", categoriesItem.getCount()));
                 }
            }
            else {
                holder.ItemAppointmentCategoryLaysBinding.tvTotalPrices.setVisibility(View.GONE);
                holder.ItemAppointmentCategoryLaysBinding.types.setVisibility(View.VISIBLE);
        }

            holder.ItemAppointmentCategoryLaysBinding.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onItemClickListener(holder.ItemAppointmentCategoryLaysBinding.cardView.getContext(), position );
                }
            });
        }


        @Override
        public int getItemCount () {
            return categoriesItemList.size();
        }

        @SuppressLint("NotifyDataSetChanged")
        public void refreshList (List < DataItem > categories) {
            this.categoriesItemList = categories;
            notifyDataSetChanged();
        }

        static class ViewHolders extends RecyclerView.ViewHolder {
            private final ItemAppointmentCategoryLaysBinding ItemAppointmentCategoryLaysBinding;

            public ViewHolders(@NonNull ItemAppointmentCategoryLaysBinding ItemAppointmentCategoryLaysBinding) {
                super(ItemAppointmentCategoryLaysBinding.getRoot());
                this.ItemAppointmentCategoryLaysBinding = ItemAppointmentCategoryLaysBinding;
            }
        }


        @SuppressLint("NotifyDataSetChanged")
        public void addItems (List < DataItem > data) {
            categoriesItemList.addAll(data);
            notifyDataSetChanged();
        }

}
