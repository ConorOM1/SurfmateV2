package ie.setu.surfmate.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.setu.surfmate.firebase.FirebaseDBManager
import ie.setu.surfmate.models.SurfmateManager
import ie.setu.surfmate.models.SurfmateModel
import timber.log.Timber

class SurfspotsViewModel : ViewModel() {

    private val surfspots = MutableLiveData<List<SurfmateModel>>()
    val observableSurfspots: LiveData<List<SurfmateModel>>
        get() = surfspots

    private val surfspotsList =
        MutableLiveData<List<SurfmateModel>>()

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()


    init {
       load()
   }

    fun load() {
        try {
            //SurfmateManager.findAll(liveFirebaseUser.value?.email!!, surfspotsList)
            FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!!,surfspotsList)
            Timber.i("Report Load Success : ${surfspotsList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Report Load Error : $e.message")
        }
    }
}