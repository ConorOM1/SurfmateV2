package ie.setu.surfmate.ui.map

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ie.setu.surfmate.R
import ie.setu.surfmate.models.SurfmateModel
import ie.setu.surfmate.ui.auth.LoggedInViewModel
import ie.setu.surfmate.ui.list.SurfspotsViewModel
import ie.setu.surfmate.ui.auth.LoginRegisterViewModel
import ie.setu.surfmate.utils.createLoader
import ie.setu.surfmate.utils.hideLoader
import ie.setu.surfmate.utils.showLoader

class MapsFragment : Fragment() {

    private val mapsViewModel: MapsViewModel by activityViewModels()
    private val surfspotsViewModel: SurfspotsViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private lateinit var loginRegisterViewModel: LoginRegisterViewModel
    lateinit var loader : AlertDialog

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        mapsViewModel.map = googleMap
        mapsViewModel.map.isMyLocationEnabled = true
        mapsViewModel.currentLocation.observe(viewLifecycleOwner) {
            val loc = LatLng(
                mapsViewModel.currentLocation.value!!.latitude,
                mapsViewModel.currentLocation.value!!.longitude
            )

            mapsViewModel.map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 7f))
            mapsViewModel.map.uiSettings.isZoomControlsEnabled = true
            mapsViewModel.map.uiSettings.isMyLocationButtonEnabled = true

            surfspotsViewModel.observableSurfspotsList.observe(
                viewLifecycleOwner,
                Observer { surfspots ->
                    surfspots?.let {
                        render(surfspots as ArrayList<SurfmateModel>)
                        hideLoader(loader)
                    }
                })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loader = createLoader(requireActivity())

        loginRegisterViewModel = ViewModelProvider(requireActivity()).get(LoginRegisterViewModel::class.java)
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        loginRegisterViewModel.liveFirebaseUser.observe(viewLifecycleOwner) { firebaseUser ->
            if (firebaseUser != null) {
                mapsViewModel.setFirebaseUser(firebaseUser)
                surfspotsViewModel.setFirebaseUser(firebaseUser)
                surfspotsViewModel.load()
            }
        }
    }

    private fun render(surfspotsList: ArrayList<SurfmateModel>) {
        if (!surfspotsList.isEmpty()) {
            mapsViewModel.map.clear()
            surfspotsList.forEach {
                mapsViewModel.map.addMarker(
                    MarkerOptions().position(LatLng(it.lat, it.lng))
                        .title("${it.name}")
                        .snippet(it.observations)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showLoader(loader, "Downloading Surf spots")
        surfspotsViewModel.load()
    }
}