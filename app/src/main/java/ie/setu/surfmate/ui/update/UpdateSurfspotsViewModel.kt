package ie.setu.surfmate.ui.update

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.setu.surfmate.firebase.FirebaseDBManager
import ie.setu.surfmate.models.SurfmateModel
import timber.log.Timber

class UpdateSurfspotsViewModel() : ViewModel() {
    private val surfspot = MutableLiveData<SurfmateModel>()

    var observableSurfspot: MutableLiveData<SurfmateModel>
        get() = surfspot
        set(value) {
            surfspot.value = value.value
        }


    fun getSurfspot(userid:String, uid: String) {
        try {
            FirebaseDBManager.findById(userid, uid, surfspot)
            Timber.i("Detail getSurfspot() Success : ${
                surfspot.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Detail getSurfspot() Error : $e.message")
        }
    }

    fun updateSurfspot(userid:String, uid: String,surfspot: SurfmateModel) {
        try {
            FirebaseDBManager.update(userid, uid, surfspot)
            Timber.i("Surf spot update() Success : $surfspot")
        }
        catch (e: Exception) {
            Timber.i("Surf spot update() Error : $e.message")
        }
    }
}