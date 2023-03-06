package com.vendor.salon.data_Class.addclient;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class AddClientActivityResponse implements Serializable {

	@SerializedName("status")
	private boolean status;

	@SerializedName("message")
	private String message;

	@SerializedName("data")
	private Data data;

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

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}
}