package ie.setu.surfmate.ui.add

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ie.setu.surfmate.R
import ie.setu.surfmate.databinding.FragmentAddSpotBinding
import ie.setu.surfmate.main.SurfmateApp
import ie.setu.surfmate.models.SurfmateModel
import timber.log.Timber
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ie.setu.surfmate.ui.auth.LoggedInViewModel
import ie.setu.surfmate.ui.list.SurfspotsViewModel
import ie.setu.surfmate.ui.map.MapsViewModel

class AddSpotFragment : Fragment() {

    private var _binding: FragmentAddSpotBinding? = null
    private val binding get() = _binding!!
    private lateinit var addViewModel: AddSpotViewModel
    private lateinit var ratingBar: RatingBar

    private lateinit var addSpotViewModel: AddSpotViewModel
    private val args by navArgs<AddSpotFragmentArgs>()
    private val surfspotsViewModel: SurfspotsViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val mapsViewModel: MapsViewModel by activityViewModels()



    var surfspot = SurfmateModel()
    lateinit var app: SurfmateApp

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val imageUri: Uri? = result.data?.data
            imageUri?.let { uri ->
                val takeFlags: Int = result.data?.flags?.and(
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                ) ?: 0
                requireActivity().contentResolver.takePersistableUriPermission(uri, takeFlags)

                surfspot.image = uri.toString()
                binding.SurfspotImage.setImageURI(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = SurfmateApp()
        Timber.i("ON CREATE ADD FRAGMENT")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.i("ON CREATE VIEW ADD FRAGMENT")

        _binding = FragmentAddSpotBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val surfspotId = args.surfspotid

        addSpotViewModel = ViewModelProvider(this).get(AddSpotViewModel::class.java)
        addSpotViewModel.observableStatus.observe(viewLifecycleOwner, Observer {
                status -> status?.let { render(status) }
        })

        setButtonListener(binding)

        ratingBar = binding.ratingBar
        ratingBar.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { _, rating, _ ->
            surfspot.rating = rating
        }

        binding.chooseImage.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            galleryIntent.type = "image/*"
            pickImageLauncher.launch(galleryIntent)
        }

        return root
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> view?.let {
            }
            false -> Toast.makeText(context, getString(R.string.surfspotError), Toast.LENGTH_LONG).show()
        }
    }

    fun setButtonListener(layout: FragmentAddSpotBinding) {
        layout.btnAdd.setOnClickListener {
            surfspot.name = binding.name.text?.toString() ?: ""
            surfspot.observations = binding.observations.text?.toString() ?: ""
            surfspot.rating = ratingBar.rating

            if (!surfspot.observations.isNullOrEmpty() && !surfspot.name.isNullOrEmpty()) {
                Timber.i("ADDING NEW SURF SPOT: $surfspot")

                addSpotViewModel.addSurfspot(loggedInViewModel.liveFirebaseUser,
                    SurfmateModel(
                        uid = surfspot.uid,
                        id = surfspot.id,
                        name = surfspot.name,
                        observations = surfspot.observations,
                        image = surfspot.image,
                        lat = mapsViewModel.currentLocation.value!!.latitude,
                        lng = mapsViewModel.currentLocation.value!!.longitude,
                        zoom = surfspot.zoom,
                        rating = surfspot.rating,
                        email = loggedInViewModel.liveFirebaseUser.value?.email ?: ""
                    ))

                findNavController().popBackStack() // Navigate back after operation
            } else {
                Snackbar.make(it, R.string.some_fields_required, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
