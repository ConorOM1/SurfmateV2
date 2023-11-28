package ie.setu.surfmate.models

import timber.log.Timber

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

object SurfmateManager : SurfmateStore {

    private val surfspots = ArrayList<SurfmateModel>()

    override fun findAll(): List<SurfmateModel> {
        return surfspots
    }

    override fun create(surfspot: SurfmateModel) {
        surfspot.id = getId()
        surfspots.add(surfspot)
        logAll()
    }

    override fun update(surfspot: SurfmateModel) {
        var foundSurfspot: SurfmateModel? = surfspots.find { l -> l.id == surfspot.id }
        if (foundSurfspot != null) {
            foundSurfspot.name = surfspot.name
            foundSurfspot.observations = surfspot.observations
            foundSurfspot.image = surfspot.image
            foundSurfspot.lat = surfspot.lat
            foundSurfspot.lng = surfspot.lng
            foundSurfspot.zoom = surfspot.zoom
            foundSurfspot.rating = surfspot.rating
            logAll()
        }
    }

    override fun delete(surfspot: SurfmateModel) {
        surfspots.remove(surfspot)
        logAll()
    }

    fun logAll() {
        surfspots.forEach { Timber.i("${it}") }
    }


}