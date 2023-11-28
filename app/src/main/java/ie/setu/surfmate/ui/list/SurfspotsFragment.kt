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
import ie.setu.surfmate.main.SurfmateApp
import ie.setu.surfmate.models.SurfmateModel
import timber.log.Timber

class SurfspotsFragment : Fragment(), SurfmateListener {

    private var _binding: FragmentListSpotsBinding? = null

    private val binding get() = _binding!!
    lateinit var app: SurfmateApp
    private lateinit var listViewModel: SurfspotsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("ON CREATE LIST FRAGMENT")

    }

    override fun onSurfspotClick(surfspot: SurfmateModel) {
        // TBD edit surf spot
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(SurfspotsViewModel::class.java)

        _binding = FragmentListSpotsBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        listViewModel = ViewModelProvider(this).get(SurfspotsViewModel::class.java)
        listViewModel.observableSurfspots.observe(viewLifecycleOwner, Observer {
                surfspots ->
            surfspots?.let { render(surfspots)}
        })
        return binding.root
    }

    private fun render(surfspots: List<SurfmateModel>) {
        binding.recyclerView.adapter = SurfmateAdapter(surfspots,this)
        if (surfspots.isEmpty()) {
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.recyclerView.visibility = View.VISIBLE
        }
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