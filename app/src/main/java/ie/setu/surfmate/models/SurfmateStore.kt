package ie.setu.surfmate.models

interface SurfmateStore {
    fun findAll(): List<SurfmateModel>
    fun create(surfspot: SurfmateModel)
    fun update(surfspot: SurfmateModel)
    fun delete(surfspot: SurfmateModel)
}