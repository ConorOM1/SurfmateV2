package ie.setu.surfmate.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.setu.surfmate.R
import ie.setu.surfmate.adapters.SurfmateAdapter
import ie.setu.surfmate.adapters.SurfmateListener
import ie.setu.surfmate.databinding.FragmentListSpotsBinding
import ie.setu.surfmate.models.SurfmateModel
import ie.setu.surfmate.ui.auth.LoginRegisterViewModel
import timber.log.Timber
import ie.setu.surfmate.utils.SwipeToDeleteCallback
import ie.setu.surfmate.utils.SwipeToEditCallback
import ie.setu.surfmate.utils.createLoader
import ie.setu.surfmate.utils.hideLoader
import ie.setu.surfmate.utils.showLoader


class SurfspotsFragment : Fragment(), SurfmateListener {

    private var _binding: FragmentListSpotsBinding? = null
    lateinit var loader : AlertDialog
    private val binding get() = _binding!!
    private lateinit var listViewModel: SurfspotsViewModel
    private lateinit var loginRegisterViewModel: LoginRegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        setupMenu()
        loader = createLoader(requireActivity())

        _binding = FragmentListSpotsBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = SurfmateAdapter(arrayListOf(), this)
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

        setSwipeRefresh()

        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                showLoader(loader,"Deleting Surf spot")

                val adapter = binding.recyclerView.adapter as SurfmateAdapter
                if (adapter is SurfmateAdapter) {
                    adapter.removeAt(viewHolder.adapterPosition)
                    val surfmateModel = viewHolder.itemView.tag as? SurfmateModel
                    surfmateModel?.uid?.let { id ->
                        listViewModel.liveFirebaseUser.value?.uid?.let { userid ->
                            listViewModel.delete(userid, id)
                        }
                    }
                }

                hideLoader(loader)
            }
        }

        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
    itemTouchDeleteHelper.attachToRecyclerView(binding.recyclerView)

    val swipeEditHandler = object : SwipeToEditCallback(requireContext()) {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            onSurfspotClick(viewHolder.itemView.tag as SurfmateModel)
        }
    }
    val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
    itemTouchEditHelper.attachToRecyclerView(binding.recyclerView)

        return binding.root
    }


    override fun onSurfspotClick(surfspot: SurfmateModel) {
        surfspot.uid?.let { surfspotuid ->
            val action = SurfspotsFragmentDirections.actionListFragmentToUpdateSurfspotsFragment(surfspotuid)
            findNavController().navigate(action)
        } ?: Timber.e("Surfspot ID is null")
    }

    private fun setSwipeRefresh() {
        binding.swiperefresh.setOnRefreshListener {
            binding.swiperefresh.isRefreshing = true
            showLoader(loader,"Downloading Surf spots")
            listViewModel.load()
        }
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_surfspots, menu)
                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        listViewModel.filterSurfspots(newText)
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun render(surfspots: ArrayList<SurfmateModel>) {
        (binding.recyclerView.adapter as? SurfmateAdapter)?.updateData(surfspots)
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