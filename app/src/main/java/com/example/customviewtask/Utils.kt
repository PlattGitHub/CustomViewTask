package com.example.customviewtask

import android.content.Context
import android.util.TypedValue
import android.widget.Toast

/**
 * Function to get accent color.
 *
 * @author Alexander Gorin
 */
fun getThemeAccentColor(context: Context?): Int {
    val colorAttr: Int = android.R.attr.colorAccent
    val outValue = TypedValue()
    context?.theme?.resolveAttribute(colorAttr, outValue, true)
    return outValue.data
}

/**
 * Extension function to display toasts.
 *
 * @author Alexander Gorin
 */
fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}