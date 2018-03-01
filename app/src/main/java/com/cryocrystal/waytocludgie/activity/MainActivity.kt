package com.cryocrystal.waytocludgie.activity

import android.annotation.SuppressLint
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.cryocrystal.mvp.app.PresenterAppCompatActivity
import com.cryocrystal.waytocludgie.R
import com.cryocrystal.waytocludgie.fragment.SanisetteDetailFragment
import com.cryocrystal.waytocludgie.model.SanisetteInfo
import com.cryocrystal.waytocludgie.presenter.MainContract
import com.cryocrystal.waytocludgie.presenter.MainPresenter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : PresenterAppCompatActivity<MainPresenter>(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, MainContract {

    override fun createPresenter(): MainPresenter {
        return MainPresenter(this, this)
    }

    private lateinit var mMap: GoogleMap

    @SuppressLint("MissingSuperCall") // AS bug
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onDisplayLoader() {

    }

    override fun onSanisettesUpdated(sanisettes: List<SanisetteInfo>?) {
        val descriptor = BitmapDescriptorFactory.fromResource(R.drawable.toilet_opened_arrow)
        if (sanisettes != null){
            sanisettes.forEach {
                val marker = mMap.addMarker(MarkerOptions()
                        .anchor(0.5f, 1f)
                        .icon(descriptor)
                        .position(LatLng(it.lat, it.lng))
                        .title(it.streetName))
                marker.snippet = it.streetNumber
                marker.tag = it
            }
        }
    }

    override fun onWebError(e: Throwable) {
        Toast.makeText(this, getString(R.string.web_error), Toast.LENGTH_LONG).show()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val paris = LatLng(48.8597977, 2.3338404)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(paris, 14f))
        mMap.setOnMarkerClickListener(this)
        mMap.setOnInfoWindowClickListener{ onMarkerClick(it) }

        presenter.fetchInfo()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        showDetail(marker.tag as SanisetteInfo)
        marker.showInfoWindow()
        mMap.animateCamera(CameraUpdateFactory.newLatLng(getMarkerShiftedUpperSide(marker)))
        return true
    }

    private fun getMarkerShiftedUpperSide(marker: Marker): LatLng {
        val projection = mMap.projection
        val markerPoint = projection.toScreenLocation(marker.position)
        val targetPoint = Point(markerPoint.x, markerPoint.y + flRoot.height / 10) // Shifted by 10%
        return projection.fromScreenLocation(targetPoint)
    }

    fun showDetail(info: SanisetteInfo){
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_bottom, R.anim.slide_in_up, R.anim.slide_out_bottom)
                .add(R.id.detailFragment, SanisetteDetailFragment.newInstance(info), SanisetteDetailFragment.TAG)
                .addToBackStack(SanisetteDetailFragment.TAG)
                .commit()
    }
}
