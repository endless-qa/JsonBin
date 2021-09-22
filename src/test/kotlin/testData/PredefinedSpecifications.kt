package testData

import BinContent
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.json.JSONObject

/**
 * A storage of some predefined RestAssured request specifications that can be re-used in tests
 */
object PredefinedSpecifications {
    private val apiToken = AuthUtil.getToken()
    private val binContent = BinContent("Question", "How're you?")
    private var baseSpecification: RequestSpecification = RestAssured
        .given()
        .baseUri(BASE_URL)
        .basePath("/b")

    val requestWithOnlyMasterKeyHeader: RequestSpecification =
        RestAssured.given(baseSpecification)
            .header(ServerHeaders.X_MASTER_KEY.header, AuthUtil.getToken())

    val requestWithEmptyBody: RequestSpecification =
        RestAssured.given(baseSpecification)
            .header(ServerHeaders.X_MASTER_KEY.header, apiToken)
            .contentType(ContentType.JSON)
            .body(JSONObject().toString())

    val requestWithContentBody: RequestSpecification =
        RestAssured.given(baseSpecification)
            .header(ServerHeaders.X_MASTER_KEY.header, apiToken)
            .contentType(ContentType.JSON)
            .body(binContent.toJson())

    val requestWithInvalidCollectionId: RequestSpecification =
        RestAssured.given(baseSpecification)
            .header(ServerHeaders.X_MASTER_KEY.header, apiToken)
            .header(ServerHeaders.X_COLLECTION_ID.header, "(%&*%%&^")
            .contentType(ContentType.JSON)
            .body(binContent.toJson())

    val requestWithNotFoundCollection: RequestSpecification =
        RestAssured.given(baseSpecification)
            .header(ServerHeaders.X_MASTER_KEY.header, apiToken)
            .header(ServerHeaders.X_COLLECTION_ID.header, "614b5437aa02be1d444cf999")
            .contentType(ContentType.JSON)
            .body(binContent.toJson())

    val requestWithoutMasterKeyHeader: RequestSpecification =
        RestAssured.given(baseSpecification)
            .contentType(ContentType.JSON)
            .body(binContent.toJson())

    val requestWithInvalidMasterKeyHeader: RequestSpecification =
        RestAssured.given(baseSpecification)
            .header(ServerHeaders.X_MASTER_KEY.header, "Maybe I'm lucky and this is gonna work?")
            .contentType(ContentType.JSON)
            .body(binContent)
}