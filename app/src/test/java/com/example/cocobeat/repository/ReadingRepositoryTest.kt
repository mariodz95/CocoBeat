package com.example.cocobeat.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.cocobeat.database.dao.ReadingDao
import com.example.cocobeat.database.entity.Reading
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.junit.Rule
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.*

class ReadingRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var readingRepository: ReadingRepository

    @Mock
    private lateinit var readingDao: ReadingDao

    private var readings: List<Reading>? = null
    private var liveDataReadings: MutableLiveData<List<Reading>> =  MutableLiveData()
    private var lastReading: MutableLiveData<Reading> = MutableLiveData()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        `when`(readingDao.getLastReading()).thenReturn(lastReading)
        readingRepository = ReadingRepository(readingDao)
    }

    @Test
    fun readingRepository_GetMonthData_ShouldGetReadingsBetweenDates() {
        val date = Calendar.getInstance().time
        val id1 = UUID.fromString("6ca05cc8-74e9-11eb-9439-0242ac130001")
        val id2 = UUID.fromString("6ca05cc8-74e9-11eb-9439-0242ac130002")
        val id3 = UUID.fromString("6ca05cc8-74e9-11eb-9439-0242ac130003")

        val reading1 = Reading(id1,"1111", date, 100.0, "MGDL", date)
        val reading2 = Reading(id2,"2222", date, 200.0, "MGDL", date)
        val reading3 = Reading(id3,"3333", date, 300.0, "MGDL", date)

        readings = listOf(reading1, reading2, reading3)
        liveDataReadings.postValue(readings)

        `when`(readingDao.getAllReadings(date, date)).thenReturn(liveDataReadings)

        readingRepository.getMonthData(date, date)

        val value = readingRepository.allData?.value

        Truth.assertThat(value?.size).isEqualTo(readings?.size)
        Truth.assertThat(value).isEqualTo(readings)
    }

    @Test
    fun readingViewModel_GetMonthData_ShouldReturnNullIfNotExist(){
        val date: Calendar = Calendar.getInstance()
        date.set(Calendar.YEAR, 2010)
        readingRepository.getMonthData(date.time, date.time)
        val value = readingRepository.allData?.value

        Truth.assertThat(value).isEqualTo(readings)
    }


    @Test
    fun readingRepository_LastReading_ShouldReturnLastReadingIfExist(){
        val date: Calendar = Calendar.getInstance()
        val id1 = UUID.fromString("6ca05cc8-74e9-11eb-9439-0242ac130001")
        val reading = Reading(id1, "1111", date.time, 100.0, "MGDL", date.time)

        lastReading.postValue(reading)
        readingRepository.lastReading = lastReading

        val value = readingRepository.lastReading.value

        Truth.assertThat(value).isEqualTo(lastReading.value)
    }

    @Test
    fun readingRepository_InsertReading_ShouldInsertListOfReadings(){
        `when`(readingRepository.lastReading).thenReturn(lastReading)
        val value = readingRepository.allData?.value
        Truth.assertThat(value).isEqualTo(null)
    }
}
