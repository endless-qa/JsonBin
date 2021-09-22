import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.json.JSONObject
import java.util.Date

/**
 * Different JSON models meant for the Moshi adapter for responses deserialization
 */

@JsonClass(generateAdapter = true)
data class Bin(
    @Json(name = "record") val content: Map<String, String>?,
    @Json(name = "metadata") val options: Parameters,
    val message: String?
)

@JsonClass(generateAdapter = true)
data class Parameters(
    val id: String?,
    val parentId: String?,
    val createdAt: Date?,
    @Json(name = "private") val isPrivate: Boolean?,
    val versionsDeleted: Int?
)

@JsonClass(generateAdapter = true)
data class UpdatedBin(
    @Json(name = "record") val content: ContentMap,
    @Json(name = "metadata") val options: Parameters
)

@JsonClass(generateAdapter = true)
data class ContentMap(
    @Json(name = "map") val map: Map<String, String>
)

@JsonClass(generateAdapter = true)
data class ServerError(
    val message: String
)

data class BinContent(
    val key: String,
    val value: String) {

    fun toJson(): JSONObject {
        return JSONObject().put(key, value)
    }
}