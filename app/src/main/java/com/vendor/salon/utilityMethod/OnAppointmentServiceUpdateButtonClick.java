package com.vendor.salon.utilityMethod;

import android.content.Context;

public interface OnAppointmentServiceUpdateButtonClick {
    void OnPlusButtonClick(Context tv_count_context , Context btn_minus_context , Context service_main_lays_context , int count , int position );

    void OnMinusButtonClick(Context tv_count_context , Context btn_minus_context , Context service_main_lays_context , int count , int position );

}
