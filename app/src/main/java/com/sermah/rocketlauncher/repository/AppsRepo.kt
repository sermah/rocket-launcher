package com.sermah.rocketlauncher.repository

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.os.Build
import com.sermah.rocketlauncher.data.AppInfo

object AppsRepo {

    private fun retrievePackagesList(context: Context): List<ResolveInfo> {
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        return if (Build.VERSION.SDK_INT >= 33) {
            context.packageManager.queryIntentActivities(mainIntent,
                PackageManager.ResolveInfoFlags.of(0L)).toList()
        } else {
            context.packageManager.queryIntentActivities(mainIntent, 0).toList()
        }
    }

    private fun loadLabelIcon(context: Context, resolveInfo: ResolveInfo): Pair<String, Drawable> {
        return Pair(
            resolveInfo.loadLabel(context.packageManager) as String,
            resolveInfo.loadIcon(context.packageManager)
        )
    }

    fun loadAppsList(context: Context): List<AppInfo> {
        return retrievePackagesList(context).map {
            val (label, icon) = loadLabelIcon(context, it)
            AppInfo(
                label = label,
                icon = icon,
                packageName = it.activityInfo.packageName,
                activityName = it.activityInfo.name
            )
        }
    }

}