package com.vendor.salon.data_Class.salon_timings

import com.google.gson.annotations.SerializedName
data class DataDisable(
    @SerializedName("id"         ) var id        : Int?    = null,
    @SerializedName("vendor_id"  ) var vendorId  : String? = null,
    @SerializedName("day"        ) var day       : String? = null,
    @SerializedName("start_time" ) var startTime : String? = null,
    @SerializedName("end_time"   ) var endTime   : String? = null,
    @SerializedName("disabled"   ) var disabled  : String? = null,
    @SerializedName("created_at" ) var createdAt : String? = null,
    @SerializedName("updated_at" ) var updatedAt : String? = null,
    @SerializedName("place"      ) var place     : String? = null
)

