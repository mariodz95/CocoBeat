package com.example.cocobeat.util

import kotlin.experimental.and

private const val BYTE_TO_INT_CONVERSION_MASK = 0xFF

//Conversion constants
private const val CHECK_SIGN_MASK = 0x00000080
private const val FIRST_NIBBLE_TO_INT_CONVERSION_MASK = 0xF0
private const val LAST_NIBBLE_TO_INT_CONVERSION_MASK = 0x0F
private const val DISREGARD_SIGN_NIBBLE_MASK = 0x07
private const val LOWEST_NEGATIVE_NIBBLE = -8

class ConvertBytesAndNumbers {
    fun intFromOneByte(thisByte: Byte): Int {
        return (thisByte and BYTE_TO_INT_CONVERSION_MASK.toByte()).toInt()
    }

    fun intFromTwoBytes(leastSignificant: Byte, mostSignificant: Byte): Int {
        val bits0to7 = (leastSignificant.toInt() and BYTE_TO_INT_CONVERSION_MASK)
        val bits8to15 = (mostSignificant.toInt() and BYTE_TO_INT_CONVERSION_MASK)
        return (bits8to15 shl 8) or bits0to7
    }


    fun sfloatMantissaFromTwoBytes(leastSignificant: Byte, mostSignificant: Byte): Double {
        val bits0to7 = (leastSignificant.toInt() and BYTE_TO_INT_CONVERSION_MASK)
        val bits8to15 = (mostSignificant.toInt() and LAST_NIBBLE_TO_INT_CONVERSION_MASK)
        val mantissa = bits8to15 shl 8 or bits0to7
        return mantissa / 1.0
    }

    fun sfloatExponentFromOneByte(mostSignificant: Byte): Double {
        val bits0to7 = (mostSignificant.toInt() and FIRST_NIBBLE_TO_INT_CONVERSION_MASK)
        var negative = false
        if (mostSignificant.toInt() and CHECK_SIGN_MASK == CHECK_SIGN_MASK) {
            negative = true
        }
        var exp = bits0to7 shr 4 and DISREGARD_SIGN_NIBBLE_MASK
        if (negative) {
            exp += LOWEST_NEGATIVE_NIBBLE
        }
        return exp / 1.0
    }
}