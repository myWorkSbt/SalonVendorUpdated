package com.vendor.salon.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.vendor.salon.R;
import com.vendor.salon.data_Class.inventoryCategory.DataItem;
import com.vendor.salon.databinding.ItemOptionsListRecyclerBinding;
import com.vendor.salon.utilityMethod.OnClickListenerInterfaceKotlin;

import java.util.List;

public class InventoryOptionsItemRecyclerAdapter extends RecyclerView.Adapter<InventoryOptionsItemRecyclerAdapter.ViewHolder> {
    List<DataItem> myInventoryOptionsLst;
    int prev_selected = -1;
    OnClickListenerInterfaceKotlin onClickListener;

    public InventoryOptionsItemRecyclerAdapter(List<DataItem> myInventoryOptionsList, OnClickListenerInterfaceKotlin onClickListener) {
        this.myInventoryOptionsLst = myInventoryOptionsList;
        this.onClickListener = onClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOptionsListRecyclerBinding optionsListRecyclerBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_options_list_recycler, parent, false);

        return new ViewHolder(optionsListRecyclerBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryOptionsItemRecyclerAdapter.ViewHolder holder, int position) {
        DataItem dataItems = myInventoryOptionsLst.get(position);
        if (null != dataItems) {
            holder.optionsListRecyclerBinding.itemNames.setText(dataItems.getName());
            if (dataItems.isSelected()) {
                holder.optionsListRecyclerBinding.blueLine.setVisibility(View.VISIBLE);
            } else {
                holder.optionsListRecyclerBinding.blueLine.setVisibility(View.GONE);
            }
            holder.optionsListRecyclerBinding.itemInventoryCategory.setOnClickListener(View -> {
                myInventoryOptionsLst.get(position).setSelected(true);
                myInventoryOptionsLst.get(prev_selected).setSelected(false);
                notifyItemChanged(position);
                notifyItemChanged(prev_selected);
                prev_selected = position;
                onClickListener.onItemClicks(dataItems.getId(), position);

            });
            if (position == 0 && prev_selected == -1) {

                prev_selected = 0;
                myInventoryOptionsLst.get(0).setSelected(true);
                holder.optionsListRecyclerBinding.blueLine.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return myInventoryOptionsLst.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemOptionsListRecyclerBinding optionsListRecyclerBinding;

        public ViewHolder(@NonNull ItemOptionsListRecyclerBinding optionsListRecyclerBinding) {
            super(optionsListRecyclerBinding.getRoot());
            this.optionsListRecyclerBinding = optionsListRecyclerBinding;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshDataList(List<DataItem> myInventoryOptionsList) {
        this.myInventoryOptionsLst = myInventoryOptionsList;
        notifyDataSetChanged();
    }
}
