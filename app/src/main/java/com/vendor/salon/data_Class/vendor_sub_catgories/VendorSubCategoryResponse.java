package com.vendor.salon.data_Class.vendor_sub_catgories;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class VendorSubCategoryResponse implements Serializable {

	@SerializedName("status")
	private boolean status;

	@SerializedName("message")
	private String message;

	@SerializedName("data")
	private List<DataItem> data;

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
}