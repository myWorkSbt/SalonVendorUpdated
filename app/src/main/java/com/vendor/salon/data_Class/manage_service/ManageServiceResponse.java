package com.vendor.salon.data_Class.manage_service;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ManageServiceResponse implements Serializable {

	@SerializedName("status")
	private boolean status;

	@SerializedName("message")
	private String message;

	@SerializedName("data")
	private List<DataItem> data;

	@SerializedName("pages")
	private Pages pages;

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setData(List<DataItem> data){
		this.data = data;
	}

	public List<DataItem> getData(){
		return data;
	}

	public void setPages(Pages pages){
		this.pages = pages;
	}

	public Pages getPages(){
		return pages;
	}
}