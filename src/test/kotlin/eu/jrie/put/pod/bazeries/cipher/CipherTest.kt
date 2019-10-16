package eu.jrie.put.pod.bazeries.cipher

import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    @ExperimentalCoroutinesApi
    fun `should encode given text with basic alphabet and digit name key`() {
        // when
        val result = cipher(TEXT, KEY, useFullNumberKeyName = false, useExtendedAlphabet = false)

        // then
        assertEquals(TEXT_ENCODED, result)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `should encode given file with basic alphabet and digit name key`() {
        // given
        val file = createTempFile().apply { writeText(TEXT) }

        // when
        val result = cipher(file, KEY, useFullNumberKeyName = false, useExtendedAlphabet = false)

        // then
        assertEquals(TEXT_ENCODED, result.readText())
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `should decode given text with basic alphabet`() {
        // when
        val result = decipher(TEXT_ENCODED, KEY, useFullNumberKeyName = false, useExtendedAlphabet = false)

        // then
        assertEquals(TEXT, result)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `should decode given file with basic alphabet`() {
        // given
        val file = createTempFile().apply { writeText(TEXT_ENCODED) }

        // when
        val result = decipher(file, KEY, useFullNumberKeyName = false, useExtendedAlphabet = false)

        // then
        assertEquals(TEXT, result.readText())
    }
}
