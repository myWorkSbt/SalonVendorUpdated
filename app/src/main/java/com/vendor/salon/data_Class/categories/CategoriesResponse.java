package com.vendor.salon.data_Class.categories;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CategoriesResponse implements Serializable {

	@SerializedName("result")
	private boolean result;

	@SerializedName("message")
	private String message;

	@SerializedName("categories")
	private List<CategoriesItem> categories;

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

	public void setCategories(List<CategoriesItem> categories){
		this.categories = categories;
	}

	public List<CategoriesItem> getCategories(){
		return categories;
	}
}