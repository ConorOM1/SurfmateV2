package ie.setu.surfmate.ui.update

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import ie.setu.surfmate.R
import ie.setu.surfmate.databinding.FragmentUpdateSpotBinding
import ie.setu.surfmate.models.SurfmateModel
import ie.setu.surfmate.ui.auth.LoggedInViewModel
import timber.log.Timber
import com.google.android.material.snackbar.Snackbar

class UpdateSurfspotsFragment : Fragment() {

    private lateinit var updateViewModel: UpdateSurfspotsViewModel
    private val args by navArgs<UpdateSurfspotsFragmentArgs>()
    private var _binding: FragmentUpdateSpotBinding? = null
    private val binding get() = _binding!!
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private var surfspot = SurfmateModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentUpdateSpotBinding.inflate(inflater, container, false)
        val root = binding.root

        updateViewModel = ViewModelProvider(this).get(UpdateSurfspotsViewModel::class.java)
        updateViewModel.getSurfspot(loggedInViewModel.liveFirebaseUser.value?.uid!!, args.surfspotid)
        updateViewModel.observableSurfspot.observe(viewLifecycleOwner, Observer { currentSurfspot ->
            if (currentSurfspot != null) {
                render(currentSurfspot)
                surfspot = currentSurfspot
            } else {
                Timber.i("No surfspot data available")
            }
        })

        setButtonListener(binding)
        registerImagePickerCallback()

        return root
    }

    private fun render(currentSurfspot: SurfmateModel) {
        binding.name.setText(currentSurfspot.name)
        binding.surfspotObservations.setText(currentSurfspot.observations)
        binding.ratingBar.rating = currentSurfspot.rating ?: 0f
        Picasso.get().load(currentSurfspot.image).into(binding.SurfspotImage)
    }

    private fun setButtonListener(layout: FragmentUpdateSpotBinding) {
        layout.btnUpdate.setOnClickListener {
            surfspot.name = binding.name.text.toString()
            surfspot.observations = binding.surfspotObservations.text.toString()
            surfspot.rating = binding.ratingBar.rating

            if (!surfspot.name.isNullOrEmpty() && !surfspot.observations.isNullOrEmpty()) {
                updateViewModel.updateSurfspot(loggedInViewModel.liveFirebaseUser.value?.uid!!, args.surfspotid, surfspot)
                findNavController().popBackStack()
            } else {
                Snackbar.make(it, R.string.some_fields_required, Snackbar.LENGTH_LONG).show()            }
        }
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            var updated = updateViewModel.observableSurfspot.value
                            updated?.image = result.data!!.data.toString()!!
                            Picasso.get().load(updated?.image).into(binding.SurfspotImage)
                            binding.chooseImage.setText(R.string.edit_image)
                            updateViewModel.updateSurfspot(
                                loggedInViewModel.liveFirebaseUser.value?.uid!!,
                                args.surfspotid,
                                updated!!
                            )
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }
}