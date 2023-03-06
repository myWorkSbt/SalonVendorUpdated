package com.vendor.salon.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.vendor.salon.OnItemClickInterface
import com.vendor.salon.R
import com.vendor.salon.adapters.SalonTimingAdapter
import com.vendor.salon.data_Class.salon_timings.SalonData
import com.vendor.salon.data_Class.salon_timings.SalonDisableTimeData
import com.vendor.salon.data_Class.salon_timings.SalonTimeData
import com.vendor.salon.databinding.ActivitySalonTimeBinding
import com.vendor.salon.networking.RetrofitClient
import com.vendor.salon.utilityMethod.FunctionCall
import com.vendor.salon.utilityMethod.loginResponsePref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


var place = "salon"
var IsSalonSelected: Boolean = true

class SalonTime : AppCompatActivity(), OnItemClickInterface {
    private lateinit var adapterSalon: SalonTimingAdapter
    private lateinit var binding: ActivitySalonTimeBinding

    @SuppressLint("ResourceAsColor", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, com.vendor.salon.R.layout.activity_salon_time)


        adapterSalon = SalonTimingAdapter(this@SalonTime, ArrayList<SalonData>(), this@SalonTime)
        binding.itemSalonTimeRecyclerLays.adapter = adapterSalon
        "".getSelectedSalonVendor("", "", "")

        binding.DoorStep.setOnClickListener {
            if (IsSalonSelected) {
                IsSalonSelected = false
                place = "doorstep"
                binding.DoorStep.background =
                    this.resources.getDrawable(R.drawable.bg_saloonbutton);
                binding.DoorStep.setTextColor(Color.WHITE)
                binding.salon.setTextColor(R.color.blue_light_popular)
                binding.salon.background =
                    this.resources.getDrawable(R.drawable.rectangular_blue)
                "".getSelectedSalonVendor("", "", "")

            }
        }
        binding.arrowBack.setOnClickListener {
            finish()
        }

        binding.salon.setOnClickListener {
            place = "salon"
            if (!IsSalonSelected) {
                IsSalonSelected = true
                binding.salon.setTextColor(Color.WHITE)
                binding.DoorStep.setTextColor(R.color.blue_light_popular)
                binding.salon.background = resources.getDrawable(R.drawable.bg_saloonbutton)
                binding.DoorStep.background = resources.getDrawable(R.drawable.rectangular_blue)
                "".getSelectedSalonVendor("", "", "")
            }
        }
    }


    private fun String.getSelectedSalonVendor(day: String, start_time: String, end_time: String) {
        FunctionCall.showProgressDialog(this@SalonTime)
        val token: String
        token = loginResponsePref.getInstance(applicationContext).token
        var hash = HashMap<String, String>()
        hash["update"] = this
        hash["day"] = day
        hash["place"] = place
        hash["start_time"] = start_time
        hash["end_time"] = end_time

        val call: Call<SalonTimeData> =
            RetrofitClient.getVendorService().getTimeSalon("Bearer " + token, hash)

        call.enqueue(object : Callback<SalonTimeData> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<SalonTimeData>, response: Response<SalonTimeData>) {
                FunctionCall.DismissDialog(this@SalonTime)
                if (response.isSuccessful) {
                    Log.d("salonTimegone", "salondata " + response)
                    adapterSalon.refreshDataLst(ArrayList(response.body()!!.data))
                    adapterSalon.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<SalonTimeData>, t: Throwable) {
                Toast.makeText(this@SalonTime, "Not getting Response", Toast.LENGTH_SHORT).show()
                FunctionCall.DismissDialog(this@SalonTime)
            }
        })
    }


    override fun onItemClick(position: Int, day: String) {
        disableStatus(day)
    }

    private fun disableStatus(day: String) {
        FunctionCall.showProgressDialog(this@SalonTime)
        var hashMap = HashMap<String, String>()
        hashMap["update"] = "disable_status"
        hashMap["day"] = day
        hashMap["place"] = place
        val call: Call<SalonDisableTimeData> = RetrofitClient.getVendorService()
            .postSalonTime("Bearer " + loginResponsePref.getInstance(applicationContext).token,
                hashMap)
        call.enqueue(object : Callback<SalonDisableTimeData> {
            override fun onResponse(
                call: Call<SalonDisableTimeData>,
                response: Response<SalonDisableTimeData>,
            ) {
                FunctionCall.DismissDialog(this@SalonTime)

                Toast.makeText(this@SalonTime, "" + response.body()!!.message, Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onFailure(call: Call<SalonDisableTimeData>, t: Throwable) {
                Toast.makeText(this@SalonTime, "not getting Response", Toast.LENGTH_SHORT).show()
                FunctionCall.DismissDialog(this@SalonTime)
            }
        })
    }
}
