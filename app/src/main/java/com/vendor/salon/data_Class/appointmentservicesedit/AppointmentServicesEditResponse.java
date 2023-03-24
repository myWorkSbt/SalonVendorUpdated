package com.vendor.salon.data_Class.appointmentservicesedit;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AppointmentServicesEditResponse implements Serializable {

	@SerializedName("status")
	private boolean status;

	@SerializedName("message")
	private String message;


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


}