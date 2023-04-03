package com.vendor.salon.data_Class.appointmentsfilter;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class AppointmentsFilterResponse implements Serializable {

	@SerializedName("status")
	private boolean status;

	@SerializedName("appointments")
	private List<AppointmentsItem> appointments;

	@SerializedName("pages")
	private Pages pages;

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}

	public void setAppointments(List<AppointmentsItem> appointments){
		this.appointments = appointments;
	}

	public List<AppointmentsItem> getAppointments(){
		return appointments;
	}

	public void setPages(Pages pages){
		this.pages = pages;
	}

	public Pages getPages(){
		return pages;
	}
}