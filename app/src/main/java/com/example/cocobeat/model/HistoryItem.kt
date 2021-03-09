package com.example.cocobeat.model

import com.example.cocobeat.util.HistoryItemType
import java.util.*

data class HistoryItem(val type: HistoryItemType, val date: Date?, val value: Double?, val unit: String?, val name: String?, val hourDuration: Int?, val minuteDuration: Int?)
