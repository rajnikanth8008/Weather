package com.rk.weather.ui.main.search

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.gson.JsonObject
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.api.geocoding.v5.GeocodingCriteria
import com.mapbox.api.geocoding.v5.MapboxGeocoding
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import com.mapbox.api.geocoding.v5.models.GeocodingResponse
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils
import com.rk.weather.R
import com.rk.weather.data.db.WeatherDatabase
import com.rk.weather.data.db.entity.BookmarkEntity
import com.rk.weather.databinding.SearchFragmentBinding
import com.rk.weather.ui.main.MainActivity
import com.rk.weather.ui.main.bookmark.BookmarkViewModel
import com.rk.weather.utills.Constants
import com.rk.weather.utills.clearText
import com.rk.weather.utills.hide
import com.rk.weather.utills.show
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION")
class SearchFragment : Fragment(), OnMapReadyCallback, PermissionsListener {
    companion object {
        private const val REQUEST_CODE_AUTOCOMPLETE = 1
        private const val ROUTE_SOURCE_ID = "route-source-id"
        private const val ICON_LAYER_ID = "icon-layer-id"
        private const val ICON_SOURCE_ID = "icon-source-id"
        private const val RED_PIN_ICON_ID = "red-pin-icon-id"
        private val geojsonSourceLayerId = "geojsonSourceLayerId"
        private val symbolIconId = "symbolIconId"
        private const val TAG = "SearchFragment"
    }

    private lateinit var viewModel: SearchViewModel
    private lateinit var bookmarkViewModel: BookmarkViewModel
    lateinit var binding: SearchFragmentBinding
    var mapboxMap: MapboxMap? = null
    private var permissionsManager: PermissionsManager? = null
    private var home: CarmenFeature? = null
    private var work: CarmenFeature? = null
    var origin = Point.fromLngLat(90.399452, 23.777176)
    var destination = Point.fromLngLat(90.399452, 23.777176)
    var lat: String = "0.0"
    var lon: String = "0.0"
    var weatherDatabase: WeatherDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context?.let { Mapbox.getInstance(it, Constants.MapBox.ACCESS_TOKEN) }
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        bookmarkViewModel = ViewModelProvider(this).get(BookmarkViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.search_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        try {
            binding.mapView.onCreate(savedInstanceState)
            binding.mapView.getMapAsync(this)
            weatherDatabase = context?.let { WeatherDatabase.getDatabase(it) }
        } catch (e: Exception) {
            Log.e(TAG, "onActivityCreated: " + e.message)
        }
    }

    override fun onMapReady(mapboxMapReady: MapboxMap) {
        this.mapboxMap = mapboxMapReady
        mapboxMap!!.setStyle(Style.MAPBOX_STREETS) { style ->
            val drawable = ResourcesCompat.getDrawable(resources, R.drawable.marker, null)
            val mBitmap = BitmapUtils.getBitmapFromDrawable(drawable)
            style.addImage(symbolIconId, mBitmap!!)
            enableLocationComponent(style)
            initSearchFab()
            addUserLocations()
            setUpSource(style)
            setupLayer(style)
            initSource(style)
            initLayers(style)
            mapboxMap!!.addOnMapClickListener(object : MapboxMap.OnMapClickListener {
                var source: LatLng? = null
                override fun onMapClick(point: LatLng): Boolean {
                    mapboxMap!!.clear()
                    origin = Point.fromLngLat(point.longitude, point.latitude)
                    source = point
                    val markerOptions = MarkerOptions()
                    markerOptions.position(point)
                    mapboxMap!!.addMarker(markerOptions)
                    reverseGeocodeFunc(point)
                    return true
                }
            })
        }
    }

    private fun reverseGeocodeFunc(point: LatLng) {
        val reverseGeocode = MapboxGeocoding.builder()
            .accessToken(Constants.MapBox.ACCESS_TOKEN)
            .query(Point.fromLngLat(point.longitude, point.latitude))
            .geocodingTypes(GeocodingCriteria.TYPE_POI)
            .build()
        reverseGeocode.enqueueCall(object : Callback<GeocodingResponse> {
            override fun onResponse(
                call: Call<GeocodingResponse>,
                response: Response<GeocodingResponse>
            ) {
                lat = "0.0"
                lon = "0.0"
                binding.placename.clearText()
                binding.cardView.hide()
                binding.buttonConfirm.hide()
                val results = response.body()!!.features()
                if (results.size > 0) {
                    val feature: CarmenFeature = results[0]
                    val placeName = feature.placeName()
                    binding.cardView.show()
                    binding.placename.text = placeName
                    binding.buttonConfirm.show()
                    lat = (feature.geometry() as Point).latitude().toString()
                    lon = (feature.geometry() as Point).longitude().toString()
                } else {
                    Toast.makeText(context, "Not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GeocodingResponse>, throwable: Throwable) {
                throwable.printStackTrace()
            }
        })
    }

    private fun setUpSource(loadedMapStyle: Style) {
        loadedMapStyle.addSource(GeoJsonSource(geojsonSourceLayerId))
    }

    private fun setupLayer(loadedMapStyle: Style) {
        loadedMapStyle.addLayer(
            SymbolLayer("SYMBOL_LAYER_ID", geojsonSourceLayerId).withProperties(
                PropertyFactory.iconImage(symbolIconId),
                PropertyFactory.iconOffset(arrayOf(0f, -8f))
            )
        )
    }

    private fun initSource(loadedMapStyle: Style) {
        loadedMapStyle.addSource(GeoJsonSource(ROUTE_SOURCE_ID))
        val iconGeoJsonSource = GeoJsonSource(
            ICON_SOURCE_ID, FeatureCollection.fromFeatures(
                arrayOf(
                    Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                    Feature.fromGeometry(
                        Point.fromLngLat(
                            destination.longitude(),
                            destination.latitude()
                        )
                    )
                )
            )
        )
        loadedMapStyle.addSource(iconGeoJsonSource)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initLayers(loadedMapStyle: Style) {

// Add the red marker icon image to the map
        loadedMapStyle.addImage(
            RED_PIN_ICON_ID, BitmapUtils.getBitmapFromDrawable(
                resources.getDrawable(R.drawable.marker)
            )!!
        )

// Add the red marker icon SymbolLayer to the map
        loadedMapStyle.addLayer(
            SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
                PropertyFactory.iconImage(RED_PIN_ICON_ID),
                PropertyFactory.iconIgnorePlacement(true),
                PropertyFactory.iconAllowOverlap(true),
                PropertyFactory.iconOffset(arrayOf(0f, -8f))
            )
        )
    }

    private fun initSearchFab() {
        binding.fabLocationSearch.setOnClickListener {
            lat = "0.0"
            lon = "0.0"
            binding.placename.clearText()
            binding.buttonConfirm.hide()
            binding.cardView.hide()
            val intent = PlaceAutocomplete.IntentBuilder()
                .accessToken((if (Mapbox.getAccessToken() != null) Mapbox.getAccessToken() else Constants.MapBox.ACCESS_TOKEN)!!)
                .placeOptions(
                    PlaceOptions.builder()
                        .backgroundColor(Color.parseColor("#EEEEEE"))
                        .limit(10)
                        .addInjectedFeature(home)
                        .addInjectedFeature(work)
                        .build(PlaceOptions.MODE_CARDS)
                )
                .build(requireActivity())
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE)
        }
    }

    private fun addUserLocations() {
        home = CarmenFeature.builder().text("Cyber Towers")
            .geometry(Point.fromLngLat(78.38109290904112, 17.45043760058168))
            .placeName("Hitech City Rd, Patrika Nagar, HITEC City, Hyderabad, Telangana")
            .id("Cyber Towers")
            .properties(JsonObject())
            .build()
        work = CarmenFeature.builder().text("IT Hub")
            .placeName("103,105,203,gyan arcade, 203, Sheesh Mahal Theatre Rd, beside seeshmahal theatre, Ameerpet, Hyderabad, Telangana")
            .geometry(Point.fromLngLat(77.9898049, 17.492627))
            .id("IT Hub")
            .properties(JsonObject())
            .build()
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Toast.makeText(context, "Permissions not granted $permissionsToExplain", Toast.LENGTH_SHORT)
            .show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            mapboxMap!!.getStyle { style -> enableLocationComponent(style) }
        } else {
            Toast.makeText(context, "Permissions Required", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            val selectedCarmenFeature = PlaceAutocomplete.getPlace(data)
            if (mapboxMap != null) {
                val style = mapboxMap!!.style
                if (style != null) {
                    val source = style.getSourceAs<GeoJsonSource>(geojsonSourceLayerId)
                    source?.setGeoJson(
                        FeatureCollection.fromFeatures(
                            arrayOf(
                                Feature.fromJson(
                                    selectedCarmenFeature.toJson()
                                )
                            )
                        )
                    )
                    mapboxMap!!.animateCamera(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition.Builder()
                                .target(
                                    LatLng(
                                        (selectedCarmenFeature.geometry() as Point?)!!.latitude(),
                                        (selectedCarmenFeature.geometry() as Point?)!!.longitude()
                                    )
                                )
                                .zoom(14.0)
                                .build()
                        ), 4000
                    )
                }
                binding.cardView.show()
                binding.placename.text = selectedCarmenFeature.placeName()
                binding.buttonConfirm.show()
                lat = (selectedCarmenFeature.geometry() as Point).latitude().toString()
                lon = (selectedCarmenFeature.geometry() as Point).longitude().toString()
            }
        }
    }

    private fun enableLocationComponent(loadedMapStyle: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(context)) {
            val locationComponent = mapboxMap!!.locationComponent
            locationComponent.activateLocationComponent(
                LocationComponentActivationOptions.builder(
                    requireContext(),
                    loadedMapStyle
                ).build()
            )
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                locationComponent.isLocationComponentEnabled = true
                locationComponent.cameraMode = CameraMode.TRACKING
                locationComponent.renderMode = RenderMode.COMPASS
            } else {
                return
            }
        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager!!.requestLocationPermissions(requireActivity())
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
        binding.buttonConfirm.setOnClickListener {
            (activity as MainActivity).sharedPreferences.edit().putString(Constants.Coords.LAT, lat)
                .apply()
            (activity as MainActivity).sharedPreferences.edit().putString(Constants.Coords.LON, lon)
                .apply()
//            findNavController().navigate(R.id.action_searchFragment_to_dashBoardFragment)
            findNavController().popBackStack()
        }

        binding.fabLocationBookmark.setOnClickListener {
            if (binding.placename.text.toString() == "") {
                Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT).show()
            } else {
                val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if (Build.VERSION.SDK_INT >= 26) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            200,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                } else {
                    vibrator.vibrate(200)
                }
                val bookmark = BookmarkEntity(
                    binding.placename.text.toString(),
                    lat.toDouble(),
                    lon.toDouble()
                )
                weatherDatabase?.let { it1 -> bookmarkViewModel.saveBookmark(it1, bookmark) }
                (activity as MainActivity).sharedPreferences.edit()
                    .putString(Constants.Coords.LAT, lat)
                    .apply()
                (activity as MainActivity).sharedPreferences.edit()
                    .putString(Constants.Coords.LON, lon)
                    .apply()
//                findNavController().navigate(R.id.action_searchFragment_to_dashBoardFragment)
                Toast.makeText(context, "Location Bookmarked", Toast.LENGTH_LONG).show()
                findNavController().popBackStack()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}