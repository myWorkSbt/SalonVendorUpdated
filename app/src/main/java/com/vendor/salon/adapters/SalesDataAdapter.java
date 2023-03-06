package com.vendor.salon.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.vendor.salon.R;
import com.vendor.salon.data_Class.sales.Data;
import com.vendor.salon.databinding.ItemSaleLayssBinding;

import java.util.List;

public class SalesDataAdapter extends RecyclerView.Adapter<SalesDataAdapter.ViewHolder>  {

    List<Data> salesDataLists ;
    Context thisContext ;
    public SalesDataAdapter(Context co, List<Data> salesDataList) {
        this.salesDataLists = salesDataList;
        this.thisContext =  co;
    }

    @NonNull
    @Override
    public SalesDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSaleLayssBinding saleLayBinding = DataBindingUtil.inflate( LayoutInflater.from(parent.getContext()),  R.layout.item_sale_layss, parent, false);

         return new ViewHolder(saleLayBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesDataAdapter.ViewHolder holder, int position) {
         Data mySingleSalesData = salesDataLists.get(position);
         if (mySingleSalesData != null ) {
              holder.saleLayBinding.amountPrice.setText( String.valueOf(mySingleSalesData.getAmount()) );
              int error_start_position = mySingleSalesData.getStartDate().indexOf("T");
          //    holder.saleLayBinding.timeHeading.setText(mySingleSalesData.getStartDate().toString().substring(0, error_start_position ) );
              holder.saleLayBinding.clientNo.setText(String.valueOf(mySingleSalesData.getNoOfClients()) );
         }
    }



    @Override
    public int getItemCount() {
        return salesDataLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemSaleLayssBinding saleLayBinding ;
        public ViewHolder(ItemSaleLayssBinding saleLaysBinding) {
            super(saleLaysBinding.getRoot());
            this.saleLayBinding = saleLaysBinding ;
        }
    }

     public  void refreshsaleList(List<Data>  salesDataLists  ) {
        this.salesDataLists = salesDataLists ;
     }
}
