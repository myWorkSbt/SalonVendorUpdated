package com.vendor.salon.data_Class.home;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class YearlySalesItem implements Serializable {

	@SerializedName("year")
	private int year;

	@SerializedName("total_sales")
	private long totalSales;

	public void setYear(int year){
		this.year = year;
	}

	public int getYear(){
		return year;
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
			"YearlySalesItem{" + 
			"year = '" + year + '\'' + 
			",total_sales = '" + totalSales + '\'' + 
			"}";
		}
}