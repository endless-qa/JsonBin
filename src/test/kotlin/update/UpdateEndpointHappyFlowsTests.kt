package update

import BaseTestConfigurator
import Bin
import BinContent
import ServerResponses
import UpdatedBin
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode

@Execution(ExecutionMode.SAME_THREAD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UpdateEndpointHappyFlowsTests : BaseTestConfigurator() {
    private lateinit var bin: Bin
    private val expectedBinContent = BinContent("Question", "How're you?")
    private val updatedBinContent = BinContent("Answer", "I am fine!")

    @BeforeAll
    fun setupClient() {
        val createBinResponse = binClient.createBin(expectedBinContent.toJson())
        bin = moshi.adapter(Bin::class.java).fromJson(createBinResponse.asPrettyString())!!
    }

    @Execution(ExecutionMode.SAME_THREAD)
    @Test
    fun `Check that an existing bin's content can be updated`() {
        val serverResponse = binClient.updateBin(bin.options.id!!, updatedBinContent.toJson())
        val updatedBin = moshi.adapter(UpdatedBin::class.java).fromJson(serverResponse.asPrettyString())

        SoftAssertions().apply {
            assertThat(serverResponse.statusCode).isEqualTo(ServerResponses.SUCCESS.code)
            assertThat(updatedBin!!.content.map.keys).isEqualTo(updatedBinContent.key)
            assertThat(updatedBin.content.map.values).isEqualTo(updatedBinContent.value)
            assertThat(updatedBin.options.id).isNull()
            assertThat(updatedBin.options.parentId).isEqualTo(bin.options.id)
        }
    }
}