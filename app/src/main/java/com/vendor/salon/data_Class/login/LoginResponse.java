package com.vendor.salon.data_Class.login;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginResponse implements Serializable {

	@SerializedName("status")
	private boolean status;

	@SerializedName("token")
	private String token;

	@SerializedName("message")
	private String message;

	@SerializedName("agent")
	private Agent agent;

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setAgent(Agent agent){
		this.agent = agent;
	}

	public Agent getAgent(){
		return agent;
	}
}