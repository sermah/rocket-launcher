package com.sermah.rocketlauncher.data

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat.startActivity


data class AppInfo (
    val label: String,
    val icon: Drawable,
    val packageName: String,
    val activityName: String,
) {
    fun launch(context: Context) {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.setClassName(packageName, activityName)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
            Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        startActivity(context, intent, null)
    }
}