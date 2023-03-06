package com.vendor.salon.activity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.marcinmoskala.arcseekbar.ProgressListener
import com.vendor.salon.R
import com.vendor.salon.databinding.ActivityCoverRangeBinding


class CoverRangeActivity : AppCompatActivity(),OnMapReadyCallback {
    private lateinit var circles: Circle
    lateinit var circleOptions: CircleOptions
    private lateinit var binding : ActivityCoverRangeBinding
      private lateinit var map : GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this,R.layout.activity_cover_range)
        binding.mapview.onCreate(savedInstanceState)
        binding.mapview.getMapAsync(this@CoverRangeActivity)
        binding.progressArcs.maxProgress = 100;



    }

    override fun onMapReady(googlemap: GoogleMap) {
            this.map = googlemap
        showMapWithDifferentRadiuss (1000.0 )
        var progressListener = ProgressListener {
                i: Int ->
            run {
                binding.txtKms.text = i.toString() + " Km "
                showMapWithDifferentRadiuss(i.toDouble())}

        }
        binding.progressArcs.onProgressChangedListener = progressListener;
        progressListener.invoke(30);

//        val sydney = LatLng(-34.0, 151.0)
//        map.addMarker(MarkerOptions().position(sydney).title("Marker "))
//
//        val cameraPosition = CameraPosition.Builder()
//            .target(sydney)
//            .zoom(12f)
//            .build()
//        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
//        map.animateCamera(cameraUpdate)
    }

    private fun showMapWithDifferentRadiuss( radius_vals : Double) {


        map.addMarker(MarkerOptions().position(LatLng(37.7749, -122.4194)).icon(bitmapDescriptorFromVector(applicationContext ,R.drawable.standingman)))

        circleOptions = CircleOptions()
            .center(LatLng(37.7749, -122.4194))
            .radius( radius_vals)
            .fillColor(0x220000FF)
            .strokeColor(-0xffff01)
            .strokeWidth(2f)

        circles = map.addCircle(circleOptions)
        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(37.7749, -122.4194))
            .zoom(12f)
            .build()
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

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

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
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

}
