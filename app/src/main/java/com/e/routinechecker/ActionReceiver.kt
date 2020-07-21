package com.example.routinechecks

import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import com.example.routinechecks.data.database.Routine
import com.example.routinechecks.data.database.RoutineRoomDatabase


class ActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val routine : Routine = intent.getParcelableExtra("ROUTINE")

        //Mark routine as done
        val routineDao = RoutineRoomDatabase.getDatabase(context).routineDao()
        routineDao.markAsDone(routine.title)

        //This is used to close the notification tray
        val it = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        context.sendBroadcast(it)
    }

}