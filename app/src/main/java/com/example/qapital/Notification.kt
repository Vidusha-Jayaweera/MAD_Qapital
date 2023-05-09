package com.example.qapital

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import java.util.Random

const val CHANNEL_ID = "com.example.qapital.quotes"
const val notificationID = 0
const val titleExtra = "title"
const val messageExtra = "message"

class NotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Retrieve a random quote from a list of quotes
        val quotes = listOf(
            "The best way to predict the future is to invent it. - Alan Kay",
            "Innovation distinguishes between a leader and a follower. - Steve Jobs",
            "The best ideas come as jokes. Make your thinking as funny as possible. - David Ogilvy",
            "Don't worry about failure; you only have to be right once. - Drew Houston",
            "I have not failed. I've just found 10,000 ways that won't work. - Thomas Edison",
            "If you can't explain it simply, you don't understand it well enough. - Albert Einstein",
            "It's not that I'm so smart, it's just that I stay with problems longer. - Albert Einstein",
            "The secret of change is to focus all of your energy, not on fighting the old, but on building the new. - Socrates"
        )
        val randomIndex = Random().nextInt(quotes.size)
        val quote = quotes[randomIndex]

        // Build and show the notification
        val notificationManager =
            ContextCompat.getSystemService(context, NotificationManager::class.java) as NotificationManager

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("Daily Quote")
            .setContentText(quote)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(notificationID, notification)
    }
}
