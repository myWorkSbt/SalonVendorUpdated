package com.vendor.salon.adapters

import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.vendor.salon.OnItemClickInterface
import com.vendor.salon.R
import com.vendor.salon.activity.place
import com.vendor.salon.data_Class.salon_timings.SalonData
import com.vendor.salon.data_Class.salon_timings.SalonDisableTimeData
import com.vendor.salon.databinding.ItemSalonTimeRecyclerBinding
import com.vendor.salon.networking.RetrofitClient
import com.vendor.salon.utilityMethod.FunctionCall
import com.vendor.salon.utilityMethod.loginResponsePref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

var weekDayOfMonth = ""
class SalonTimingAdapter(
    var context: Context,
    var list: List<SalonData>,
    val callback: OnItemClickInterface,
) : RecyclerView.Adapter<SalonTimingAdapter.MyViewHolder>() {
    inner class MyViewHolder(var binding : ItemSalonTimeRecyclerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding : ItemSalonTimeRecyclerBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_salon_time_recycler,parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var listSalon : SalonData = list[position]
        holder.binding.weekDay.text= listSalon.day.toString()
        holder.binding.endTime.text = listSalon.endTime.toString()
        holder.binding.startTime.text =  listSalon.startTime.toString()
        holder.binding.switchRoomAvailability.setOnClickListener {
            callback.onItemClick(position , listSalon.day!! )
             if (listSalon.disabled!!.equals("2")) {
                 listSalon.disabled = "1"
             }
            else {
                listSalon.disabled = "2"
             }
            notifyItemChanged( position)
        }

        holder.binding.endTime.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val c = Calendar.getInstance()
            val hour = c[Calendar.HOUR_OF_DAY]
            val minute = c[Calendar.MINUTE]

            mTimePicker = TimePickerDialog(context,
                { timePicker, selectedHour, selectedMinute ->
                    val time = "$selectedHour:$selectedMinute"
                    val fmt = SimpleDateFormat("HH:mm")
                    var date: Date? = null
                    try {
                        date = fmt.parse(time)
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                    val fmtOut = SimpleDateFormat("hh:mm aa")
                    val formattedTime: String = fmtOut.format(date)
                    holder.binding.endTime.setText(formattedTime)
                    weekDayOfMonth=  holder.binding.weekDay.text.toString()
                    getRecentTime(holder.binding)

                }, hour, minute, false
            ) //No 24 hour time
            mTimePicker.setTitle("Select Time")
            mTimePicker.show()
        }

//        val context = holder.binding.startTime.context
        holder.binding.startTime.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val c = Calendar.getInstance()
            val hour = c[Calendar.HOUR_OF_DAY]
            val minute = c[Calendar.MINUTE]


            mTimePicker = TimePickerDialog(context,
                { timePicker, selectedHour, selectedMinute ->
                    val time = "$selectedHour:$selectedMinute"
                    val fmt = SimpleDateFormat("HH:mm")
                    var date: Date? = null
                    try {
                        date = fmt.parse(time)
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                    val fmtOut = SimpleDateFormat("hh:mm aa")
                    val formattedTime: String = fmtOut.format(date)
                    holder.binding.startTime.setText(formattedTime).toString()
                    weekDayOfMonth=  holder.binding.weekDay.text.toString()
                    getRecentTime(holder.binding)
                }, hour, minute, false
            ) //No 24 hour time
            mTimePicker.setTitle("Select Time")
            mTimePicker.show()
        }

        holder.binding.startTimeArwUp.setOnClickListener {
            changeAmP(holder.binding.startTime , holder.binding) ;
        }

        holder.binding.endTmeArrUp.setOnClickListener {
            changeAmP(holder.binding.endTime, holder.binding) ;
        }
        holder.binding.startArrowDown.setOnClickListener {
            changeAmP(holder.binding.startTime, holder.binding) ;
        }
        holder.binding.endTmeArrowDown.setOnClickListener {
            changeAmP(holder.binding.endTime, holder.binding) ;
        }




        if (listSalon.disabled.equals("2")) {
            holder.binding.salonTimeItemsLay.alpha = 0.7f
             holder.binding.switchRoomAvailability.setImageResource( R.drawable.in_active)
        }
         else {
             holder.binding.salonTimeItemsLay.alpha = 1f
            holder.binding.switchRoomAvailability.setImageResource( R.drawable.active)
        }

    }

    private fun changeAmP(time_text: TextView, binding: ItemSalonTimeRecyclerBinding) {
        var starttime = time_text.text.toString()
        if (starttime.substring(starttime.length-2, starttime.length).equals("AM")) {
            var new_starttime = starttime.substring(0 , starttime.length-2)  + "PM"
            time_text.setText(new_starttime )
            getRecentTime(binding)
        }
        else {
            var new_starttime = starttime.substring(0 , starttime.length-2)  + "AM"
            time_text.setText(new_starttime )
            getRecentTime(binding)
        }

    }

    private fun getRecentTime(binding: ItemSalonTimeRecyclerBinding) {
        val token: String
        FunctionCall.showProgressDialog(context)
        token = loginResponsePref.getInstance(context).token
        var hashMap = HashMap<String,String> ()
        hashMap["update"] = "timing"
        hashMap["day"] = weekDayOfMonth.toString()
        hashMap["place"] = place.toString()
        hashMap["start_time"] = binding.startTime.text.toString()
        hashMap["end_time"] = binding.endTime.text.toString()
        val call: Call<SalonDisableTimeData>? =
            RetrofitClient.getVendorService().postSalonTime("Bearer " + token, hashMap)
        call!!.enqueue(object : Callback<SalonDisableTimeData> {
            override fun onResponse(

                call: Call<SalonDisableTimeData>,
                response: Response<SalonDisableTimeData>
            ) {
                FunctionCall.DismissDialog(context)

                Toast.makeText(context, "" + response.body()!!.message, Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: Call<SalonDisableTimeData>, t: Throwable) {
                FunctionCall.DismissDialog(context)
                Toast.makeText(context, "not getting Response", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun refreshDataLst (list : List<SalonData>) {
        this.list = list
    }
    override fun getItemCount(): Int {
        return list.size
    }




}