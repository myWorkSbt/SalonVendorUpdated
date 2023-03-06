package com.vendor.salon.model;

public class SelectedServicesListModel {
    String mrp,  payable_amount, category_id , gender ;
    int selected_service_position ;
    boolean is_door_step;


    public SelectedServicesListModel(String mrp_price, String offer_price, String category_id, boolean is_DoorStep, int selected_services_position, String gender ) {
        this.mrp = mrp_price;
        this.payable_amount = offer_price;
        this.selected_service_position = selected_services_position ;
        this.category_id = category_id;
        this.is_door_step = is_DoorStep ;
        this.gender = gender;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getPayable_amount() {
        return payable_amount;
    }

    public void setPayable_amount(String payable_amount) {
        this.payable_amount = payable_amount;
    }

    public int getSelected_service_position() {
        return selected_service_position;
    }

    public void setSelected_service_position(int selected_service_position) {
        this.selected_service_position = selected_service_position;
    }

    public boolean is_door_step() {
        return is_door_step;
    }

    public void setIs_door_step(boolean is_door_step) {
        this.is_door_step = is_door_step;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
