package eu.jrie.put.pod.bazeries.cipher

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class BazeriesTest {

    private lateinit var bazeries: Bazeries

    @BeforeEach
    fun initBazeries() {
        bazeries = Bazeries(KEY)
    }

    companion object {
        private const val KEY = 900_004

        private const val BASIC_TEXT = "university"
        private const val BASIC_TEXT_ENCODED_WITH_DIGIT_KEY = "qmhatrmgxs"
        private const val BASIC_TEXT_ENCODED_WITH_FULL_NAME_KEY = "plcovulbyq"

        private const val EXTENDED_TEXT = ""
        private const val EXTENDED_TEXT_ENCODED_WITH_DIGIT_KEY = ""
        private const val EXTENDED_TEXT_ENCODED_WITH_FULL_NAME_KEY = ""

        private const val BASIC_FILE_NAME = "basic_file.txt"
        private const val EXTENDED_FILE_NAME = "extended_file.txt"
    }

    @Test
    fun `should encode given text with basic alphabet and digit key`() {
        // given
        val result = mutableListOf<Char>()
        val receiver: suspend (Flow<Char>) -> Unit = { flow -> flow.collect { result.add(it) } }

        // when
        bazeries.encode(BASIC_TEXT.byteInputStream(), receiver)

        // then
        assertEquals(BASIC_TEXT_ENCODED_WITH_DIGIT_KEY, result.joinToString(separator = ""))
    }

    @Test
    fun `should encode given text with basic alphabet and full name key`() {
        // given
        val fullNameBazeries = Bazeries(KEY, useFullNumberName = true)
        val result = mutableListOf<Char>()
        val receiver: suspend (Flow<Char>) -> Unit = { flow -> flow.collect { result.add(it) } }

        // when
        fullNameBazeries.encode(BASIC_TEXT.byteInputStream(), receiver)

        // then
        assertEquals(BASIC_TEXT_ENCODED_WITH_FULL_NAME_KEY, result.joinToString(separator = ""))
    }

    @Test
    fun `should decode given text with basic alphabet and digit key`() {
        // given
        val result = mutableListOf<Char>()
        val receiver: suspend (Flow<Char>) -> Unit = { flow -> flow.collect { result.add(it) } }

        // when
        bazeries.decode(BASIC_TEXT_ENCODED_WITH_DIGIT_KEY.byteInputStream(), receiver)

        // then
        assertEquals(BASIC_TEXT, result.joinToString(separator = ""))
    }

    @Test
    fun `should decode given text with basic alphabet and full name key`() {
        // given
        val fullNameBazeries = Bazeries(KEY, useFullNumberName = true)
        val result = mutableListOf<Char>()
        val receiver: suspend (Flow<Char>) -> Unit = { flow -> flow.collect { result.add(it) } }

        // when
        fullNameBazeries.decode(BASIC_TEXT_ENCODED_WITH_FULL_NAME_KEY.byteInputStream(), receiver)

        // then
        assertEquals(BASIC_TEXT, result.joinToString(separator = ""))
    }
}
