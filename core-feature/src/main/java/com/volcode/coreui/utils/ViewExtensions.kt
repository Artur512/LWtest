package com.volcode.coreui.utils

import android.view.View


fun View.showConditionally(condition: Boolean) {
    if (condition) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}