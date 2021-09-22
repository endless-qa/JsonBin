package update

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.specification.RequestSpecification
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import testData.*
import testData.PredefinedSpecifications.requestWithContentBody
import testData.PredefinedSpecifications.requestWithEmptyBody
import testData.PredefinedSpecifications.requestWithInvalidMasterKeyHeader
import testData.PredefinedSpecifications.requestWithOnlyMasterKeyHeader
import testData.PredefinedSpecifications.requestWithoutMasterKeyHeader

@Execution(ExecutionMode.SAME_THREAD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UpdateEndpointErrorsParameterizedTests {

    @Execution(ExecutionMode.SAME_THREAD)
    @ParameterizedTest(name = "{index} : {2} - {3}")
    @MethodSource("testParameters")
    fun `Check that UPDATE endpoint returns correct error codes & messages`(
        requestSpecification: RequestSpecification,
        binId: String,
        expectedErrorCode: Int,
        expectedErrorMessage: String
    ) {
        Given {
            requestSpecification
        } When {
            put("/$binId")
        } Then {
            statusCode(expectedErrorCode)
            body("message", CoreMatchers.equalTo(expectedErrorMessage))
        }
    }

    companion object {
        @JvmStatic
        fun testParameters() = listOf(
            Arguments.of(
                requestWithOnlyMasterKeyHeader,
                EXISTING_BIN_ID,
                ServerResponses.BAD_REQUEST.code,
                "You need to pass Content-Type set to application/json"
            ),
            Arguments.of(
                requestWithOnlyMasterKeyHeader,
                INVALID_BIN_ID,
                ServerResponses.BAD_REQUEST.code,
                "Invalid Bin Id provided"
            ),
            Arguments.of(
                requestWithEmptyBody,
                EXISTING_BIN_ID,
                ServerResponses.BAD_REQUEST.code,
                "Bin cannot be blank"
            ),
            Arguments.of(
                requestWithContentBody,
                NON_EXISTING_BIN_ID,
                ServerResponses.NOT_FOUND.code,
                "Bin not found"
            ),
            Arguments.of(
                requestWithoutMasterKeyHeader,
                EXISTING_BIN_ID,
                ServerResponses.UNAUTHORIZED.code,
                "You need to pass X-Master-Key in the header to update a private bin"
            ),
            Arguments.of(
                requestWithInvalidMasterKeyHeader,
                EXISTING_BIN_ID,
                ServerResponses.UNAUTHORIZED.code,
                "Invalid X-Master-Key provided or the bin does not belong to your account"
            )
        )
    }
}