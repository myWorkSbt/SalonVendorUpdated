package com.vendor.salon.data_Class.get_ManagePackageData;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ServicesListingItem implements Serializable {

	@SerializedName("id")
	private int id;

	@SerializedName("name")
	private String name;

	@SerializedName("selected")
	private String selected;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setSelected(String selected){
		this.selected = selected;
	}

	public String getSelected(){
		return selected;
	}
}