package com.example.routinechecks.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.routinechecks.data.database.Routine
import com.example.routinechecks.data.database.RoutineDao

class Repository(private val routineDao: RoutineDao) {

    val allRoutines: LiveData<List<Routine>> = routineDao.getAllRoutines()

    @WorkerThread
    fun addRoutine(routine: Routine) {
        routineDao.addRoutine(routine)
    }

    @WorkerThread
    fun updateRoutine(routine: Routine) {
        routineDao.updateRoutine(routine)
    }

}