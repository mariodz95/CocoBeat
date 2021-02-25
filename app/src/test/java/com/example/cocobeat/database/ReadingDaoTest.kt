package com.example.cocobeat.database
import android.content.Context
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.cocobeat.database.dao.ReadingDao
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.example.cocobeat.database.entity.Reading
import com.example.cocobeat.getOrAwaitValueTest
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.koin.core.context.stopKoin
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
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
    @Throws(Exception::class)
    fun closeDb() {
        db.close()
        stopKoin()
    }

    @Test
    @Throws(Exception::class)
    fun insertReadingsAndReadItFromDb_ShouldInsertReadingsInDb(){
        val dateStart = Calendar.getInstance()
        dateStart.set(Calendar.YEAR, 2010)

        val date = Calendar.getInstance()

        val endDate = Calendar.getInstance()
        val id1 = UUID.fromString("6ca05cc8-74e9-11eb-9439-0242ac130001")
        val id2 = UUID.fromString("6ca05cc8-74e9-11eb-9439-0242ac130002")
        val id3 = UUID.fromString("6ca05cc8-74e9-11eb-9439-0242ac130003")

        val reading1 = Reading(id1,"1111", date.time, 100.0, "MGDL", date.time)
        val reading2 = Reading(id2,"2222",  date.time,200.0, "MGDL", date.time)
        val reading3 = Reading(id3,"3333",  date.time, 300.0, "MGDL", date.time)

        readings = mutableListOf(reading1, reading2, reading3)

        readingDao.insertReadings(readings)

        endDate.set(Calendar.YEAR, 2100)

        val allReadings = readingDao.getAllReadings(dateStart.time, endDate.time).getOrAwaitValueTest()
        assertThat(allReadings).isEqualTo(readings)
        assertThat(allReadings.size).isEqualTo(readings?.size)
    }

    @Test
    @Throws(Exception::class)
    fun getLastReading_ShouldReturnLastReadingIfExist(){
        val date = Calendar.getInstance()
        val id1 = UUID.fromString("6ca05cc8-74e9-11eb-9439-0242ac130001")
        val id2 = UUID.fromString("6ca05cc8-74e9-11eb-9439-0242ac130002")
        val id3 = UUID.fromString("6ca05cc8-74e9-11eb-9439-0242ac130003")

        val reading1 = Reading(id1,"1111", date.time, 100.0, "MGDL", date.time)
        val reading2 = Reading(id2,"2222",  date.time,200.0, "MGDL", date.time)
        val reading3 = Reading(id3,"3333",  date.time, 300.0, "MGDL", date.time)

        readings = mutableListOf(reading1, reading2, reading3)
        readingDao.insertReadings(readings)

        val lastValue = readingDao.getLastReading().getOrAwaitValueTest()
        assertThat(lastValue).isEqualTo(reading3)
    }

    @Test
    @Throws(Exception::class)
    fun getLastReading_ShouldReturnNullIfNotExist(){
        val lastValue = readingDao.getLastReading().getOrAwaitValueTest()
        assertThat(lastValue).isEqualTo(null)
    }

}