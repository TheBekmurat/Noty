package com.example.noty.utils

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.noty.MainActivity
import com.example.noty.R
import com.example.noty.ui.add.CHANNEL_ID
import com.example.noty.ui.add.ID_KEY
import com.example.noty.ui.add.TITLE_KEY

const val KEY_TO_NAVIGATE = "KEY_TO_NAVIGATE"
const val ADD_FRAGMENT = "Add_fragment"

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(KEY_TO_NAVIGATE, ADD_FRAGMENT)
        }
        val title = intent.getStringExtra(TITLE_KEY)
        val id = intent.getLongExtra(ID_KEY, 2)
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(id.toInt(), notification)
    }
}