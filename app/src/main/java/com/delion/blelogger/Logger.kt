package com.delion.blelogger

import android.os.Environment
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Logger {
    private var timestamp: LocalDateTime = LocalDateTime.MIN
    private val _records: MutableList<DeviceEntry> = mutableListOf()
    val records get() = _records

    var onLogged: (() -> Unit)? = null

    fun reset() {
        _records.clear()
        timestamp = LocalDateTime.now()
    }

    fun log(device: DeviceEntry) {
        val record = device.copy()
        _records.add(record)
        onLogged?.invoke()
    }

    fun commit(dir: File) {
        val filename = timestamp.format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss")) + ".csv"
        val file = File(dir, filename)
        file.bufferedWriter().use { out ->
            _records.forEach {
                out.appendLine("${it.timestamp}, ${it.id}, ${it.power}, ${it.rssi}")
            }
        }
    }
}