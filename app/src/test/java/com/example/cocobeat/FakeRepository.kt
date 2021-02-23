package com.example.cocobeat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cocobeat.database.entity.Reading
import com.example.cocobeat.repository.DefaultReadingRepository
import java.util.*

class FakeRepository() : DefaultReadingRepository {

    private val test = MutableLiveData<Reading>()

    private val readingList = mutableListOf<Reading>()

    private val observableReadingList = MutableLiveData<List<Reading>>(readingList)

    override fun getLastReading(): LiveData<Reading> {
        return test
    }

    override fun getMonthData(startDate: Date, endDate: Date): LiveData<List<Reading>>? {
        return observableReadingList
        refreshLiveData()
    }

    override fun insertReadings(readings: MutableList<Reading>) {
        readingList.forEach{it->
            readingList.add(it)
        }

        refreshLiveData()
    }

    private fun refreshLiveData(){
        observableReadingList.postValue(readingList)
    }
}