package ie.setu.surfmate.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class SurfmateModel(
    var email: String? = "joe@bloggs.com",
    var id: String? = "",
    var image: String? = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var name: String? = "",
    var observations: String? = "",
    var rating: Float? = 0f,
    var uid: String? = "",
    var zoom: Float? = 0f,


) : Parcelable

{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "email" to email,
            "id" to id,
            "image" to image,
            "lat" to lat,
            "lng" to lng,
            "name" to name,
            "observations" to observations,
            "rating" to rating,
            "uid" to uid,
            "zoom" to zoom,
        )
    }
}
//@Parcelize
//data class Location(
//    var lat: Double = 0.0,
//    var lng: Double = 0.0,
//    var zoom: Float = 0f
//) : Parcelable