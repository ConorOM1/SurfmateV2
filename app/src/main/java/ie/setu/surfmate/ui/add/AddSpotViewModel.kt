package ie.setu.surfmate.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.setu.surfmate.firebase.FirebaseDBManager
import ie.setu.surfmate.models.SurfmateManager
import ie.setu.surfmate.models.SurfmateModel
import timber.log.Timber

class AddSpotViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()
    val observableStatus: LiveData<Boolean>
        get() = status

    fun addSurfspot(firebaseUser: MutableLiveData<FirebaseUser>,
                    surfspot: SurfmateModel) {
        status.value = try {
            //SurfmateManager.create(surfspot)
            FirebaseDBManager.create(firebaseUser,surfspot)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

//    fun updateSurfspot(surfspot: SurfmateModel) {
//        status.value = try {
//            SurfmateManager.update(surfspot)
//            Timber.i("Updated surf spot: $surfspot")
//            true
//        } catch (e: Exception) {
//            Timber.e("Error updating surf spot: $e")
//            false
//        }
//    }
//
//    fun deleteSurfspot(surfspot: SurfmateModel) {
//        try {
//            SurfmateManager.delete(surfspot)
//            status.value = true
//        } catch (e: Exception) {
//            Timber.e(e, "Error deleting surf spot")
//            status.value = false
//        }
//    }
}
