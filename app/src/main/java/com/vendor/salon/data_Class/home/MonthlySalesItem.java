package com.vendor.salon.data_Class.home;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MonthlySalesItem implements Serializable {

	@SerializedName("year")
	private int year;

	@SerializedName("month")
	private int month;

	@SerializedName("total_sales")
	private long totalSales;

	public void setYear(int year){
		this.year = year;
	}

	public int getYear(){
		return year;
	}

	public void setMonth(int month){
		this.month = month;
	}

	public int getMonth(){
		return month;
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
			"MonthlySalesItem{" + 
			"year = '" + year + '\'' + 
			",month = '" + month + '\'' + 
			",total_sales = '" + totalSales + '\'' + 
			"}";
		}
}