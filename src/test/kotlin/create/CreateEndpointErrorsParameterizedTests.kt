package create

import ServerResponses
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.specification.RequestSpecification
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import testData.PredefinedSpecifications.requestWithEmptyBody
import testData.PredefinedSpecifications.requestWithInvalidCollectionId
import testData.PredefinedSpecifications.requestWithNotFoundCollection
import testData.PredefinedSpecifications.requestWithOnlyMasterKeyHeader
import testData.PredefinedSpecifications.requestWithoutMasterKeyHeader

@Execution(ExecutionMode.SAME_THREAD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateEndpointErrorsParameterizedTests {

    @Execution(ExecutionMode.SAME_THREAD)
    @ParameterizedTest(name = "{index} : {1} - {2}")
    @MethodSource("testParameters")
    fun `Check that CREATE endpoint returns correct error codes & messages`(
        requestSpecification: RequestSpecification,
        expectedErrorCode: Int,
        expectedErrorMessage: String
    ) {
        Given {
            requestSpecification
        } When {
            post()
        } Then {
            statusCode(expectedErrorCode)
            body("message", equalTo(expectedErrorMessage))
        }
    }

    companion object {
        @JvmStatic
        fun testParameters() = listOf(
            Arguments.of(
                requestWithOnlyMasterKeyHeader,
                ServerResponses.BAD_REQUEST.code,
                "You need to pass Content-Type set to application/json"
            ),
            Arguments.of(
                requestWithEmptyBody,
                ServerResponses.BAD_REQUEST.code,
                "Bin cannot be blank"
            ),
            Arguments.of(
                requestWithInvalidCollectionId,
                ServerResponses.BAD_REQUEST.code,
                "Invalid X-Collection-Id provided"
            ),
            Arguments.of(
                requestWithNotFoundCollection,
                ServerResponses.BAD_REQUEST.code,
                "Collection not found or the Collection does not belong to the X-Master-Key provided"
            ),
            Arguments.of(
                requestWithoutMasterKeyHeader,
                ServerResponses.UNAUTHORIZED.code,
                "You need to pass X-Master-Key in the header"
            )
        )
    }
}