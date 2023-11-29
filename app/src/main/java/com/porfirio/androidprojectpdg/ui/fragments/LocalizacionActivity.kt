package com.porfirio.androidprojectpdg.ui.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.porfirio.androidprojectpdg.R
import com.porfirio.androidprojectpdg.databinding.ActivityLocalizacionBinding
import com.porfirio.androidprojectpdg.ui.MainActivity

class LocalizacionActivity: AppCompatActivity(), OnMapReadyCallback, LocationListener {

    private var latitud: Double? = null
    private var longitud: Double? = null

    private lateinit var binding: ActivityLocalizacionBinding

    //Para Google Maps
    private lateinit var map: GoogleMap

    //Para los permisos
    private var fineLocationPermissionGranted = false

    private val permissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){ isGranted ->
        if(isGranted){
            //Se concedió el permiso
            actionPermissionGranted()
        }else{
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.permisoRequerido))
                    .setMessage(getString(R.string.permisoUsuarioPosicion))
                    .setPositiveButton(getString(R.string.entendido)){ _, _ ->
                        updateOrRequestPermissions()
                    }
                    .setNegativeButton(getString(R.string.salir)){ dialog, _ ->
                        dialog.dismiss()
                        finish()
                    }
                    .create()
                    .show()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.permisoUbicacionNegadoPermanentemente),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocalizacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            binding.pbLoading.visibility = View.GONE

        }
        val botonIrTenistasListFragment = findViewById<Button>(R.id.btnRegresar)
        botonIrTenistasListFragment.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    private fun actionPermissionGranted() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Manejar el permiso
            //Pero en este caso no es necesario
            return
        }
        map.isMyLocationEnabled = true

    }

    private fun updateOrRequestPermissions() {
        //Revisando el permiso
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        fineLocationPermissionGranted = hasFineLocationPermission

        if (!fineLocationPermissionGranted) {
            //Pedimos el permiso
            permissionsLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }else{
            //Tenemos los permisos otorgados
            actionPermissionGranted()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {

        map = googleMap
        createMarker()
        updateOrRequestPermissions()
        map.setOnMapLongClickListener {position ->
            val marker = MarkerOptions()
                .position(position)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_cup))
            map.addMarker(marker)
        }
    }

    private fun createMarker(){

        latitud = intent.getDoubleExtra("EXTRA_LAT", 0.0)
        longitud = intent.getDoubleExtra("EXTRA_LNG", 0.0)
        val coordinates = LatLng(latitud!!,longitud!!)
        val marker = MarkerOptions()
            .position(coordinates)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_cup))

        map.addMarker(marker)

        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates, 18f),
            4000,
            null
        )
    }

    override fun onRestart(){
        super.onRestart()
        if(!::map.isInitialized) return
        if(!fineLocationPermissionGranted)
            updateOrRequestPermissions()
    }

    override fun onLocationChanged(location: Location) {
        map.clear()
        val coordinates = LatLng(location.latitude, location.longitude)
        val marker = MarkerOptions()
            .position(coordinates)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_cup))

        map.addMarker(marker)
        map.moveCamera((CameraUpdateFactory.newLatLngZoom(coordinates,18f)))
    }

}