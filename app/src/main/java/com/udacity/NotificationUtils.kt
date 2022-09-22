package com.udacity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

private const val CHANNEL_ID = "channelId"
private const val NOTIFICATION_ID = 0

fun createNotification(context: Context,title: String,content: String,fileName: String,status: String){

    val intent = Intent(context,DetailActivity::class.java)
    intent.putExtra("fileName",fileName)
    intent.putExtra("status",status)

    val pendingIntent = PendingIntent.getActivity(
        context,
        NOTIFICATION_ID,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val notificationBuilder = NotificationCompat.Builder(context,CHANNEL_ID)
        .setContentTitle(title)
        .setContentText(content)
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setAutoCancel(true)
        .addAction(
            R.drawable.ic_assistant_black_24dp,
            "Go to download",
            pendingIntent
        )

    with(NotificationManagerCompat.from(context)) {
        // notificationId is a unique int for each notification that you must define
        notify(NOTIFICATION_ID, notificationBuilder.build())
    }
}

fun createChannel(context: Context,channelName: String, channelDescription: String){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, channelName, importance).apply {
            description = channelDescription
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

