package com.vendor.salon.data_Class.assign_staff;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class AssignStaffResponse implements Serializable {

	@SerializedName("status")
	private boolean status;

	@SerializedName("message")
	private String message;

	@SerializedName("appointment")
	private Appointment appointment;

	@SerializedName("errors")
	private Errors errors;


	public void setStatus(boolean status){
		this.status = status;
	}


	public void setErrors(Errors errors){
		this.errors = errors;
	}

	public Errors getErrors(){
		return errors;
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

	public void setAppointment(Appointment appointment){
		this.appointment = appointment;
	}

	public Appointment getAppointment(){
		return appointment;
	}
}