package com.vendor.salon.activity

import android.content.Context
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.marcinmoskala.arcseekbar.ProgressListener
import com.vendor.salon.R
import com.vendor.salon.data_Class.areacover.CoverRangeResponse
import com.vendor.salon.databinding.ActivityCoverRangeBinding
import com.vendor.salon.networking.RetrofitClient
import com.vendor.salon.utilityMethod.NetworkChangeListener
import com.vendor.salon.utilityMethod.loginResponsePref
import retrofit2.Response


class CoverRangeActivity : AppCompatActivity(), OnMapReadyCallback {
    private val networkChangeListener: NetworkChangeListener = NetworkChangeListener()
    private var lat_vals: Double = 0.0
    private var lng: Double = 0.0
    private lateinit var newMaps: Unit
    private var radius_val: Double = 3000.0
    private lateinit var circles: Circle
    private lateinit var binding: ActivityCoverRangeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cover_range)
        binding.progressArcs.maxProgress = 100
        radius_val = intent.getDoubleExtra("radius",30.0 )
        lat_vals = intent.getDoubleExtra("lats", 0.0 )
        lng = intent.getDoubleExtra("lng" , 0.0 )
        binding.mapview.onCreate(savedInstanceState)
        binding.mapview.getMapAsync(this@CoverRangeActivity)


        binding.arrowBack.setOnClickListener {
            finish()
        }
    }

    private fun updateData() {
        val call: retrofit2.Call<CoverRangeResponse>
        val token: String = "Bearer " + loginResponsePref.getInstance(this@CoverRangeActivity).token
        call = RetrofitClient.getVendorService().updateCoverRanges(token,
            radius_val.toString())
        call.enqueue(object : retrofit2.Callback<CoverRangeResponse> {
            override fun onResponse(
                call: retrofit2.Call<CoverRangeResponse>,
                response: Response<CoverRangeResponse>,
            ) {
                if (response.isSuccessful && response.body() != null && response.body()!!.isStatus) {
                    radius_val = response.body()!!.data.areaCover.toDouble()
                    binding.txtKms.text = "$radius_val Km"
                } else {
                    Log.d("coverrangehit", "onResponse: " + response.body())
                }
            }

            override fun onFailure(call: retrofit2.Call<CoverRangeResponse>, t: Throwable) {
                Log.d("coverrangehit", "onFailure: " + t.message)
            }
        })
    }

    override fun onMapReady(map: GoogleMap) {

        showMapWithDifferentRadiuss(map, 3000.0)
        var progressListener = ProgressListener { i: Int ->
            run {

                radius_val = i.toDouble()
                binding.txtKms.text = "$radius_val Km "
                updateData()
//                 circles.radius = radius_val
            }
//            map.setOnCircleClickListener {
//                circles.radius = radius_val
//            }
        }
        binding.progressArcs.onProgressChangedListener = progressListener


        progressListener.invoke(30)
    }

    private fun showMapWithDifferentRadiuss(googleMap: GoogleMap, radius_Vals: Double) {

        googleMap.addMarker(MarkerOptions().position(LatLng(lat_vals , lng))
            .icon(bitmapDescriptorFromVector(applicationContext, R.drawable.standingman)))

        var circleOptions = CircleOptions()
            .center(LatLng(lat_vals, lng))
            .radius(radius_Vals)
            .fillColor(0x220000FF)
            .strokeColor(-0xffff01)
            .strokeWidth(2f)

        circles = googleMap.addCircle(circleOptions)
        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(lat_vals, lng))
            .zoom(12f)
            .build()
        newMaps = googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))


    }

    override fun onResume() {
        super.onResume()
        binding.mapview.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapview.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapview.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapview.onLowMemory()
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }


    override fun onStart() {
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeListener, intentFilter)
        super.onStart()
    }

    override fun onStop() {
        unregisterReceiver(networkChangeListener)
        super.onStop()
    }
}
