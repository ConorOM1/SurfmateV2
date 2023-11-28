package ie.setu.surfmate.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.setu.surfmate.models.SurfmateManager
import ie.setu.surfmate.models.SurfmateModel
import timber.log.Timber

class AddSpotViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()
    val observableStatus: LiveData<Boolean>
        get() = status

    fun addSurfspot(surfspot: SurfmateModel) {
        status.value = try {
            SurfmateManager.create(surfspot)
            Timber.i("Added new surf spot: $surfspot")
            true
        } catch (e: Exception) {
            Timber.e("Error adding new surf spot: $e")
            false
        }
    }

    fun updateSurfspot(surfspot: SurfmateModel) {
        status.value = try {
            SurfmateManager.update(surfspot)
            Timber.i("Updated surf spot: $surfspot")
            true
        } catch (e: Exception) {
            Timber.e("Error updating surf spot: $e")
            false
        }
    }

    fun deleteSurfspot(surfspot: SurfmateModel) {
        try {
            SurfmateManager.delete(surfspot)
            status.value = true
        } catch (e: Exception) {
            Timber.e(e, "Error deleting surf spot")
            status.value = false
        }
    }
}
