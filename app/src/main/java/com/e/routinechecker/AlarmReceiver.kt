package com.example.routinechecks

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.media.RingtoneManager
import android.util.Log
import com.example.routinechecks.ui.activities.MainActivity
import com.example.routinechecks.data.database.Routine


class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_ROUTINE = "mRoutine"
    }

    private lateinit var mRoutine: Routine

    private val channelId: String = "reminder_channel"

    override fun onReceive(context: Context, intent: Intent) {
        //Get the routine
        if (intent.hasExtra(EXTRA_ROUTINE)) {

            mRoutine = intent.getParcelableExtra(EXTRA_ROUTINE)

            val notificationId = mRoutine.id

            // Create an explicit intent to start MainActivity
            val mainActivityIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, 0)

            //Create notification
            val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val intentAction = Intent(context, ActionReceiver::class.java)
            intentAction.putExtra("ROUTINE", mRoutine)
            val actionPendingIntent: PendingIntent =
                PendingIntent.getBroadcast(context, 1, intentAction, PendingIntent.FLAG_UPDATE_CURRENT)

            val markAsDoneAction =
                NotificationCompat.Action.Builder(
                    NotificationCompat.Action.SEMANTIC_ACTION_MARK_AS_READ,
                    "Mark as done",
                    actionPendingIntent
                )
                    .build()

            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_check_white_24dp)
                .setContentTitle(mRoutine.title)
                .setContentText("Next routine is in 5 minutes")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setTimeoutAfter(5 * 60 * 1000)
                .addAction(markAsDoneAction)

            with(NotificationManagerCompat.from(context)) {
                notify(notificationId, builder.build())
            }

        } else {
            Log.d("ON_RECEIVE>>>", "onReceive called, no extras")
        }
    }

}