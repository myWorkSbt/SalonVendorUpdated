package com.vendor.salon.data_Class.removestaff;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class RemoveStaffResponse implements Serializable {

	@SerializedName("status")
	private boolean status;

	@SerializedName("messaage")
	private String messaage;

	@SerializedName("data")
	private Data data;

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}

	public void setMessaage(String messaage){
		this.messaage = messaage;
	}

	public String getMessaage(){
		return messaage;
	}

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}
}