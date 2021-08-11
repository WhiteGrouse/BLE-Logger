package com.delion.blelogger

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.*
import android.os.ParcelUuid
import android.util.Log
import java.nio.ByteBuffer

class Scanner(val adapter: BluetoothAdapter) {
    private val serviceUuid = ParcelUuid.fromString("9703e04c-991f-40e5-bff2-2234cc25788b")
    var enabled = false
    private var scanning = false

    var onScanResult: ((device: DeviceEntry) -> Unit)? = null
    var onScanFailed: ((errorCode: Int) -> Unit)? = null
    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)

            if(result == null) return
            val id = ByteBuffer.wrap(result.scanRecord!!.serviceData[serviceUuid]!!).short
            onScanResult?.invoke(DeviceEntry(id, result.rssi, result.scanRecord!!.txPowerLevel, result.timestampNanos))
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            onScanFailed?.invoke(errorCode)
            Log.e("BLE Logger", "Scan failed..............($errorCode)")
        }
    }

    fun enable() {
        enabled = true
        startScan()
    }

    fun disable() {
        enabled = false
        stopScan()
    }

    fun onStateChanged(state: Int) {
        when(state) {
            BluetoothAdapter.STATE_ON -> {
                if(enabled) {
                    startScan()
                }
                else if(scanning) {
                    stopScan()
                }
            }
            BluetoothAdapter.STATE_TURNING_OFF -> {
                if(scanning) {
                    stopScan()
                }
            }
        }
    }

    private fun startScan() {
        if(!adapter.isEnabled) return
        if(scanning) {
            stopScan()
        }

        val scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .setLegacy(false)
            .setMatchMode(ScanSettings.MATCH_MODE_STICKY)
            .setNumOfMatches(ScanSettings.MATCH_NUM_MAX_ADVERTISEMENT)
            .build()
        val filter = ScanFilter.Builder()
            .setServiceData(serviceUuid, ByteArray(0))
            .build()
        adapter.bluetoothLeScanner.startScan(listOf(filter), scanSettings, scanCallback)
        scanning = true
        Log.d("BLE Logger", "Start scan")
    }

    private fun stopScan() {
        if (!adapter.isEnabled) return
        adapter.bluetoothLeScanner.stopScan(scanCallback)
        scanning = false
        Log.d("BLE Logger", "Stop scan")
    }
}