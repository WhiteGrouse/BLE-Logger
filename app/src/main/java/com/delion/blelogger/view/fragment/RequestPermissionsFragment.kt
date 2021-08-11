package com.delion.blelogger.view.fragment

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.*
import com.delion.blelogger.view.MainActivity
import com.delion.blelogger.R
import com.delion.blelogger.appShutdown

class RequestPermissionsFragment : Fragment() {
    private val launcher = registerForActivityResult(RequestMultiplePermissions()) { grantResults ->
        val activity = requireActivity() as MainActivity

        if (!grantResults.all { it.value }){
            appShutdown(activity, "エラー", "動作に必要な権限が取得できなかったため終了します。")
        }
        else {
            activity.onPermissionGranted()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_request_permissions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launcher.launch(arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        ))
    }
}