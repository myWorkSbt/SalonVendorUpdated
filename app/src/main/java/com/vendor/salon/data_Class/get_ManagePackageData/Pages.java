package com.vendor.salon.data_Class.get_ManagePackageData;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Pages implements Serializable {

	@SerializedName("path")
	private String path;

	@SerializedName("prev_page_url")
	private Object prevPageUrl;

	@SerializedName("current_page")
	private int currentPage;

	@SerializedName("next_page_url")
	private Object nextPageUrl;

	@SerializedName("last_page")
	private int lastPage;

	public void setPath(String path){
		this.path = path;
	}

	public String getPath(){
		return path;
	}

	public void setPrevPageUrl(Object prevPageUrl){
		this.prevPageUrl = prevPageUrl;
	}

	public Object getPrevPageUrl(){
		return prevPageUrl;
	}

	public void setCurrentPage(int currentPage){
		this.currentPage = currentPage;
	}

	public int getCurrentPage(){
		return currentPage;
	}

	public void setNextPageUrl(Object nextPageUrl){
		this.nextPageUrl = nextPageUrl;
	}

	public Object getNextPageUrl(){
		return nextPageUrl;
	}

	public void setLastPage(int lastPage){
		this.lastPage = lastPage;
	}

	public int getLastPage(){
		return lastPage;
	}
}