package create

import BaseTestConfigurator
import Bin
import BinContent
import ServerResponses
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode

@Execution(ExecutionMode.SAME_THREAD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateEndpointHappyFlowsTests : BaseTestConfigurator() {
    private val expectedBinContent = BinContent("Question", "How're you?")

    @Execution(ExecutionMode.SAME_THREAD)
    @Test
    fun `Check that a new private bin with the expected content can be created`() {
        val serverResponse = binClient.createBin(expectedBinContent.toJson())
        val createdBin = moshi.adapter(Bin::class.java).fromJson(serverResponse.asPrettyString())

        SoftAssertions().apply {
            assertThat(serverResponse.statusCode).isEqualTo(ServerResponses.SUCCESS.code)
            assertThat(createdBin!!.content!!.keys.first()).isEqualTo(expectedBinContent.key)
            assertThat(createdBin.content!!.values.first()).isEqualTo(expectedBinContent.value)
            assertThat(createdBin.options.id).isNotBlank
            assertThat(createdBin.options.isPrivate).isTrue
            assertThat(createdBin.options.createdAt).isNotNull
        }.assertAll()
    }

    @Execution(ExecutionMode.SAME_THREAD)
    @Test
    fun `Check that a new public bin with the expected content can be created`() {
        val serverResponse = binClient.createBin(expectedBinContent.toJson(), isPrivate = false)
        val createdBin = moshi.adapter(Bin::class.java).fromJson(serverResponse.asPrettyString())

        SoftAssertions().apply {
            assertThat(serverResponse.statusCode).isEqualTo(ServerResponses.SUCCESS.code)
            assertThat(createdBin!!.content!!.keys.first()).isEqualTo(expectedBinContent.key)
            assertThat(createdBin.content!!.values.first()).isEqualTo(expectedBinContent.value)
            assertThat(createdBin.options.id).isNotBlank
            assertThat(createdBin.options.isPrivate).isFalse
            assertThat(createdBin.options.createdAt).isNotNull
        }.assertAll()
    }
}