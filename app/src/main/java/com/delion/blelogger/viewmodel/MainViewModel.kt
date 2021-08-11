package com.delion.blelogger.viewmodel

import androidx.lifecycle.ViewModel
import com.delion.blelogger.Logger
import com.delion.blelogger.Scanner

class MainViewModel: ViewModel() {
    var scanner: Scanner? = null
    var logger: Logger? = null
}