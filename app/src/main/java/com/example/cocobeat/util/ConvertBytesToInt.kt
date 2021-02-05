package com.example.cocobeat.util

import kotlin.experimental.and

private const val BYTE_TO_INT_CONVERSION_MASK = 0xFF

class ConvertBytesToInt {
    fun intFromOneByte(thisByte: Byte): Int {
        return (thisByte and BYTE_TO_INT_CONVERSION_MASK.toByte()).toInt()
    }

    fun intFromTwoBytes(leastSignificant: Byte, mostSignificant: Byte): Int {
        val bits0to7 = (leastSignificant.toInt() and BYTE_TO_INT_CONVERSION_MASK)
        val bits8to15 = (mostSignificant.toInt() and BYTE_TO_INT_CONVERSION_MASK)
        return (bits8to15 shl 8) or bits0to7
    }
}