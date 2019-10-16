package eu.jrie.put.pod.bazeries.cipher

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class BazeriesTest {

    private lateinit var bazeries: Bazeries
    private lateinit var result: MutableList<Char>
    private val receiver: suspend (Flow<Char>) -> Unit = { flow -> flow.collect { result.add(it) } }

    @BeforeEach
    fun init() {
        result = mutableListOf()
    }

    companion object {
        private const val KEY = 900_004

        private const val BASIC_TEXT = "university"
        private const val BASIC_TEXT_ENCODED_WITH_DIGIT_KEY = "qmhatrmgxs"
        private const val BASIC_TEXT_ENCODED_WITH_FULL_NAME_KEY = "plcovulbyq"

        private const val EXTENDED_TEXT = "GżegŻóŁka#@\$"
        private const val EXTENDED_TEXT_ENCODED_WITH_DIGIT_KEY = "n&#u\$OĄ\\H8>2"
        private const val EXTENDED_TEXT_ENCODED_WITH_FULL_NAME_KEY = "n&#r\$ŃA\\F8>2"
    }

    @Test
    fun `should encode given text with basic alphabet and digit key`() {
        // given
        bazeries = Bazeries(KEY)

        // when
        bazeries.encode(BASIC_TEXT.asSequence(), receiver)

        // then
        assertEquals(BASIC_TEXT_ENCODED_WITH_DIGIT_KEY, result.joinToString(separator = ""))
    }

    @Test
    fun `should encode given text with basic alphabet and full name key`() {
        // given
        bazeries = Bazeries(KEY, useFullNumberName = true)

        // when
        bazeries.encode(BASIC_TEXT.asSequence(), receiver)

        // then
        assertEquals(BASIC_TEXT_ENCODED_WITH_FULL_NAME_KEY, result.joinToString(separator = ""))
    }

    @Test
    fun `should decode given text with basic alphabet and digit key`() {
        // given
        bazeries = Bazeries(KEY)

        // when
        bazeries.decode(BASIC_TEXT_ENCODED_WITH_DIGIT_KEY.asSequence(), receiver)

        // then
        assertEquals(BASIC_TEXT, result.joinToString(separator = ""))
    }

    @Test
    fun `should decode given text with basic alphabet and full name key`() {
        // given
        bazeries = Bazeries(KEY, useFullNumberName = true)

        // when
        bazeries.decode(BASIC_TEXT_ENCODED_WITH_FULL_NAME_KEY.asSequence(), receiver)

        // then
        assertEquals(BASIC_TEXT, result.joinToString(separator = ""))
    }

    @Test
    fun `should encode given text with extended alphabet and digit key`() {
        // given
        bazeries = Bazeries(KEY, useExtendedAlphabet = true)

        // when
        bazeries.encode(EXTENDED_TEXT.asSequence(), receiver)

        // then
        assertEquals(EXTENDED_TEXT_ENCODED_WITH_DIGIT_KEY, result.joinToString(separator = ""))
    }

    @Test
    fun `should encode given text with extended alphabet and full name key`() {
        // given
        bazeries = Bazeries(KEY, useFullNumberName = true, useExtendedAlphabet = true)

        // when
        bazeries.encode(EXTENDED_TEXT.asSequence(), receiver)

        // then
        assertEquals(EXTENDED_TEXT_ENCODED_WITH_FULL_NAME_KEY, result.joinToString(separator = ""))
    }

    @Test
    fun `should decode given text with extended alphabet and digit key`() {
        // given
        bazeries = Bazeries(KEY, useExtendedAlphabet = true)

        // when
        bazeries.decode(EXTENDED_TEXT_ENCODED_WITH_DIGIT_KEY.asSequence(), receiver)

        // then
        assertEquals(EXTENDED_TEXT, result.joinToString(separator = ""))
    }

    @Test
    fun `should decode given text with extended alphabet and full name key`() {
        // given
        bazeries = Bazeries(KEY, useFullNumberName = true, useExtendedAlphabet = true)

        // when
        bazeries.decode(EXTENDED_TEXT_ENCODED_WITH_FULL_NAME_KEY.asSequence(), receiver)

        // then
        assertEquals(EXTENDED_TEXT, result.joinToString(separator = ""))
    }
}
