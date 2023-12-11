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
import ie.setu.surfmate.databinding.FragmentUpdateSpotBinding
import ie.setu.surfmate.ui.auth.LoggedInViewModel
import ie.setu.surfmate.ui.list.SurfspotsViewModel
import timber.log.Timber
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import ie.setu.surfmate.R
import ie.setu.surfmate.models.SurfmateModel
import ie.setu.surfmate.utils.showImagePicker
import android.widget.Toast




class UpdateSurfspotsFragment : Fragment() {

    private lateinit var updateViewModel: UpdateSurfspotsViewModel
    private val args by navArgs<UpdateSurfspotsFragmentArgs>()
    private var _binding: FragmentUpdateSpotBinding? = null
    private val binding get() = _binding!!
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentUpdateSpotBinding.inflate(inflater, container, false)
        val root = binding.root

        updateViewModel = ViewModelProvider(this).get(UpdateSurfspotsViewModel::class.java)
        updateViewModel.observableSurfspot.observe(viewLifecycleOwner, Observer { surfspot ->
            if (surfspot != null) {
                render(surfspot)
            } else {
                Timber.i("No surfspot data available")
            }
        })
        // Fetch the surf spot data
        val surfspotId = args.surfspotid
        updateViewModel.getSurfspot(loggedInViewModel.liveFirebaseUser.value?.uid!!, surfspotId)

        // Set up observers
        updateViewModel.observableSurfspot.observe(viewLifecycleOwner, Observer { surfspot ->
            if (surfspot != null) {
                render(surfspot)
            } else {
                Timber.i("No surfspot data available")
            }
        })

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }
        registerImagePickerCallback()


        binding.btnUpdate.setOnClickListener {
            updateViewModel.updateSurfspot(loggedInViewModel.liveFirebaseUser.value?.uid!!,
                args.surfspotid, binding.surfspotvm?.observableSurfspot!!.value!!)
            findNavController().navigateUp()
        }

//        binding.deleteSurfspot.setOnClickListener {
//            updateViewModel.delete(loggedInViewModel.liveFirebaseUser.value?.email!!,
//                updateViewModel.observableSurfspot.value?.uid!!)
//            findNavController().navigateUp()
//        }

        return root
    }

    private fun render(surfspot: SurfmateModel?) {
        if (surfspot != null) {
            binding.name.setText(surfspot.name)
            binding.surfspotObservations.setText(surfspot.observations)

            if (!surfspot.image.isNullOrEmpty()) {
                Picasso.get().load(surfspot.image).into(binding.SurfspotImage)
            } else {
                binding.SurfspotImage.setImageResource(R.mipmap.ic_launcher) // Default image
            }

            binding.ratingBar.rating = surfspot.rating ?: 0f
        } else {
            Toast.makeText(context, getString(R.string.surfspotError), Toast.LENGTH_LONG).show()
        }
    }



    override fun onResume() {
        super.onResume()
        updateViewModel.getSurfspot(loggedInViewModel.liveFirebaseUser.value?.uid!!,
            args.surfspotid)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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