package com.delion.blelogger

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.nio.ByteBuffer
import java.util.*

fun navigateTo(manager: FragmentManager, fragment: Fragment) {
    manager.beginTransaction()
        .replace(R.id.container, fragment)
        .commitNow()
}

fun appShutdown(activity: Activity, title: String?, message: String) {
    val builder = AlertDialog.Builder(activity)

    if(title != null) {
        builder.setTitle(title)
    }
    builder
        .setMessage(message)
        .setPositiveButton("OK") { _, _ ->
            activity.finish()
            activity.moveTaskToBack(true)
        }
        .setCancelable(false)
        .show()
}

fun UUID.toByteArray(): ByteArray {
    val buffer = ByteBuffer.wrap(ByteArray(16))
    buffer.putLong(this.mostSignificantBits)
    buffer.putLong(this.leastSignificantBits)
    return buffer.array()
}