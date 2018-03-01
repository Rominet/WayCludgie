package com.cryocrystal.waytocludgie.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Toast
import com.cryocrystal.mvp.app.PresenterAppCompatActivity
import com.cryocrystal.waytocludgie.R
import com.cryocrystal.waytocludgie.fragment.SanisetteDetailFragment
import com.cryocrystal.waytocludgie.model.SanisetteInfo
import com.cryocrystal.waytocludgie.presenter.MainContract
import com.cryocrystal.waytocludgie.presenter.MainPresenter
import com.cryocrystal.waytocludgie.statics.Config
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : PresenterAppCompatActivity<MainPresenter>(), OnMapReadyCallback, MainContract {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    private val markersByInfo: HashMap<SanisetteInfo, Marker> = HashMap()
    private val handler = Handler()

    @SuppressLint("MissingSuperCall") // AS bug
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        slidingLayout.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View, slideOffset: Float) {

            }

            override fun onPanelStateChanged(panel: View, previousState: SlidingUpPanelLayout.PanelState, newState: SlidingUpPanelLayout.PanelState) {
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED){
                    updateCurrentLocation()
                }
            }

        })
    }

    override fun createPresenter(): MainPresenter {
        return MainPresenter(this, this)
    }

    override fun onDisplayLoader() {

    }

    @SuppressLint("MissingPermission")
    override fun onSanisettesUpdated(sanisettes: List<SanisetteInfo>?) {
        val descriptor = BitmapDescriptorFactory.fromResource(R.drawable.toilet_opened_arrow)

        mMap.clear()
        markersByInfo.clear()
        sanisettes?.forEach {
            val marker = mMap.addMarker(MarkerOptions()
                    .anchor(0.5f, 1f)
                    .icon(descriptor)
                    .position(LatLng(it.lat, it.lng))
                    .title(it.streetName))
            marker.snippet = it.streetNumber
            marker.tag = it
            markersByInfo.put(it, marker)
        }
    }

    private fun updateCurrentLocation(retryCount : Int = 0) {
        handler.removeCallbacksAndMessages(null)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation
                    .addOnSuccessListener {
                        if (it == null && retryCount < Config.POSITION_MAX_RETRY_COUNT){
                            handler.postDelayed({updateCurrentLocation(retryCount + 1)}, Math.pow(retryCount.toDouble(), 2.0).toLong() * Config.POSITION_RETRY_STARTING_DELAY )
                        } else {
                            presenter.updateInfosWithLocation(it)
                        }
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
        mMap.setOnInfoWindowClickListener { onMarkerClick(it, false) }
        mMap.setOnMarkerClickListener { onMarkerClick(it) }

        updateCurrentLocation()

        presenter.fetchInfo()
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_LOCATION_REQUEST_CODE)
        } else {
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = true
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MY_LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.isMyLocationEnabled = true
                    mMap.uiSettings.isMyLocationButtonEnabled = true
                } else {
                    Toast.makeText(this, "You need location permission to see your position", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun onMarkerClick(marker: Marker, delayShowDetail: Boolean = true): Boolean {
        // Delay the fragment apparation for smoother ux
        if (delayShowDetail){
            Handler().postDelayed ({showDetailFragment(marker.tag as SanisetteInfo)}, resources.getInteger(android.R.integer.config_longAnimTime) + 100L)
        } else {
            showDetailFragment(marker.tag as SanisetteInfo)
        }

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

    private fun showDetailFragment(info: SanisetteInfo) {
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_bottom, R.anim.slide_in_up, R.anim.slide_out_bottom)
                .replace(R.id.detailFragment, SanisetteDetailFragment.newInstance(info), SanisetteDetailFragment.TAG)
                .addToBackStack(SanisetteDetailFragment.TAG)
                .commit()
    }

    fun displayDetail(info: SanisetteInfo){
        slidingLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED

        val marker = markersByInfo[info]!!
        onMarkerClick(marker)
    }


    companion object {
        private const val MY_LOCATION_REQUEST_CODE: Int = 4242
    }
}
