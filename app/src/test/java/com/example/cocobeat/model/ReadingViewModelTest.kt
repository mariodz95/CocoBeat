package com.example.cocobeat.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cocobeat.FakeRepository
import com.example.cocobeat.database.dao.ReadingDao
import com.example.cocobeat.database.entity.Reading
import com.example.cocobeat.getOrAwaitValueTest
import com.example.cocobeat.repository.ReadingRepository
import com.example.cocobeat.util.Resource
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.AdditionalMatchers.not
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.*


class ReadingViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var viewModel: ReadingViewModel
    private lateinit var readings: List<Reading>
    private var liveDataReadings: MutableLiveData<List<Reading>> =  MutableLiveData()
    @Mock
    private lateinit var readingRepository: ReadingRepository
    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this); // this is needed for inititalizytion of mocks, if you use @Mock
        viewModel = ReadingViewModel(readingRepository)
    }
    @Test
    fun readingViewModel_LoadMonthData_ShouldGetReadingsBetweenDates(){
        val date: Calendar = Calendar.getInstance()
        date.set(Calendar.YEAR, 2010)
        val ene: Calendar = Calendar.getInstance()
        val id1 = UUID.fromString("6ca05cc8-74e9-11eb-9439-0242ac130001")
        val id2 = UUID.fromString("6ca05cc8-74e9-11eb-9439-0242ac130002")
        val id3 = UUID.fromString("6ca05cc8-74e9-11eb-9439-0242ac130003")
        val reading1 = Reading(id1, "1111", date.time, 100.0, "MGDL", date.time)
        val reading2 = Reading(id2, "2222", date.time, 200.0, "MGDL", date.time)
        val reading3 = Reading(id3, "3333", date.time, 300.0, "MGDL", date.time)
        readings = listOf(reading1, reading2, reading3)
        liveDataReadings.postValue(readings)
        `when`(readingRepository.getMonthData(date.time, date.time)).thenReturn(liveDataReadings)
        viewModel.loadMonthData(date.time, ene.time)
        assertThat( viewModel.monthData?.getOrAwaitValueTest(), not(nullValue()))
    }
}