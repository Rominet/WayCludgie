package com.cryocrystal.waytocludgie.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.cryocrystal.mvp.app.PresenterAppCompatActivity
import com.cryocrystal.waytocludgie.R
import com.cryocrystal.waytocludgie.model.SanisetteRecordItem
import com.cryocrystal.waytocludgie.presenter.MarkersContract
import com.cryocrystal.waytocludgie.presenter.MarkersPresenter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MarkersActivity : PresenterAppCompatActivity<MarkersPresenter>(), OnMapReadyCallback, MarkersContract {

    override fun createPresenter(): MarkersPresenter {
        return MarkersPresenter(this)
    }

    private lateinit var mMap: GoogleMap

    @SuppressLint("MissingSuperCall") // AS bug
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_markers)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onRecordsUpdated(records: List<SanisetteRecordItem>?) {
        val descriptor = BitmapDescriptorFactory.fromResource(R.drawable.toilet_opened_arrow)
        records?.forEach { mMap.addMarker(MarkerOptions()
                .anchor(0.5f, 1f)
                .icon(descriptor)
                .position(LatLng(it.geometry.lat, it.geometry.lng)).title(it.fields.nomVoie)) }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val paris = LatLng(48.8597977, 2.3338404)
        //mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(paris, 14f))

        presenter.fetchMarkers()
    }
}
