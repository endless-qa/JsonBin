import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.json.JSONObject

/**
 * Entry point for helper wrapper methods for basic CRUD operations with the Json Bin API
 */
class JsonBinClient {
    private var clientRequestSpecification: RequestSpecification = RestAssured
        .given()
        .baseUri("https://api.jsonbin.io/v3")
        .basePath("/b")
        .header(ServerHeaders.X_MASTER_KEY.header, AuthUtil.getToken())

    init {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    /**
     * Creates a new bin with the content provided
     * @param content a content to be placed into a new bin
     * @param isPrivate option to determine whether a new bin has to be private (default) or not
     * @return a server's response for further validations
     */
    fun createBin(content: JSONObject, isPrivate: Boolean = true): Response {
        return RestAssured
            .given(clientRequestSpecification)
            .contentType(ContentType.JSON)
            .header(ServerHeaders.X_BIN_PRIVATE.header, isPrivate)
            .body(content.toString())
            .post()
    }

    /**
     * Fetches the bin's content and metadata
     * @param binId an ID of the bin to be retrieved
     * @param fetchMetadata option to determine whether the bin's metadata has to be also fetched (default) or not
     * @return a server's response for further validations
     */
    fun readBin(binId: String, fetchMetadata: Boolean = true): Response {
        return RestAssured
            .given(clientRequestSpecification)
            .header(ServerHeaders.X_BIN_META.header, fetchMetadata)
            .get("/$binId")
    }

    /**
     * Updates the bin with the specified content
     * @param binId an ID of the bin to be updated
     * @param content a new content to be placed into the bin
     * @return a server's response for further validations
     */
    fun updateBin(binId: String, content: JSONObject): Response {
        return RestAssured
            .given(clientRequestSpecification)
            .contentType(ContentType.JSON)
            .header(ServerHeaders.X_BIN_VERSIONING.header, false)
            .body(content)
            .put("/$binId")
    }

    /**
     * Deletes the bin by the ID
     * @param binId an ID of the bin to be deleted
     * @return a server's response for further validations
     */
    fun deleteBin(binId: String): Response {
        return RestAssured
            .given(clientRequestSpecification)
            .delete("/$binId")
    }
}

/**
 * Class-holder of possible Json Bin server response codes for better readability
 */
enum class ServerResponses(val code: Int) {
    SUCCESS(200),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404)
}

/**
 * Class-holder of possible Json Bin requests headers for better readability
 */
enum class ServerHeaders(val header: String) {
    X_MASTER_KEY("X-Master-Key"),
    X_COLLECTION_ID("X-Collection-Id"),
    X_BIN_PRIVATE("X-Bin-Private"),
    X_BIN_META("X-Bin-Meta"),
    X_BIN_VERSIONING("X-Bin-Versioning")
}