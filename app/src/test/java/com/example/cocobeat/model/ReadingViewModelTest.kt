package com.example.cocobeat.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.cocobeat.database.entity.Reading
import com.example.cocobeat.repository.ReadingRepository
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.*


class ReadingViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ReadingViewModel
    private var readings: List<Reading>? = null
    private var liveDataReadings: MutableLiveData<List<Reading>> =  MutableLiveData()
    private var lastReading: MutableLiveData<Reading> = MutableLiveData()

    @Mock
    private lateinit var readingRepository: ReadingRepository

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        `when`(readingRepository.lastReading).thenReturn(lastReading)

        viewModel = ReadingViewModel(readingRepository)
    }

    @Test
    fun readingViewModel_LoadMonthData_ShouldGetReadingsBetweenDates(){
        val date: Calendar = Calendar.getInstance()
        date.set(Calendar.YEAR, 2010)

        val id1 = UUID.fromString("6ca05cc8-74e9-11eb-9439-0242ac130001")
        val id2 = UUID.fromString("6ca05cc8-74e9-11eb-9439-0242ac130002")
        val id3 = UUID.fromString("6ca05cc8-74e9-11eb-9439-0242ac130003")
        val reading1 = Reading(id1, "1111", date.time, 100.0, "MGDL", date.time)
        val reading2 = Reading(id2, "2222", date.time, 200.0, "MGDL", date.time)
        val reading3 = Reading(id3, "3333", date.time, 300.0, "MGDL", date.time)
        readings = listOf(reading1, reading2, reading3)
        liveDataReadings.postValue(readings)
        `when`(readingRepository.getMonthData(date.time, date.time)).thenReturn(liveDataReadings)
        viewModel.loadMonthData(date.time, date.time)
        val value = viewModel.monthData?.value

        assertThat(value?.size).isEqualTo(readings?.size)
        assertThat(value).isEqualTo(readings)
    }


    @Test
    fun readingViewModel_LoadMonthData_ShouldReturnNullIfNotExist(){
        val date: Calendar = Calendar.getInstance()
        date.set(Calendar.YEAR, 2010)

        viewModel.loadMonthData(date.time, date.time)
        val value = viewModel.monthData?.value

        assertThat(value).isEqualTo(readings)
    }


    @Test
    fun readingViewModel_GetLastReading_ShouldGetLastReadingIfExist(){
        val date: Calendar = Calendar.getInstance()
        val id1 = UUID.fromString("6ca05cc8-74e9-11eb-9439-0242ac130001")
        val reading = Reading(id1, "1111", date.time, 100.0, "MGDL", date.time)

        lastReading.postValue(reading)
        readingRepository.lastReading = lastReading

        val value = viewModel.lastReading.value

        assertThat(value).isEqualTo(lastReading.value)
    }

    @Test
    fun readingViewModel_LastReading_ShouldGReturnNullIfNotExist(){
        `when`(readingRepository.lastReading).thenReturn(lastReading)
        val value = viewModel.monthData?.value
        assertThat(value).isEqualTo(null)
    }
}