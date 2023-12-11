package ie.setu.surfmate.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import ie.setu.surfmate.models.SurfmateModel
import ie.setu.surfmate.models.SurfmateStore
import timber.log.Timber

object FirebaseDBManager : SurfmateStore {
    var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    override fun findAll(surfspots: MutableLiveData<List<SurfmateModel>>) {
        TODO("Not yet implemented")
    }

    override fun findAll(userid: String, surfspots: MutableLiveData<List<SurfmateModel>>) {

        database.child("user-surfspots").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Surf spot error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<SurfmateModel>()
                    val children = snapshot.children
                    children.forEach {
                        val surfspot = it.getValue(SurfmateModel::class.java)
                        surfspot?.let { localList.add(it) }
                    }
                    database.child("user-surfspots").child(userid)
                        .removeEventListener(this)

                    surfspots.value = localList
                }
            })
    }

    override fun findById(userid: String, surfspotid: String, surfspot: MutableLiveData<SurfmateModel>) {

        database.child("user-surfspots").child(userid)
            .child(surfspotid).get().addOnSuccessListener {
                surfspot.value = it.getValue(SurfmateModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener{
                Timber.e("firebase Error getting data $it")
            }
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

    override fun delete(userid: String, surfspotid: String, callback: (Boolean) -> Unit) {
        val childDelete : MutableMap<String, Any?> = HashMap()
        childDelete["/surfspots/$surfspotid"] = null
        childDelete["/user-surfspots/$userid/$surfspotid"] = null

        database.updateChildren(childDelete) { databaseError, _ ->
            if (databaseError != null) {
                Timber.e("Firebase deletion failed: ${databaseError.message}")
            } else {
                Timber.i("Firebase deletion succeeded")
            }
        }
    }


    override fun update(userid: String, surfspotid: String, surfspot: SurfmateModel) {

        val surfspotValues = surfspot.toMap()

        val childUpdate : MutableMap<String, Any?> = HashMap()
        childUpdate["surfspots/$surfspotid"] = surfspotValues
        childUpdate["user-surfspots/$userid/$surfspotid"] = surfspotValues

        database.updateChildren(childUpdate)
    }

}