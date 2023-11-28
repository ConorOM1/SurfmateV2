package ie.setu.surfmate.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.setu.surfmate.models.SurfmateManager
import ie.setu.surfmate.models.SurfmateModel

class SurfspotsViewModel : ViewModel() {

    private val surfspots = MutableLiveData<List<SurfmateModel>>()
    val observableSurfspots: LiveData<List<SurfmateModel>>
        get() = surfspots

   init {
       load()
   }

    fun load() {
        surfspots.value = SurfmateManager.findAll()
    }
}