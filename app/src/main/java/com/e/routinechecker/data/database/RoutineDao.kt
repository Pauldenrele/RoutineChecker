package com.example.routinechecks.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RoutineDao {

    @Query("SELECT * from routines_table")
    fun getAllRoutines(): LiveData<List<Routine>>

    @Query("SELECT title from routines_table")
    fun getAllTitles(): List<String>

    @Insert
    fun addRoutine(routine: Routine)

    @Update
    fun updateRoutine (routine: Routine)

    @Query ("UPDATE routines_table SET completedRoutines = completedRoutines + 1 WHERE title = :title")
    fun markAsDone (title: String)

    @Query ("UPDATE routines_table SET missedRoutines = missedRoutines + 1 WHERE title = :title")
    fun markAsMissed (title: String)
}