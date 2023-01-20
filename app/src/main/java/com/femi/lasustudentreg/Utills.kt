package com.femi.lasustudentreg

import android.content.Context
import android.widget.Toast

fun Context.handleNetworkExceptions(exception: java.lang.Exception?){

    Toast.makeText(this,
        exception?.message.toString(),
        Toast.LENGTH_SHORT)
        .show()

}