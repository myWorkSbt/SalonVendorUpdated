package com.vendor.salon.data_Class.sales;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Data implements Serializable {

	@SerializedName("no_of_clients")
	private int noOfClients;

	@SerializedName("amount")
	private int amount;

	@SerializedName("start_date")
	private String startDate;

	@SerializedName("end_date")
	private String endDate;

	public void setNoOfClients(int noOfClients){
		this.noOfClients = noOfClients;
	}

	public int getNoOfClients(){
		return noOfClients;
	}

	public void setAmount(int amount){
		this.amount = amount;
	}

	public int getAmount(){
		return amount;
	}

	public void setStartDate(String startDate){
		this.startDate = startDate;
	}

	public String getStartDate(){
		return startDate;
	}

	public void setEndDate(String endDate){
		this.endDate = endDate;
	}

	public String getEndDate(){
		return endDate;
	}
}