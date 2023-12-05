package ie.setu.surfmate.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import ie.setu.surfmate.firebase.FirebaseDBManager.database
import ie.setu.surfmate.models.SurfmateModel
import ie.setu.surfmate.models.SurfmateStore
import timber.log.Timber

object FirebaseDBManager : SurfmateStore {
    var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    override fun findAll(surfspotsList: MutableLiveData<List<SurfmateModel>>) {
        TODO("Not yet implemented")
    }

    override fun findAll(userid: String, surfspotsList: MutableLiveData<List<SurfmateModel>>) {
        TODO("Not yet implemented")
    }

    override fun findById(
        userid: String,
        surfspotid: String,
        surfspot: MutableLiveData<SurfmateModel>
    ) {
        TODO("Not yet implemented")
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, surfspot: SurfmateModel) {
        Timber.i("Firebase DB Reference : $database")

        val uid = firebaseUser.value!!.uid
        val key = database.child("surfspots").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        surfspot.uid = key
        val surfspotValues = surfspot.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/surfspots/$key"] = surfspotValues
        childAdd["/user-surfspots/$uid/$key"] = surfspotValues

        database.updateChildren(childAdd)
    }

    override fun delete(userid: String, surfspotid: String) {
        TODO("Not yet implemented")
    }

    override fun update(userid: String, surfspotid: String, surfspot: SurfmateModel) {
        TODO("Not yet implemented")
    }

}
    fun findAll(surfspotsList: MutableLiveData<List<SurfmateModel>>) {
        TODO("Not yet implemented")
    }

    fun findAll(userid: String, surfspotsList: MutableLiveData<List<SurfmateModel>>) {
        TODO("Not yet implemented")
    }

    fun findById(userid: String, surfspotid: String, surfspot: MutableLiveData<SurfmateModel>) {
        TODO("Not yet implemented")
    }



    fun delete(userid: String, surfspotid: String) {
        TODO("Not yet implemented")
    }