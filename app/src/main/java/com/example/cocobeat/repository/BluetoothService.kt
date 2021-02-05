package com.example.cocobeat.repository

import android.util.Log
import com.ablelib.comm.comm
import com.ablelib.exceptions.BluetoothStateException
import com.ablelib.manager.AbleManager
import com.ablelib.manager.pair
import com.ablelib.models.AbleDevice
import com.ablelib.models.AbleUUID
import com.ablelib.storage.AbleDeviceStorage
import com.example.cocobeat.util.ConvertBytesToInt
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.*

class BluetoothService {
     suspend fun scanForNearbyDevices(deviceName: String) {

        val value = GlobalScope.async {
            try {
                val devices = AbleManager.shared.scan()
                for (device in devices) {
                    AbleDeviceStorage.default.add(
                            AbleDevice(
                                    device.name,
                                    true,
                                    true,
                                    true,
                                    device.address
                            )
                    )
                }
            } catch (e: BluetoothStateException) {
                // handle the exception
            }
        }
    }

     suspend fun pairWithDevice(deviceName: String) {
        val myDevice = AbleDeviceStorage.default.findByName(deviceName)
        try {
            val pairedDevice = myDevice.pair()
        } catch (e: Exception) {
            // handle the exception
        }
    }

    suspend fun readCharacteristic(deviceName: String){

        val myDevice = AbleDeviceStorage.default.findByName(deviceName)
        Log.v("test", "myDevice $myDevice")
        val comm = myDevice.comm
        Log.v("test", "comm $comm")
        val connect = comm.connect()
        Log.v("test", "connect $connect")

        try {
            val characteristicModelNumber = comm.discoverServices().first { service -> service.uuid == AbleUUID("0000180a-0000-1000-8000-00805f9b34fb") }
                    .getCharacteristic(AbleUUID("00002a24-0000-1000-8000-00805f9b34fb"))
            val dataModelNumber = comm.readCharacteristic(characteristicModelNumber).value

            val characteristicSerialNumber = comm.discoverServices().first { service -> service.uuid == AbleUUID("0000180a-0000-1000-8000-00805f9b34fb") }
                    .getCharacteristic(AbleUUID("00002a25-0000-1000-8000-00805f9b34fb"))
            val dataSerialNumber = comm.readCharacteristic(characteristicSerialNumber).value

            val deviceModel = String(dataSerialNumber) + String(dataModelNumber)
            Log.v("test", "deviceModel ${deviceModel}")

            val characteristicDateTime = comm.discoverServices().first { service -> service.uuid == AbleUUID("00001808-0000-1000-8000-00805f9b34fb") }
                    .getCharacteristic(AbleUUID("00002a08-0000-1000-8000-00805f9b34fb"))
            val dateTimeBytes= comm.readCharacteristic(characteristicDateTime).value

            val glucose = comm.discoverServices().first { service -> service.uuid == AbleUUID("00001808-0000-1000-8000-00805f9b34fb") }
                    .getCharacteristic(AbleUUID("00002a34-0000-1000-8000-00805f9b34fb"))
            Log.v("test", "glucose ${glucose}")

            val convert = ConvertBytesToInt()
            val year: Int = convert.intFromTwoBytes(dateTimeBytes[0], dateTimeBytes[1])
            val month: Int = convert.intFromOneByte(dateTimeBytes[2])
            val day: Int = convert.intFromOneByte(dateTimeBytes[3])
            val hours: Int = convert.intFromOneByte(dateTimeBytes[4])
            val minutes: Int = convert.intFromOneByte(dateTimeBytes[5])
            val seconds: Int = convert.intFromOneByte(dateTimeBytes[6])

            val calendar = Calendar.getInstance()
            calendar.set(year, month - 1, day, hours, minutes, seconds);
            Log.v("test", "date ${calendar.time}")
        }catch (e: java.lang.Exception)
        {
        }
    }

}