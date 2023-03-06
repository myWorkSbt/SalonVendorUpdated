package com.vendor.salon.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.vendor.salon.R;
import com.vendor.salon.activity.AddServices;
import com.vendor.salon.data_Class.category_services.CategoriesItem;
import com.vendor.salon.databinding.RowAddMoreServicesBinding;
import com.vendor.salon.model.SelectedServicesListModel;

import java.util.ArrayList;
import java.util.List;

public class ListAddMoreServicesAdapter extends RecyclerView.Adapter<ListAddMoreServicesAdapter.viewHolder> {

    List<SelectedServicesListModel> selectedServiceslists = new ArrayList<>();
    Context context;
    List<CategoriesItem> servicesList;
    String[] serviceLocationList;

    public ListAddMoreServicesAdapter(AddServices context, List<CategoriesItem> servicesList, String[] serviceLocationList, List<SelectedServicesListModel> selectedServiceslists) {
        this.context = context;
        this.selectedServiceslists = selectedServiceslists;
        this.servicesList = servicesList;
        this.serviceLocationList = serviceLocationList;
    }


    @NonNull
    @Override
    public ListAddMoreServicesAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowAddMoreServicesBinding addMoreServicesBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_add_more_services, parent, false);
         return new viewHolder(addMoreServicesBinding) ;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        SelectedServicesListModel selectedServicesListMo = selectedServiceslists.get(position);
        if (selectedServicesListMo != null) {
            holder.addMoreServicesBinding.rowMrpPrice.setText(selectedServicesListMo.getMrp() + "");
            holder.addMoreServicesBinding.rowServiceLocationSpinner.setAdapter(new AddServicesServiceLocationAdapter(context, serviceLocationList));
            if (!selectedServicesListMo.is_door_step()) {
                holder.addMoreServicesBinding.rowServiceLocationSpinner.setSelection(0);
            } else {
                holder.addMoreServicesBinding.rowServiceLocationSpinner.setSelection(1);
            }
            holder.addMoreServicesBinding.rowServicesSpinner.setAdapter(new AddServicesServiceSpinnerAdapter(holder.addMoreServicesBinding.rowServicesSpinner.getContext(), servicesList));
            holder.addMoreServicesBinding.rowServicesSpinner.setSelection(selectedServicesListMo.getSelected_service_position());
            holder.addMoreServicesBinding.rowEtOfferPrice.setText(selectedServicesListMo.getPayable_amount() + "");

            holder.addMoreServicesBinding.rowMrpPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {

                }
            });
        }

    }

    @Override
    public int getItemCount() {  return selectedServiceslists.size() ;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        RowAddMoreServicesBinding addMoreServicesBinding ;
        public viewHolder(@NonNull RowAddMoreServicesBinding rowAddMoreServicesBinding) {
            super( rowAddMoreServicesBinding.getRoot() );
             this.addMoreServicesBinding =  rowAddMoreServicesBinding ;
        }
    }

}
