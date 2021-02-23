package com.example.cocobeat.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cocobeat.database.dao.ReadingDao
import com.example.cocobeat.database.entity.Reading
import org.hamcrest.CoreMatchers.nullValue

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.AdditionalMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule

import org.mockito.MockitoAnnotations
import java.util.*

class ReadingRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    private lateinit var readingRepository: ReadingRepository

    @Mock
    private lateinit var readingDao: ReadingDao
    @Mock
    private lateinit var readings: List<Reading>
    @Mock
    private var liveDataReadings: MutableLiveData<List<Reading>> =  MutableLiveData()

    private lateinit var lista: MutableList<Reading>


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        readingRepository = ReadingRepository(readingDao)
    }

    @Test
    fun readingRepository() {
        val date = Calendar.getInstance().time
        val id1 = UUID.fromString("6ca05cc8-74e9-11eb-9439-0242ac130001")
        val id2 = UUID.fromString("6ca05cc8-74e9-11eb-9439-0242ac130002")
        val id3 = UUID.fromString("6ca05cc8-74e9-11eb-9439-0242ac130003")

        val reading1 = Reading(id1,"1111", date, 100.0, "MGDL", date)
        val reading2 = Reading(id2,"2222", date, 200.0, "MGDL", date)
        val reading3 = Reading(id3,"3333", date, 300.0, "MGDL", date)

        readings = listOf(reading1, reading2, reading3)

        liveDataReadings.postValue(readings)

/*
        `when`(readingDao.getAllReadings(date, date)).thenReturn(liveDataReadings)
*/
        lista = mutableListOf<Reading>(reading1, reading2, reading3)
        val test = readingRepository.insertReadings(lista)
        val allReadings = readingRepository.getMonthData(date, date)

        assertThat( allReadings, not(nullValue()))
    }


    @Test
    fun readingRepository_LastReading_ShouldReturnLastReadingIfExist(){

    }

    @Test
    fun readingRepository_InsertReading_ShouldInsertListOfReadings(){

    }
}
