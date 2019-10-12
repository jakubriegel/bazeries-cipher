package eu.jrie.put.pod.bazeries.cipher

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CipherTest {

    private lateinit var bazeries: Bazeries

    @BeforeEach
    fun initBazeries() {
        bazeries = Bazeries(KEY)
    }

    companion object {
        private const val KEY = 900_004
        private const val TEXT = "university"
        private const val TEXT_ENCODED = "qmhatrmgxs"
    }

    @Test
    fun `should encode given text with basic alphabet`() {
        // when
        val result = cipher(TEXT, KEY)

        // then
        assertEquals(TEXT_ENCODED, result)
    }

    @Test
    fun `should encode given file with basic alphabet`() {
        // given
        val file = createTempFile().apply { writeText(TEXT) }

        // when
        val result = cipher(file, KEY)

        // then
        assertEquals(TEXT_ENCODED, result.readText())
    }

    @Test
    fun `should decode given text with basic alphabet`() {
    }

    @Test
    fun `should decode given file with basic alphabet`() {

    }
}
