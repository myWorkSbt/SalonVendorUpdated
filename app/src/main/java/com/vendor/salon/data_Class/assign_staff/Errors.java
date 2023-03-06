package com.vendor.salon.data_Class.assign_staff;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Errors implements Serializable {

	@SerializedName("staff_name")
	private List<String> staffName;

	@SerializedName("staff_id")
	private List<String> staffId;

	public void setStaffName(List<String> staffName){
		this.staffName = staffName;
	}

	public List<String> getStaffName(){
		return staffName;
	}

	public void setStaffId(List<String> staffId){
		this.staffId = staffId;
	}

	public List<String> getStaffId(){
		return staffId;
	}

	@Override
 	public String toString(){
		return 
			"Errors{" +
			"staff_name = '" + staffName + '\'' + 
			",staff_id = '" + staffId + '\'' + 
			"}";
		}
}