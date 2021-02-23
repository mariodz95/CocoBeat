package com.example.cocobeat.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cocobeat.database.dao.ReadingDao
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.example.cocobeat.database.entity.Reading
import com.example.cocobeat.getOrAwaitValueTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ReadingDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var readingDao: ReadingDao
    private lateinit var db: AppDatabase
    private lateinit var readings: MutableList<Reading>

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
                context, AppDatabase::class.java).allowMainThreadQueries().build()
        readingDao = db.readingDao()
    }

    @After
    fun closeDb() {
        db.close()
    }


    @Test
    fun insertReadings() = runBlockingTest {
        val date = Calendar.getInstance().time
        val id1 = UUID.fromString("6ca05cc8-74e9-11eb-9439-0242ac130001")
        val id2 = UUID.fromString("6ca05cc8-74e9-11eb-9439-0242ac130002")
        val id3 = UUID.fromString("6ca05cc8-74e9-11eb-9439-0242ac130003")

        val reading1 = Reading(id1,"1111", date, 100.0, "MGDL", date)
        val reading2 = Reading(id2,"2222", date, 200.0, "MGDL", date)
        val reading3 = Reading(id3,"3333", date, 300.0, "MGDL", date)

        readings.add(reading1)
        readings.add(reading2)
        readings.add(reading3)

        readingDao.insertReadings(readings)
        val allReadings = readingDao.getAllReadings(date, date).getOrAwaitValueTest()

        assertThat(allReadings).contains(readings)
    }

    @Test
    fun getLastReading(){
    }

    @Test
    fun getAllReadings(){}
}