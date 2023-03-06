package com.vendor.salon.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.vendor.salon.R;
import com.vendor.salon.data_Class.addstaff.Data;
import com.vendor.salon.data_Class.inventoryCategory.DataItem;
import com.vendor.salon.databinding.ItemOptionsListRecyclerBinding;
import com.vendor.salon.utilityMethod.OnClickListenerInterfaceKotlin;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InventoryOptionsItemRecyclerAdapter  extends RecyclerView.Adapter<InventoryOptionsItemRecyclerAdapter.ViewHolder> {
    List<DataItem> myInventoryOptionsLst ;
    OnClickListenerInterfaceKotlin onClickListener ;
    public InventoryOptionsItemRecyclerAdapter(List<DataItem> myInventoryOptionsList, OnClickListenerInterfaceKotlin onClickListener) {
        this.myInventoryOptionsLst = myInventoryOptionsList ;
        this.onClickListener = onClickListener ;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOptionsListRecyclerBinding optionsListRecyclerBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_options_list_recycler , parent , false ) ;

        return  new ViewHolder(optionsListRecyclerBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryOptionsItemRecyclerAdapter.ViewHolder holder, int position) {
        DataItem dataItems = myInventoryOptionsLst.get(position);
        if (dataItems != null ) {
            holder.optionsListRecyclerBinding.itemNames.setText(dataItems.getName());
             holder.optionsListRecyclerBinding.itemInventoryCategory.setOnClickListener(View -> {
                 holder.optionsListRecyclerBinding.blueLine.setVisibility(View.VISIBLE);
                 onClickListener.onItemClick( dataItems.getId());
             });
        }
        if (position == 0 ) {
             holder.optionsListRecyclerBinding.blueLine.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return myInventoryOptionsLst.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemOptionsListRecyclerBinding optionsListRecyclerBinding ;
        public ViewHolder(@NonNull ItemOptionsListRecyclerBinding optionsListRecyclerBinding) {
            super(optionsListRecyclerBinding.getRoot());
            this.optionsListRecyclerBinding = optionsListRecyclerBinding ;
        }
    }

    public void refreshDataList (List<DataItem> myInventoryOptionsList ) {
        this.myInventoryOptionsLst = myInventoryOptionsList ;
    }
}
