package ie.setu.surfmate.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

object SurfmateManager : SurfmateStore {

    private val surfspots = ArrayList<SurfmateModel>()



//    override fun update(surfspot: SurfmateModel) {
//        var foundSurfspot: SurfmateModel? = surfspots.find { l -> l.id == surfspot.id }
//        if (foundSurfspot != null) {
//            foundSurfspot.name = surfspot.name
//            foundSurfspot.observations = surfspot.observations
//            foundSurfspot.image = surfspot.image
//            foundSurfspot.lat = surfspot.lat
//            foundSurfspot.lng = surfspot.lng
//            foundSurfspot.zoom = surfspot.zoom
//            foundSurfspot.rating = surfspot.rating
//            logAll()
//        }
//    }


    fun logAll() {
        surfspots.forEach { Timber.i("${it}") }
    }

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
        TODO("Not yet implemented")
    }

    override fun delete(userid: String, surfspotid: String) {
        TODO("Not yet implemented")
    }

    override fun update(userid: String, surfspotid: String, surfspot: SurfmateModel) {
        TODO("Not yet implemented")
    }


}