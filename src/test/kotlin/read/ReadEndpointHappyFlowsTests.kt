package read

import BaseTestConfigurator
import Bin
import BinContent
import ServerResponses
import org.assertj.core.api.SoftAssertions
import org.json.JSONObject
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode

@Execution(ExecutionMode.SAME_THREAD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReadEndpointHappyFlowsTests : BaseTestConfigurator() {
    private lateinit var bin: Bin
    private val expectedBinContent = BinContent("Question", "How're you?")

    @BeforeAll
    fun setupClient() {
        val createBinResponse = binClient.createBin(expectedBinContent.toJson())
        bin = moshi.adapter(Bin::class.java).fromJson(createBinResponse.asPrettyString())!!
    }

    @Execution(ExecutionMode.SAME_THREAD)
    @Test
    fun `Check that the content of an existing bin can be read`() {
        val serverResponse = binClient.readBin(bin.options.id!!, fetchMetadata = false)
        val fetchedBinContent = JSONObject(serverResponse.asPrettyString())

        SoftAssertions().apply {
            assertThat(serverResponse.statusCode).isEqualTo(ServerResponses.SUCCESS.code)
            assertThat(fetchedBinContent.toMap().keys).isEqualTo(expectedBinContent.key)
            assertThat(fetchedBinContent.toMap().values).isEqualTo(expectedBinContent.value)
            assertThat(fetchedBinContent.has("metadata")).isFalse
        }
    }

    @Execution(ExecutionMode.SAME_THREAD)
    @Test
    fun `Check that the content and metadata of an existing bin can be fetched`() {
        val serverResponse = binClient.readBin(bin.options.id!!)
        val fetchedBin = moshi.adapter(Bin::class.java).fromJson(serverResponse.asPrettyString())

        SoftAssertions().apply {
            assertThat(serverResponse.statusCode).isEqualTo(ServerResponses.SUCCESS.code)
            assertThat(fetchedBin!!.content!!.keys.first()).isEqualTo(expectedBinContent.key)
            assertThat(fetchedBin.content!!.values.first()).isEqualTo(expectedBinContent.value)
            assertThat(fetchedBin.options.id).isEqualTo(bin.options.id)
            assertThat(fetchedBin.options.isPrivate).isEqualTo(bin.options.isPrivate)
            assertThat(fetchedBin.options.createdAt).isNull()
        }
    }
}