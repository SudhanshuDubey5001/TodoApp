package com.sudhanshu.todoapp.util

import android.util.Log

object Constants {
    const val TODO_ID = "todoID"
    const val TAG = "myLog"
    fun log(s: String) {
        Log.d(TAG, s)
    }
}