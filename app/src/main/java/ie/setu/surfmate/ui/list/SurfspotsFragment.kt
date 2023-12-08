package ie.setu.surfmate.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.surfmate.adapters.SurfmateAdapter
import ie.setu.surfmate.adapters.SurfmateListener
import ie.setu.surfmate.databinding.FragmentListSpotsBinding
import ie.setu.surfmate.models.SurfmateModel
import ie.setu.surfmate.ui.auth.LoginRegisterViewModel
import timber.log.Timber

class SurfspotsFragment : Fragment(), SurfmateListener {

    private var _binding: FragmentListSpotsBinding? = null
    private val binding get() = _binding!!
    private lateinit var listViewModel: SurfspotsViewModel
    private lateinit var loginRegisterViewModel: LoginRegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListSpotsBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)

        listViewModel = ViewModelProvider(this).get(SurfspotsViewModel::class.java)
        loginRegisterViewModel = ViewModelProvider(requireActivity()).get(LoginRegisterViewModel::class.java)

        loginRegisterViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                listViewModel.setFirebaseUser(firebaseUser)
            }
        })

        listViewModel.observableSurfspotsList.observe(viewLifecycleOwner, Observer { surfspots ->
            surfspots?.let { render(surfspots as ArrayList<SurfmateModel>) }
        })

        return binding.root
    }

    override fun onSurfspotClick(surfspot: SurfmateModel) {
        Timber.i("Surfspot clicked: ${surfspot.name}")
    }

    private fun render(surfspots: ArrayList<SurfmateModel>) {
        binding.recyclerView.adapter = SurfmateAdapter(surfspots, this)
    }

    override fun onResume() {
        super.onResume()
        listViewModel.load()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}