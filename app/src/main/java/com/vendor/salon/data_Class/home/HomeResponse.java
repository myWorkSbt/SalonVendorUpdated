package com.vendor.salon.data_Class.home;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class HomeResponse implements Serializable {

	@SerializedName("banners")
	private List<BannerItem> banners;

	@SerializedName("categories")
	private List<CategoriesItem> categories;

	@SerializedName("sales")
	private Sales sales;

	@SerializedName("top_services")
	private List<TopServicesItem> topServices;

	@SerializedName("recent_appointment")
	private List<RecentAppointmentItem> recentAppointment;

	@SerializedName("yearly_sales")
	private List<YearlySalesItem> yearlySales;

	@SerializedName("monthly_sales")
	private List<MonthlySalesItem> monthlySales;

	@SerializedName("daily_sales")
	private List<DailySalesItem> dailySales;

	@SerializedName("status")
	private boolean status;

	public void setBanners(List<BannerItem> banners){
		this.banners = banners;
	}

	public List<BannerItem> getBanners(){
		return banners;
	}

	public void setCategories(List<CategoriesItem> categories){
		this.categories = categories;
	}

	public List<CategoriesItem> getCategories(){
		return categories;
	}

	public void setSales(Sales sales){
		this.sales = sales;
	}

	public Sales getSales(){
		return sales;
	}

	public void setTopServices(List<TopServicesItem> topServices){
		this.topServices = topServices;
	}

	public List<TopServicesItem> getTopServices(){
		return topServices;
	}

	public void setRecentAppointment(List<RecentAppointmentItem> recentAppointment){
		this.recentAppointment = recentAppointment;
	}

	public List<RecentAppointmentItem> getRecentAppointment(){
		return recentAppointment;
	}

	public void setYearlySales(List<YearlySalesItem> yearlySales){
		this.yearlySales = yearlySales;
	}

	public List<YearlySalesItem> getYearlySales(){
		return yearlySales;
	}

	public void setMonthlySales(List<MonthlySalesItem> monthlySales){
		this.monthlySales = monthlySales;
	}

	public List<MonthlySalesItem> getMonthlySales(){
		return monthlySales;
	}

	public void setDailySales(List<DailySalesItem> dailySales){
		this.dailySales = dailySales;
	}

	public List<DailySalesItem> getDailySales(){
		return dailySales;
	}

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"HomeResponse{" + 
			"banners = '" + banners + '\'' + 
			",categories = '" + categories + '\'' + 
			",sales = '" + sales + '\'' + 
			",top_services = '" + topServices + '\'' + 
			",recent_appointment = '" + recentAppointment + '\'' + 
			",yearly_sales = '" + yearlySales + '\'' + 
			",monthly_sales = '" + monthlySales + '\'' + 
			",daily_sales = '" + dailySales + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}