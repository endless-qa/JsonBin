package delete

import BaseTestConfigurator
import Bin
import BinContent
import ServerResponses
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode

@Execution(ExecutionMode.SAME_THREAD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeleteEndpointHappyFlowsTests : BaseTestConfigurator() {
    private lateinit var bin: Bin
    private val expectedBinContent = BinContent("Question", "How're you?")

    @BeforeAll
    fun setupClient() {
        val createdBinResponse = binClient.createBin(expectedBinContent.toJson())
        bin = moshi.adapter(Bin::class.java).fromJson(createdBinResponse.asPrettyString())!!
    }

    @Execution(ExecutionMode.SAME_THREAD)
    @Test
    fun `Check that an existing bin can be deleted`() {
        val serverResponse = binClient.deleteBin(bin.options.id!!)
        val deletedBin = moshi.adapter(Bin::class.java).fromJson(serverResponse.asPrettyString())

        SoftAssertions().apply {
            assertThat(serverResponse.statusCode).isEqualTo(ServerResponses.SUCCESS.code)
            assertThat(deletedBin!!.content).isNull()
            assertThat(deletedBin.options.id).isEqualTo(bin.options.id)
            assertThat(deletedBin.message).isEqualTo("Bin deleted successfully")
        }
    }
}