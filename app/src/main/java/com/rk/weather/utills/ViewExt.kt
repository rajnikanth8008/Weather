package com.rk.weather.utills

import android.view.View
import android.view.View.*
import android.widget.TextView

fun View.hide() {
    visibility = GONE
}

fun View.invisible() {
    visibility = INVISIBLE
}

fun View.show() {
    visibility = VISIBLE
}

fun TextView.clearText() {
    text = ""
}

