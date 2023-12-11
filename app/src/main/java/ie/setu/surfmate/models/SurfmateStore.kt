package ie.setu.surfmate.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface SurfmateStore {
    fun findAll(surfspots:
                MutableLiveData<List<SurfmateModel>>
    )
    fun findAll(userid:String,
                surfspots:
                MutableLiveData<List<SurfmateModel>>)
    fun findById(userid:String, surfspotid: String,
                 surfspot: MutableLiveData<SurfmateModel>)
    fun create(firebaseUser: MutableLiveData<FirebaseUser>, surfspot: SurfmateModel)
    fun delete(userid:String, surfspotid: String, callback: (Boolean) -> Unit)
    fun update(userid:String, surfspotid: String, surfspot: SurfmateModel)
}