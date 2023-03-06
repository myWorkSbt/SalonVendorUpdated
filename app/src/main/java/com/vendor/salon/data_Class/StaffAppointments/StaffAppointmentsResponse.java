package com.vendor.salon.data_Class.StaffAppointments;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class StaffAppointmentsResponse implements Serializable {

	@SerializedName("status")
	private boolean status;

	@SerializedName("message")
	private String message;

	@SerializedName("staff")
	private Staff staff;

	@SerializedName("appointments")
	private List<AppointmentsItem> appointments;

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

	public void setStaff(Staff staff){
		this.staff = staff;
	}

	public Staff getStaff(){
		return staff;
	}

	public void setAppointments(List<AppointmentsItem> appointments){
		this.appointments = appointments;
	}

	public List<AppointmentsItem> getAppointments(){
		return appointments;
	}
}