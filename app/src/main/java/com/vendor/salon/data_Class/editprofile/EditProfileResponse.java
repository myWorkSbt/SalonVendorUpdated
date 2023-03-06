package com.vendor.salon.data_Class.editprofile;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EditProfileResponse implements Serializable {

	@SerializedName("result")
	private boolean result;

	@SerializedName("message")
	private String message;

	@SerializedName("details")
	private Details details;

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

	public void setDetails(Details details){
		this.details = details;
	}

	public Details getDetails(){
		return details;
	}
}