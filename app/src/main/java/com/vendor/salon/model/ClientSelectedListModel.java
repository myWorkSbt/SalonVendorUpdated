package com.vendor.salon.model;


import com.vendor.salon.data_Class.category_services.CategoriesItem;

import java.util.ArrayList;

public class ClientSelectedListModel {
    int selected_category_position;
    int selected_services_position;
    private boolean selected = false ;
    private ArrayList<CategoriesItem> servicesList ;


    public ClientSelectedListModel(int selected_category_position, int selected_services_position, ArrayList<CategoriesItem> servicesList) {
        this.selected_category_position = selected_category_position;
        this.selected_services_position = selected_services_position;
        this.servicesList = servicesList;
    }



    public int getSelected_category_position() {
        return selected_category_position;
    }

    public void setSelected_category_position(int selected_category_position) {
        this.selected_category_position = selected_category_position;
    }

    public int getSelected_services_position() {
        return selected_services_position;
    }

    public ArrayList<CategoriesItem> getServicesList() {
        return servicesList;
    }

    public void setServicesList(ArrayList<CategoriesItem> servicesList) {
        this.servicesList = servicesList;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setSelected_services_position(int selected_services_position) {
        this.selected_services_position = selected_services_position;
    }
}
