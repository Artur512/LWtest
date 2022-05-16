package com.volcode.coredata.trip.repository

import android.content.res.Resources
import com.google.gson.Gson
import com.volcode.coredata.jsonfilereader.JsonFileReader
import com.volcode.coredata.trip.capabilities.CardModel
import com.volcode.coredata.trip.capabilities.errors.ParseToModelException
import com.volcode.coredata.trip.capabilities.errors.UnableLoadFileException
import com.volcode.coretest.InstantTaskExecutorExtension
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions

@ExperimentalCoroutinesApi
@Extensions(
    ExtendWith(InstantTaskExecutorExtension::class),
    ExtendWith(MockKExtension::class)
)
internal class CardsRepositoryImplTest {

    @RelaxedMockK
    private lateinit var jsonFileReader: JsonFileReader
    private lateinit var sut: CardsRepositoryImpl
    private val testDispatcher = TestCoroutineDispatcher()

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @BeforeEach
    fun setUp() {
        sut = CardsRepositoryImpl(jsonFileReader, Gson())
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `should return list of cards successfully`() {
        val jsonValue = "[  {\n" +
                "    \"id\": 4,\n" +
                "    \"departure\": \"Madrid\",\n" +
                "    \"arrival\": \"Barcelona\",\n" +
                "    \"seat\": \"45B\",\n" +
                "    \"vehicleNumber\": \"78A\",\n" +
                "    \"vehicleType\": \"train\",\n" +
                "    \"hints\": null\n" +
                "  }]"
        testDispatcher.runBlockingTest {
            coEvery { jsonFileReader.readFile(any()) } returns jsonValue

            val result = sut.loadCards()

            val expectedCard = CardModel(4, "Madrid", "Barcelona", "45B", "78A", "train")
            assertEquals(arrayListOf(expectedCard), result)
        }
    }

    @Test
    fun `should throws exception when text has not been parsed successfully`() {
        val unformattedJsonValue = "[  {\n" +
                "    \"id\": 4,\n" +
                "    \"departure\": \"Madrid\",\n" +
                "    \"arrival\": \"Barcelona\",\n" +
                "    \"seat\": \"45B\",\n" +
                "    \"vehicleNumber\": \"78A\",\n" +
                "    \"vehicleType\": \"train\",\n" +
                "    \"hints\": null\n"
        coEvery { jsonFileReader.readFile(any()) } returns unformattedJsonValue

        assertThrows<ParseToModelException> {
            testDispatcher.runBlockingTest { sut.loadCards() }
        }
    }

    @Test
    fun `should throws exception when file doesn't exist`() {
        coEvery { jsonFileReader.readFile(any()) } throws Resources.NotFoundException()

        assertThrows<UnableLoadFileException> {
            testDispatcher.runBlockingTest { sut.loadCards() }
        }
    }
}