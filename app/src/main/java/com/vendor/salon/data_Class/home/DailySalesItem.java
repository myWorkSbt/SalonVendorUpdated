package com.vendor.salon.data_Class.home;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DailySalesItem implements Serializable {

	@SerializedName("date")
	private String date;

	@SerializedName("total_sales")
	private long totalSales;

	public void setDate(String date){
		this.date = date;
	}

	public String getDate() {
		return date ;
//		return Integer.parseInt(date.split("-")[date.split("-").length-1]);
	}

	public void setTotalSales(long totalSales){
		this.totalSales = totalSales;
	}

	public long getTotalSales(){
		return totalSales;
	}

	@Override
 	public String toString(){
		return 
			"DailySalesItem{" + 
			"date = '" + date + '\'' + 
			",total_sales = '" + totalSales + '\'' + 
			"}";
		}
}