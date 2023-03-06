package com.vendor.salon.data_Class.salon_timings

import com.google.gson.annotations.SerializedName

data class SalonDisableTimeData(
    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : DataDisable?    = DataDisable()
)
