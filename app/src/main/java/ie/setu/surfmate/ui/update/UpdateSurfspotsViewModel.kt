package ie.setu.surfmate.ui.update

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.setu.surfmate.firebase.FirebaseDBManager
import ie.setu.surfmate.models.SurfmateModel
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import android.content.Context


class UpdateSurfspotsViewModel() : ViewModel() {
    private val surfspot = MutableLiveData<SurfmateModel>()

    var observableSurfspot: MutableLiveData<SurfmateModel>
        get() = surfspot
        set(value) {
            surfspot.value = value.value
        }


    fun getSurfspot(userid:String, id: String) {
        try {
            FirebaseDBManager.findById(userid, id, surfspot)
            Timber.i("Detail getSurfspot() Success : ${
                surfspot.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Detail getSurfspot() Error : $e.message")
        }
    }

    fun updateSurfspot(userid:String, id: String,surfspot: SurfmateModel) {
        try {
            FirebaseDBManager.update(userid, id, surfspot)
            Timber.i("Surf spot update() Success : $surfspot")
        }
        catch (e: Exception) {
            Timber.i("Surf spot update() Error : $e.message")
        }
    }
}