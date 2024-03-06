package com.arfdevs.myproject.movment.presentation.helper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.presentation.helper.Constants.NOTIFICATION_CHANNEL_ID
import com.arfdevs.myproject.movment.presentation.helper.Constants.TRANSACTION_ID
import com.arfdevs.myproject.movment.presentation.helper.Constants.TRANSACTION_MOVIE_COUNT
import com.arfdevs.myproject.movment.presentation.helper.Constants.VIBRATE_1000
import com.arfdevs.myproject.movment.presentation.helper.Constants.VIBRATE_2000

class NotificationWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    private val transactionId = inputData.getInt(TRANSACTION_ID, 0)
    private val transactionTitle = inputData.getString(TRANSACTION_MOVIE_COUNT)

    override fun doWork(): Result {

        val contentText = applicationContext.getString(R.string.notify_content)
        val channelName = applicationContext.getString(R.string.notify_channel_name)

        val intent = NavDeepLinkBuilder(applicationContext)
            .setGraph(R.navigation.bottom_main_navigation)
            .setDestination(R.id.nav_transaction)
            .createPendingIntent()

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
                .setContentIntent(intent)
                .setSmallIcon(R.drawable.ic_movie)
                .setContentTitle(transactionTitle)
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(longArrayOf(VIBRATE_1000, VIBRATE_1000, VIBRATE_2000))
                .setSound(alarmSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(VIBRATE_1000, VIBRATE_1000, VIBRATE_2000)
            builder.setChannelId(NOTIFICATION_CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManager.notify(transactionId, notification)

        return Result.success()
    }

}


