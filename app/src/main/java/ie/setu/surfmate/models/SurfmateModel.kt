package ie.setu.surfmate.models

import android.net.Uri
import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties

@Parcelize
data class SurfmateModel(
    var id: Long = 0,
    var uid: String? = "",
    var name: String = "",
    var observations: String = "",
    var image: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f,
    var rating: Float = 0f,
    var email: String? = "joe@bloggs.com",
) : Parcelable

{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "id" to id,
            "name" to name,
            "observations" to observations,
            "image" to image,
            "lat" to lat,
            "lng" to lng,
            "zoom" to zoom,
            "rating" to rating,
            "email" to email
        )
    }
}
@Parcelize
data class Location(
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f
) : Parcelable