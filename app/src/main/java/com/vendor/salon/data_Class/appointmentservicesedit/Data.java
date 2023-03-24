package com.vendor.salon.data_Class.appointmentservicesedit;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Data implements Serializable {

	@SerializedName("appointment_id")
	private String appointmentId;

	@SerializedName("service_id")
	private String serviceId;

	@SerializedName("qty")
	private String qty;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	public void setAppointmentId(String appointmentId){
		this.appointmentId = appointmentId;
	}

	public String getAppointmentId(){
		return appointmentId;
	}

	public void setServiceId(String serviceId){
		this.serviceId = serviceId;
	}

	public String getServiceId(){
		return serviceId;
	}

	public void setQty(String qty){
		this.qty = qty;
	}

	public String getQty(){
		return qty;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}
}