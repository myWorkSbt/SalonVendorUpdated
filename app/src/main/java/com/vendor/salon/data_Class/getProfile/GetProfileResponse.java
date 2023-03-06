package com.vendor.salon.data_Class.getProfile;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetProfileResponse implements Serializable {

	@SerializedName("status")
	private boolean status;

	@SerializedName("message")
	private String message;

	@SerializedName("owner_detail")
	private OwnerDetail ownerDetail;


	@SerializedName("salon_detail")
	private SalonDetail salonDetail;

	@SerializedName("bank_detail")
	private BankDetail bankDetail;


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

	public void setOwnerDetail(OwnerDetail ownerDetail){
		this.ownerDetail = ownerDetail;
	}

	public OwnerDetail getOwnerDetail(){
		return ownerDetail;
	}

	public void setSalonDetail(SalonDetail salonDetail){ this.salonDetail = salonDetail; }

	public SalonDetail getSalonDetail(){
		return salonDetail;
	}

	public void setBankDetail(BankDetail bankDetail){
		this.bankDetail = bankDetail;
	}


	public BankDetail getBankDetail(){
		return bankDetail;
	}

	@Override
 	public String toString(){
		return 
			"GetProfileResponse{" + 
			"status = '" + status + '\'' + 
			",message = '" + message + '\'' + 
			",owner_detail = '" + ownerDetail + '\'' + 
			",salon_detail = '" + salonDetail + '\'' + 
			",bank_detail = '" + bankDetail + '\'' + 
			"}";
		}
}