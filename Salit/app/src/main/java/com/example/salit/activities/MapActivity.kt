package com.example.salit.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.salit.R
import com.example.salit.db.AppDatabase
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    private var currentSaleId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        getCurrentSaleValues()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun getCurrentSaleValues() {
        currentSaleId = intent.getIntExtra("SALE_ID", 0)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        setSaleMarker()
    }

    private fun setSaleMarker() {
        val appDatabase = AppDatabase.getDatabase(baseContext)
        val saleDao = appDatabase.SaleDao()
        GlobalScope.launch(Dispatchers.IO) {
            val selectedSale = saleDao.getSaleById(currentSaleId)
            val latitude = selectedSale.latitude!!
            val longitude = selectedSale.longitude!!
            launch(Dispatchers.Main) {
                val location = LatLng(latitude, longitude)
                mMap!!.addMarker(MarkerOptions().position(location).title("Sale place"))
                mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17f))
            }
        }
    }
}
