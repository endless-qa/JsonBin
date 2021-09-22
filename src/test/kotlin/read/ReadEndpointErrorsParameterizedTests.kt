package read

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
import testData.*
import testData.PredefinedSpecifications.requestWithInvalidMasterKeyHeader
import testData.PredefinedSpecifications.requestWithOnlyMasterKeyHeader
import testData.PredefinedSpecifications.requestWithoutMasterKeyHeader

@Execution(ExecutionMode.SAME_THREAD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReadEndpointErrorsParameterizedTests {

    @Execution(ExecutionMode.SAME_THREAD)
    @ParameterizedTest(name = "{index} : {2} - {3}")
    @MethodSource("testParameters")
    fun `Check that READ endpoint returns correct error codes & messages`(
        requestSpecification: RequestSpecification,
        binId: String,
        expectedErrorCode: Int,
        expectedErrorMessage: String
    ) {
        Given {
            requestSpecification
        } When {
            get("/$binId")
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
                INVALID_BIN_ID,
                ServerResponses.BAD_REQUEST.code,
                "Invalid Bin Id provided"
            ),
            Arguments.of(
                requestWithOnlyMasterKeyHeader,
                EXISTING_BIN_VERSION_INVALID,
                ServerResponses.BAD_REQUEST.code,
                "Bin version is invalid"
            ),
            Arguments.of(
                requestWithOnlyMasterKeyHeader,
                NON_EXISTING_BIN_ID,
                ServerResponses.NOT_FOUND.code,
                "Bin not found or it doesn't belong to your account"
            ),
            Arguments.of(
                requestWithOnlyMasterKeyHeader,
                EXISTING_BIN_VERSION_NOT_FOUND,
                ServerResponses.NOT_FOUND.code,
                "Bin version not found"
            ),
            Arguments.of(
                requestWithoutMasterKeyHeader,
                EXISTING_BIN_ID,
                ServerResponses.UNAUTHORIZED.code,
                "You need to pass X-Master-Key in the header to read a private bin"
            ),
            Arguments.of(
                requestWithInvalidMasterKeyHeader,
                EXISTING_BIN_ID,
                ServerResponses.UNAUTHORIZED.code,
                "X-Master-Key is invalid or the bin doesn't belong to your account"
            )
        )
    }
}