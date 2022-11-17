package jp.funmake.example.background

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.work.ForegroundInfo

fun Context.createNotificationChannel(): String {
    val channelId = "main-channel"
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationManager = checkNotNull(getSystemService<NotificationManager>())
        if (notificationManager.getNotificationChannel(channelId) == null) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    "Main Channel",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }
    }
    return channelId
}

fun Context.createNotification(text: String, cancelAction: PendingIntent? = null): Notification {
    val channelId = applicationContext.createNotificationChannel()
    return NotificationCompat.Builder(applicationContext, channelId)
        .setContentTitle("background-example")
        .setContentText(text)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .apply {
            cancelAction?.let {
                addAction(android.R.drawable.ic_delete, "cancel", it)
            }
        }
        .build()
}