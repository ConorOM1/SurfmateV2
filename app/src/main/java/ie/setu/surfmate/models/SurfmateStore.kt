package ie.setu.surfmate.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface SurfmateStore {
    fun findAll(surfspotsList:
                MutableLiveData<List<SurfmateModel>>
    )
    fun findAll(userid:String,
                surfspotsList:
                MutableLiveData<List<SurfmateModel>>)
    fun findById(userid:String, surfspotid: String,
                 surfspot: MutableLiveData<SurfmateModel>)
    fun create(firebaseUser: MutableLiveData<FirebaseUser>, surfspot: SurfmateModel)
    fun delete(userid:String, surfspotid: String)
    fun update(userid:String, surfspotid: String, surfspot: SurfmateModel)
}