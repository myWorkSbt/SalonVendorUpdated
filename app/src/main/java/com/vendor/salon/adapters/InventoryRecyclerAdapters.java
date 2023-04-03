package com.vendor.salon.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.vendor.salon.R;
import com.vendor.salon.activity.InventoryActivity;
import com.vendor.salon.data_Class.manage_inventory.DataItem;
import com.vendor.salon.databinding.ItemInventoryListRecyclerBinding;
import com.vendor.salon.utilityMethod.OnClickListen;
import com.vendor.salon.utilityMethod.OnClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InventoryRecyclerAdapters extends RecyclerView.Adapter<InventoryRecyclerAdapters.ViewHolder> {
    List<DataItem> myInventoryList;
    OnClickListen onClickListener ;


    public InventoryRecyclerAdapters(@NonNull List<DataItem>  inventoryLists , OnClickListen onClickListener ) {
        this.myInventoryList = inventoryLists ;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public InventoryRecyclerAdapters.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemInventoryListRecyclerBinding itemInventoryListRecyclerBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_inventory_list_recycler, parent, false);

        return new ViewHolder(itemInventoryListRecyclerBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull InventoryRecyclerAdapters.ViewHolder holder, int position) {
        DataItem data = myInventoryList.get(position);
        if (data != null) {
            holder.itemInventoryListRecyclerBinding.inventoryNames.setText(String.valueOf(data.getItem()));
            holder.itemInventoryListRecyclerBinding.qtyNo.setText(String.valueOf(data.getQty()));
            if (data.getQty().equals("0") || data.getQty().contains("-")) {
                holder.itemInventoryListRecyclerBinding.inStock.setText("Out of Stock");
            } else {
                holder.itemInventoryListRecyclerBinding.inStock.setText("In - stock ");
            }

            holder.itemInventoryListRecyclerBinding.btnItemsMenus.setOnClickListener(View -> {
//                     AlertDialog menuAlertDialogBox = new AlertDialog( inventory);
                 onClickListener.OnItemClick( holder.itemInventoryListRecyclerBinding.btnItemsMenus.getContext() , position , String.valueOf(data.getId()) , data.getItem() , data.getQty() );

            });
        }

    }

    @Override
    public int getItemCount() {
        return myInventoryList.size();
    }


    public void refreshLists(List<DataItem> myInventoryList) {
        this.myInventoryList = myInventoryList;
        this.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemInventoryListRecyclerBinding itemInventoryListRecyclerBinding;

        public ViewHolder(@NonNull ItemInventoryListRecyclerBinding itemInventoryListRecyclerBinding) {
            super(itemInventoryListRecyclerBinding.getRoot());
            this.itemInventoryListRecyclerBinding = itemInventoryListRecyclerBinding;
        }
    }
}
