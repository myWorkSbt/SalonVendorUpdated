package com.vendor.salon.data_Class.bankedit;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BankEditResponse implements Serializable {

	@SerializedName("result")
	private boolean result;

	@SerializedName("message")
	private String message;

	@SerializedName("data")
	private Data data;

	public void setResult(boolean result){
		this.result = result;
	}

	public boolean isResult(){
		return result;
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