package ie.setu.surfmate.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.setu.surfmate.firebase.FirebaseDBManager
import ie.setu.surfmate.models.SurfmateModel
import timber.log.Timber

class SurfspotsViewModel : ViewModel() {

    private val surfspots = MutableLiveData<List<SurfmateModel>>()
    val observableSurfspotsList: LiveData<List<SurfmateModel>>
        get() = surfspots
    private var firebaseUser: FirebaseUser? = null
    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    fun setFirebaseUser(user: FirebaseUser) {
        firebaseUser = user
        liveFirebaseUser.value = user
        load()
    }

    fun load() {
        try {
            FirebaseDBManager.findAll(firebaseUser!!.uid, surfspots)
            Timber.i("Surf spots Load Success : ${surfspots.value.toString()}")
        } catch (e: Exception) {
            Timber.i("Surf spots Load Error : ${e.message}")
        }
    }

    fun delete(userid: String, id: String?) {
        if (id.isNullOrEmpty()) {
            Timber.e("Invalid 'id' for deletion")
            return
        }

        FirebaseDBManager.delete(userid, id) { success ->
            if (success) {
                Timber.i("Surf spot Delete Success")
            } else {
                Timber.e("Surf spot Delete Error")
            }
        }
    }

    fun filterSurfspots(query: String?) {
        val filteredSpots = surfspots.value?.filter {
            it.name?.contains(query ?: "", ignoreCase = true) == true
        }
        surfspots.value = filteredSpots.orEmpty()
    }
}