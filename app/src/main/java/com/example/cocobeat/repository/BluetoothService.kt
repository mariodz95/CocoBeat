package com.example.cocobeat.repository

import android.util.Log
import com.ablelib.comm.asyncComm
import com.ablelib.exceptions.BluetoothStateException
import com.ablelib.manager.AbleManager
import com.ablelib.manager.pair
import com.ablelib.models.*
import com.ablelib.storage.AbleDeviceStorage
import com.example.cocobeat.util.ConvertBytesAndNumbers
import java.util.*
import kotlin.experimental.and


private val DEVICE_INFORMATION_SERVICE = AbleUUID("0000180a-0000-1000-8000-00805f9b34fb")
private val DATE_TIME_SERVICE = AbleUUID("00001808-0000-1000-8000-00805f9b34fb")


private val DEVICE_MODEL_NUMBER_CHARACTERISTIC = AbleUUID("00002a24-0000-1000-8000-00805f9b34fb")
private val DEVICE_SERIAL_NUMBER_CHARACTERISTIC = AbleUUID("00002a25-0000-1000-8000-00805f9b34fb")
private val GLUCOSE_MEASUREMENT_CONTEXT_CHARACTERISTIC = AbleUUID("00002a34-0000-1000-8000-00805f9b34fb")
private val UUID_GLUCOSE_MEASUREMENT_CHARACTERISTIC = AbleUUID("00002a18-0000-1000-8000-00805f9b34fb")
private val DATE_TIME_CHARACTERISTIC = AbleUUID("00002a08-0000-1000-8000-00805f9b34fb")
private val UUID_RECORD_ACCESS_CONTROL_POINT_CHARACTERISTIC = AbleUUID("00002a52-0000-1000-8000-00805f9b34fb")

private val CLIENT_CHARACTERISTIC_CONFIGURATION_DESCRIPTOR = AbleUUID("00002902-0000-1000-8000-00805f9b34fb")
private lateinit var lastCharacteristicUUID : AbleUUID

class BluetoothService {

     suspend fun scanForNearbyDevices(deviceName: String) {
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

     suspend fun pairWithDevice(deviceName: String) {
        try {
            val myDevice = AbleDeviceStorage.default.findByName(deviceName)
            val pairedDevice = myDevice.pair()
        } catch (e: Exception) {
            // handle the exception
        }
    }

    fun readCharacteristic(deviceName: String){

        val myDevice = AbleDeviceStorage.default.findByName(deviceName)

        val deviceServices = mutableListOf<AbleService>();
        val deviceCharacteristic = mutableListOf<AbleCharacteristic>();
        var modelNumber : String = ""
        var serialNumber : String = ""
        var deviceModel : String = ""

        val comm = myDevice.asyncComm
                .onConnectionStateChanged { newState, status ->
                    // respond to connection state changes - connections, disconnects, etc
                }
                .onServicesDiscovered { services ->
                    // here's where the services will appear once you trigger discoverServices
                    deviceServices.addAll(services)
                    val characteristicModelNumber = deviceServices.first { service -> service.uuid == DEVICE_INFORMATION_SERVICE }
                            .getCharacteristic(DEVICE_MODEL_NUMBER_CHARACTERISTIC)
                    deviceCharacteristic.add(characteristicModelNumber)

                    val characteristicSerialNumber = deviceServices.first { service -> service.uuid == DEVICE_INFORMATION_SERVICE }
                            .getCharacteristic(DEVICE_SERIAL_NUMBER_CHARACTERISTIC)
                    deviceCharacteristic.add(characteristicSerialNumber)

                    val characteristicDateTime = deviceServices.first { service -> service.uuid == DATE_TIME_SERVICE }
                            .getCharacteristic(DATE_TIME_CHARACTERISTIC)
                    deviceCharacteristic.add(characteristicDateTime)

                    val glucoseMeasurementCharacteristic = deviceServices.first { service -> service.uuid == DATE_TIME_SERVICE }
                            .getCharacteristic(UUID_GLUCOSE_MEASUREMENT_CHARACTERISTIC)
                    deviceCharacteristic.add(glucoseMeasurementCharacteristic)

                    val glucoseMeasurementContextCharacteristic = deviceServices.first { service -> service.uuid == DATE_TIME_SERVICE }
                            .getCharacteristic(GLUCOSE_MEASUREMENT_CONTEXT_CHARACTERISTIC)
                    deviceCharacteristic.add(glucoseMeasurementContextCharacteristic)

                    val recordAccessControlPointCharacteristic = deviceServices.first { service -> service.uuid == DATE_TIME_SERVICE}
                            .getCharacteristic(UUID_RECORD_ACCESS_CONTROL_POINT_CHARACTERISTIC)
                    deviceCharacteristic.add(recordAccessControlPointCharacteristic)

                    readCharacteristic(characteristicModelNumber)

                }
                .onCharacteristicRead { characteristic ->
                    // called after comm.readCharacteristic
                    if(characteristic.uuid == DEVICE_MODEL_NUMBER_CHARACTERISTIC){
                        modelNumber = String(characteristic.value)
                        val deviceSerialNumberCharacteristic = deviceCharacteristic.first{ characteristic -> characteristic.uuid == DEVICE_SERIAL_NUMBER_CHARACTERISTIC}
                        readCharacteristic(deviceSerialNumberCharacteristic)
                    }else if(characteristic.uuid == DEVICE_SERIAL_NUMBER_CHARACTERISTIC){
                        serialNumber = String(characteristic.value)
                        val characteristicDateTimeCharacteristic = deviceCharacteristic.first{ characteristic -> characteristic.uuid == DATE_TIME_CHARACTERISTIC}
                        readCharacteristic(characteristicDateTimeCharacteristic)
                    }else if(characteristic.uuid == DATE_TIME_CHARACTERISTIC) {
                        val dateTimeBytes = characteristic.value
                        val convert = ConvertBytesAndNumbers()
                        val year: Int = convert.intFromTwoBytes(dateTimeBytes[0], dateTimeBytes[1])
                        val month: Int = convert.intFromOneByte(dateTimeBytes[2])
                        val day: Int = convert.intFromOneByte(dateTimeBytes[3])
                        val hours: Int = convert.intFromOneByte(dateTimeBytes[4])
                        val minutes: Int = convert.intFromOneByte(dateTimeBytes[5])
                        val seconds: Int = convert.intFromOneByte(dateTimeBytes[6])
                        val calendar = Calendar.getInstance()
                        calendar.set(year, month - 1, day, hours, minutes, seconds);

                        val glucoseMeasurementCharacteristic = deviceCharacteristic.first { characteristic -> characteristic.uuid == UUID_GLUCOSE_MEASUREMENT_CHARACTERISTIC }
                        val glucoseDescriptor = glucoseMeasurementCharacteristic.descriptors.find {
                            it.uuid == CLIENT_CHARACTERISTIC_CONFIGURATION_DESCRIPTOR
                        }

                        lastCharacteristicUUID = UUID_GLUCOSE_MEASUREMENT_CHARACTERISTIC
                        writeDescriptor(glucoseMeasurementCharacteristic, glucoseDescriptor!!, false)
                    }

                    deviceModel = serialNumber + modelNumber
                }
                .onCharacteristicChanged { characteristic ->
                    // called when a characteristic for which setNotifyValue was called changes its value
                    Log.v("test", "test onCharacteristicChanged: ${characteristic.uuid}")
                    val characteristicValue = characteristic.value
                    val convert = ConvertBytesAndNumbers()
/*
                    val data = convert.intFromTwoBytes(characteristicValue[2], characteristicValue[3])
*/

                    val command = byteArrayOf(0x01, 0x01)

                    Log.v("test", "characteristicValue ${String(characteristicValue).length}")

                    if(characteristic.uuid == UUID_GLUCOSE_MEASUREMENT_CHARACTERISTIC) {
                        var index = 0
                        var data2 = characteristic.value;
                        //Read Measurement Flags
                        var timeOffsetPresent = (data2.get(index) and 0x01 > 0)
                        var glucoseConcentrationPresent = data2.get(index) and 0x02 > 0

                        var units = if (data2.get(index) and 0x04 > 0) {
                            "MMOL"
                        } else {
                            "MGDL"
                        }
                        index++;

                        var sequenceNumber = convert.intFromTwoBytes(data2[index++], data2[index++]);

                        index += 7;

                        if (timeOffsetPresent) {
                            index += 2;
                        }

                        //Get glucoseConcentration
                        var leastSignificant = data2[index++];
                        var mostSignificant = data2[index++];

                        var mantissa = convert.sfloatMantissaFromTwoBytes(leastSignificant, mostSignificant);
                        var exponent = convert.sfloatExponentFromOneByte(mostSignificant);
                        Log.v("test", "mantissa ${mantissa}")
                        Log.v("test", "mantissa ${exponent}")

                        var glucoseValue = Math.pow(10.0, exponent) * mantissa;

                        Log.v("test", "glucoseValue ${glucoseValue}")
                    }

                    writeCharacteristic(characteristic, command)


                }
                .onDescriptorRead { descriptor ->
                    // called after comm.readDescriptor
                    Log.v("test", "onDescriptorRead")
                    if(lastCharacteristicUUID == UUID_GLUCOSE_MEASUREMENT_CHARACTERISTIC)
                    {
                        val glucoseMeasurementContextCharacteristic = deviceCharacteristic.first { characteristic -> characteristic.uuid == GLUCOSE_MEASUREMENT_CONTEXT_CHARACTERISTIC }
                        val glucoseMeasurementContextDescriptor  = glucoseMeasurementContextCharacteristic.descriptors.find{
                            it.uuid == CLIENT_CHARACTERISTIC_CONFIGURATION_DESCRIPTOR
                        }
                        lastCharacteristicUUID = GLUCOSE_MEASUREMENT_CONTEXT_CHARACTERISTIC
                        writeDescriptor(glucoseMeasurementContextCharacteristic, glucoseMeasurementContextDescriptor!!, false)
                    }else if(lastCharacteristicUUID == GLUCOSE_MEASUREMENT_CONTEXT_CHARACTERISTIC){
                        val recordAccessControlPointCharacteristic = deviceCharacteristic.first { characteristic -> characteristic.uuid == UUID_RECORD_ACCESS_CONTROL_POINT_CHARACTERISTIC}
                        val recordAccessControlPointDescriptor = recordAccessControlPointCharacteristic.descriptors.find{
                            it.uuid == CLIENT_CHARACTERISTIC_CONFIGURATION_DESCRIPTOR
                        }
                        lastCharacteristicUUID = UUID_RECORD_ACCESS_CONTROL_POINT_CHARACTERISTIC
                        writeDescriptor(recordAccessControlPointCharacteristic, recordAccessControlPointDescriptor!!, true)
                    }else if(lastCharacteristicUUID == UUID_RECORD_ACCESS_CONTROL_POINT_CHARACTERISTIC){
                        Log.v("test", "treci")
                        val command = byteArrayOf(0x04, 0x01)
                        val recordAccessControlPointCharacteristic = deviceCharacteristic.first { characteristic -> characteristic.uuid == UUID_RECORD_ACCESS_CONTROL_POINT_CHARACTERISTIC}
                        writeCharacteristic(recordAccessControlPointCharacteristic, command)
                        Log.v("test", "zadnje result")
                    }

                }
                .onError { error ->
                }
                .onDescriptorWrite { descriptor ->
                    // called after comm.writeDescriptor
                    readDescriptor(descriptor)
                }
                .connect()

        comm.discoverServices()
    }
}