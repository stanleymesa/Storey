package com.example.storey.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.storey.R
import com.example.storey.data.local.database.MainDatabase
import com.example.storey.data.local.statics.StaticData
import com.example.storey.data.remote.response.ListStoryItem
import com.example.storey.databinding.ActivityMapsBinding
import com.example.storey.ui.viewmodel.ListStoryItemViewModel
import com.example.storey.ui.viewmodel.ListStoryItemViewModelFactory
import com.example.storey.ui.viewmodel.MapsViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var fromActivity: String = StaticData.ADD_STORY_ACTIVITY
    private var address: String = ""
    private lateinit var latLng: LatLng
    private var _binding: ActivityMapsBinding? = null
    private val binding get() = _binding!!
    private var markerOptions = MarkerOptions()
    private lateinit var listStoryItemViewModel: ListStoryItemViewModel
    private val mapsViewModel by viewModels<MapsViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        init()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setPadding(0, 0, 0, 160)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getMyLocation()
        setMapStyle()
        observeData()
    }

    private fun init() {
        setToolbar()
        setListStoryViewModel()
        getIntentItem()
        binding.mapsToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.includeMaps.btnSaveLocation.setOnClickListener(this)
    }

    private fun setListStoryViewModel() {
        val db = MainDatabase.getDatabase(applicationContext)
        val listStoryItemViewModelFactory =
            ListStoryItemViewModelFactory.getInstance(db, db.listStoryitemDao())
        listStoryItemViewModel = ViewModelProvider(this,
            listStoryItemViewModelFactory)[ListStoryItemViewModel::class.java]
    }

    private fun observeData() {

        when (fromActivity) {
            StaticData.MAIN_ACTIVITY -> {

                supportActionBar?.title = getString(R.string.open_maps_title)

                val latLngIndo = LatLng(StaticData.LATITUDE_IN, StaticData.LONGITUDE_IN)

                listStoryItemViewModel.getAllListStoryItem().observe(this, { listStory ->
                    placeMarkers(listStory)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngIndo, 7F))
                })

            }

            else -> {
                mapsViewModel.getLatLng.observe(this, { latLng ->
                    this.latLng = latLng

                    mapsViewModel.setAddress(applicationContext, latLng)

                    mapsViewModel.getAddress.observe(this, { address ->
                        this.address = address
                        supportActionBar?.title = address

                        placeMarker(latLng, address)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    })

                })
            }

        }


    }

    private fun placeMarker(latLng: LatLng, address: String) {
        mMap.clear()
        mMap.addMarker(
            markerOptions
                .position(latLng)
                .title(address)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
        )
    }

    private fun placeMarkers(listStoryItem: List<ListStoryItem>) {

        listStoryItem.map { item ->
            item.lat?.let { lat ->
                item.lon?.let { lon ->
                    val latLng = LatLng(
                        lat.toDouble(),
                        lon.toDouble()
                    )
                    mMap.addMarker(
                        markerOptions
                            .position(latLng)
                            .title(item.name)
                            .snippet(item.description)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                    )
                }
            }
        }


    }

    private fun setToolbar() {
        setSupportActionBar(binding.mapsToolbar)
    }

    private fun getIntentItem() {

        when (intent.getStringExtra(StaticData.FROM_ACTIVITY)) {
            StaticData.DETAIL_ACTIVITY -> {
                fromActivity = StaticData.DETAIL_ACTIVITY

                intent.getParcelableExtra<ListStoryItem>(StaticData.KEY_INTENT_DETAIL)
                    ?.let { storyItem ->

                        if (storyItem.lat != null && storyItem.lon != null) {
                            mapsViewModel.setLatLng(
                                LatLng(storyItem.lat.toDouble(), storyItem.lon.toDouble())
                            )
                        }

                    }
            }

            StaticData.ADD_STORY_ACTIVITY -> {
                fromActivity = StaticData.ADD_STORY_ACTIVITY
                binding.includeMaps.btnSaveLocation.isVisible = true
            }

            StaticData.MAIN_ACTIVITY -> {
                fromActivity = StaticData.MAIN_ACTIVITY
            }
        }
    }

    private fun setMapStyle() {
        try {
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
        } catch (exception: Resources.NotFoundException) {
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLocation()
                }
                permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLocation()
                }
                else -> {
                    Toast.makeText(this,
                        getString(R.string.no_location_permission),
                        Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLocation() {
        if (checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            mMap.isMyLocationEnabled = true

            if (fromActivity == StaticData.ADD_STORY_ACTIVITY) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->

                    if (location != null) {
                        mapsViewModel.setLatLng(
                            LatLng(location.latitude, location.longitude)
                        )
                    } else {
                        Toast.makeText(this,
                            getString(R.string.location_not_found),
                            Toast.LENGTH_SHORT).show()
                    }

                }

                mMap.setOnMapClickListener { latLng ->
                    mapsViewModel.setLatLng(latLng)
                }
            }

        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_save_location -> {
                val intent = Intent()
                val bundle = Bundle()
                bundle.putParcelable(StaticData.KEY_LATLNG, latLng)
                intent.putExtra(StaticData.KEY_LATLNG, bundle)
                intent.putExtra(StaticData.KEY_ADDRESS, address)
                setResult(StaticData.RESULT_SAVE_LOCATION, intent)
                finish()
            }
        }
    }
}